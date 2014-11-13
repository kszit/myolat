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

import java.util.Date;
import java.util.List;

import org.olat.lms.core.notification.service.AbstractGroupConfirmationInfo.GROUP_CONFIRMATION_TYPE;
import org.olat.lms.core.notification.service.RecipientInfo;
import org.olat.lms.core.notification.service.WaitingListLearningGroupConfirmationInfo;

/**
 * Initial Date: Oct 30, 2012 <br>
 * 
 * @author Branislav Balaz
 */
public class WaitingListLearningGroupConfirmationSender extends
        AbstractWaitingListGroupConfirmationSender<RightLearningGroupConfirmationSenderInfo, WaitingListLearningGroupConfirmationInfo> {

    public WaitingListLearningGroupConfirmationSender(RightLearningGroupConfirmationSenderInfo confirmationSenderInfo) {
        super(confirmationSenderInfo);
    }

    @Override
    protected GROUP_CONFIRMATION_TYPE getAddUserGroupConfirmationType() {
        return GROUP_CONFIRMATION_TYPE.ADD_USER_WAITING_LIST_LEARNING_GROUP;
    }

    @Override
    protected GROUP_CONFIRMATION_TYPE getRemoveUserGroupConfirmationType() {
        return GROUP_CONFIRMATION_TYPE.REMOVE_USER_WAITING_LIST_LEARNING_GROUP;
    }

    @Override
    protected GROUP_CONFIRMATION_TYPE getMoveUserGroupConfirmationType() {
        return GROUP_CONFIRMATION_TYPE.MOVE_USER_WAITING_LIST_LEARNING_GROUP;
    }

    @Override
    protected WaitingListLearningGroupConfirmationInfo getConfirmationInfo(List<RecipientInfo> recipientInfos, GROUP_CONFIRMATION_TYPE groupConfirmationType) {
        WaitingListLearningGroupConfirmationInfo waitingListRightLearningGroupConfirmationInfo = WaitingListLearningGroupConfirmationInfo
                .createWaitingListLearningGroupConfirmationInfo(groupConfirmationType, recipientInfos, confirmationSenderInfo.getOriginatorIdentity(), new Date(),
                        confirmationSenderInfo.getGroup().getKey(), confirmationSenderInfo.getGroup().getName(), confirmationSenderInfo.getRepositoryEntry().getKey(),
                        confirmationSenderInfo.getRepositoryEntry().getDisplayname());
        return waitingListRightLearningGroupConfirmationInfo;
    }
}
