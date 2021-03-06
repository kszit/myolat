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
 * Copyright (c) since 2007 at frentix GmbH, Switzerland, www.frentix.com
 * <p>
 */
package org.olat.data.commons.filter.impl;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.log4j.Logger;
import org.olat.data.commons.filter.Filter;
import org.olat.data.commons.vfs.VFSManager;
import org.olat.system.exception.AssertException;
import org.olat.system.exception.OLATRuntimeException;
import org.olat.system.logging.log4j.LoggerHelper;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;

/**
 * Description:<br>
 * OWASP AntiSamy XSSFilter creates a DOM-Tree, parses it and filters everything invalid out, expect items in the policy-file this is way better than trying to handle
 * input by regexp's: http://htmlparsing.icenine.ca/doku.php/#summary OWASP AntiSamy docu: see http://www.owasp.org/index.php/AntiSamy HTML Parser based on:
 * http://nekohtml.sourceforge.net/
 * <P>
 * Initial Date: 30.07.2009 <br>
 * 
 * @author Roman Haag, roman.haag@frentix.com
 */
public class OWASPAntiSamyXSSFilter implements Filter {

    private static final Logger log = LoggerHelper.getLogger();

    // to be found in /_resources
    private static final String POLICY_FILE = "antisamy-tinymce.xml";
    private static boolean jUnitDebug;
    private CleanResults cr;
    private final int maxLength;

    /**
     * @param maxLength
     * @param junitDebug
     */
    public OWASPAntiSamyXSSFilter(int maxLength, boolean junitDebug) {
        OWASPAntiSamyXSSFilter.jUnitDebug = junitDebug;
        this.maxLength = maxLength;
    }

    /**
     * the maximum length allowed by the xss filter is -1, takes the default value from the policy file
     */
    public OWASPAntiSamyXSSFilter() {
        this(-1, false);
    }

    /**
	 */
    @Override
    public String filter(String original) {
        if (jUnitDebug)
            System.out.println("************************************************");
        if (jUnitDebug)
            System.out.println("              Input: " + original);
        if (original == null) {
            if (log.isDebugEnabled())
                log.debug("  Filter-Input was null, is this intended?", null);
            return null;
        }
        String output = getCleanHTML(original);
        if (original.equals(output)) {
            // log.info("          filter worked correctly!", null);
        } else {
            String errMsg = getOrPrintErrorMessages();
            if (!errMsg.equals("")) {
                log.warn(" Filter applied! => message from filter, check if this should not be allowed: " + errMsg, null);
                log.info(" Original Input: \n" + original, null);
                log.info("  Filter Result: \n" + output, null);
            } else {
                log.debug(" Filter result doesn't match input! / no message from filter! maybe only some formatting differences.", null);
            }
        }
        return output;
    }

    private void printOriginStackTrace() {
        // use stacktrace to find out more where the filter was used
        OLATRuntimeException ore = new OLATRuntimeException("XSSFilter dummy", null);
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ore.printStackTrace(printWriter);
    }

    private String getCleanHTML(String original) {
        Policy policy = null;
        try {
            String fPath = VFSManager.sanitizePath(this.getClass().getPackage().getName());
            fPath = fPath.replace('.', '/');
            fPath = fPath + "/_resources/" + POLICY_FILE;
            InputStream inStream = this.getClass().getResourceAsStream(fPath);
            policy = Policy.getInstance(inStream);
            if (maxLength > 0) {
                policy.setDirective("maxInputSize", Integer.toString(maxLength));
            }
        } catch (PolicyException e) {
            if (jUnitDebug)
                System.err.println("Policy file not found/readable/valid!");
            printOriginStackTrace();
            throw new AssertException("Owasp AntiSamy XSS Filter missing a correct policy file.");
        }
        AntiSamy as = new AntiSamy();
        cr = null;
        try {
            cr = as.scan(original, policy);
        } catch (ScanException e) {
            log.error("XSS Filter scan error", e);
            printOriginStackTrace();
        } catch (PolicyException e) {
            log.error("XSS Filter policy error", e);
            printOriginStackTrace();
        }
        String output = null;
        try {
            output = cr.getCleanHTML();
        } catch (Error e) {
            output = "";
            log.error("Error getting cleaned HTML from string::" + original, e);
        }
        if (jUnitDebug)
            System.out.println("OWASP-AntiSamy-Outp: " + output);
        getOrPrintErrorMessages();
        if (jUnitDebug)
            System.out.println("OWASP-ParseTime:                    " + cr.getScanTime());

        return output;
    }

    /**
     * get Errors/Messages from filter. This have not to be "errors", its what has been filtered and gets reported.
     * 
     * @return
     */
    public String getOrPrintErrorMessages() {
        String errors = "";
        if (cr != null) {
            if (cr.getNumberOfErrors() != 0) {
                errors = "OWASP-Errors: " + cr.getErrorMessages();
                if (jUnitDebug)
                    System.out.println(errors);
            }
        }
        return errors;
    }

}
