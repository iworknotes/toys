package org.com.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

/**
 * 词汇过滤
 * 
 * @author binary
 *
 */
public class KeyFilter extends BaseLog {

    private static final char endTag = (char) (1);

    @SuppressWarnings("rawtypes")
    private static Map<Character, HashMap> filterMap;

    // 读取配置间隔时间(h)
    private static int loadInterval = 1;

    public KeyFilter() {
        init();
    }

    private void init() {
        loadFile();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                loadFile();
            }
        }, loadInterval, loadInterval, TimeUnit.HOURS);

    }

    private void loadFile() {
        String webPath = KeyFilter.class.getClassLoader().getResource("").getPath();
        File confFolder = new File(webPath);
        File[] files = confFolder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.startsWith("words") && name.endsWith(".properties"));
            }
        });
        Properties property = new Properties();
        for (File file : files) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                property.load(new InputStreamReader(fis, "UTF-8"));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                if (null != fis) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
        initFilterWord(property);
    }

    /**
     * 加载敏感词词库
     * 
     * @param fileName
     *            文件名
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initFilterWord(Properties property) {
        filterMap = new HashMap<Character, HashMap>(1024);
        Enumeration enu = property.propertyNames();
        while (enu.hasMoreElements()) {
            String filterWord = (String) enu.nextElement();
            char[] charArray = filterWord.trim().toCharArray();
            int len = charArray.length;
            if (len > 0) {
                Map<Character, HashMap> subMap = filterMap;
                for (int i = 0; i < len - 1; i++) {
                    Map<Character, HashMap> obj = subMap.get(charArray[i]);
                    if (obj == null) {
                        // 新索引，增加HashMap
                        int size = (int) Math.max(2, 16 / Math.pow(2, i));
                        HashMap<Character, HashMap> subMapTmp = new HashMap<Character, HashMap>(size);
                        subMap.put(charArray[i], subMapTmp);
                        subMap = subMapTmp;
                    } else {
                        // 索引已经存在
                        subMap = obj;
                    }
                }
                // 处理最后一个字符
                Map<Character, HashMap> obj = subMap.get(charArray[len - 1]);
                if (obj == null) {
                    // 新索引，增加HashMap，并设置结束符
                    int size = (int) Math.max(2, 16 / Math.pow(2, len - 1));
                    HashMap<Character, HashMap> subMapTmp = new HashMap<Character, HashMap>(size);
                    subMapTmp.put(endTag, null);
                    subMap.put(charArray[len - 1], subMapTmp);
                } else {
                    // 索引已经存在,设置结束符
                    obj.put(endTag, null);
                }
            }
        }

    }

    /**
     * 将敏感词替换成*
     * 
     * @param info
     * @return
     */
    public static String getFilterString(String info) {
        return getFilterString(info, "*");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String getFilterString(String info, String replaceTag) {
        if (StringUtils.isEmpty(info) || StringUtils.isEmpty(replaceTag)) {
            return info;
        }
        char[] charArray = info.toCharArray();
        int len = charArray.length;
        String newInfo = "";
        int i = 0;
        while (i < len) {
            int end = -1;
            int index;
            Map<Character, HashMap> sub = filterMap;
            for (index = i; index < len; index++) {
                sub = sub.get(charArray[index]);
                if (sub == null) {
                    // 匹配失败，将已匹配的最长字符进行替换
                    if (end == -1) {
                        // 没匹配到任何关键词
                        newInfo += charArray[i];
                        i++;
                        break;
                    } else {
                        // 将最长匹配字符串替换为特定字符
                        for (int j = i; j <= end; j++) {
                            newInfo += replaceTag;
                        }
                        i = end + 1;
                        break;
                    }
                } else {
                    if (sub.containsKey(endTag)) {
                        // 匹配
                        end = index;
                    }
                }
            }
            if (index >= len) {
                // 字符串结束
                if (end == -1) {
                    // 没匹配到任何关键词
                    newInfo += charArray[i];
                    i++;
                } else {
                    // 将最长匹配字符串替换为特定字符
                    for (int j = i; j <= end; j++) {
                        newInfo += replaceTag;
                    }
                    i = end + 1;
                }
            }
        }
        return newInfo;
    }

}