package org.com.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @Description: 记录日常开发时的小技巧
 * @author binary
 */
public class DevelopSkill {

    public static void main(String[] args) {
        try {

            Properties pps = System.getProperties();
            pps.list(System.out);

            String path = "/Users/binary/Documents/Tools/tomcat2/webapps/oa-server/upload/0419/test.txt";
            // String path =
            // "/Users/binary/Documents/Tools/tomcat2/webapps/oa-server/upload";
            File file = new File(path);
            // file.mkdirs();
            if (!file.exists()) {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void doSomething() {
        /**
         * 转换为String类型，使用toString方法效率最高
         */
        Integer.toString(1);
        
        /**
         * 向文件末尾添加内容
         */
        try (BufferedWriter out = new BufferedWriter(new FileWriter(new File(""), true))) {
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }

}
