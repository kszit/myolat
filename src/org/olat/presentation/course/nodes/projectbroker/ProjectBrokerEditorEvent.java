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
package org.olat.presentation.course.nodes.projectbroker;

import org.olat.data.course.nodes.projectbroker.Project;
import org.olat.system.event.Event;

/**
 * @author Christian Guretzki
 */
public class ProjectBrokerEditorEvent extends Event {
    private static final long serialVersionUID = 5928305911095490482L;
    private final Project project;
    public static String CANCEL_NEW_PROJECT = "cancel_new_project";
    public static String CREATED_NEW_PROJECT = "created_new_project";
    public static String CHANGED_PROJECT = "changed_project";
    public static String DELETED_PROJECT = "deleted_project";

    /**
     * @param command
     */
    public ProjectBrokerEditorEvent(final Project project, String command) {
        super(command);
        this.project = project;
    }

    /**
     * @return the project (never null)
     */
    public Project getProject() {
        return project;
    }

    public boolean isCancelEvent() {
        return getCommand().equals(CANCEL_NEW_PROJECT);
    }

    public boolean isCreateEvent() {
        return getCommand().equals(CREATED_NEW_PROJECT);
    }

    public boolean isChangedEvent() {
        return getCommand().equals(CHANGED_PROJECT);
    }

    public boolean isDeletedEvent() {
        return getCommand().equals(DELETED_PROJECT);
    }

}
