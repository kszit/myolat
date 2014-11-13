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
package org.olat.lms.core.course.campus.impl.mapper;

import java.util.List;

import javax.annotation.PreDestroy;

import org.apache.log4j.Logger;
import org.olat.data.course.campus.Student;
import org.olat.lms.core.course.campus.impl.metric.CampusNotifier;
import org.olat.system.logging.log4j.LoggerHelper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * {@link ItemWriter} for
 * 
 * Initial Date: 27.11.2012 <br>
 * 
 * @author aabouc
 */
public class StudentMappingWriter implements ItemWriter<Student> {
    private static final Logger LOG = LoggerHelper.getLogger();

    private MappingStatistic mappingStatistic;

    @Autowired
    StudentMapper studentMapper;
    @Autowired
    CampusNotifier campusNotifier;

    public void setMappingStatistic(MappingStatistic mappingStatistic) {
        this.mappingStatistic = mappingStatistic;
    }

    public MappingStatistic getMappingStatistic() {
        return mappingStatistic;
    }

    @PreDestroy
    public void destroy() {
        LOG.info("MappingStatistic(Students)=" + mappingStatistic);
        campusNotifier.notifyUserMapperStatistic(new OverallUserMapperStatistic(null, mappingStatistic));
    }

    @Override
    public void write(List<? extends Student> students) throws Exception {
        for (Student student : students) {
            try {
                mappingStatistic.addMappingResult(studentMapper.synchronizeStudentMapping(student));
            } catch (Exception ex) {
                LOG.warn("synchronizeAllStudentMapping: Could not syncronized student:" + student + " exception:" + ex);
            }
        }
    }
}
