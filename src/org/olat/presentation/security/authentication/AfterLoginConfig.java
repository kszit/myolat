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
package org.olat.presentation.security.authentication;

import java.util.List;
import java.util.Map;

/**
 * Description:<br>
 * Data-Container used by Spring to set AfterLoginControllerList.
 * <P>
 * Initial Date: 25.11.2009 <br>
 * 
 * @author Roman Haag, roman.haag@frentix.com, www.frentix.com
 */
public class AfterLoginConfig {
    private List<Map<String, Object>> afterLoginControllerList;

    /**
     * @return Returns the afterLoginControllerList.
     */
    public List<Map<String, Object>> getAfterLoginControllerList() {
        return afterLoginControllerList;
    }

    /**
     * @param afterLoginControllerList
     *            The afterLoginControllerList to set.
     */
    public void setAfterLoginControllerList(final List<Map<String, Object>> afterLoginControllerList) {
        this.afterLoginControllerList = afterLoginControllerList;
    }

}
