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

package org.olat.presentation.tunneling;

import org.olat.lms.commons.ModuleConfiguration;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.Component;
import org.olat.presentation.framework.core.components.velocity.VelocityContainer;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.DefaultController;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.presentation.framework.core.control.generic.clone.CloneableController;
import org.olat.presentation.framework.core.translator.PackageTranslator;
import org.olat.presentation.framework.core.translator.PackageUtil;
import org.olat.system.event.Event;

/**
 * Description:<BR>
 * Wrapper controller to wrap a tunnel component
 * <P>
 * Initial Date: Dec 15, 2004
 * 
 * @author gnaegi
 */
public class TunnelController extends DefaultController implements CloneableController {
    private static final String PACKAGE = PackageUtil.getPackageName(TunnelController.class);
    private static final String VELOCITY_ROOT = PackageUtil.getPackageVelocityRoot(TunnelController.class);

    private TunnelComponent tuc;
    private final ModuleConfiguration config;
    private final VelocityContainer main;

    /**
     * Constructor for a tunnel component wrapper controller
     * 
     * @param ureq
     * @param config
     *            the module configuration
     */
    public TunnelController(final UserRequest ureq, final WindowControl wControl, final ModuleConfiguration config) {
        super(wControl);
        this.config = config;
        final PackageTranslator trans = new PackageTranslator(PACKAGE, ureq.getLocale());
        main = new VelocityContainer("tucMain", VELOCITY_ROOT + "/index.html", trans, null);
        tuc = new TunnelComponent("tuc", config, ureq);
        main.put("tuc", tuc);
        setInitialComponent(main);
    }

    /**
	 */
    @Override
    protected void event(final UserRequest ureq, final Component source, final Event event) {
        // nothing to do
    }

    /**
	 */
    @Override
    protected void doDispose() {
        tuc = null;
    }

    /**
	 */
    @Override
    public Controller cloneController(final UserRequest ureq, final WindowControl control) {
        return new TunnelController(ureq, control, config);
    }

}
