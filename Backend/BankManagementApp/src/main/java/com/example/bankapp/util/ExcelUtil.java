package com.example.bankapp.util;


import java.io.IOException;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.bankapp.entity.Transaction;

public class ExcelUtil {


	public static void transactionExport(List<Transaction> transactions, OutputStream outputStream) throws IOException {

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Transactions");

		//for highlight header of excel
		CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);

        //style for date cell
        CellStyle dateCellStyle = workbook.createCellStyle();
        CreationHelper creationHelper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(
                creationHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm")
        );

		//header for excel
		Row headerRow = sheet.createRow(0);

		String[] headers = {"Date", "Description","To whom" , "Amount" , "Account number", "Status" , "Type"};
		for(int i = 0; i< headers.length ; i++) {

			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(headerStyle);
		}


		//date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        int rowIndex = 1;

		//storing data
		for(Transaction t : transactions) {

			Row row = sheet.createRow(rowIndex++);

			//date
			Cell dateCell = row.createCell(0);
			dateCell.setCellValue(t.getTimestamp().format(formatter));
			dateCell.setCellStyle(dateCellStyle);

			 // Description
            row.createCell(1).setCellValue(t.getDescription() != null ? t.getDescription() : "");

            // To whom
            row.createCell(2).setCellValue(t.getCounterPartyName() != null ? t.getCounterPartyName() : "");

            // Amount
            row.createCell(3).setCellValue(t.getAmount() != null ? t.getAmount() : 0.0);

            // Account number
            if (t.getAccount() != null && t.getAccount().getAccountNumber() != null) {
                row.createCell(4).setCellValue(t.getAccount().getAccountNumber());
            } else {
                row.createCell(4).setCellValue("");
            }


         // Status
            row.createCell(5).setCellValue(t.getStatus() != null ? t.getStatus().name() : "");

            // Type -- (debit / credit)

            row.createCell(6).setCellValue(t.getDirection() != null ? t.getDirection() : "");

		}
		 for (int i = 0; i < headers.length; i++) {
	            sheet.autoSizeColumn(i);
	        }


	        workbook.write(outputStream);
	        workbook.close();

	}

}
