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

import org.olat.data.hello.HelloMessageDao;
import org.olat.lms.core.hello.service.MessageModelCoreTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Initial Date: 02.11.2011 <br>
 * 
 * @author guretzki
 */
@Component
@Scope("prototype")
public class MessageBuilder {

    @Autowired
    HelloMessageDao helloMessageDao;
    String message1;
    String message2;
    @Autowired
    HelloModelTransferObjectFactory returnObjectFactory;

    public MessageBuilder(String message1, String message2) {
        super();
        this.message1 = message1;
        this.message2 = message2;
    }

    public MessageModelCoreTO build() {
        MessageModelCoreTO messageReturnObject = returnObjectFactory.createMessageReturnObject(helloMessageDao.getMessageEntity().getMessage() + ", " + message1 + " "
                + message2);
        return messageReturnObject;
    }

}
