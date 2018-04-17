package org.com.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @Description: </br>File类使用
 *               </br>字节流操作：OutputStream、InputStream
 *               </br>字符流操作：Reader、Writer
 *               </br>对象序列化：serializable
 * @author binary
 */
public class FUtil {

    public static Boolean mkdirs(String path) {
        if (StringUtils.isNotBlank(path))
            return mkdirs(new File(path));
        return false;
    }

    public static Boolean mkdirs(File file) {
        if (!file.exists() && !file.isDirectory())
            return file.mkdirs();
        return false;
    }

    public static void main(String[] args) {
        String path = "/Users/binary/Documents/Tools/tomcat2/webapps/oa-server/upload/0419/";
        System.err.println(mkdirs(path));
    }

    public void doSomething() throws IOException {
        LineIterator it = FileUtils.lineIterator(new File(""), "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                // do something with line
            }
        } finally {
            LineIterator.closeQuietly(it);
        }
    }

}
