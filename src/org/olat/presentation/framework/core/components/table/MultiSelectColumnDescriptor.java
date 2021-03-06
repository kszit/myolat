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
 * Copyright (c) 1999-2007 at Multimedia- & E-Learning Services (MELS),<br>
 * University of Zurich, Switzerland.
 * <p>
 */

package org.olat.presentation.framework.core.components.table;

import org.olat.presentation.framework.core.render.Renderer;
import org.olat.presentation.framework.core.render.StringOutput;
import org.olat.system.exception.AssertException;

class MultiSelectColumnDescriptor implements ColumnDescriptor {

    private static final String DOUBLE_QUOTE = "\"";
    private static final String VALUE = "\" value=\"";
    private static final String CLOSE_HTML_TAG = " />";
    Table table;

    MultiSelectColumnDescriptor() {
        // package visibility for constructor
    }

    @Override
    public void renderValue(final StringOutput sb, final int row, final Renderer renderer) {
        // add checkbox
        int currentPosInModel = table.getSortedRow(row);
        if (renderer == null) {
            // render for export
            if (table.getMultiSelectSelectedRows().get(currentPosInModel)) {
                sb.append("x");
            }
        } else {
            boolean checked = table.getMultiSelectSelectedRows().get(currentPosInModel);
            boolean readonly = table.getMultiSelectReadonlyRows().get(currentPosInModel);
            sb.append("<input type=\"checkbox\" name=\"" + TableRenderer.TABLE_MULTISELECT_GROUP + VALUE).append(currentPosInModel).append(DOUBLE_QUOTE);
            if (checked) {
                sb.append(" checked=\"checked\"");
            }
            if (readonly) {
                sb.append(" readonly=\"readonly\" disabled=\"disabled\"");
            }
            sb.append(CLOSE_HTML_TAG);
            // workaround: value of disabled checkboxes will not be returned on submit
            if (readonly & checked) {
                sb.append("<input type=\"hidden\" name=\"" + TableRenderer.TABLE_MULTISELECT_GROUP + VALUE).append(currentPosInModel).append(DOUBLE_QUOTE);
                sb.append(CLOSE_HTML_TAG);
            }
        }
    }

    @Override
    public int compareTo(final int rowa, final int rowb) {
        boolean rowaChecked = table.getMultiSelectSelectedRows().get(rowa);
        boolean rowbChecked = table.getMultiSelectSelectedRows().get(rowb);
        if (rowaChecked && !rowbChecked) {
            return -1;
        } else if (!rowaChecked && rowbChecked) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (getClass() != object.getClass()) {
            return false;
        }

        MultiSelectColumnDescriptor other = (MultiSelectColumnDescriptor) object;
        return other.table == table;

    }

    @Override
    public int hashCode() {
        if (table == null) {
            return 1;
        } else {
            return table.hashCode();
        }
    }

    @Override
    public String getHeaderKey() {
        return "table.header.multiselect";
    }

    @Override
    public boolean translateHeaderKey() {
        return true;
    }

    @Override
    public int getAlignment() {
        return ColumnDescriptor.ALIGNMENT_CENTER;
    }

    @Override
    public String getAction(final int row) {
        return null;
    }

    @Override
    public HrefGenerator getHrefGenerator() {
        return null;
    }

    @Override
    public String getPopUpWindowAttributes() {
        return null;
    }

    @Override
    public boolean isPopUpWindowAction() {
        return false;
    }

    @Override
    public boolean isSortingAllowed() {
        return true;
    }

    @Override
    public void modelChanged() {
        // nothing to do here
    }

    @Override
    public void otherColumnDescriptorSorted() {
        // nothing to do here
    }

    @Override
    public void setHrefGenerator(final HrefGenerator h) {
        throw new AssertException("Not allowed to set HrefGenerator on MultiSelectColumn.");
    }

    @Override
    public void setTable(final Table table) {
        this.table = table;
    }

    @Override
    public void sortingAboutToStart() {
        // nothing to do here
    }

    @Override
    public String toString(final int rowid) {
        return table.getMultiSelectSelectedRows().get(rowid) ? "checked" : "unchecked";
    }

}
