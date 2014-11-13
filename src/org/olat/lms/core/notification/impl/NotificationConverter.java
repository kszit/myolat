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
package org.olat.lms.core.notification.impl;

import java.util.ArrayList;
import java.util.List;

import org.olat.data.notification.NotificationEvent;
import org.springframework.stereotype.Component;

/**
 * Initial Date: 21.03.2012 <br>
 * 
 * @author aabouc
 */
@Component
public class NotificationConverter {

    public List<NotificationEventTO> toEventTOList(List<NotificationEvent> events) {
        // Prepare the list of NotificationEventTO
        List<NotificationEventTO> eventTOList = new ArrayList<NotificationEventTO>();
        for (NotificationEvent event : events) {
            NotificationEventTO eventTO = convertToEventTO(event);
            if (eventTO.isEventEligible()) {
                eventTOList.add(eventTO);
            }
        }
        return eventTOList;
    }

    public NotificationEventTO convertToEventTO(NotificationEvent event) {
        return new NotificationEventTO(event);
    }
}
