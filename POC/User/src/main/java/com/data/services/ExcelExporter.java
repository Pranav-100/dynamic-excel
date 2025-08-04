package com.data.services;

import com.data.dao.BData;
import com.data.repository.BDataRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.List;

@Service
public class ExcelExporter {

    @Autowired
    private BDataRepository bDataRepository;

    public void generateExcelFromDB() throws Exception {
        List<BData> dataList = bDataRepository.findAll();
        generateExcelWithAllSheets(dataList);
    }

    public void generateExcelWithAllSheets(List<BData> dataList) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook();

        // ========== Styles ==========
        CellStyle unlocked = workbook.createCellStyle();
        unlocked.setLocked(false);

        // ========== Hidden Sheet for Lookups ==========
        XSSFSheet hiddenSheet = workbook.createSheet("id_lookup");
        workbook.setSheetHidden(workbook.getSheetIndex(hiddenSheet), true);
        for (int i = 0; i < dataList.size(); i++) {
            Row row = hiddenSheet.createRow(i);
            row.createCell(0).setCellValue(dataList.get(i).getId());
            row.createCell(1).setCellValue(dataList.get(i).getCode());
            row.createCell(2).setCellValue(dataList.get(i).getCity());
            row.createCell(3).setCellValue(dataList.get(i).getState());
            row.createCell(4).setCellValue(dataList.get(i).getCreatedOn() != null ?
                    dataList.get(i).getCreatedOn().toString() : "");
        }

        // ================= Sheet 1: Add =================
        XSSFSheet addSheet = workbook.createSheet("Add Records");
        Row addHeader = addSheet.createRow(0);
        String[] headers = {"ID", "CODE", "CITY", "STATE", "CREATED ON"};
        for (int i = 0; i < headers.length; i++) {
            addHeader.createCell(i).setCellValue(headers[i]);
        }

        for (int i = 1; i <= 10; i++) {
            Row row = addSheet.createRow(i);
            for (int j = 0; j < headers.length; j++) {
                Cell cell = row.createCell(j);
                cell.setCellStyle(unlocked);
            }
        }
        addSheet.protectSheet("lock123");

        // ================= Sheet 2: Delete =================
        XSSFSheet deleteSheet = workbook.createSheet("Delete Records");
        Row deleteHeader = deleteSheet.createRow(0);
        deleteHeader.createCell(0).setCellValue("ID");
        deleteHeader.createCell(1).setCellValue("CODE");

        XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper(deleteSheet);
        CellRangeAddressList idAddressList = new CellRangeAddressList(1, 10, 0, 0);
        DataValidationConstraint idConstraint = dvHelper.createFormulaListConstraint(
                "id_lookup!$A$1:$A$" + dataList.size()
        );
        DataValidation idValidation = dvHelper.createValidation(idConstraint, idAddressList);
        idValidation.setSuppressDropDownArrow(true);
        idValidation.createPromptBox("Select ID", "Choose ID from dropdown.");
        idValidation.setShowPromptBox(true);
        deleteSheet.addValidationData(idValidation);

        for (int i = 1; i <= 10; i++) {
            Row row = deleteSheet.createRow(i);
            Cell idCell = row.createCell(0);
            idCell.setCellStyle(unlocked);

            Cell codeCell = row.createCell(1);
            codeCell.setCellFormula("IFERROR(VLOOKUP(A" + (i + 1) + ", id_lookup!A:B, 2, FALSE), \"\")");
        }
        deleteSheet.protectSheet("lock123");

        // ================= Sheet 3: Update =================
        // ================= Sheet 3: Update =================
        XSSFSheet updateSheet = workbook.createSheet("Update Records");
        Row updateHeader = updateSheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            updateHeader.createCell(i).setCellValue(headers[i]);
        }

// ID dropdown
        CellRangeAddressList updateIdList = new CellRangeAddressList(1, 100, 0, 0);
        DataValidationConstraint updateIdConstraint = dvHelper.createFormulaListConstraint(
                "id_lookup!$A$1:$A$" + dataList.size()
        );
        DataValidation updateIdValidation = dvHelper.createValidation(updateIdConstraint, updateIdList);
        updateIdValidation.setSuppressDropDownArrow(true);
        updateSheet.addValidationData(updateIdValidation);

// VLOOKUP formulas
        for (int i = 1; i <= 100; i++) {
            Row row = updateSheet.createRow(i);

            // ID - dropdown
            Cell idCell = row.createCell(0);
            idCell.setCellStyle(unlocked);

            // CODE - formula
            Cell codeCell = row.createCell(1);
            codeCell.setCellFormula("IFERROR(VLOOKUP(A" + (i + 1) + ", id_lookup!A:D, 2, FALSE), \"\")");
            codeCell.setCellStyle(unlocked);

            // CITY - formula but editable
            Cell cityCell = row.createCell(2);
            cityCell.setCellFormula("IFERROR(VLOOKUP(A" + (i + 1) + ", id_lookup!A:D, 3, FALSE), \"\")");
            cityCell.setCellStyle(unlocked);

            // STATE - formula but editable
            Cell stateCell = row.createCell(3);
            stateCell.setCellFormula("IFERROR(VLOOKUP(A" + (i + 1) + ", id_lookup!A:E, 4, FALSE), \"\")");
            stateCell.setCellStyle(unlocked);

            // CREATED ON - formula but editable
            Cell createdCell = row.createCell(4);
            createdCell.setCellFormula("IFERROR(VLOOKUP(A" + (i + 1) + ", id_lookup!A:E, 5, FALSE), \"\")");
            createdCell.setCellStyle(unlocked);
        }

        updateSheet.protectSheet("lock123");

        // ========== Save File ==========
        try (FileOutputStream out = new FileOutputStream("multi_sheet_data.xlsx")) {
            workbook.write(out);
        }
        workbook.close();
        System.out.println("Excel file with Add, Delete, and Update sheets generated.");
    }
}
