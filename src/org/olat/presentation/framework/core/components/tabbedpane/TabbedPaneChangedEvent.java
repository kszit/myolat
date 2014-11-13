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

package org.olat.presentation.framework.core.components.tabbedpane;

import org.olat.presentation.framework.core.components.Component;
import org.olat.system.event.Event;

/**
 * @author Mike Stock
 */
public class TabbedPaneChangedEvent extends Event {

    /**
     * <code>TAB_CHANGED</code>
     */
    public static String TAB_CHANGED = "tabChanged";

    private Component oldComponent;
    private Component newComponent;

    /**
     * @param oldComponent
     * @param newComponent
     */
    public TabbedPaneChangedEvent(Component oldComponent, Component newComponent) {
        super(TAB_CHANGED);
        this.oldComponent = oldComponent;
        this.newComponent = newComponent;
    }

    /**
     * @return Returns the newComponent.
     */
    public Component getNewComponent() {
        return newComponent;
    }

    /**
     * @return Returns the oldComponent.
     */
    public Component getOldComponent() {
        return oldComponent;
    }
}
