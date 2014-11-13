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
 * Copyright (c) 2008 frentix GmbH, Switzerland<br>
 * http://www.frentix.com
 * <p>
 */
package org.olat.lms.security;

import org.apache.log4j.Logger;
import org.olat.lms.commons.scheduler.JobWithDB;
import org.olat.lms.portfolio.EPPolicyManager;
import org.olat.system.logging.log4j.LoggerHelper;
import org.olat.system.spring.CoreSpringFactory;
import org.quartz.JobExecutionContext;

import com.ibm.icu.util.Calendar;

/**
 * Description:<br>
 * A job to remove invitation without policies.
 * <P>
 * Initial Date: 11 nov. 2010 <br>
 * 
 * @author srosse
 */
public class InvitationCleanupJob extends JobWithDB {

    private static final Logger log = LoggerHelper.getLogger();

    /**
	 */
    @Override
    public void executeWithDB(final JobExecutionContext context) {
        try {
            log.info("Starting invitation clean up job");
            EPPolicyManager policyManager = (EPPolicyManager) CoreSpringFactory.getBean(EPPolicyManager.class);
            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, -6);
            policyManager.cleanUpInvitations(cal.getTime());
        } catch (final Exception e) {
            // ups, something went completely wrong! We log this but continue next time
            log.error("Error while cleaning up invitation", e);
        }
        // db closed by JobWithDB class
    }
}
