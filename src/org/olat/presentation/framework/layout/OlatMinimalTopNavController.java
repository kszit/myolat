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
package org.olat.presentation.framework.layout;

import org.olat.lms.commons.mediaresource.RedirectMediaResource;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.Windows;
import org.olat.presentation.framework.core.components.Component;
import org.olat.presentation.framework.core.components.Window;
import org.olat.presentation.framework.core.components.link.Link;
import org.olat.presentation.framework.core.components.link.LinkFactory;
import org.olat.presentation.framework.core.components.velocity.VelocityContainer;
import org.olat.presentation.framework.core.control.WindowBackOffice;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.presentation.framework.core.control.controller.BasicController;
import org.olat.presentation.framework.dispatcher.StaticMediaDispatcher;
import org.olat.system.event.Event;

/**
 * Description:<br>
 * TODO: patrickb Class Description for OlatMinimalTopNavController
 * <P>
 * Initial Date: 15.02.2008 <br>
 * 
 * @author patrickb
 */
public class OlatMinimalTopNavController extends BasicController {

    private final VelocityContainer topNavVC;
    private final Link closeLink;

    public OlatMinimalTopNavController(final UserRequest ureq, final WindowControl wControl) {
        super(ureq, wControl);
        topNavVC = createVelocityContainer("topnavminimal");
        closeLink = LinkFactory.createLink("topnav.close", topNavVC, this);
        putInitialPanel(topNavVC);
    }

    /**
	 */
    @Override
    protected void doDispose() {
        // TODO Auto-generated method stub

    }

    /**
	 */
    @Override
    protected void event(final UserRequest ureq, final Component source, final Event event) {
        if (source == closeLink) {
            // close window (a html page which calls Window.close onLoad
            ureq.getDispatchResult().setResultingMediaResource(new RedirectMediaResource(StaticMediaDispatcher.createStaticURIFor("closewindow.html")));
            // release all resources and close window
            final WindowBackOffice wbo = getWindowControl().getWindowBackOffice();
            final Window w = wbo.getWindow();
            Windows.getWindows(ureq).deregisterWindow(w);
            wbo.dispose();
        }

    }

}
