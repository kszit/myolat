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

package org.olat.lms.user;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.olat.system.logging.log4j.LoggerHelper;

/**
 * @author Christian Guretzki
 */
public class TestUserAdder {
    private final UserModule userModule;
    private static final Logger log = LoggerHelper.getLogger();

    /**
     * [used by spring]
     * 
     * @param authenticationProviderConstant
     */
    private TestUserAdder(final UserModule userModule) {
        log.info("Constructor TestUserAdder");
        this.userModule = userModule;
    }

    /**
     * [used by Spring]
     * 
     * @param addionalTestUsers
     */
    public void setAdditionalTestUsers(final List<DefaultUser> additionalTestUsers) {
        createAdditionalTestUsers(additionalTestUsers);
    }

    private void createAdditionalTestUsers(final List<DefaultUser> additionalTestUsers) {
        if (additionalTestUsers != null) {
            for (final Iterator iter = additionalTestUsers.iterator(); iter.hasNext();) {
                final DefaultUser user = (DefaultUser) iter.next();
                userModule.createUser(user);
                log.info("created additional test-user username=" + user.getUserName());
            }
        }
    }
}
