package org.com.utils;

import com.sky.entity.ExportExcelColumn;
import com.sky.support.BaseLog;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.*;

public class ExportExcel extends BaseLog {

    public String exportExcelFile(HttpServletRequest request, HttpServletResponse response,
                                  List<?> paymentInfoList) throws Exception {
        // Excel名称// fileChName为下载时用户看到的文件名
        String fileChName = request.getParameter("moduleName");
        // 列标题
        String gridTitle = request.getParameter("gridTitle");
        // 实体列
        String gridField = request.getParameter("gridField");

//		fileChName = java.net.URLEncoder.encode(fileChName, "UTF-8");
        response.reset();

        response.setCharacterEncoding("gb2312");
        response.setHeader("Content-Disposition", "attachment; filename="
                + new String((fileChName + ".xls").getBytes(), "iso-8859-1") + ".xls");

        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/msexcel;charset=gb2312");
        OutputStream out = response.getOutputStream();

        Map<String, ExportExcelColumn> map = new HashMap<String, ExportExcelColumn>();
        // List<Object> paymentInfoList = commonDao.queryForList(sqlMap, obj);

        String[] title = gridTitle.split(",");
        // 实体列
        String[] propery = gridField.split(",");
        // 列标题
        Integer titleNum = title.length;// Excel中字段的个数
        String FileTitle = "";// Excel中正文标题

        // --建立EXCEL索引、字段名、字段值的关系，存放到map中
        for (int i = 0; i < title.length; i++) {
            String PojoPropery = propery[i];// grid中title和field是一一对应的，所以可以这么写
            String toUpp = PojoPropery.replaceFirst(PojoPropery.substring(0, 1),
                    PojoPropery.substring(0, 1).toUpperCase());// 把首字母转换为大写
            // 方法名
            String methodName = "get" + toUpp;// 拼成pojo类中getXXX的方法名称
            map.put(PojoPropery, new ExportExcelColumn(i, title[i], methodName));
        }

        this.export(out, FileTitle, titleNum, paymentInfoList, map);
        return "ok";
    }

    public String exportExcelFile2(HttpServletRequest request, HttpServletResponse response, Object obj,
                                   List<?> resultList) throws Exception {
        // Excel名称
        String fileChName = request.getParameter("moduleName");
        // 列标题
        String gridTitle = request.getParameter("gridTitle");
        // 实体列
        String gridField = request.getParameter("gridField");

        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition",
                "attachment; filename=" + new String(fileChName.getBytes("GBK"), "utf-8"));// fileChName为下载时用户看到的文件名
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "application/octet-stream");
        OutputStream out = response.getOutputStream();

        Map<String, ExportExcelColumn> map = new HashMap<String, ExportExcelColumn>();

        String[] title = gridTitle.split(",");
        // 实体列
        String[] propery = gridField.split(",");
        // 列标题
        Integer titleNum = title.length;// Excel中字段的个数
        String FileTitle = "";// Excel中正文标题

        // --建立EXCEL索引、字段名、字段值的关系，存放到map中
        for (int i = 0; i < title.length; i++) {
            String PojoPropery = propery[i];// grid中title和field是一一对应的，所以可以这么写
            String toUpp = PojoPropery.replaceFirst(PojoPropery.substring(0, 1),
                    PojoPropery.substring(0, 1).toUpperCase());// 把首字母转换为大写
            // 方法名
            String methodName = "get" + toUpp;// 拼成pojo类中getXXX的方法名称
            map.put(PojoPropery, new ExportExcelColumn(i, title[i], methodName));
        }

        this.export(out, FileTitle, titleNum, resultList, map);
        return "ok";
    }
    
    /**
     * @param response
     * @param fileChName 下载时用户看到的文件名
     * @param gridTitle 列标题,逗号分隔
     * @param gridField 实体列,逗号分隔
     * @param list
     * @return
     * @throws Exception
     */
    public String exportExcelFile(HttpServletResponse response, 
    		String fileChName, String gridTitle, String gridField,
            List<?> list) throws Exception {
		//fileChName = java.net.URLEncoder.encode(fileChName, "UTF-8");
		response.reset();
		
		response.setCharacterEncoding("gb2312");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ new String((fileChName).getBytes(), "iso-8859-1") + ".xls");
		
		response.setHeader("Connection", "close");
		response.setHeader("Content-Type", "application/msexcel;charset=gb2312");
		OutputStream out = response.getOutputStream();
		
		Map<String, ExportExcelColumn> map = new HashMap<String, ExportExcelColumn>();
		// List<Object> paymentInfoList = commonDao.queryForList(sqlMap, obj);
		
		String[] title = gridTitle.split(",");
		// 实体列
		String[] propery = gridField.split(",");
		// 列标题
		Integer titleNum = title.length;// Excel中字段的个数
		String FileTitle = "";// Excel中正文标题
		
		// --建立EXCEL索引、字段名、字段值的关系，存放到map中
		for (int i = 0; i < title.length; i++) {
			String PojoPropery = propery[i];// grid中title和field是一一对应的，所以可以这么写
			String toUpp = PojoPropery.replaceFirst(PojoPropery.substring(0, 1),
			PojoPropery.substring(0, 1).toUpperCase());// 把首字母转换为大写
			// 方法名
			String methodName = "get" + toUpp;// 拼成pojo类中getXXX的方法名称
			map.put(PojoPropery, new ExportExcelColumn(i, title[i], methodName));
		}
		
		this.export(out, FileTitle, titleNum, list, map);
		return "ok";
	}

    /**
     * 根据用户所选择的字段，动态导出成excel表格(通用方法，调用即可)
     * <p/>
     * os为输出流， title为EXCEL正文标题， titleNum为EXCEL标题字段总数， dataList为要导出的数据，
     * map为列索引、标题、数据间的映射关系
     */
    public void export(OutputStream os, String title, Integer titleNum, List<?> dataList,
                       Map<String, ExportExcelColumn> map) throws Exception {

        WritableWorkbook wbook = Workbook.createWorkbook(os);// 直接写入内存，不要存放到硬盘中
        WritableSheet wsheet = wbook.createSheet("Sheet1", 0);// 定义sheet的名称
        WritableFont wfont = null; // 字体
        WritableCellFormat wcfFC = null; // 字体格式
        Label wlabel = null; // Excel表格的Cell

        for (int i = 0; i < titleNum; i++) {
            wsheet.setColumnView(i, 20);// 设置列宽
        }

        // 设置excel标题字体
        wfont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                Colour.BLACK);
        wcfFC = new WritableCellFormat(wfont);

        wcfFC.setAlignment(Alignment.CENTRE); // 设置对齐方式

        // 添加excel标题
        Label wlabel1 = new Label(5, 0, title, wcfFC);
        wsheet.addCell(wlabel1);

        // 设置列名字体
        // 如果有标题的话，要设置一下偏移
        int offset = 2;
        if (title == null || title.trim().equals("")) {
            offset = 0;
        } else {
            wfont = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                    Colour.BLACK);
            wcfFC = new WritableCellFormat(wfont);
        }

        // 添加excel表头
        Collection<ExportExcelColumn> array1 = map.values();
        Iterator<ExportExcelColumn> it1 = array1.iterator();
        while (it1.hasNext()) {
            ExportExcelColumn col = it1.next();
            wlabel = new Label(col.getIndex(), offset, col.getTitle(), wcfFC);
            wsheet.addCell(wlabel);
        }

        // 设置正文字体
        wfont = new WritableFont(WritableFont.TIMES, 14, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,
                Colour.BLACK);
        wcfFC = new WritableCellFormat(wfont);

        // 往Excel输出数据
        int rowIndex = 1 + offset;
        Collection<ExportExcelColumn> array = map.values();
        for (Object obj : dataList) {// 循环待导出的list数据集
            Iterator<ExportExcelColumn> it = array.iterator();
            while (it.hasNext()) {
                ExportExcelColumn col = it.next();
                String value = "";
                try {
                    value = String.valueOf(invokeNoArgMethod(obj, col.getMethodName()));// 利用反射机制，动态执行pojo类中get方法，获取属性值
                    if ("null".equals(value)) {
                        value = "";
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                wlabel = new Label(col.getIndex(), rowIndex, value);
                wsheet.addCell(wlabel);
            }
            rowIndex++;
        }

        wbook.write(); // 写入文件
        wbook.close();
        os.flush();
        os.close();
    }

    /**
     * (通用方法，调用即可) 使用反射机制，动态执行方法（无参方法）
     */
    public Object invokeNoArgMethod(Object owner, String methodName) throws Exception {
        Class<?> cls = owner.getClass();
        Method method = cls.getMethod(methodName);
        return method.invoke(owner);
    }

    public String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0)
                        k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }
}
