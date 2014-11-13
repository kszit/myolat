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
package org.olat.presentation.webfeed;

/**
 * Description:<br>
 * allows to configure the presentation of a Feed Item and its GUI components
 * 
 * <P>
 * Initial Date: 20.04.2011 <br>
 * 
 * @author Roman Haag, roman.haag@frentix.com, http://www.frentix.com
 */
public class FeedItemDisplayConfig {
    private boolean showCRInMinimized;
    private boolean showCRInDetails;
    private boolean showDateNavigation;

    /**
     * 
     * @param showCRInMinimized
     *            show Comments and Ratings in minimized Item view
     * @param showCRInDetails
     *            show Comments and Ratings item detail/full view
     * @param showDateNavigation
     *            show Calendar on the right with date navigation
     */
    public FeedItemDisplayConfig(boolean showCRInMinimized, boolean showCRInDetails, boolean showDateNavigation) {
        this.showCRInMinimized = showCRInMinimized;
        this.showCRInDetails = showCRInDetails;
        this.showDateNavigation = showDateNavigation;
    }

    // show Comments and Ratings in minimized Item view
    public boolean isShowCRInMinimized() {
        return showCRInMinimized;
    }

    // show Comments and Ratings item detail/full view
    public boolean isShowCRInDetails() {
        return showCRInDetails;
    }

    // show Calendar on the right with date navigation
    public boolean isShowDateNavigation() {
        return showDateNavigation;
    }
}
