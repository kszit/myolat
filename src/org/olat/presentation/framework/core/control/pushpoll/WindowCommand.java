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

package org.olat.presentation.framework.core.control.pushpoll;

import org.olat.presentation.framework.core.control.WindowBackOffice;
import org.olat.presentation.framework.core.control.winmgr.Command;

/**
 * Description:<br>
 * Initial Date: 24.03.2006 <br>
 * 
 * @author Felix Jost
 */
public class WindowCommand {
    private WindowBackOffice wbo;
    private Command command;

    /**
     * @param window
     * @param command
     */
    public WindowCommand(WindowBackOffice wbo, Command command) {
        this.wbo = wbo;
        this.command = command;
    }

    /**
     * @return Returns the command.
     */
    public Command getCommand() {
        return command;
    }

    /**
     * @return Returns the windowbackoffice
     */
    public WindowBackOffice getWindowBackOffice() {
        return wbo;
    }
}
