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
package org.olat.presentation.framework.layout.fullWebApp.popup;

import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.presentation.framework.core.control.creator.ControllerCreator;
import org.olat.system.exception.AssertException;

/**
 * Description:<br>
 * This factory offers methods to create popup layout creator controllers
 * <P>
 * Initial Date: 26.07.2007 <br>
 * 
 * @author patrickb
 */
public class BaseFullWebappPopupLayoutFactory {

    /**
     * creates a controller creator which can be used for a popup browser window, allows only authenticated user session. <br>
     * The popup browser windows layout contains:
     * <ul>
     * <li>minimal header (printview and close buttons)</li>
     * <li>content controller provided by parameter controllerCreator
     * <li>minmal footer</li<
     * </ul>
     * 
     * @param ureq
     * @param controllerCreator
     * @return
     */
    public static BaseFullWebappPopupLayout createAuthMinimalPopupLayout(UserRequest ureq, ControllerCreator controllerCreator) {
        if (!ureq.getUserSession().isAuthenticated())
            throw new AssertException("not authenticated!");
        BaseFullWebappPopupLayout layoutCC = new BaseFullWebappMinimalLayoutControllerCreator(controllerCreator);
        return layoutCC;
    }

    /**
     * Open a new Window which redirects somewhere else
     * 
     * @param ureq
     * @param redirectUrl
     * @return
     */
    public static BaseFullWebappPopupLayout createRedirectingPopup(UserRequest ureq, final String redirectUrl) {
        ControllerCreator cc = new ControllerCreator() {
            @Override
            public Controller createController(UserRequest lureq, WindowControl lwControl) {
                return new RedirectionDummyController(lureq, lwControl, redirectUrl);
            }
        };
        return new BaseFullWebappMinimalLayoutControllerCreator(cc);
    }

}
