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

import org.olat.data.basesecurity.Identity;
import org.olat.data.course.campus.Lecturer;
import org.olat.data.course.campus.Student;
import org.olat.lms.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Initial Date: 28.06.2012 <br>
 * 
 * @author cg
 */
@Component
public class MappingByEmail {

    @Autowired
    UserService userService;

    public Identity tryToMap(Lecturer lecturer) {
        return userService.findIdentityByEmail(lecturer.getEmail());
    }

    public Identity tryToMap(Student student) {
        return userService.findIdentityByEmail(student.getEmail());
    }

}
