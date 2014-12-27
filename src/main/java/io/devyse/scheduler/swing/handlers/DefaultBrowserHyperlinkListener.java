/**
 * @(#) DefaultBrowserHyperlinkListener.java
 *
 * This file is part of the Course Scheduler, an open source, cross platform
 * course scheduling tool, configurable for most universities.
 *
 * Copyright (C) 2010-2014 Devyse.io; All rights reserved.
 *
 * @license GNU General Public License version 3 (GPLv3)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package io.devyse.scheduler.swing.handlers;

import java.awt.Desktop;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * Basic HyperLink Listener that opens the hyperlink in the system default browser.
 * 
 * @author Mike Reinhold
 * @since 4.12.7
 *
 */
public class DefaultBrowserHyperlinkListener implements HyperlinkListener {

	private static XLogger logger = XLoggerFactory.getXLogger(DefaultBrowserHyperlinkListener.class);
	
	/* (non-Javadoc)
	 * @see javax.swing.event.HyperlinkListener#hyperlinkUpdate(javax.swing.event.HyperlinkEvent)
	 */
	@Override
	public void hyperlinkUpdate(HyperlinkEvent hle) {
		if (HyperlinkEvent.EventType.ACTIVATED.equals(hle.getEventType())) {
            logger.debug("Hyperlink activated {}", hle.getURL());
            Desktop desktop = Desktop.getDesktop();
            try {
            	logger.info("Attempting to open link {} in default desktop browser", hle.getURL());
                desktop.browse(hle.getURL().toURI());
            } catch (Exception ex) {
                logger.error("Unable to open hyperlink in external application", ex);
            }
        }
	}

}
