/*
 * Copyright (c) Andrey Kuznetsov. All Rights Reserved.
 *
 * http://jgui.imagero.com
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Andrey Kuznetsov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.swing;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

/**
 * Transform any swing component.
 */
public class AffinePanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3486541619930533783L;

	/**
	 * Static Logger
	 */
	private static XLogger logger = XLoggerFactory.getXLogger(AffinePanel.class);
	
    AffineTransform at;

    CellRendererPane crp = new CellRendererPane();
    Component view;

    public AffinePanel(Component renderer) {
        this(null, renderer);
    }

    public Component getView() {
        return view;
    }

    public void setView(Component view) {
        this.view = view;
    }

    public AffinePanel(AffineTransform at, Component renderer) {
        setLayout(null);
        this.at = at;
        this.view = renderer;
        add(crp);
        add(view);
        view.setVisible(true);
        setTransform(at);

        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(new KeyEventPostProcessor() {
            public boolean postProcessKeyEvent(KeyEvent e) {
                if (AffinePanel.this.at == null) {
                    return false;
                }
                Component c = e.getComponent();
                if (SwingUtilities.isDescendingFrom(c, AffinePanel.this.view)) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().redispatchEvent(c, e);
                    repaint();
                    return true;
                }
                return false;
            }
        });
    }

    public Dimension getPreferredSize() {
        if (view == null) {
            return super.getPreferredSize();
        } else {
            Insets insets = getInsets();
            Dimension d = view.getPreferredSize();
            if (at == null) {
                return new Dimension(d.width + insets.left + insets.right, d.height + insets.top + insets.bottom);
            } else {
                Rectangle r = new Rectangle(0, 0, d.width, d.height);
                Shape shape = at.createTransformedShape(r);
                return shape.getBounds().getSize();
            }
        }
    }

    protected void processMouseEvent(MouseEvent e) {
        if (at != null && view != null) {
            processME(e);
        } else {
            super.processMouseEvent(e);
        }
    }

    protected void processMouseMotionEvent(MouseEvent e) {
        if (at != null && view != null) {
            boolean autoscrolls = false;
            if (view instanceof JComponent) {
                JComponent comp = (JComponent) view;
                autoscrolls = comp.getAutoscrolls();
                comp.setAutoscrolls(false);
            }
            processME(e);
            if (autoscrolls) {
                JComponent comp = (JComponent) view;
                comp.setAutoscrolls(true);
            }
        } else {
            super.processMouseMotionEvent(e);
        }
    }

    boolean pressed;

    private void processME(MouseEvent e) {
        Point p = e.getPoint();
        repaint();
        Rectangle r = view.getBounds();

        Shape shape = at.createTransformedShape(r);
        Rectangle r2 = shape.getBounds();

        AffineTransform at2 = new AffineTransform(at);
        at2.preConcatenate(AffineTransform.getTranslateInstance(r2.x < 0 ? -r2.x : 0, r2.y < 0 ? -r2.y : 0));

        shape = at2.createTransformedShape(r);
        if (!shape.contains(p)) {
            return;
        }

        switch (e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                pressed = true;
                break;
            case MouseEvent.MOUSE_RELEASED:
                pressed = false;
                break;
            case MouseEvent.MOUSE_DRAGGED:
                if (!pressed) {
                    return;
                }
                break;
        }

        Point p0;
        try {
            AffineTransform inverse = at2.createInverse();
            p0 = (Point) inverse.transform(p, new Point());
        } catch (NoninvertibleTransformException ex) {
            logger.warn("Unable to invert transform", ex);
            p0 = findPoint(r, at2, p);
        }
        Component c = SwingUtilities.getDeepestComponentAt(view, p0.x, p0.y);
        if (c != null) {
            p0 = SwingUtilities.convertPoint(view, p0, c);
        } else {
            c = view;
        }
        MouseEvent me = createEvent(e, c, p0.x, p0.y);
        try {
            c.dispatchEvent(me);
        } catch (Throwable t) {
            //ignore
        }
    }

    MouseEvent createEvent(MouseEvent e, Component source, int x, int y) {
        return new MouseEvent(
                source, e.getID(), e.getWhen(), e.getModifiers(), x, y,
                e.getClickCount(), e.isPopupTrigger(), e.getButton());
    }

    /**
     * used to search point in case of noninvertible AffineTransform
     * @param r Rectangle
     * @param at AffineTransform
     * @param p0 point to search
     * @return Point on Rectangle <code>r</code> which after transform with AffineTransform <code>at</code> gives Point p0
     */
    Point findPoint(Rectangle r, AffineTransform at, Point p0) {
        if (r.width == 1 && r.height == 1) {
            return r.getLocation();
        }

        int w2 = Math.max((r.width + 1) / 2, 1);
        int h2 = Math.max((r.height + 1) / 2, 1);
        Rectangle[] rrs = new Rectangle[4];
        rrs[0] = new Rectangle(r.x, r.y, w2, h2);
        rrs[1] = new Rectangle(r.x + w2, r.y, w2, h2);
        rrs[2] = new Rectangle(r.x, r.y + h2, w2, h2);
        rrs[3] = new Rectangle(r.x + w2, r.y + h2, w2, h2);
        for (int i = 0; i < rrs.length; i++) {
            Shape s0 = at.createTransformedShape(rrs[i]);
            if (s0.contains(p0)) {
                return findPoint(rrs[i], at, p0);
            }
        }
        return null;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (view == null || at == null) {
            return;
        }
        Dimension d = view.getPreferredSize();
        Dimension d2 = view.getSize();
        if ((d.width != d2.width) || (d.height != d2.height)) {
            view.setSize(d);
        }
        if (at != null) {
            Graphics2D g2d = (Graphics2D) g.create();

            Rectangle r = new Rectangle(0, 0, d.width, d.height);
            Shape shape = at.createTransformedShape(r);
            Rectangle r2 = shape.getBounds();

            g2d.translate(r2.x < 0 ? -r2.x : 0, r2.y < 0 ? -r2.y : 0);
            g2d.transform(at);
            view.paint(g2d);
            g2d.dispose();
        } else {
            view.paint(g);
        }
    }

    public void scale(double sx, double sy) {
        if (at == null) {
            setScale(sx, sy);
        } else {
            at.scale(sx, sy);
        }
    }

    public void setScale(double sx, double sy) {
        setTransform(AffineTransform.getScaleInstance(sx, sy));
    }

    public void rotate(double angle) {
        if (at == null) {
            setRotate(angle);
        } else {
            at.rotate(Math.toRadians(angle));
        }
    }

    public void setRotate(double angle) {
        setTransform(AffineTransform.getRotateInstance(Math.toRadians(angle)));
    }

    public void shear(double sx, double sy) {
        if (at == null) {
            setShear(sx, sy);
        } else {
            at.shear(sx, sy);
        }
    }

    public void setShear(double sx, double sy) {
        setTransform(AffineTransform.getShearInstance(sx, sy));
    }

    public void setTransform(AffineTransform at) {
        if (equals(this.at, at)) {
            return;
        }

        if (this.at == null) {
            crp.add(view);
        } else if (at == null) {
            add(view);
        }

        this.at = at;

        revalidate();
        repaint();
    }

    private static boolean equals(AffineTransform at1, AffineTransform at2) {
        if (at1 == null && at2 == null) {
            return true;
        }
        if (at1 == null || at2 == null) {
            return false;
        }
        return at1.equals(at2);
    }

    public AffineTransform getTransform() {
        return at;
    }
}