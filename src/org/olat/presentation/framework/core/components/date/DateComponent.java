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
package org.olat.presentation.framework.core.components.date;

import java.util.Date;

import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.Component;
import org.olat.presentation.framework.core.components.ComponentRenderer;

/**
 * Description:<br>
 * The date component can display a date using a little calendar symbol
 * <P>
 * Initial Date: 01.12.2009 <br>
 * 
 * @author gnaegi
 */
public class DateComponent extends Component {
    private static final ComponentRenderer RENDERER = new DateComponentRenderer();
    private Date date;
    private boolean showYear;

    /**
     * Package constructor, use the factory instead
     * 
     * @param name
     * @param date
     * @param showYear
     */
    DateComponent(String name, Date date, boolean showYear) {
        super(name);
        this.setDate(date);
        this.setShowYear(showYear);
    }

    /**
	 */
    @Override
    protected void doDispatchRequest(UserRequest ureq) {
        // currently nothing to dispatch
    }

    /**
	 */
    @Override
    public ComponentRenderer getHTMLRendererSingleton() {
        return RENDERER;
    }

    /**
     * Set a new date to display
     * 
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
        this.setDirty(true);
    }

    /**
     * Get the current date
     * 
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * Define if year should be displayed or not
     * 
     * @param showYear
     */
    public void setShowYear(boolean showYear) {
        this.showYear = showYear;
        this.setDirty(true);
    }

    /**
     * @return true: display year; false: only display day and month
     */
    public boolean isShowYear() {
        return showYear;
    }

}
