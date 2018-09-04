package vn.myclass.core.common.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelPoiUtil {
    // hàm này sẽ dựa vào Factory Method Pattern
    // đây là 1 dạng design pattern cho phép tạo ra 1 địa điểm tập trung tất cả các đối tượng đã được tạo ra
    // ta sẽ dựa vào đuôi file (.xls hay .xlsx) để tạo ra instance đọc file tương ứng (HSSF hay XSSF)
    public static Workbook getWorkbook(String fileName, String fileLocation) throws IOException {
        FileInputStream excelFile = new FileInputStream(new File(fileLocation));
        Workbook workbook = null;
        if(fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook(excelFile);
        } else if(fileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(excelFile);
        }
        return workbook;
    }

    // đọc kiểu và giá trị của cell và chuyển tất cả các kiểu về kiểu String
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if(cell == null) {
            return cellValue;
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = Boolean.toString(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        cellValue = cell.getStringCellValue();
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
                        break;
                }
        }
        return cellValue;
    }
}
