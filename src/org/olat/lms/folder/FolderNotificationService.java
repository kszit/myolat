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
package org.olat.lms.folder;

import org.apache.log4j.Logger;
import org.olat.lms.core.notification.service.NotificationService;
import org.olat.lms.core.notification.service.PublishEventTO;
import org.olat.system.logging.log4j.LoggerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Initial Date: 29.03.2012 <br>
 * 
 * @author guretzki
 */
@Component
public class FolderNotificationService {
    private static final Logger log = LoggerHelper.getLogger();

    @Autowired
    private NotificationService notificationService;

    public void publishEvent(PublishEventTO publishEventTO) {
        try {
            notificationService.publishEvent(publishEventTO);
        } catch (RuntimeException e) {
            log.error("publishEvent failed: ", e);
        }
    }
}
