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

package org.olat.presentation.framework.core.components.form.flexible.impl.elements.richText.plugins.olatmatheditor;

import java.util.HashMap;
import java.util.Map;

import org.olat.presentation.framework.core.components.form.flexible.impl.elements.richText.RichTextConfiguration;
import org.olat.presentation.framework.core.components.form.flexible.impl.elements.richText.plugins.TinyMCECustomPlugin;
import org.olat.presentation.framework.dispatcher.StaticMediaDispatcher;

/**
 * Provides a plugin for the rich text editor to edit math formulae in LaTeX syntax.
 * <P>
 * Initial Date: Jun 12, 2009 <br>
 * 
 * @author twuersch
 */
public class OlatMathEditorPlugin extends TinyMCECustomPlugin {

    /**
     * Params handed over to the js code
     */
    private static final String PARAM_JS_MATH_LIB_BASE_PATH = "jsMathLibBasePath";
    private static final String PARAM_TRANSPARENT_IMAGE = "transparentImage";

    /** The TinyMCE plugin name */
    public static final String PLUGIN_NAME = "olatmatheditor";

    /** The TinyMCE button name for this plugin */
    public static final String BUTTONS = "olatmatheditor";

    /** Tells TinyMCE which menu bar to add this plugin button to */
    private static final String BUTTONS_LOCATION = RichTextConfiguration.THEME_ADVANCED_BUTTONS2_ADD;

    /**
	 */
    @Override
    public String getPluginButtons() {
        return BUTTONS;
    }

    /**
	 */
    @Override
    public String getPluginButtonsLocation() {
        return BUTTONS_LOCATION;
    }

    /**
	 */
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }

    /**
     * Decides in which configurations the math editor plugin is available (default: in all "full" profiles).
     * 
     */
    @Override
    public boolean isEnabledForProfile(int profile) {
        // Check configuration now
        switch (profile) {
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_MINIMALISTIC:
            return false;
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_SIMPLE:
            return true;
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_SIMPLE_WITH_MEDIABROWSER:
            return true;
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_FULL:
            return true;
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_FULL_WITH_MEDIABROWSER:
            return true;
        case RichTextConfiguration.CONFIG_PROFILE_FILE_EDITOR_FULL:
            return true;
        case RichTextConfiguration.CONFIG_PROFILE_FILE_EDITOR_FULL_WITH_MEDIABROWSER:
            return true;
        default:
            // not enabled by default
            return false;
        }
    }

    /**
	 */
    @Override
    public int getPluginButtonsRowForProfile(int profile) {
        // Check configuration now
        switch (profile) {
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_MINIMALISTIC:
            return -1;
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_SIMPLE:
            return 2;
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_SIMPLE_WITH_MEDIABROWSER:
            return 2;
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_FULL:
            return 3;
        case RichTextConfiguration.CONFIG_PROFILE_FORM_EDITOR_FULL_WITH_MEDIABROWSER:
            return 3;
        case RichTextConfiguration.CONFIG_PROFILE_FILE_EDITOR_FULL:
            return 3;
        case RichTextConfiguration.CONFIG_PROFILE_FILE_EDITOR_FULL_WITH_MEDIABROWSER:
            return 3;
        default:
            // not enabled by default
            return -1;
        }
    }

    @Override
    public Map<String, String> getPluginParameters() {

        // Create only if not already present.
        Map<String, String> params = super.getPluginParameters();
        if (params != null) {
            return params;
        } else {
            params = new HashMap<String, String>();

            // Get static URI for transparent GIF.
            params.put(PARAM_TRANSPARENT_IMAGE, StaticMediaDispatcher.createStaticURIFor("images/transparent.gif", false));

            // Get static URI for jsMath library.
            params.put(PARAM_JS_MATH_LIB_BASE_PATH, StaticMediaDispatcher.createStaticURIFor("js/jsMath/", false));

            setPluginParameters(params);
            return params;
        }
    }
}
