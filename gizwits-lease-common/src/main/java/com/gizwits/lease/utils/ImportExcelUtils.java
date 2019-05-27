package com.gizwits.lease.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author lilh
 * @date 2017/8/31 15:03
 */
public class ImportExcelUtils {

    private final static String excel2003L = "xls"; // 2003- 版本的excel
    private final static String excel2007U = "xlsx"; // 2007+ 版本的excel

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     */
    public static List<List<Object>> parse(InputStream in, String fileName) throws Exception {
        // 创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("创建Excel工作薄为空！");
        }
        Sheet sheet;
        Row row;
        Cell cell;

        List<List<Object>> list = new ArrayList<>();
        // 遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            // 遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j < sheet.getLastRowNum() + 1; j++) { // 这里的加一是因为下面的循环跳过取第一行表头的数据内容了
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                // 遍历所有的列
                List<Object> li = new ArrayList<>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    li.add(getCellValue(cell));
                }
                list.add(li);
            }
        }
        return list;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     */
    private static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = FilenameUtils.getExtension(fileName);
        if (excel2003L.equals(fileType)) {
            wb = new HSSFWorkbook(inStr); // 2003-
        } else if (excel2007U.equals(fileType)) {
            wb = new XSSFWorkbook(inStr); // 2007+
        } else {
            LeaseException.throwSystemException(LeaseExceEnums.EXCEL_FILE_FORMAT_ERROR);
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        if (null != cell) {
            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue();
                    }
                    return cell.getNumericCellValue();
                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    return cell.getStringCellValue();
                case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                    return cell.getBooleanCellValue();
                case HSSFCell.CELL_TYPE_FORMULA: // 公式
                    return cell.getCellFormula();
                case HSSFCell.CELL_TYPE_BLANK: // 空值
                case HSSFCell.CELL_TYPE_ERROR: // 故障
                default:
                    return null;
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        InputStream in = new FileInputStream("/data/lease/files/qrcode/device_template.xls");
        List<List<Object>> list = parse(in, "device_template.xls");
        list.forEach(item -> System.out.println(item.get(0) + "-----" + item.get(1)));
    }
}
