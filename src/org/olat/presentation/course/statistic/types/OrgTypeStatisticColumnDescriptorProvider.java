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
package org.olat.presentation.course.statistic.types;

import org.olat.lms.security.authentication.shibboleth.ShibbolethModule;
import org.olat.presentation.course.statistic.StatisticDisplayController;
import org.olat.presentation.course.statistic.TotalAwareColumnDescriptor;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.table.ColumnDescriptor;
import org.olat.presentation.framework.core.components.table.DefaultColumnDescriptor;
import org.olat.presentation.framework.core.translator.PackageUtil;
import org.olat.presentation.framework.core.translator.Translator;

/**
 * TODO: Class Description for OrgTypeStatisticColumnDescriptorProvider
 * 
 * <P>
 * Initial Date: 05.04.2011 <br>
 * 
 * @author lavinia
 */
public class OrgTypeStatisticColumnDescriptorProvider implements ColumnDescriptorProvider {

    @Override
    public ColumnDescriptor createColumnDescriptor(final UserRequest ureq, final int column, String headerId) {
        if (column == 0) {
            return new DefaultColumnDescriptor("stat.table.header.node", 0, null, ureq.getLocale());
        }

        if (headerId != null) {
            final Translator translator = PackageUtil.createPackageTranslator(ShibbolethModule.class, ureq.getLocale());
            if (translator != null) {
                final String newHeaderId = translator.translate("swissEduPersonHomeOrganizationType." + headerId);
                if (newHeaderId != null && !newHeaderId.startsWith(Translator.NO_TRANSLATION_ERROR_PREFIX)) {
                    headerId = newHeaderId;
                }
            }
        }

        final TotalAwareColumnDescriptor cd = new TotalAwareColumnDescriptor(headerId, column, StatisticDisplayController.CLICK_TOTAL_ACTION + column, ureq.getLocale(),
                ColumnDescriptor.ALIGNMENT_RIGHT);
        cd.setTranslateHeaderKey(false);
        return cd;
    }

}
