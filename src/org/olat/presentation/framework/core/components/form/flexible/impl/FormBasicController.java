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
package org.olat.presentation.framework.core.components.form.flexible.impl;

import java.util.Map;

import org.olat.lms.activitylogging.ThreadLocalUserActivityLoggerInstaller;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.Component;
import org.olat.presentation.framework.core.components.form.flexible.FormItem;
import org.olat.presentation.framework.core.components.form.flexible.FormItemContainer;
import org.olat.presentation.framework.core.components.form.flexible.FormUIFactory;
import org.olat.presentation.framework.core.components.form.flexible.elements.InlineElement;
import org.olat.presentation.framework.core.components.panel.Panel;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.Disposable;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.presentation.framework.core.control.controller.BasicController;
import org.olat.presentation.framework.core.translator.Translator;
import org.olat.system.event.Event;
import org.olat.system.exception.AssertException;

/**
 * Description:<br>
 * The form basic controller acts as a facade for <tt>Flexi.Form</tt> generation.
 * <ul>
 * <li>subclass it, and generate a constructor calling <tt>super(..)</tt></li>
 * <li>add also <tt>initForm(this.flc, this, ureq)</tt> to your constructor as a last line</li>
 * <li>if you want your form layout, provide the velocity page name in the <tt>super(..,name)</tt> call.</li>
 * <li>add your desired form elements in the <tt>initForm(..)</tt> method implementaion</li>
 * <li>add your complex business validation logic by overriding the method <tt>validateFormLogic(..)</tt></li>
 * <li>add your code to read form values and process them further in the <tt>formOK(..)</tt> method implementation</li>
 * <li>in complex forms with subworkflows the <tt>formInnerEvent(..)</tt> method can be overwritten and used the same way as known from <tt>event(..)</tt>.</li>
 * </ul>
 * <P>
 * Initial Date: 01.02.2007 <br>
 * 
 * @author patrickb
 */
public abstract class FormBasicController extends BasicController {

    public static final int LAYOUT_DEFAULT = 0;
    public static final int LAYOUT_HORIZONTAL = 1;
    public static final int LAYOUT_VERTICAL = 2;
    public static final int LAYOUT_CUSTOM = 3;

    protected FormLayoutContainer flc;

    protected Form mainForm;

    protected Panel initialPanel;

    protected FormUIFactory uifactory = FormUIFactory.getInstance();

    public FormBasicController(UserRequest ureq, WindowControl wControl) {
        this(ureq, wControl, null);
    }

    public FormBasicController(UserRequest ureq, WindowControl wControl, String pageName) {
        super(ureq, wControl);
        constructorInit(pageName);
    }

    public FormBasicController(UserRequest ureq, WindowControl wControl, String pageName, Translator fallbackTranslator) {
        super(ureq, wControl, fallbackTranslator);
        constructorInit(pageName);
    }

    protected FormBasicController(UserRequest ureq, WindowControl wControl, int layout) {
        super(ureq, wControl);
        if (layout == LAYOUT_HORIZONTAL) {
            // init with horizontal layout
            flc = FormLayoutContainer.createHorizontalFormLayout("ffo_horizontal", getTranslator());
            mainForm = Form.create("ffo_main_horizontal", flc, this);

        } else if (layout == LAYOUT_VERTICAL) {
            // init with vertical layout
            flc = FormLayoutContainer.createVerticalFormLayout("ffo_vertical", getTranslator());
            mainForm = Form.create("ffo_main_vertical", flc, this);

        } else if (layout == LAYOUT_CUSTOM) {
            throw new AssertException("Use another constructor to work with a custom layout!");

        } else {
            // init with default layout
            flc = FormLayoutContainer.createDefaultFormLayout("ffo_default", getTranslator());
            mainForm = Form.create("ffo_main_default", flc, this);
        }
        initialPanel = putInitialPanel(mainForm.getInitialComponent());
    }

    protected FormBasicController(UserRequest ureq, WindowControl wControl, int layout, String customLayoutPageName, Form externalMainForm) {
        super(ureq, wControl);
        if (layout == LAYOUT_HORIZONTAL) {
            // init with horizontal layout
            flc = FormLayoutContainer.createHorizontalFormLayout("ffo_horizontal", getTranslator());

        } else if (layout == LAYOUT_VERTICAL) {
            // init with vertical layout
            flc = FormLayoutContainer.createVerticalFormLayout("ffo_vertical", getTranslator());

        } else if (layout == LAYOUT_CUSTOM && customLayoutPageName != null) {
            // init with provided layout
            String vc_pageName = velocity_root + "/" + customLayoutPageName + ".html";
            flc = FormLayoutContainer.createCustomFormLayout("ffo_" + customLayoutPageName + this.hashCode(), getTranslator(), vc_pageName);

        } else {
            // init with default layout
            flc = FormLayoutContainer.createDefaultFormLayout("ffo_default", getTranslator());
        }
        // instead of the constructorInit's Form.create... use a supplied one
        mainForm = externalMainForm;
        flc.setRootForm(externalMainForm);
        mainForm.addSubFormListener(this);
        initialPanel = putInitialPanel(flc.getComponent());
    }

    /**
     * should be rarely overwritten, only if you provide infrastructure around flexi forms
     */
    protected void constructorInit(String pageName) {
        String ffo_pagename = null;
        if (pageName != null) {
            // init with provided layout
            String vc_pageName = velocity_root + "/" + pageName + ".html";
            ffo_pagename = "ffo_" + pageName;
            flc = FormLayoutContainer.createCustomFormLayout(ffo_pagename, getTranslator(), vc_pageName);
        } else {
            // init with default layout
            ffo_pagename = "ffo_default";
            flc = FormLayoutContainer.createDefaultFormLayout(ffo_pagename, getTranslator());
        }
        //
        mainForm = Form.create("ffo_main_" + pageName, flc, this);
        /*
         * implementor must call initFormElements(...)
         */
        initialPanel = putInitialPanel(mainForm.getInitialComponent());
    }

    protected void initForm(UserRequest ureq) {
        initForm(this.flc, this, ureq);
    }

    /**
     * The creation initialisation and adding form elements to layout happens here.<br>
     * The method is not called automatically, but it should be the last line in your constructor.<br>
     * 
     * @param formLayout
     * @param listener
     * @param ureq
     */
    abstract protected void initForm(FormItemContainer formLayout, Controller listener, UserRequest ureq);

    @Override
    protected void removeAsListenerAndDispose(Controller controller) {
        super.removeAsListenerAndDispose(controller);

        if (controller instanceof FormBasicController) {
            FormLayoutContainer container = ((FormBasicController) controller).flc;
            if (flc.getRootForm() == container.getRootForm()) {
                flc.getRootForm().removeSubFormListener((FormBasicController) controller);
            }
        }
    }

    /**
     * The form first validates each element, then it calls the <tt>boolean validateFormLogic(ureq)</tt> from the listening controller. This gives the possibility to
     * implement complex business logic checks.<br>
     * In the case of valid elements and a valid form logic this method is called. Typically one will read and save/update values then.
     */
    abstract protected void formOK(UserRequest ureq);

    /**
     * called if form validation was not ok.<br>
     * default implementation does nothing. Each element is assumed to set errormessages. Needed only if complex business logic is checked even
     */
    protected void formNOK(UserRequest ureq) {
        // by default nothing to do -> e.g. looping until form is ok
    }

    /**
     * Called when a form cancel button has been pressed. The form will automatically be resetted.
     * 
     * @param ureq
     */
    protected void formCancelled(UserRequest ureq) {
        // by default nothing to do
    }

    /**
     * called if an element was activated resetting the form elements to their initial values. After all resetting took place this method is called.
     * 
     * @param ureq
     */
    protected void formResetted(UserRequest ureq) {
        // by default no cleanup is needed outside of the form after resetting
    }

    /**
     * called if an element inside of the form triggered an event
     * 
     * @param source
     * @param event
     */
    protected void formInnerEvent(UserRequest ureq, FormItem source, FormEvent event) {
        // overwrite if you want to listen to inner form elements events
    }

    /**
	 */
    @Override
    protected void event(UserRequest ureq, Component source, Event event) {
        if (source == mainForm.getInitialComponent()) {
            // general form events
            if (event == Form.EVNT_VALIDATION_OK) {
                // Set container dirty to remove potentially rendered error messages. Do
                // this before calling formOK() to let formOK override the dirtiness
                // flag
                this.flc.setDirty(true);
                formOK(ureq);
            } else if (event == Form.EVNT_VALIDATION_NOK) {
                // Set container dirty to rendered error messages. Do this before calling
                // formNOK() to let formNOK override the dirtiness flag
                this.flc.setDirty(true);
                formNOK(ureq);
            } else if (event == FormEvent.RESET) {
                // Set container dirty to render everything from scratch, remove error
                // messages. Do this before calling
                // formResetted() to let formResetted override the dirtiness flag
                this.flc.setDirty(true);
                formResetted(ureq);
            } else if (event instanceof FormEvent) {
                FormEvent fe = (FormEvent) event;
                // Special case: cancel events are wrapped as form inner events
                if (fe.getCommand().equals(Form.EVNT_FORM_CANCELLED.getCommand())) {
                    // Set container dirty to clear error messages. Do this before calling
                    // formCancelled() to let formCancelled override the dirtiness flag
                    this.flc.setDirty(true);
                    formResetted(ureq);
                    formCancelled(ureq);
                    return;
                }
                /*
                 * evaluate normal inner form events
                 */
                FormItem fiSrc = fe.getFormItemSource();
                // check for InlineElments
                if (fiSrc instanceof InlineElement) {
                    this.flc.setDirty(true);
                }
                //
                formInnerEvent(ureq, fiSrc, fe);
                // no need to set container dirty, up to controller code if something is dirty
            }
        }
    }

    /**
     * Set an optional form title that is rendered as a fieldset legend. If you use a custom template this will have no effect
     * 
     * @param i18nKey
     */
    protected void setFormTitle(String i18nKey) {
        if (i18nKey == null) {
            this.flc.contextRemove("off_title");
        } else {
            this.flc.contextPut("off_title", getTranslator().translate(i18nKey));
        }
    }

    /**
     * Set an optional form title that is rendered as a fieldset legend. If you use a custom template this will have no effect
     * 
     * @param i18nKey
     * @param args
     *            optional arguments
     */
    protected void setFormTitle(String i18nKey, String[] args) {
        if (i18nKey == null) {
            this.flc.contextRemove("off_title");
        } else {
            this.flc.contextPut("off_title", getTranslator().translate(i18nKey, args));
        }
    }

    /**
     * Set an optional description. This will appear above the form. If you use a custom template this will have no effect
     * 
     * @param i18nKey
     */
    protected void setFormDescription(String i18nKey) {
        if (i18nKey == null) {
            this.flc.contextRemove("off_desc");
        } else {
            this.flc.contextPut("off_desc", getTranslator().translate(i18nKey));
        }
    }

    /**
     * Set an optional description. This will appear above the form. If you use a custom template this will have no effect
     * 
     * @param i18nKey
     * @args args optional arguments
     */
    protected void setFormDescription(String i18nKey, String[] args) {
        if (i18nKey == null) {
            this.flc.contextRemove("off_desc");
        } else {
            this.flc.contextPut("off_desc", getTranslator().translate(i18nKey, args));
        }
    }

    /**
     * Set a warning. This will appear before the form, after the description. If you use a custom template this will have no effect
     * 
     * @param i18nKey
     * @args args optional arguments
     */
    protected void setFormWarning(String i18nKey, String[] args) {
        if (i18nKey == null) {
            this.flc.contextRemove("off_warn");
        } else {
            this.flc.contextPut("off_warn", getTranslator().translate(i18nKey, args));
        }
    }

    /**
     * Set a warning. This will appear before the form, after the description. If you use a custom template this will have no effect
     * 
     * @param i18nKey
     */
    protected void setFormWarning(String i18nKey) {
        this.flc.contextRemove("off_info");
        if (i18nKey == null) {
            this.flc.contextRemove("off_warn");
        } else {
            this.flc.contextPut("off_warn", getTranslator().translate(i18nKey));
        }
    }

    /**
     * Set a message to be displayed in the form, after the description. The form warning, if there is one, will be removed.
     * 
     */

    public void setFormInfo(String i18nKey) {
        this.flc.contextRemove("off_warn");
        if (i18nKey == null) {
            this.flc.contextRemove("off_info");
        } else {
            this.flc.contextPut("off_info", getTranslator().translate(i18nKey));
        }
    }

    /**
     * Set a message to be displayed in the form, after the description. The form warning, if there is one, will be removed.
     * 
     */

    protected void setFormInfo(String i18nKey, String args) {
        this.flc.contextRemove("off_warn");
        if (i18nKey == null) {
            this.flc.contextRemove("off_info");
        } else {
            this.flc.contextPut("off_info", getTranslator().translate(i18nKey, new String[] { args }));
        }
    }

    /**
     * Set an optional context help link for this form. If you use a custom template this will have no effect
     * 
     * @param packageName
     *            The bundle name, e.g. org.olat.presentation
     * @param pageName
     *            The page name, e.g. my-helppage.html
     * @param hoverTextKey
     *            The hover text to indicate what this help is about (i18nkey)
     */
    protected void setFormContextHelp(String packageName, String pageName, String hoverTextKey) {
        if (packageName == null) {
            this.flc.contextRemove("off_chelp_package");
        } else {
            this.flc.contextPut("off_chelp_package", packageName);
            this.flc.contextPut("off_chelp_page", pageName);
            this.flc.contextPut("off_chelp_hover", hoverTextKey);
        }
    }

    /**
     * Set an optional css class to use for this form. May help to achieve custom formatting without a separate velocity container.
     * 
     * @param cssClassName
     *            the css class name to wrap around form
     */
    protected void setFormStyle(String cssClassName) {
        if (cssClassName == null) {
            this.flc.contextRemove("off_css_class");
        } else {
            this.flc.contextPut("off_css_class", cssClassName);
        }
    }

    @Override
    protected void setTranslator(Translator translator) {
        super.setTranslator(translator);
        flc.setTranslator(translator);
    }

    /**
     * @param ureq
     * @return
     */
    protected boolean validateFormLogic(UserRequest ureq) {
        // TODO Auto-generated method stub
        return true;
    }

    /**
     * Adding disposing of form items that implement the disposable interface. Some Form items might generate temporary data that needs to be cleaned up.
     * <p>
     * First, super.dispose() is called.
     * 
     */
    @Override
    public void dispose() {
        super.dispose();
        ThreadLocalUserActivityLoggerInstaller.runWithUserActivityLogger(new Runnable() {
            @Override
            public void run() {
                // Dispose also disposable form items (such as file uploads that needs to
                // cleanup temporary files)
                Map<String, FormItem> formItems = FormBasicController.this.flc.getFormComponents();
                for (FormItem formItem : formItems.values()) {
                    if (formItem instanceof Disposable) {
                        Disposable disposableFormItem = (Disposable) formItem;
                        disposableFormItem.dispose();
                    }
                }
            }
        }, getUserActivityLogger());
    }

}
