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
package org.olat.lms.core.hello.impl;

import org.olat.system.spring.CoreSpringFactory;
import org.springframework.stereotype.Component;

/**
 * Initial Date: 02.11.2011 <br>
 * 
 * @author guretzki
 */
@Component
public class MessageBuilderFactory {

    public MessageBuilder createMessageBuilder(String message1, String message2) {
        return CoreSpringFactory.getBean(MessageBuilder.class, new Object[] { message1, message2 });
    }

}