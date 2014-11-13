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
package org.olat.lms.instantmessaging.task;

import org.olat.connectors.instantmessaging.InstantMessagingSessionCount;

/**
 * Description:<br>
 * decouples the lookup of the session count from the main login/logout flow to improve performace and to
 * <P>
 * Initial Date: 29.07.2010 <br>
 * 
 * @author guido
 */
public class CountSessionsOnServerTask implements Runnable {

    private final InstantMessagingSessionCount sessionCountService;
    private int[] sessionCount;

    public CountSessionsOnServerTask(final InstantMessagingSessionCount sessionCountService, int[] sessionCount) {
        this.sessionCountService = sessionCountService;
        this.sessionCount = sessionCount;
    }

    /**
	 */
    @Override
    public void run() {
        sessionCount[0] = sessionCountService.countSessions();

    }

}
