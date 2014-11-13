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

package org.olat.system.mail;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.log4j.Logger;
import org.olat.system.commons.StringHelper;
import org.olat.system.logging.log4j.LoggerHelper;
import org.olat.system.security.OLATPrincipal;

/**
 * The ContactList is used to group e-mail addresses and name such a group. It is the frameworks implementation of a MailList. The e-mail adresses are given either by
 * providing them as <code>strings</code> or in the form of identities (<code>OLATPrincipal</code>). Further it is possible to add all contacts from another ContactList.
 * <P>
 * TODO:pb:a remove dependency to <code>EmailPrioInstitutional</code> if a solution for core.id.user is found Moreover one can specify to use the users institional e-mail
 * first and use the other e-mail as fall-back. Initial Date: Sep 23, 2004
 * 
 * @author patrick
 */

public class ContactList {

    private String name;
    private String description;
    // container for addresses contributed as strings
    private Hashtable<String, String> stringEmails = new Hashtable<String, String>();
    // container for addresses contributed as olatprincipals
    private Hashtable<String, OLATPrincipal> emailToIdentityMap = new Hashtable<String, OLATPrincipal>();
    private boolean emailPrioInstitutional = false;
    private static final Logger log = LoggerHelper.getLogger();

    /**
     * A ContacList must have at least a name != null, matching ^[^;,:]*$
     * 
     * @param name
     */
    public ContactList(String name) {
        setName(name);
        this.description = null;
    }

    /**
     * A ContactList must have at least a name != null, matching ^[a-zA-Z ]*$, and can have a description.
     * 
     * @param name
     * @param description
     */
    public ContactList(String name, String description) {
        setName(name);
        this.description = description;
    }

    /**
     * check the priority of the institutional mail is set.
     * 
     * @return Returns the emailPrioInstitutional.
     */
    public boolean isEmailPrioInstitutional() {
        return emailPrioInstitutional;
    }

    /**
     * set the priority of the institutional mail.
     * 
     * @param emailPrioInstitutional
     *            The emailPrioInstitutional to set.
     */
    public void setEmailPrioInstitutional(boolean emailPrioInstitutional) {
        this.emailPrioInstitutional = emailPrioInstitutional;
    }

    /**
     * contribute a contact as a string email address.
     * 
     * @param emailAddress
     */
    public void add(String emailAddress) {
        stringEmails.put(keyFrom(emailAddress), emailAddress);
    }

    /**
     * contribute a contact as an identity.
     * 
     * @param identity
     */
    public void add(OLATPrincipal principal) {
        String email = principal.getAttributes().getEmail();
        emailToIdentityMap.put(keyFrom(email), principal);
    }

    public void remove(OLATPrincipal principal) {
        String email = principal.getAttributes().getEmail();
        emailToIdentityMap.remove(keyFrom(email));
    }

    /**
     * Name getter
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * ContactList-name as String formatted according to <a href = "http://www.rfc.net/rfc2822.html"> RFC2822 </a>
     * 
     * @return
     */
    public String getRFC2822Name() {
        return name + ":";
    }

    /**
     * ContactList-name and e-mail adresses as String formatted according to http://www.rfc.net/rfc2822.html
     * 
     * @return
     */
    public String getRFC2822NameWithAddresses() {
        return name + ":" + toString() + ";";
    }

    /**
     * Description getter
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * the returned ArrayList contains String Objects representing the e-mail addresses added.
     * 
     * @return
     */
    public ArrayList<String> getEmailsAsStrings() {
        ArrayList<String> ret = new ArrayList<String>(stringEmails.values());
        /*
         * if priority is on institutional email get all the institutional emails first, if they are present, remove the identity from the hashtable. If they were not
         * present, the user email is used in the next loop.
         */
        Enumeration<OLATPrincipal> enumeration = emailToIdentityMap.elements();
        String addEmail = null;
        if (emailPrioInstitutional) {
            while (enumeration.hasMoreElements()) {
                OLATPrincipal tmp = enumeration.nextElement();
                addEmail = tmp.getAttributes().getInstitutionalEmail();
                if (addEmail != null) {
                    ret.add(addEmail);
                    emailToIdentityMap.remove(tmp);
                }
            }
        }
        /*
         * loops over the (remaining) identities, fetches the user email.
         */
        while (enumeration.hasMoreElements()) {
            OLATPrincipal tmp = enumeration.nextElement();
            ret.add(tmp.getAttributes().getEmail());
        }
        return ret;
    }

    /**
     * add members of another ContactList to this ContactList.
     * 
     * @param emailList
     */
    public void add(ContactList emailList) {
        stringEmails.putAll(emailList.getStringEmails());
        emailToIdentityMap.putAll(emailList.getIdentityEmails());
    }

    /**
     * A comma separated list of e-mail addresses. The ContactList name is ommitted, if the form
     * ContactList.Name:member1@host.suffix,member2@host.suffix,...,memberN@host.suffix; is needed, please use the appropriate getRFC2822xxx getter method.
     * 
     */
    @Override
    public String toString() {
        String retVal = "";
        String sep = "";
        ArrayList emails = getEmailsAsStrings();
        Iterator iter = emails.iterator();
        while (iter.hasNext()) {
            retVal += sep + (String) iter.next();
            sep = ", ";
        }
        return retVal;
    }

    /**
     * add all identity-Objects in the provided list.
     * 
     * @param listOfIdentity
     *            List containing OLATPrincipal
     */
    public void addAllIdentites(List<? extends OLATPrincipal> listOfIdentity) {
        Iterator<? extends OLATPrincipal> iter = listOfIdentity.iterator();
        while (iter.hasNext()) {
            add(iter.next());
        }
    }

    /**
     * The e-mail addresses are generated as InternetAddresses, the priority of the institutional email is taken in account.
     * 
     * @return the email addresses as InternetAddresses
     * @throws AddressException
     */
    public InternetAddress[] getEmailsAsAddresses() throws AddressException {
        return InternetAddress.parse(toString());
    }

    Hashtable<String, String> getStringEmails() {
        return stringEmails;
    }

    public Hashtable<String, OLATPrincipal> getIdentityEmails() {
        return emailToIdentityMap;
    }

    private String keyFrom(String unformattedEmailAddr) {
        String key = unformattedEmailAddr.trim();
        return key.toUpperCase();
    }

    private void setName(String nameP) {
        if (!StringHelper.containsNoneOfCoDouSemi(nameP)) {
            log.warn("Contact list name \"" + nameP + "\" doesn't match " + StringHelper.ALL_WITHOUT_COMMA_2POINT_STRPNT, null);
            // replace bad chars with bad char in rfc compliant comments
            nameP = nameP.replaceAll(":", "¦");
            nameP = nameP.replaceAll(";", "_");
            nameP = nameP.replaceAll(",", "-");

        }

        this.name = nameP;
    }

}
