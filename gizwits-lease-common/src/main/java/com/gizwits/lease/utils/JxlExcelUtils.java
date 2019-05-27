package com.gizwits.lease.utils;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import com.gizwits.lease.exceptions.ExcelException;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

/**
 * @author lilh
 * @date 2017/8/11 15:45
 */
public final class JxlExcelUtils {

    /**
     * @param list       数据源
     * @param titles     类的英文属性和Excel中的中文列名的对应关系 例：{id=编号}
     *                   如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式
     *                   如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写
     *                   fieldMap.put("college.collegeName","学院名称")
     * @param properties 英文属性
     * @param sheetName  工作表的名称
     * @param sheetSize  每个工作表中记录的最大个数
     * @param os         导出流
     * @throws ExcelException 异常
     */
    public static <T> void listToExcel(List<T> list, String[] titles, String[] properties, String sheetName, int sheetSize, OutputStream os) throws ExcelException {

        if (Objects.isNull(list)) {
            list = Collections.emptyList();
        }

        if (sheetSize < 1 || sheetSize > 65535) {
            sheetSize = 65535;
        }

        WritableWorkbook wwb;
        try {
            // 创建工作簿并发送到OutputStream指定的地方
            wwb = Workbook.createWorkbook(os);

            // 因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
            // 所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
            // 1.计算一共有多少个工作表
            double sheetNum = Math.ceil(list.size() / new Integer(sheetSize).doubleValue());
            if (sheetNum < 1) {
                sheetNum = 1;
            }
            // 2.创建相应的工作表,并向其中填充数据
            for (int i = 0; i < sheetNum; i++) {
                // 如果只有一个工作表的情况
                if (sheetNum == 1) {
                    WritableSheet sheet = wwb.createSheet(sheetName, i);
                    // 向工作表中填充数据
                    fillSheet(sheet, list, titles, properties, 0, list.size() - 1);

                    // 有多个工作表的情况
                } else {
                    WritableSheet sheet = wwb.createSheet(sheetName + (i + 1), i);
                    // 获取开始索引和结束索引
                    int firstIndex = i * sheetSize;
                    int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list.size() - 1 : (i + 1) * sheetSize - 1;
                    // 填充工作表
                    fillSheet(sheet, list, titles, properties, firstIndex, lastIndex);
                }
            }

            wwb.write();
            wwb.close();

        } catch (Exception e) {
            e.printStackTrace();
            // 如果是ExcelException,则直接抛出
            if (e instanceof ExcelException) {
                throw (ExcelException) e;

                // 否则将其他异常包装成ExcelException再抛出
            } else {
                throw new ExcelException("导出Excel失败");
            }
        }

    }

    /**
     * @param list       数据源
     * @param titles     类的英文属性和Excel中的中文列名的对应关系
     * @param properties 英文属性
     * @param sheetName  工作表的名称
     * @param os         导出流
     */
    public static <T> void listToExcel(List<T> list, String[] titles, String[] properties, String sheetName, OutputStream os) throws ExcelException {
        listToExcel(list, titles, properties, sheetName, 65535, os);
    }

    /**
     * @param list       数据源
     * @param titles     类的英文属性和Excel中的中文列名的对应关系
     * @param properties 英文属性
     * @param sheetName  工作表的名称
     * @param sheetSize  每个工作表中记录的最大个数
     * @param response   使用response可以导出到浏览器
     */
    public static <T> void listToExcel(List<T> list, String[] titles, String[] properties, String sheetName, int sheetSize, HttpServletResponse response) throws ExcelException {
        // 文件名默认设置为当前时间：年月日时分秒
        String fileName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
        // 设置response头信息
        response.reset();
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xls");

        // 创建工作簿并发送到浏览器
        try {
            try (OutputStream os = response.getOutputStream()) {
                listToExcel(list, titles, properties, sheetName, sheetSize, os);
            }
        } catch (Exception e) {
            e.printStackTrace();

            // 如果是ExcelException,则直接抛出
            if (e instanceof ExcelException) {
                throw (ExcelException) e;
            } else {
                // 否则将其他异常包装成ExcelException再抛出
                throw new ExcelException("导出excel失败");
            }
        }
    }

    /**
     * @param list      数据源
     * @param titles    类的英文属性和Excel中的中文列名的对应关系
     * @param sheetName 工作表的名称
     * @param response  使用response可以导出到浏览器
     */
    public static <T> void listToExcel(List<T> list, String[] titles, String[] properties, String sheetName, HttpServletResponse response) throws ExcelException {
        listToExcel(list, titles, properties, sheetName, 65535, response);
    }

    /**
     * @param sheet      工作表
     * @param list       数据源数据
     * @param titles     中英文属性对照关系map
     * @param firstIndex 开始索引
     * @param lastIndex  结束索引
     * @param <E>
     */
    private static <E> void fillSheet(WritableSheet sheet, List<E> list, String[] titles, String[] properties, int firstIndex, int lastIndex) throws Exception {
        // 定义存放英文字段名和中文字段名的数组


        // 填充表头
        for (int i = 0; i < titles.length; i++) {
            Label label = new Label(i, 0, titles[i]);
            sheet.addCell(label);
        }

        // 填充内容
        int rowNo = 1;
        for (int index = firstIndex; index <= lastIndex; index++) {
            E item = list.get(index);
            for (int i = 0; i < properties.length; i++) {
                Object objValue = getFieldValueByNameSequence(properties[i], item);
                String fieldValue = objValue == null ? "" : objValue.toString();
                Label label = new Label(i, rowNo, fieldValue);
                sheet.addCell(label);
            }
            rowNo++;
        }
        // 设置自动列宽
        setColumnAutoSize(sheet, 5);
    }

    /**
     * @param fieldNameSequence 带路径的属性名或简单属性名
     * @param o                 对象
     * @return 属性值
     */
    private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {

        Object value;
        String opts[];
        if (fieldNameSequence.indexOf('.') < 0) {
            value = PropertyUtils.getProperty(o, fieldNameSequence);
        } else {
            //当包含x.y这种属性时
            opts = StringUtils.split(fieldNameSequence, ".");
            value = PropertyUtils.getProperty(o, opts[0]);
            for (int j = 1; j < opts.length; j++) {
                if (value == null) {
                    break;
                } else {
                    //递归获取
                    value = PropertyUtils.getProperty(value, opts[j]);
                }

            }
        }
        if (Objects.nonNull(value)) {
            if (value instanceof Date) {
                value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
            }
        }
        return value;
    }

    private static void setColumnAutoSize(WritableSheet ws, int extraWith) {
        // 获取本列的最宽单元格的宽度
        for (int i = 0; i < ws.getColumns(); i++) {
            int colWith = 0;
            for (int j = 0; j < ws.getRows(); j++) {
                String content = ws.getCell(i, j).getContents();
                int cellWith = content.length();
                if (colWith < cellWith) {
                    colWith = cellWith;
                }
            }
            // 设置单元格的宽度为最宽宽度+额外宽度
            ws.setColumnView(i, colWith + extraWith);
        }
    }

    public static void main(String args[]) throws Exception {
        try {
//            List<Student> students = new ArrayList<Student>();
//            Student s1 = new Student();
//            s1.setId(1);
//            s1.setName("Tom");
//            s1.setScore(78);
//            students.add(s1);
//
//            Student s2 = new Student();
//            s2.setId(2);
//            s2.setName("Hanks");
//            s2.setScore(56);
//            students.add(s2);
//
//            Student s3 = new Student();
//            s3.setId(3);
//            s3.setName("jerry");
//            s3.setScore(99);
//            students.add(s3);
//
//            LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
//            fieldMap.put("id", "编号");
//            fieldMap.put("name", "姓名");
//            fieldMap.put("score", "分数");
//            File osFile = new File("e:/test.xls");
//            FileOutputStream fos = new FileOutputStream(osFile);
//            listToExcel(students, fieldMap, "studentScore", fos);
            System.out.println("download success!");
        } catch (Exception e) {
            throw new Exception("export error:" + e.getMessage());
        }
    }
}
