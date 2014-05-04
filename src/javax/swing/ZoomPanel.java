package javax.swing;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.util.HashMap;

import Scheduler.MakeSchedule;
import Scheduler.MakeSchedule.ClassPanel;

public class ZoomPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected double zoom;
	
	private JPanel item;
	private AffineTransform scale;
	private AffineTransform forward;
	protected CustomZoomListener zoomListen;
	

    public ZoomPanel(double initialZoom, JPanel item) {
        super(new FlowLayout());
        zoom = initialZoom;
        this.item = item;
        add(item);
        zoomListen = new CustomZoomListener();
        addMouseListener(zoomListen);
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g); // clears background
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform backup = g2.getTransform();
        
        scale = new AffineTransform();
        scale.setToIdentity();
        scale.scale(zoom, zoom);
        
        forward = new AffineTransform();
        forward.concatenate(scale);
        forward.translate(getLayout().preferredLayoutSize(this).getWidth()*(1-zoom)/2, 0);
        
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        g2.transform(forward);
        
        
        
        super.paint(g);
        g2.setTransform(backup);
    }

    @Override
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    @SuppressWarnings("cast")
	@Override
    public Dimension getPreferredSize() {
        Dimension unzoomed
          = getLayout().preferredLayoutSize(this);
        Dimension zoomed
          = new Dimension((int)(unzoomed.width*zoom + (unzoomed.width * 0.15 *(1-zoom))),//*2),
                          (int)(unzoomed.height*zoom));
        return zoomed;
    }
    
    public void setZoom(double newZoom)
        throws PropertyVetoException {
        if (newZoom <= 0.0) {
            throw new PropertyVetoException
                ("Zoom must be positive-valued",
                 new PropertyChangeEvent(this,
                                         "zoom",
                                         new Double(zoom),
                                         new Double(newZoom)));
        }
        double oldZoom = zoom;
        if (newZoom != oldZoom) {
            zoom = newZoom;
            revalidate();
            repaint();
        }
    }

    public double getZoom() {
        return zoom;
    }
    
    
    public class CustomZoomListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			MouseEvent temp = findSubComponent(arg0);
			
			if(temp != null)
			{
				arg0 = temp;
				maybeShowPopup(arg0);	
			}
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			MouseEvent temp = findSubComponent(arg0);
			
			if(temp != null)
			{
				arg0 = temp;
				maybeShowPopup(arg0);
			}			
		}
    	
		private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	            ((ClassPanel)e.getComponent()).showContextMenu(e.getX(), e.getY());
	        }
	    }
    }
    
    
    public AffineTransform getForwardTx(){
    	AffineTransform first = new AffineTransform(scale);
        AffineTransform second = new AffineTransform(forward);
        
        first.preConcatenate(second);
        
        return first;
    }
    
    public MouseEvent findSubComponent(MouseEvent e){
    	Point click = e.getPoint();

        AffineTransform first = getForwardTx();
        
        try {
	        AffineTransform inverse = first.createInverse();
	        Point newClick = (Point) inverse.transform(click, new Point());
	               
	        if(newClick.equals(click)){
	        	return e;
	        }
	        
	        
	        
	        
	        /*
	        Component c;
	        if(e.getSource() instanceof MakeSchedule.ClassPanel){
	        	c = (Component)e.getSource();
	        }
	        else{
	        	c= SwingUtilities.getDeepestComponentAt(item, newClick.x, newClick.y).getParent().getParent();
	        }
	        
	        Shape convertedShape = first.createTransformedShape(c.getBounds());
	        
	        if(convertedShape.contains(click)){
		        Point panelPoint = SwingUtilities.convertPoint(this, click, c);
		        
		        MouseEvent newMouse = new MouseEvent(c, e.getID(), e.getWhen(), e.getModifiers(), panelPoint.x, panelPoint.y,
		                e.getClickCount(), e.isPopupTrigger(), e.getButton());
		        
		        if(c instanceof MakeSchedule.ClassPanel){
		        	return newMouse;
		        }
	        }
	        else if(c.getBounds().equals(convertedShape.getBounds())){
	        	if(c instanceof MakeSchedule.ClassPanel){
		        	return e;
		        }
	        }*/
	        
	        return null;
        } catch (Throwable t) {
        	return null;
        }
    }
    

	public CustomZoomListener getZoomListen() {
		return zoomListen;
	}

	public void setZoomListen(CustomZoomListener zoomListen) {
		this.zoomListen = zoomListen;
	}
}
