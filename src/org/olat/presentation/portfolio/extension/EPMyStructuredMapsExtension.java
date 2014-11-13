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
 * Copyright (c) frentix GmbH<br>
 * http://www.frentix.com<br>
 * <p>
 */
package org.olat.presentation.portfolio.extension;

import org.olat.data.portfolio.structure.EPStructuredMap;
import org.olat.lms.commons.context.ContextEntry;
import org.olat.presentation.commons.context.ContextEntryControllerCreator;
import org.olat.presentation.framework.common.NewControllerFactory;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.presentation.home.site.HomeSite;

/**
 * Description:<br>
 * Load a context entry creator
 * <P>
 * Initial Date: 03.08.2010 <br>
 * 
 * @author Roman Haag, roman.haag@frentix.com, http://www.frentix.com
 */
public class EPMyStructuredMapsExtension {

    public EPMyStructuredMapsExtension() {

        NewControllerFactory.getInstance().addContextEntryControllerCreator(EPStructuredMap.class.getSimpleName(), new ContextEntryControllerCreator() {

            @Override
            public Controller createController(final ContextEntry ce, final UserRequest ureq, final WindowControl wControl) {
                return null;
            }

            @Override
            public String getTabName(final ContextEntry ce) {
                // opens in home-tab
                return null;
            }

            @Override
            public String getSiteClassName(final ContextEntry ce) {
                return HomeSite.class.getName();
            }

            @Override
            public boolean validateContextEntryAndShowError(final ContextEntry ce, final UserRequest ureq, final WindowControl wControl) {
                return true;
            }

        });
    }
}
