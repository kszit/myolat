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
 * Copyright (c) frentix GmbH<br>
 * http://www.frentix.com<br>
 * <p>
 */

package org.olat.data.filebrowser.thumbnail;

import org.olat.data.commons.vfs.VFSLeaf;
import org.olat.system.commons.configuration.ConfigOnOff;

/**
 * Description:<br>
 * Thumbnail service
 * <P>
 * Initial Date: 30 mar. 2010 <br>
 * 
 * @author srosse, stephane.rosse@frentix.com
 */
public interface ThumbnailService extends ConfigOnOff {

    /**
     * Can the service generate a thumbnail for this thumbnail
     * 
     * @param file
     * @return
     */
    public boolean isThumbnailPossible(VFSLeaf file);

    /**
     * Generate the thumbnail
     * 
     * @param file
     *            to thumbnail
     * @param thumbnailFile
     *            where the thumbnail will be saved
     * @param maxWidth
     * @param maxHeight
     * @return the real size of the thumbnail
     */
    public FinalSize generateThumbnail(VFSLeaf file, VFSLeaf thumbnailFile, int maxWidth, int maxHeight) throws CannotGenerateThumbnailException;
}
