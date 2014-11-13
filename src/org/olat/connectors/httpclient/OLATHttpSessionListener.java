/**
 * OLAT - Online Learning and Training<br>
 * http://www.olat.org
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Copyright (c) since 2004 at Multimedia- & E-Learning Services (MELS),<br>
 * University of Zurich, Switzerland.
 * <p>
 */
package org.olat.connectors.httpclient;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.olat.system.logging.log4j.LoggerHelper;

/**
 * Initial Date: 22.05.2013 <br>
 * 
 * @author oliver.buehler@agility-informatik.ch
 */
public class OLATHttpSessionListener implements HttpSessionListener {

    private static final Logger log = LoggerHelper.getLogger();

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        log.debug("HTTP session created [id=" + se.getSession().getId() + ", timeout=" + se.getSession().getMaxInactiveInterval() + " s]");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        final HttpSession session = se.getSession();
        final int duration = (int) ((System.currentTimeMillis() - session.getCreationTime()) / 1000);
        final int inactivity = (int) ((System.currentTimeMillis() - session.getLastAccessedTime()) / 1000);
        final String sessionInfo = "[id=" + se.getSession().getId() + ", timeout=" + se.getSession().getMaxInactiveInterval() + "s, duration=" + duration
                + "s, inactivity=" + inactivity + "s]";
        final boolean expired = inactivity >= session.getMaxInactiveInterval();
        if (expired) {
            log.debug("HTTP session timed out " + sessionInfo);
        } else {
            log.debug("HTTP session closed " + sessionInfo);
        }
    }
}
