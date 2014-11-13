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
package org.olat.data.commentandrate;

import org.olat.data.basesecurity.Identity;
import org.olat.data.commons.database.PersistentObject;
import org.olat.system.commons.resource.OLATResourceable;

/**
 * Description:<br>
 * Implemenation of the user rating class
 * <P>
 * Initial Date: 23.11.2009 <br>
 * 
 * @author gnaegi
 */
public class UserRatingImpl extends PersistentObject implements UserRating {
    private String resName;
    private Long resId;
    private String resSubPath;

    private Identity creator;
    private Integer rating;

    /**
     * Default constructor for hibernate
     */
    private UserRatingImpl() {
        //
    }

    /**
     * Package constructor to create a new user rating with the given arguments
     * 
     * @param ores
     * @param subpath
     * @param creator
     * @param ratingValue
     */
    UserRatingImpl(OLATResourceable ores, String subpath, Identity creator, Integer ratingValue) {
        this.creator = creator;
        this.resName = ores.getResourceableTypeName();
        this.resId = ores.getResourceableId();
        this.resSubPath = subpath;
        this.rating = ratingValue;
    }

    /**
	 */
    @Override
    public Identity getCreator() {
        return creator;
    }

    /**
	 */
    @Override
    public Long getResId() {
        return this.resId;
    }

    /**
	 */
    @Override
    public String getResName() {
        return this.resName;
    }

    /**
	 */
    @Override
    public String getResSubPath() {
        return this.resSubPath;
    }

    /**
	 */
    @Override
    public Integer getRating() {
        return this.rating;
    }

    /**
     * Set the resource subpath
     * 
     * @param resSubPath
     */
    public void setResSubPath(String resSubPath) {
        this.resSubPath = resSubPath;
    }

    /**
     * Set the resoruce name
     * 
     * @param resName
     */
    public void setResName(String resName) {
        this.resName = resName;
    }

    /**
     * Set the resource ID
     * 
     * @param resId
     */
    public void setResId(Long resId) {
        this.resId = resId;
    }

    /**
	 */
    @Override
    public void setCreator(Identity creator) {
        this.creator = creator;
    }

    /**
	 */
    @Override
    public void setRating(Integer ratingValue) {
        this.rating = ratingValue;
    }

}
