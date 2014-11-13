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

package org.olat.presentation.framework.extensions;

import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.extensions.action.ActionExtensionSecurityCallback;
import org.olat.system.commons.configuration.AbstractConfigOnOff;
import org.olat.system.commons.configuration.ConfigOnOff;
import org.olat.system.exception.AssertException;

/**
 * @author Christian Guretzki
 */
abstract public class AbstractExtension extends AbstractConfigOnOff implements ConfigOnOff {

    private int order = 0;
    private String secCallbackName = null;
    private String nodeIdentifierIfParent;
    private String parentTreeNodeIdentifier;

    public int getOrder() {
        return order;
    }

    // used by Spring
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * [ Spring ] define the name of a class implementing ActionExtensionSecurityCallback. this gets asked later on to validate a UserRequest
     */
    public void setSecurityCallbackClassName(String secCallbackName) {
        this.secCallbackName = secCallbackName;
    }

    /**
     * returns the defined securityCallback, if none was defined a default SecurityCallback is returned
     */
    public ActionExtensionSecurityCallback getActionExtensionSecurityCallback() {
        ActionExtensionSecurityCallback securityCallback = null;
        if (secCallbackName != null) {
            // try to lazy instantiate the callback Object
            Class<?> cclazz;
            Exception re = null;
            try {
                cclazz = Thread.currentThread().getContextClassLoader().loadClass(secCallbackName);
                Object o = cclazz.newInstance();
                securityCallback = (ActionExtensionSecurityCallback) o;
            } catch (ClassNotFoundException e) {
                re = e;
            } catch (SecurityException e) {
                re = e;
            } catch (IllegalArgumentException e) {
                re = e;
            } catch (InstantiationException e) {
                re = e;
            } catch (IllegalAccessException e) {
                re = e;
            } finally {
                if (re != null) {
                    throw new AssertException("Could not create ActionExtensionSecurityCallback via reflection. classname: " + secCallbackName, re);
                }
            }
        } else {
            // load a default callback
            @SuppressWarnings("unused")
            ActionExtensionSecurityCallback aescDefault = new ActionExtensionSecurityCallback() {
                @Override
                public boolean isAllowedToLaunchActionController(UserRequest ureq) {
                    return true;
                }
            };
            securityCallback = aescDefault;
        }
        return securityCallback;
    }

    /**
     * @return Returns the nodeIdentifierIfParent. if this is a parent node it should have a nodeIdentifierIfParent
     */
    public String getNodeIdentifierIfParent() {
        return nodeIdentifierIfParent;
    }

    /**
     * [ Spring ]
     * 
     * @param nodeIdentifierIfParent
     *            The nodeIdentifierIfParent to set. if this is a parent node it should have a nodeIdentifierIfParent
     */
    public void setNodeIdentifierIfParent(String nodeIdentifierIfParent) {
        this.nodeIdentifierIfParent = nodeIdentifierIfParent;
    }

    /**
     * @return Returns the parentTreeNodeIdentifier. get the corresponding parentNodeIdentifier, this actionExtension will get attached to
     */
    public String getParentTreeNodeIdentifier() {
        return parentTreeNodeIdentifier;
    }

    /**
     * [ Spring ]
     * 
     * @param parentTreeNodeIdentifier
     *            The parentTreeNodeIdentifier to set. set the corresponding parentNodeIdentifier, this actionExtension will get attached to
     */
    public void setParentTreeNodeIdentifier(String parentTreeNodeIdentifier) {
        this.parentTreeNodeIdentifier = parentTreeNodeIdentifier;
    }

}
