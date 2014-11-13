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
package org.olat.lms.core.notification.service;

import java.util.Date;
import java.util.List;

import org.olat.data.basesecurity.Identity;
import org.olat.data.group.BusinessGroup;

/**
 * Contains the info for sending a confirmation about delete unused groups action. <br/>
 * 
 * Initial Date: Oct 18, 2012 <br>
 * 
 * @author Branislav Balaz
 */
public class GroupsConfirmationInfo extends ConfirmationInfo {

    private final List<BusinessGroup> groups;
    private final GROUPS_CONFIRMATION_TYPE groupsConfirmationType;
    private final int numberOfMonths;
    private final int numberOfDays;

    public static enum GROUPS_CONFIRMATION_TYPE {
        DELETE_GROUPS
    }

    public GroupsConfirmationInfo(List<RecipientInfo> allRecipientInfos, Identity originatorIdentity, Date dateTime, List<BusinessGroup> groups,
            GROUPS_CONFIRMATION_TYPE groupsConfirmationType, int numberOfMonths, int numberOfDays) {
        super(allRecipientInfos, originatorIdentity, null, null, dateTime);
        this.groups = groups;
        this.groupsConfirmationType = groupsConfirmationType;
        this.numberOfMonths = numberOfMonths;
        this.numberOfDays = numberOfDays;
    }

    @Override
    public CONFIRMATION_TYPE getType() {
        return CONFIRMATION_TYPE.GROUPS;
    }

    public List<BusinessGroup> getGroups() {
        return groups;
    }

    public GROUPS_CONFIRMATION_TYPE getGroupsConfirmationType() {
        return groupsConfirmationType;
    }

    public int getNumberOfMonths() {
        return numberOfMonths;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

}
