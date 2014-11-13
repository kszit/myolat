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

package org.olat.presentation.user.administration;

import java.util.ArrayList;
import java.util.List;

import org.olat.data.basesecurity.Identity;
import org.olat.data.basesecurity.Roles;
import org.olat.data.user.UserConstants;
import org.olat.lms.security.BaseSecurityEBL;
import org.olat.lms.user.UserService;
import org.olat.presentation.framework.core.UserRequest;
import org.olat.presentation.framework.core.components.form.flexible.FormItem;
import org.olat.presentation.framework.core.components.form.flexible.FormItemContainer;
import org.olat.presentation.framework.core.components.form.flexible.elements.SelectionElement;
import org.olat.presentation.framework.core.components.form.flexible.elements.SingleSelection;
import org.olat.presentation.framework.core.components.form.flexible.elements.SpacerElement;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormBasicController;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormEvent;
import org.olat.presentation.framework.core.components.form.flexible.impl.FormLayoutContainer;
import org.olat.presentation.framework.core.control.Controller;
import org.olat.presentation.framework.core.control.WindowControl;
import org.olat.system.event.Event;
import org.olat.system.spring.CoreSpringFactory;

/**
 * Initial Date: Jan 27, 2006
 * 
 * @author gnaegi
 * @author matthai
 * 
 *         <pre>
 * Description:
 * Form to configure the users system roles and the basic type
 * of the user account (user or guest).
 */

public class SystemRolesAndRightsForm extends FormBasicController {

    private SingleSelection AnonymousRE;
    private SelectionElement RolesSE;
    private SpacerElement rolesSep;
    private SpacerElement sysSep;
    private SingleSelection statusRE;

    private final Identity identity;
    private final Roles myRoles;
    private final Roles roles;

    private final List<String> statusKeys, statusValues;
    private final List<String> roleKeys, roleValues;

    private static final String KUSER = "isUserManager";
    private static final String KGROUP = "isGroupManager";
    private static final String KAUTHOR = "isAuthor";
    private static final String KADMIN = "isAdmin";
    private static final String KRESMAN = "isInstitutionalResourcemanager";

    public SystemRolesAndRightsForm(final UserRequest ureq, final WindowControl wControl, final Identity identity) {
        super(ureq, wControl);

        this.identity = identity;

        myRoles = ureq.getUserSession().getRoles();

        roles = getBaseSecurityEBL().getRoles(identity);

        statusKeys = new ArrayList<String>(4);
        statusKeys.add(Integer.toString(Identity.STATUS_ACTIV));
        statusKeys.add(Integer.toString(Identity.STATUS_PERMANENT));
        statusKeys.add(Integer.toString(Identity.STATUS_LOGIN_DENIED));

        statusValues = new ArrayList<String>(4);
        statusValues.add(translate("rightsForm.status.activ"));
        statusValues.add(translate("rightsForm.status.permanent"));
        statusValues.add(translate("rightsForm.status.login_denied"));

        if (identity.getStatus() == Identity.STATUS_DELETED) {
            statusKeys.add(Integer.toString(Identity.STATUS_DELETED));
            statusValues.add(translate("rightsForm.status.deleted"));
        }

        roleKeys = new ArrayList<String>();
        roleValues = new ArrayList<String>();

        if (myRoles.isOLATAdmin()) {
            roleKeys.add(KUSER);
            roleValues.add(translate("rightsForm.isUsermanager"));
        }

        if (getBaseSecurityEBL().isManagerOfGroupManagers(myRoles)) {
            roleKeys.add(KGROUP);
            roleValues.add(translate("rightsForm.isGroupmanager"));
        }

        // OLAT-6699 What is 'isGuestManager' for? 
        if (getBaseSecurityEBL().isAuthorManager(myRoles) || getBaseSecurityEBL().isGuestManager(myRoles)) {
            roleKeys.add(KAUTHOR);
            roleValues.add(translate("rightsForm.isAuthor"));
        }

        if (myRoles.isOLATAdmin()) {
            roleKeys.add(KADMIN);
            roleValues.add(translate("rightsForm.isAdmin"));
        }

        if (getBaseSecurityEBL().isManagerOfInstitutionalResourceManagers(myRoles)) {
            roleKeys.add(KRESMAN);
            final String iname = getUserService().getUserProperty(identity.getUser(), UserConstants.INSTITUTIONALNAME);
            roleValues.add(iname != null ? translate("rightsForm.isInstitutionalResourceManager.institution", iname)
                    : translate("rightsForm.isInstitutionalResourceManager"));
        }

        initForm(ureq);
    }

    private BaseSecurityEBL getBaseSecurityEBL() {
        return CoreSpringFactory.getBean(BaseSecurityEBL.class);
    }

    private void update() {
        setAnonymous(roles.isGuestOnly());

        setUsermanager(roles.isUserManager());
        setGroupmanager(roles.isGroupManager());
        setAuthor(roles.isAuthor());
        setAdmin(roles.isOLATAdmin());
        setInstitutionalResourceManager(roles.isInstitutionalResourceManager());

        setStatus(identity.getStatus());

        RolesSE.setVisible(!isAnonymous());
        rolesSep.setVisible(!isAnonymous());
    }

    protected boolean isAdmin() {
        return getRole(KADMIN);
    }

    public void setAdmin(final boolean isAdmin) {
        setRole(KADMIN, isAdmin);
    }

    public boolean isAnonymous() {
        return AnonymousRE.getSelectedKey().equals("true");
    }

    private void setAnonymous(final boolean isAnonymous) {
        AnonymousRE.select(isAnonymous ? "true" : "false", true);
    }

    protected boolean isAuthor() {
        return getRole(KAUTHOR);
    }

    protected void setAuthor(final boolean isAuthor) {
        setRole(KAUTHOR, isAuthor);
    }

    protected boolean isGroupmanager() {
        return getRole(KGROUP);
    }

    private void setGroupmanager(final boolean isGroupmanager) {
        setRole(KGROUP, isGroupmanager);
    }

    protected boolean isUsermanager() {
        return getRole(KUSER);
    }

    private void setUsermanager(final boolean isUsermanager) {
        setRole(KUSER, isUsermanager);
    }

    private void setInstitutionalResourceManager(final boolean isInstitutionalResourceManager) {
        setRole(KRESMAN, isInstitutionalResourceManager);
    }

    public boolean isInstitutionalResourceManager() {
        return getRole(KRESMAN);
    }

    protected Integer getStatus() {
        return new Integer(statusRE.getSelectedKey());
    }

    private void setStatus(final Integer status) {
        statusRE.select(status.toString(), true);
        statusRE.setEnabled(status != Identity.STATUS_DELETED);
    }

    private void setRole(final String k, final boolean tf) {
        if (roleKeys.contains(k)) {
            RolesSE.select(k, tf);
        }
    }

    private boolean getRole(final String k) {
        return roleKeys.contains(k) ? RolesSE.isSelected(roleKeys.indexOf(k)) : false;
    }

    @Override
    protected void formOK(final UserRequest ureq) {
        fireEvent(ureq, Event.DONE_EVENT);
    }

    @Override
    protected void formCancelled(final UserRequest ureq) {
        fireEvent(ureq, Event.CANCELLED_EVENT);
    }

    @Override
    protected void formInnerEvent(final UserRequest ureq, final FormItem source, final FormEvent event) {
        if (source == AnonymousRE) {
            RolesSE.setVisible(!isAnonymous());
            rolesSep.setVisible(!isAnonymous());
        }
    }

    @Override
    protected void initForm(final FormItemContainer formLayout, final Controller listener, final UserRequest ureq) {

        AnonymousRE = uifactory.addRadiosVertical("anonymous", "rightsForm.isAnonymous", formLayout, new String[] { "true", "false" }, new String[] {
                translate("rightsForm.isAnonymous.true"), translate("rightsForm.isAnonymous.false") });
        sysSep = uifactory.addSpacerElement("syssep", formLayout, false);
        if (getBaseSecurityEBL().isGuestManager(myRoles)) {
            AnonymousRE.addActionListener(this, FormEvent.ONCLICK);
        } else {
            AnonymousRE.setVisible(false);
            sysSep.setVisible(false);
        }

        RolesSE = uifactory.addCheckboxesVertical("roles", "rightsForm.roles", formLayout, roleKeys.toArray(new String[roleKeys.size()]),
                roleValues.toArray(new String[roleValues.size()]), null, 1);
        rolesSep = uifactory.addSpacerElement("rolesSep", formLayout, false);

        statusRE = uifactory.addRadiosVertical("status", "rightsForm.status", formLayout, statusKeys.toArray(new String[statusKeys.size()]),
                statusValues.toArray(new String[statusKeys.size()]));

        rolesSep.setVisible(myRoles.isOLATAdmin());
        statusRE.setVisible(myRoles.isOLATAdmin());

        final FormLayoutContainer buttonGroupLayout = FormLayoutContainer.createButtonLayout("buttonGroupLayout", getTranslator());
        formLayout.add(buttonGroupLayout);
        uifactory.addFormSubmitButton("submit", buttonGroupLayout);
        uifactory.addFormCancelButton("cancel", buttonGroupLayout, ureq, getWindowControl());

        update();
    }

    @Override
    protected void doDispose() {
        //
    }

    private UserService getUserService() {
        return CoreSpringFactory.getBean(UserService.class);
    }

}
