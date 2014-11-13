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
 * Copyright (c) 1999-2006 at Multimedia- & E-Learning Services (MELS),<br>
 * University of Zurich, Switzerland.
 * <p>
 */

package org.olat.lms.commons.mediaresource;

import java.io.File;

/**
 * This media resource deletes the file as soon as it is delivered to the client. The file is delivered as attachment.
 * 
 * @author Felix Jost
 */
public class CleanupAfterDeliveryFileMediaResource extends FileMediaResource {

    private File file;

    /**
     * file assumed to exist, but if it does not exist or cannot be read, getInputStream() will return null and the class will behave properly.
     * 
     * @param file
     */
    public CleanupAfterDeliveryFileMediaResource(File file) {
        super(file, true);
        this.file = file;
    }

    @Override
    public void release() {
        file.delete();
    }

    public String getFileName() {
        return file.getName();
    }

}
