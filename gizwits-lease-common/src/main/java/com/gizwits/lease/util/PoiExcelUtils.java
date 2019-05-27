package com.gizwits.lease.util;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class PoiExcelUtils {

    /** 默认日期格式配比 */
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static void exportExcel(String filename, String sheetName, String[] titles, String[] properties, List<?> data, HttpServletResponse response) {

        if (StringUtils.isEmpty(sheetName)) {
            sheetName = filename;
        }

        filename = filename + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xls";

        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 设置表格默认列宽度为15个字节
        sheet.setDefaultColumnWidth(15);
        // 生成表头样式
        HSSFCellStyle headStyle = getHeadCellStyle(workbook);
        // 生成内容样式
        HSSFCellStyle dataStyle = getListCellStyle(workbook);
        // 产生表格标题行
        addHeader(titles, sheet, headStyle);

        // 遍历集合数据，产生数据行
        try {
            addDataInfo(data, properties, sheet, dataStyle);
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            //response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);

            try (ServletOutputStream out = response.getOutputStream()) {
                workbook.write(out);
                out.flush();
            }
            //workbook.write(new FileOutputStream("e:/" + filename));
        } catch (Exception e) {
            //logger.error(e, e);
        }
    }

    private static void addDataInfo(List<?> data, String[] properties, HSSFSheet sheet, HSSFCellStyle dataStyle) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        int rowNumber = 1;
        for (Object item : data) {
            HSSFRow row = sheet.createRow(rowNumber++);
            for (int i = 0; i < properties.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(dataStyle);
                Object value;
                String opts[];
                if (properties[i].indexOf('.') < 0) {
                    value = PropertyUtils.getProperty(item, properties[i]);
                } else {
                    //当包含x.y这种属性时
                    opts = StringUtils.split(properties[i], ".");
                    value = PropertyUtils.getProperty(item, opts[0]);
                    for (int j = 1; j < opts.length; j++) {
                        if (value == null) {
                            break;
                        } else {
                            //递归获取
                            value = PropertyUtils.getProperty(value, opts[j]);
                        }

                    }
                }

                if (value != null) {
                    if (value instanceof Date) {
                        value = new SimpleDateFormat(DEFAULT_DATE_PATTERN).format(value);
                    }
                    cell.setCellValue(String.valueOf(value));
                }
            }
        }
    }

    private static void addHeader(String[] headers, HSSFSheet sheet, HSSFCellStyle style) {
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(headers[i]);
        }
    }

    private static HSSFCellStyle getHeadCellStyle(HSSFWorkbook workbook) {
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);
        return style;
    }

    private static HSSFCellStyle getListCellStyle(HSSFWorkbook workbook) {
        // 生成并设置另一个样式
        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 生成另一个字体
        HSSFFont font2 = workbook.createFont();
        font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
        // 把字体应用到当前的样式
        style2.setFont(font2);
        return style2;
    }
}
