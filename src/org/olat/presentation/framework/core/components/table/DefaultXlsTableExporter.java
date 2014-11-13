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
package org.olat.presentation.framework.core.components.table;

/* TODO: ORID-1007 'File' */
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.olat.data.commons.filter.FilterFactory;
import org.olat.lms.commons.mediaresource.MediaResource;
import org.olat.lms.commons.mediaresource.MediaResourceEBL;
import org.olat.presentation.framework.core.render.StringOutput;
import org.olat.presentation.framework.core.translator.Translator;
import org.olat.system.commons.StringHelper;
import org.olat.system.spring.CoreSpringFactory;

/**
 * Description:<br>
 * Export the table as real Excel file with the apache poi library.
 * <P>
 * Initial Date: Nov 18, 2010 <br>
 * 
 * @author patrick
 */
public class DefaultXlsTableExporter implements TableExporter {
    private CellStyle headerCellStyle;

    /**
	 */
    @Override
    public MediaResource export(final Table table) {
        Translator translator = table.getTranslator();
        int cdcnt = table.getColumnCount();
        int rcnt = table.getRowCount();

        Workbook wb = new HSSFWorkbook();
        headerCellStyle = getHeaderCellStyle(wb);

        String tableExportTitle = translator.translate("table.export.title");
        Sheet exportSheet = wb.createSheet(tableExportTitle);
        createHeader(table, translator, cdcnt, exportSheet);
        createData(table, cdcnt, rcnt, exportSheet);

        return getMediaResourceEBL().createMediaResourceFromDocument(wb);
    }

    private void createHeader(final Table table, final Translator translator, final int cdcnt, final Sheet exportSheet) {

        Row headerRow = exportSheet.createRow(0);
        for (int c = 0; c < cdcnt; c++) {
            ColumnDescriptor cd = table.getColumnDescriptor(c);
            if (cd instanceof StaticColumnDescriptor) {
                // ignore static column descriptors - of no value in excel download!
                continue;
            }
            String headerKey = cd.getHeaderKey();
            String headerVal = cd.translateHeaderKey() ? translator.translate(headerKey) : headerKey;
            Cell cell = headerRow.createCell(c);
            cell.setCellValue(headerVal);
            cell.setCellStyle(headerCellStyle);
        }
    }

    private void createData(final Table table, final int cdcnt, final int rcnt, final Sheet exportSheet) {

        for (int r = 0; r < rcnt; r++) {
            Row dataRow = exportSheet.createRow(r + 1);
            for (int c = 0; c < cdcnt; c++) {
                ColumnDescriptor cd = table.getColumnDescriptor(c);
                if (cd instanceof StaticColumnDescriptor) {
                    // ignore static column descriptors - of no value in excel download!
                    continue;
                }
                StringOutput so = new StringOutput();
                cd.renderValue(so, r, null);
                String cellValue = so.toString();
                cellValue = StringHelper.stripLineBreaks(cellValue);
                cellValue = FilterFactory.getHtmlTagsFilter().filter(cellValue);
                Cell cell = dataRow.createCell(c);
                cell.setCellValue(cellValue);
            }
        }
    }

    private CellStyle getHeaderCellStyle(final Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        Font boldFont = wb.createFont();
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        cellStyle.setFont(boldFont);
        return cellStyle;
    }

    private MediaResourceEBL getMediaResourceEBL() {
        return CoreSpringFactory.getBean(MediaResourceEBL.class);
    }
}
