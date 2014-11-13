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
package org.olat.presentation.portfolio.structel.edit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.olat.data.portfolio.restriction.CollectRestriction;
import org.olat.data.portfolio.restriction.RestrictionsConstants;
import org.olat.data.portfolio.structure.EPStructureElement;
import org.olat.data.portfolio.structure.EPStructuredMapTemplate;
import org.olat.data.portfolio.structure.PortfolioStructure;
import org.olat.lms.portfolio.EPFrontendManager;
import org.olat.lms.portfolio.PortfolioAbstractHandler;
import org.olat.lms.portfolio.artefacthandler.EPArtefactHandler;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.form.flexible.FormItem;
import org.olat.presentation.framework.core.components.form.flexible.FormItemContainer;
import org.olat.presentation.framework.core.components.form.flexible.elements.FormLink;
import org.olat.presentation.framework.core.components.form.flexible.elements.RichTextElement;
import org.olat.presentation.framework.core.components.form.flexible.elements.SingleSelection;
import org.olat.presentation.framework.core.components.form.flexible.elements.StaticTextElement;
import org.olat.presentation.framework.core.components.form.flexible.elements.TextElement;
import org.olat.presentation.framework.core.components.form.flexible.impl.Form;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormBasicController;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormEvent;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormLayoutContainer;
import org.olat.presentation.framework.core.components.link.Link;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.presentation.framework.core.translator.PackageTranslator;
import org.olat.presentation.portfolio.filter.PortfolioFilterController;
import org.olat.presentation.portfolio.structel.EPMapViewController;
import org.olat.presentation.portfolio.structel.EPStructureEvent;
import org.olat.system.commons.StringHelper;
import org.olat.system.logging.log4j.LoggerHelper;
import org.olat.system.spring.CoreSpringFactory;

/**
 * Description:<br>
 * edit the title and details/description of a PortfolioStructure
 * <P>
 * Initial Date: 07.10.2010 <br>
 * 
 * @author Roman Haag, roman.haag@frentix.com, http://www.frentix.com
 */
public class EPStructureDetailsController extends FormBasicController {

    private static final Logger log = LoggerHelper.getLogger();
    private final EPFrontendManager ePFMgr;
    private final PortfolioAbstractHandler portfolioModule;
    private final PortfolioStructure rootStructure;
    private PortfolioStructure editStructure;
    public static final String VIEWMODE_TABLE = "table";
    public static final String VIEWMODE_MINI = "miniview";

    private TextElement titleEl;
    private RichTextElement descriptionEl;
    private StaticTextElement noEditInfo;
    private List<SingleSelection> restrictionElements;
    private List<SingleSelection> restrictToArtefactElements;
    private List<TextElement> amountElements;
    private List<CollectRestriction> collectRestrictions;
    private SingleSelection viewRadio;
    private List<StaticTextElement> errorElements;

    public EPStructureDetailsController(final UserRequest ureq, final WindowControl wControl, final Form rootForm, final PortfolioStructure rootStructure) {
        super(ureq, wControl, FormBasicController.LAYOUT_DEFAULT, null, rootForm);

        final PackageTranslator pt = new PackageTranslator(EPMapViewController.class.getPackage().getName(), ureq.getLocale(), getTranslator());
        this.flc.setTranslator(pt);
        ePFMgr = (EPFrontendManager) CoreSpringFactory.getBean(EPFrontendManager.class);
        portfolioModule = (PortfolioAbstractHandler) CoreSpringFactory.getBean(PortfolioAbstractHandler.class);
        this.rootStructure = rootStructure;
        this.editStructure = rootStructure;
        // work on a copy of the list in case of cancel
        collectRestrictions = new ArrayList<CollectRestriction>(editStructure.getCollectRestrictions());

        initForm(ureq);
    }

    public void setNewStructure(final UserRequest ureq, final PortfolioStructure struct) {
        editStructure = struct;
        collectRestrictions = new ArrayList<CollectRestriction>(editStructure.getCollectRestrictions());
        updateUI(ureq);
    }

    protected void updateUI(final UserRequest ureq) {
        initForm(ureq);
    }

    /**
     * org.olat.presentation.framework.control.Controller, org.olat.presentation.framework.UserRequest)
     */
    @SuppressWarnings("unused")
    @Override
    protected void initForm(final FormItemContainer formLayout, final Controller listener, final UserRequest ureq) {

        if (formLayout.getFormComponent("struct.title") != null) {
            formLayout.remove("struct.title");
        }
        titleEl = uifactory.addTextElement("struct.title", "struct.title", 512, editStructure.getTitle(), formLayout);
        titleEl.setNotEmptyCheck("map.title.not.empty");
        titleEl.setMandatory(true);

        // choose representation mode (table or minimized artefact-view)
        if (formLayout.getFormComponent("view.mode") != null) {
            formLayout.remove("view.mode");
        }
        final String[] theKeys = new String[] { VIEWMODE_TABLE, VIEWMODE_MINI };
        final String[] theValues = new String[] { translate("view.mode." + VIEWMODE_TABLE), translate("view.mode." + VIEWMODE_MINI) };
        viewRadio = uifactory.addRadiosHorizontal("view.mode", formLayout, theKeys, theValues);
        final String artRepMode = editStructure.getArtefactRepresentationMode();
        if (artRepMode != null) {
            viewRadio.select(artRepMode, true);
        } else {
            viewRadio.select(VIEWMODE_MINI, true);
        }

        if (formLayout.getFormComponent("struct.description") != null) {
            formLayout.remove("struct.description");
        }
        descriptionEl = uifactory.addRichTextElementForStringDataMinimalistic("struct.description", "struct.description", editStructure.getDescription(), -1, -1, false,
                formLayout, ureq.getUserSession(), getWindowControl());
        descriptionEl.setMaxLength(2047);
        descriptionEl.setNotLongerThanCheck(2047, "map.description.too.long");

        // hint for no edit options
        if (formLayout.getFormComponent("noEditInfo") != null) {
            formLayout.remove("noEditInfo");
        }
        noEditInfo = uifactory.addStaticTextElement("noEditInfo", "no.edit.info.label", translate("no.edit.info"), formLayout);
        noEditInfo.setVisible(false);

        if (formLayout.getFormComponent("collect.restriction") != null) {
            formLayout.remove("collect.restriction");
        }
        // show restrictions only for templates and on page/structure-level, as artefacts are not linkable on maps itself
        if (editStructure instanceof EPStructureElement && rootStructure instanceof EPStructuredMapTemplate && editStructure.getRoot() != null) {
            final FormLayoutContainer collectContainer = FormLayoutContainer.createCustomFormLayout("collect.restriction", getTranslator(), velocity_root
                    + "/restrictions.html");
            collectContainer.setRootForm(mainForm);
            collectContainer.setLabel("collect.restriction", null);
            formLayout.add(collectContainer);

            final String[] restrictionKeys = new String[] { "", RestrictionsConstants.MAX, RestrictionsConstants.EQUAL, RestrictionsConstants.MIN };
            final String[] restrictionValues = new String[restrictionKeys.length];
            restrictionValues[0] = "";
            for (int i = 1; i < restrictionKeys.length; i++) {
                restrictionValues[i] = translate("restriction." + restrictionKeys[i]);
            }

            final List<EPArtefactHandler<?>> handlers = portfolioModule.getArtefactHandlers(); // allow only to use enabled handlers
            final String[] artefactKeys = new String[handlers.size() + 1];
            final String[] artefactValues = new String[artefactKeys.length];
            artefactValues[0] = artefactKeys[0] = "";
            for (int i = 0; i < handlers.size(); i++) {
                final EPArtefactHandler<?> handler = handlers.get(i);
                artefactKeys[i + 1] = handler.getType();
                final String handlerClass = PortfolioFilterController.HANDLER_PREFIX + handler.getClass().getSimpleName()
                        + PortfolioFilterController.HANDLER_TITLE_SUFFIX;
                artefactValues[i + 1] = handler.getHandlerTranslator(getTranslator()).translate(handlerClass);
            }

            if (collectRestrictions.isEmpty()) {
                collectRestrictions.add(new CollectRestriction());
            }

            restrictionElements = new ArrayList<SingleSelection>();
            restrictToArtefactElements = new ArrayList<SingleSelection>();
            amountElements = new ArrayList<TextElement>();
            errorElements = new ArrayList<StaticTextElement>();

            final List<String> counts = new ArrayList<String>();
            for (final CollectRestriction restriction : collectRestrictions) {
                final int count = restrictionElements.size();

                final SingleSelection restrictionElement = uifactory.addDropdownSingleselect("collect.restriction.restriction." + count, "", collectContainer,
                        restrictionKeys, restrictionValues, null);
                restrictionElement.setMandatory(true);
                if (restriction != null && StringHelper.containsNonWhitespace(restriction.getRestriction())) {
                    restrictionElement.select(restriction.getRestriction(), true);
                }
                restrictionElement.setUserObject(restriction);

                final SingleSelection restrictToArtefactElement = uifactory.addDropdownSingleselect("collect.restriction.artefacts." + count, "", collectContainer,
                        artefactKeys, artefactValues, null);
                restrictToArtefactElement.setMandatory(true);
                if (restriction != null && StringHelper.containsNonWhitespace(restriction.getArtefactType())) {
                    restrictToArtefactElement.select(restriction.getArtefactType(), true);
                }

                String amountStr = "";
                if (restriction != null && restriction.getAmount() > 0) {
                    amountStr = Integer.toString(restriction.getAmount());
                }
                final TextElement amountElement = uifactory.addTextElement("collect.restriction.amount." + count, "", 2, amountStr, collectContainer);
                amountElement.setDisplaySize(3);

                StaticTextElement errorElement = uifactory.addStaticTextElement("collect.restriction.error." + count, "", collectContainer);
                errorElement.setVisible(false);

                restrictionElements.add(restrictionElement);
                restrictToArtefactElements.add(restrictToArtefactElement);
                amountElements.add(amountElement);
                errorElements.add(errorElement);

                final FormLink addLink = uifactory.addFormLink("collect.restriction.add." + count, "collect.restriction.add", "collect.restriction.add",
                        collectContainer, Link.BUTTON_SMALL);
                addLink.setUserObject(restriction);
                final FormLink delLink = uifactory.addFormLink("collect.restriction.del." + count, "collect.restriction.delete", "collect.restriction.delete",
                        collectContainer, Link.BUTTON_SMALL);
                delLink.setUserObject(restriction);

                counts.add(Integer.toString(count));
            }
            collectContainer.contextPut("counts", counts);
        }

        if (formLayout.getFormComponent("save") != null) {
            formLayout.remove("save");
        }
        uifactory.addFormSubmitButton("save", formLayout);
    }

    public FormItem getInitialFormItem() {
        return flc;
    }

    /**
     * @see org.olat.core.gui.components.form.flexible.impl.FormBasicController#validateFormLogic(org.olat.core.gui.UserRequest)
     */
    @Override
    protected boolean validateFormLogic(UserRequest ureq) {
        if (rootStructure instanceof EPStructuredMapTemplate && restrictionElements != null) {
            setCollectRestrictions();
            clearErrors();
            ArrayList<String> usedTypes = new ArrayList<String>();
            int i = 0;
            boolean hasError = false;
            for (SingleSelection restrictionElement : restrictionElements) {
                CollectRestriction restriction = (CollectRestriction) restrictionElement.getUserObject();
                if (usedTypes.contains(restriction.getArtefactType())) {
                    StaticTextElement thisErrorEl = errorElements.get(i);
                    thisErrorEl.setVisible(true);
                    thisErrorEl.setValue(translate("collect.restriction.duplicate.type"));
                    hasError = true;
                }
                usedTypes.add(restriction.getArtefactType());

                boolean hasRestriction = StringHelper.containsNonWhitespace(restriction.getRestriction());
                boolean hasArtType = StringHelper.containsNonWhitespace(restriction.getArtefactType());
                boolean hasAmount = restriction.getAmount() > 0;
                boolean isValid = restriction.isValid();
                if (!isValid && (hasRestriction || hasArtType || hasAmount)) {
                    StaticTextElement thisErrorEl = errorElements.get(i);
                    thisErrorEl.setVisible(true);
                    thisErrorEl.setValue(translate("collect.restriction.incomplete"));
                    hasError = true;
                }
                i++;
            }
            return !hasError;
        }
        return true;
    }

    private void clearErrors() {
        for (StaticTextElement errorElement : errorElements) {
            errorElement.setVisible(false);
        }
    }

    /**
	 */
    @Override
    protected void formOK(final UserRequest ureq) {
        editStructure = ePFMgr.loadPortfolioStructureByKey(editStructure.getKey());
        editStructure.setTitle(titleEl.getValue());
        editStructure.setDescription(descriptionEl.getValue());
        editStructure.setArtefactRepresentationMode(viewRadio.getSelectedKey());

        if (rootStructure instanceof EPStructuredMapTemplate && restrictionElements != null) {
            clearErrors();
            editStructure.getCollectRestrictions().clear();
            setCollectRestrictions();
            for (final SingleSelection restrictionElement : restrictionElements) {
                final CollectRestriction restriction = (CollectRestriction) restrictionElement.getUserObject();
                if (restriction.isValid()) {
                    final CollectRestriction cr = new CollectRestriction(restriction);
                    editStructure.getCollectRestrictions().add(cr);
                }
            }
        }

        ePFMgr.savePortfolioStructure(editStructure);
        fireEvent(ureq, new EPStructureEvent(EPStructureEvent.CHANGE, editStructure));
    }

    @Override
    protected void formInnerEvent(final UserRequest ureq, final FormItem source, final FormEvent event) {
        if (source instanceof FormLink && source.getUserObject() instanceof CollectRestriction) {
            final CollectRestriction restriction = (CollectRestriction) source.getUserObject();
            if (source.getName().startsWith("collect.restriction.add.")) {
                addCollectRestriction(restriction);
            } else if (source.getName().startsWith("collect.restriction.del.")) {
                deleteCollectRestriction(restriction);
            }
            // secure title and description before redraw UI
            editStructure.setTitle(titleEl.getValue());
            editStructure.setDescription(descriptionEl.getValue());
            editStructure.setArtefactRepresentationMode(viewRadio.getSelectedKey());
            setCollectRestrictions();
            updateUI(ureq);
        }
        super.formInnerEvent(ureq, source, event);
    }

    protected void setCollectRestrictions() {
        if (restrictionElements == null || restrictionElements.isEmpty()) {
            return;
        }

        for (int i = 0; i < restrictionElements.size(); i++) {
            final SingleSelection restrictionElement = restrictionElements.get(i);
            final SingleSelection restrictToArtefactElement = restrictToArtefactElements.get(i);
            final TextElement amountElement = amountElements.get(i);
            final CollectRestriction cr = (CollectRestriction) restrictionElement.getUserObject();
            final String restriction = restrictionElement.getSelectedKey();
            final String artefactType = restrictToArtefactElement.getSelectedKey();
            final String amount = amountElement.getValue();

            cr.setRestriction(restriction);
            cr.setArtefactType(artefactType);
            if (StringHelper.containsNonWhitespace(amount)) {
                try {
                    cr.setAmount(Integer.parseInt(amount));
                } catch (final NumberFormatException e) {
                    log.warn("Wrong format for number", e);
                }
            }
        }

    }

    protected void addCollectRestriction(final CollectRestriction restriction) {
        final int index = collectRestrictions.indexOf(restriction);
        if (index + 1 < collectRestrictions.size()) {
            collectRestrictions.add(index + 1, new CollectRestriction());
        } else {
            collectRestrictions.add(new CollectRestriction());
        }
    }

    protected void deleteCollectRestriction(final CollectRestriction restriction) {
        collectRestrictions.remove(restriction);
    }

    /**
	 */
    @Override
    protected void doDispose() {
        // nothing to dispose

    }

    // disable all formitems expect a hint, when nothing was selected or an artefact was selected
    public void setNoStructure() {
        editStructure = null;
        final Map<String, FormItem> comps = flc.getFormComponents();
        for (final Iterator<Entry<String, FormItem>> iterator = comps.entrySet().iterator(); iterator.hasNext();) {
            final Entry<String, FormItem> entry = iterator.next();
            entry.getValue().setVisible(false);
        }
        noEditInfo.setVisible(true);
        flc.setDirty(true);
    }
}
