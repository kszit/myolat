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
package org.olat.presentation.group.securitygroup.confirmation;

import java.util.List;

import org.olat.data.basesecurity.Identity;
import org.olat.lms.core.notification.service.AbstractGroupConfirmationInfo;
import org.olat.lms.core.notification.service.AbstractGroupConfirmationInfo.GROUP_CONFIRMATION_TYPE;

/**
 * Initial Date: Oct 30, 2012 <br>
 * 
 * @author Branislav Balaz
 */
public abstract class AbstractWaitingListGroupConfirmationSender<K extends AbstractGroupConfirmationSenderInfo, T extends AbstractGroupConfirmationInfo> extends
        AbstractGroupConfirmationSender<K, T> {

    protected AbstractWaitingListGroupConfirmationSender(K confirmationSenderInfo) {
        super(confirmationSenderInfo);
    }

    public void sendMoveUserConfirmation(List<Identity> recipients) {
        sendConfirmation(recipients, getMoveUserGroupConfirmationType());
    }

    abstract protected GROUP_CONFIRMATION_TYPE getMoveUserGroupConfirmationType();

    @Override
    abstract protected GROUP_CONFIRMATION_TYPE getAddUserGroupConfirmationType();

    @Override
    abstract protected GROUP_CONFIRMATION_TYPE getRemoveUserGroupConfirmationType();

}
