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
package org.olat.presentation.portfolio.artefacts.edit;

import org.olat.data.portfolio.artefact.AbstractArtefact;
import org.olat.data.portfolio.structure.PortfolioStructure;
import org.olat.data.portfolio.structure.PortfolioStructureMap;
import org.olat.data.portfolio.structure.StructureStatusEnum;
import org.olat.lms.portfolio.EPFrontendManager;
import org.olat.lms.portfolio.security.EPSecurityCallback;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.Component;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.presentation.framework.core.control.controller.BasicController;
import org.olat.presentation.framework.core.control.generic.closablewrapper.CloseableCalloutWindowController;
import org.olat.presentation.framework.core.control.generic.closablewrapper.CloseableModalWindowWrapperController;
import org.olat.presentation.framework.core.translator.PackageTranslator;
import org.olat.presentation.portfolio.artefacts.collect.EPCollectStepForm03;
import org.olat.presentation.portfolio.artefacts.collect.EPReflexionChangeEvent;
import org.olat.presentation.portfolio.artefacts.run.EPArtefactViewController;
import org.olat.presentation.portfolio.artefacts.run.EPReflexionViewController;
import org.olat.presentation.portfolio.structel.EPStructureChangeEvent;
import org.olat.system.commons.StringHelper;
import org.olat.system.event.Event;
import org.olat.system.spring.CoreSpringFactory;

/**
 * Description:<br>
 * Wrapper controller, to instantiate a reflexion-editor or viewer depending on config
 * 
 * <P>
 * Initial Date: 21.03.2011 <br>
 * 
 * @author Roman Haag, roman.haag@frentix.com, http://www.frentix.com
 */
public class EPReflexionWrapperController extends BasicController {

    private EPFrontendManager ePFMgr;
    private Controller reflexionCtrl;
    private boolean mapClosed;
    private EPSecurityCallback secCallback;
    private AbstractArtefact artefact;
    private PortfolioStructure struct;
    private CloseableModalWindowWrapperController reflexionBox;

    public EPReflexionWrapperController(UserRequest ureq, WindowControl wControl, EPSecurityCallback secCallback, AbstractArtefact artefact, PortfolioStructure struct) {
        super(ureq, wControl);
        ePFMgr = (EPFrontendManager) CoreSpringFactory.getBean("epFrontendManager");
        if (struct != null && struct.getRoot() instanceof PortfolioStructureMap) {
            mapClosed = StructureStatusEnum.CLOSED.equals(((PortfolioStructureMap) struct.getRoot()).getStatus());
        } else {
            mapClosed = false;
        }
        this.secCallback = secCallback;
        this.artefact = artefact;
        this.struct = struct;
        PackageTranslator pt = new PackageTranslator(EPArtefactViewController.class.getPackage().getName(), ureq.getLocale(), getTranslator());
        setTranslator(pt);

        init(ureq);
    }

    private void init(UserRequest ureq) {
        removeAsListenerAndDispose(reflexionCtrl);
        String title = "";
        boolean artClosed = ePFMgr.isArtefactClosed(artefact);
        if (mapClosed || !secCallback.canEditStructure() || (artClosed && struct == null)) {
            // reflexion cannot be edited, view only!
            reflexionCtrl = new EPReflexionViewController(ureq, getWindowControl(), artefact, struct);
        } else {
            // check for an existing reflexion on the artefact <-> struct link
            String reflexion = ePFMgr.getReflexionForArtefactToStructureLink(artefact, struct);
            if (StringHelper.containsNonWhitespace(reflexion)) {
                // edit an existing reflexion
                reflexionCtrl = new EPCollectStepForm03(ureq, getWindowControl(), artefact, reflexion);
                title = translate("title.reflexion.link");
            } else if (struct != null) {
                // no reflexion on link yet, show warning and preset with
                // artefacts-reflexion
                reflexionCtrl = new EPCollectStepForm03(ureq, getWindowControl(), artefact, true);
                title = translate("title.reflexion.artefact");
            } else {
                // preset controller with reflexion of the artefact. used by
                // artefact-pool
                reflexionCtrl = new EPCollectStepForm03(ureq, getWindowControl(), artefact);
                title = translate("title.reflexion.artefact");
            }
        }
        listenTo(reflexionCtrl);
        removeAsListenerAndDispose(reflexionBox);
        reflexionBox = new CloseableModalWindowWrapperController(ureq, getWindowControl(), title, reflexionCtrl.getInitialComponent(), "reflexionBox");
        listenTo(reflexionBox);
        reflexionBox.setInitialWindowSize(550, 600);
        reflexionBox.activate();
    }

    @Override
    protected void event(UserRequest ureq, Component source, Event event) {
        //
    }

    /**
     * @see org.olat.core.gui.control.DefaultController#event(org.olat.core.gui.UserRequest, org.olat.core.gui.control.Controller, org.olat.core.gui.control.Event)
     */
    @Override
    protected void event(UserRequest ureq, Controller source, Event event) {
        if (source == reflexionCtrl && event instanceof EPReflexionChangeEvent) {
            EPReflexionChangeEvent refEv = (EPReflexionChangeEvent) event;
            if (struct != null) {
                ePFMgr.setReflexionForArtefactToStructureLink(refEv.getRefArtefact(), struct, refEv.getReflexion());
                reflexionBox.deactivate();
                fireEvent(ureq, new EPStructureChangeEvent(EPStructureChangeEvent.ADDED, struct));
            } else {
                AbstractArtefact refArtefact = refEv.getRefArtefact();
                refArtefact.setReflexion(refEv.getReflexion());
                ePFMgr.updateArtefact(refArtefact);
                reflexionBox.deactivate();
                fireEvent(ureq, Event.DONE_EVENT);
            }
            removeAsListenerAndDispose(reflexionBox);
        } else if (source == reflexionBox && event == CloseableCalloutWindowController.CLOSE_WINDOW_EVENT) {
            removeAsListenerAndDispose(reflexionBox);
            reflexionBox = null;
        }
    }

    /**
     * @see org.olat.core.gui.control.DefaultController#doDispose()
     */
    @Override
    protected void doDispose() {
        // nothing
    }

}
