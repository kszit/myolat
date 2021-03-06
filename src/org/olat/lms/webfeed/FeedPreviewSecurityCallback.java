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
package org.olat.lms.webfeed;

import org.olat.lms.core.notification.service.SubscriptionContext;

/**
 * The feed preview security callback. Only reading permitted.
 * <P>
 * Initial Date: Aug 11, 2009 <br>
 * 
 * @author gwassmann
 */
public class FeedPreviewSecurityCallback implements FeedSecurityCallback {

    /**
	 */
    @Override
    public boolean mayCreateItems() {
        return false;
    }

    /**
	 */
    @Override
    public boolean mayDeleteItems() {
        return false;
    }

    /**
	 */
    @Override
    public boolean mayEditItems() {
        return false;
    }

    /**
	 */
    @Override
    public boolean mayEditMetadata() {
        return false;
    }

    @Override
    public SubscriptionContext getSubscriptionContext() {
        // TODO Auto-generated method stub
        return null;
    }
}
