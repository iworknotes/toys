package org.com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.sky.entity.common.Result;
import com.sky.enums.ResultType;
import com.sky.utils.admin.Constant;

public class FileUtil {

    // 保存上传文件路径的日期格式部分
    public static String UPLOAD_FILE_PATH_DATE = "yyyy/MM";

    //(com.office.common.Constant中有copy 改动时也要同时修改)
    public static String HTML_REPLACE_REGEX = "(?is)<body>(.*?)</body>";
    public static String HTML_TEMPLATE_MOBILE = "<html><head><meta charset=\"utf-8\"><title>%s</title></head>"
            + "<style>img{width:320px;}</style><body style=\"margin:0 auto;text-align:center;\">%s</body></html>";
    public static String HTML_TEMPLATE = "<html><head><meta charset=\"utf-8\"><title>%s</title></head><body>%s</body></html>";
    /**
     * HTML_5 专用模板
     */
    public static String HTML_5_TEMPLATE = "<!DOCTYPE html><html><head><meta charset=\"utf-8\" />"
    		+ "<meta name=\"viewport\" content=\"initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" />"
    		+ "<script src=\"/myassets/js/zh-m-head-article.js\"></script></head><body>%s</body></html>";

    /**
	 * 
	 * @param file
	 */
	public static boolean mkdir(File file){
		boolean ifexit = false;
		if(isDir(file) == true){
			ifexit = true;
		}else{
			file.mkdirs();
		}
		return ifexit;
	}

    /**
     * 创建文件夹
     * @param path  目录
     */
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) dir.mkdirs();
    }
	
	/** 
	 * 删除文件、文件夹 
	 */  
	public static void deleteFile(String path) {  
	    File file = new File(path);  
	    if (file.isDirectory()) {  
	        File[] ff = file.listFiles();  
	        for (int i = 0; i < ff.length; i++) {  
	            deleteFile(ff[i].getPath());  
	        }  
	    }  
	    file.delete();  
	}

	/**
	 * 获取文件路径
	 *
	 * @param request
	 * @param hashPath	123
	 * @param filenetpath
	 * @param filedirpath
	 */
	public static void getAndMakePath(HttpServletRequest request, 
			int hashPath, StringBuffer filenetpath, StringBuffer filedirpath) {
		// filepath = fileupload/file/2016/08/31_10/123/
		Date date = new Date();
		String filepath = "fileupload/file" 
				+ File.separator + new SimpleDateFormat("yyyy").format(date) 
				+ File.separator + new SimpleDateFormat("MM").format(date)
				+ File.separator + new SimpleDateFormat("dd").format(date) + "_" + new SimpleDateFormat("HH").format(date) 
				+ File.separator + hashPath + File.separator;

		String dirpath = FileUtil.getDir(request, filepath);
		FileUtil.mkdir(new File(dirpath));
		filenetpath.append(filepath);
		filedirpath.append(dirpath);
	}

	/**
	 * 获取文件列表
	 *
	 * @param path
	 * @return
	 */
	public static List<String> getFileList(String path) {
		File dir = new File(path);
		File[] files = dir.listFiles();
		List<String> fileList = new ArrayList<String>();

		if (files == null)
			return null;
		for (File file : files) {
			if (file.isDirectory()) {
				fileList.addAll(getFileList(file.getAbsolutePath()));
			} else {
				fileList.add(file.getAbsolutePath());
			}
		}
		return fileList;
	}

	/**
	 * 将 inputStream 转换成 string
	 *
	 * @param stream
	 * @return
	 */
	public static String inputStreamToString(InputStream stream) {
		if (null == stream)
			return null;
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		String line = "";
		try {
			reader = new BufferedReader(new InputStreamReader(stream));
			while ((line = reader.readLine()) != null) {
				// if (0 < builder.length()) builder.append(Constant.ENTER);
				builder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	
	/**
     * 保存 html5 文件
     *
     * @param realPath 文件存放绝对路径
     * @param content 文件body内容
     */
    public static void saveHTML5File(String realPath, String content) {
        FileUtil.createFile(realPath, String.format(HTML_5_TEMPLATE, content), "utf-8");
    }
    
    /**
     * 保存 html 文件
     *
     * @param realPath 文件存放绝对路径
     * @param content 文件body内容
     */
    public static void updateHTML5File(String realPath, String content) {
        FileUtil.updateFile(realPath, String.format(HTML_5_TEMPLATE, content), "utf-8");
    }
	
	/**
     * 保存html文件
     *
     * @param realPath
     * @param title 文件title标题
     * @param content 文件body内容
     */
    public static void doSaveFile(String realPath, String title, String content, boolean is_mobile) {
        if (is_mobile) {
        	FileUtil.createFile(realPath, String.format(HTML_TEMPLATE_MOBILE, title, content), "utf-8");
        } else {
        	FileUtil.createFile(realPath, String.format(HTML_TEMPLATE, title, content), "utf-8");
        }
    }
    
    /**
     * 更新文件内容
     *
     * @param filename 文件名
     * @param fileContent 文件内容
     * @param strCode 文件编码
     */
    public static void updateFile(String filename, String fileContent, String strCode) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(filename));
            out.write(fileContent.getBytes(strCode));
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建新文件
     *
     * @param filename
     *            文件名
     * @param fileContent
     *            文件内容
     * @param strCode
     *            文件编码
     */
    public static void createFile(String filename, String fileContent, String strCode) {
        com.sky.support.utils.file.FileUtil.delFile(filename);
        File file = new File(filename);
        if (!file.getParentFile().exists()) {
        	file.getParentFile().mkdirs();
        }
        FileOutputStream out = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
                out = new FileOutputStream(file, true);
                out.write(fileContent.getBytes(strCode));
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除文件
     *
     * @param filename
     *            文件名
     */
    public static boolean delFile(String filename) {
        File file = new File(filename);
        if (file.exists() && file.isFile())
            return file.delete();
        return true;
    }

    /**
     * 读取文件
     *
     * @param fileName
     * @return
     */
    public static String readFile(String fileName, String strCode) {
        StringBuilder fileContent = new StringBuilder();
        BufferedReader reader = null;
        InputStreamReader read = null;
        try {
            File f = new File(fileName);
            if (f.isFile() && f.exists()) {
                read = new InputStreamReader(new FileInputStream(f), strCode);
                reader = new BufferedReader(read);
                String line = "";
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line);
                }
                read.close();
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (read != null) {
                    read.close();
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContent.toString();
    }

    /**
     * 读取文件
     *
     * @param url
     * @param strCode
     * @return
     */
    public static String readFile(URL url, String strCode) {
        StringBuilder fileContent = new StringBuilder();
        InputStreamReader read = null;
        BufferedReader reader = null;
        try {
            read = new InputStreamReader(url.openStream(), strCode);
            reader = new BufferedReader(read);
            String line = "";
            while ((line = reader.readLine()) != null) {
                fileContent.append(line);
            }
            // read.close();
            // reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != read)
                    read.close();
                if (null != reader)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileContent.toString();
    }

    /**
     * 通过文件路径获取HTML文件内容
     * <body>文件内容</body>
     *
     * @param url
     * @return
     */
    public static String getHtmlFile(String url) {
        return getRegexFind(FileUtil.readFile(url, "utf-8"), HTML_REPLACE_REGEX);
    }

    /**
     * 通过文件路径获取文件内容
     * <body>文件内容</body>
     *
     * @param url
     * @return
     */
    public static String getHtmlFile(URL url) {
        return getRegexFind(FileUtil.readFile(url, "utf-8"), HTML_REPLACE_REGEX);
    }

    public static String getRegexFind(String source, String regex) {
        StringBuilder builder = new StringBuilder();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        while (m.find()) {
            builder.append(m.group(1));
        }
        return builder.toString();
    }

    public static Result base64ToFile(String base64, String savepath, String returnPath) {
        Result result = new Result();
        result.setStatus(ResultType.failure.getIndex());
        result.setMsg("文件存储路径异常，请通知管理员检测配置文件项");
        try {
            FileOutputStream write = new FileOutputStream(savepath);
            byte[] decoderBytes = Base64.getDecoder().decode(base64);
            write.write(decoderBytes);
            write.flush();
            write.close();
            write = null;
            result.setStatus(ResultType.success.getIndex());
            result.setMsg(returnPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取绝对路径
     * 
     * @param path
     * @return
     */
    public static String getDir(HttpServletRequest request, String path){
        String str = "";
        // 如果已经是绝对路径，/opt  ||   D:    开头
        if(path.subSequence(0, 1).equals("/") || path.subSequence(1, 2).equals(":")){
            str = path;
        }else{
            // str = request.getSession().getServletContext().getRealPath("/") +
            // path;
            str = Constant.UPLOAD_ROOT + File.separator + path;
        }
        str = str.replaceAll("\\\\", "/");
        return str;
    }

    /**
     * 判断文件夹是否存在
     * @param file
     * @return
     */
    public static boolean isDir(File file){
        if(file == null){
            return false;
        }else if  (!file .exists()  && !file .isDirectory()){
            return false;
        } else {
            return true;
        }
    }

    public static String getSize(final Long size) {
        return getSize(size.intValue());
    }

    public static String getSize(final Integer size) {
        if (size == null)
            return "0KB";
        else {
            final Double kb = Double.valueOf(size) / 1024;
            if (kb < 1024)
                return formatFloat(kb) + "KB";
            else {
                final Double mb = kb / 1024;
                if (mb < 1024)
                    return formatFloat(mb) + "MB";
                else {
                    final Double gb = mb / 1024;
                    if (gb < 1024)
                        return formatFloat(gb) + "GB";
                    else {
                        final Double tb = gb / 1024;
                        return formatFloat(tb) + "TB";
                    }
                }
            }
        }
    }

    public static Double formatFloat(final Double source) {
        try {
            return new BigDecimal(source).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0d;
    }
    
    /**
	 * 获取文件真实存储路径
	 * 
	 * @param path
	 * @return
	 */
	public static String getSaveSrcPath(String path){
        SimpleDateFormat format = new SimpleDateFormat(UPLOAD_FILE_PATH_DATE);
        // String dd =format.format(new Date());
		return path+"/"+format.format(new Date())+"big";
	}
	
	/**
	 * 获取文件压缩存储路径
	 * 
	 * @param path
	 * @return
	 */
	public static String getSaveLastPath(String path){
        SimpleDateFormat format = new SimpleDateFormat(UPLOAD_FILE_PATH_DATE);
        // String dd =format.format(new Date());
		return path+"/"+format.format(new Date())+"last";
	}
	
	/**
     * 下载文件到本地
     *
     * @param url_path_filename
     *            url_path_filename[0] 被下载的文件地址 url_path_filename[1] 本地文件名保存路径
     *            url_path_filename[2] 本地文件名
     */
    public static String download(String... url_path_filename) {
        String urlStr = url_path_filename[0];
        String path = url_path_filename[1];
        String filename;
        if (2 < url_path_filename.length)
            filename = url_path_filename[2];
        else
            filename = urlStr.substring(urlStr.lastIndexOf("/") + 1);
        String filePathName = path + File.separator + filename;
        InputStream is = null;
        OutputStream os = null;

        try {
            // 构造URL
            URL url = new URL(urlStr);
            // 打开连接
            URLConnection con = url.openConnection();
            // 输入流
            is = con.getInputStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            os = new FileOutputStream(filePathName);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePathName;
    }
}
