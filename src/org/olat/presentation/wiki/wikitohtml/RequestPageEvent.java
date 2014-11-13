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

package org.olat.presentation.wiki.wikitohtml;

import org.olat.system.event.Event;

/**
 * Description:<br>
 * A click on a wiki topic that exists fires this event, that says that a user wants to view this page.
 * <P>
 * Initial Date: May 22, 2006 <br>
 * 
 * @author guido
 */
public class RequestPageEvent extends Event {

    public RequestPageEvent(final String pageId) {
        super(pageId);
    }

}
