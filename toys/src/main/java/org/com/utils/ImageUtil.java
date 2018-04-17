package org.com.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import com.sky.support.utils.file.FileUtil;

/**
 * Created by Thompson Tian on 14-2-26. 图片处理工具类
 */
public class ImageUtil {
    
    //private static Logger logger = Logger.getLogger(ImageUtil.class);
    
    // private static String imageMagickPath = "C:\\Program Files\\ImageMagick-6.9.2-Q16";
    // 仅在 mac 下测试时设置该值
    // private static String imageMagickPath = "/usr/local/Cellar/imagemagick/6.8.9-8/bin";

    public static List<String> imgExtends = Arrays.asList(".gif", ".jpg", ".jpeg", 
            ".jpe", ".png", ".bmp", ".dib", ".jfif", ".tif", ".tiff", ".ico");
    
//    public static void main(String[] args) {
//        try {
//
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//	}
    
    /**
     * 压缩图片存储大小，不改变图片尺寸，覆盖原文件
     * 输出到本目录，输出文件名为 src + ".0.jpg"
     *
     * @param rootPath 源图片地址
     * @param src 源图片名
     * @return
     */
    public static String reImage(String rootPath, String src) {
        try {
            String out = src + ".0.jpg";
            String rPath = rootPath + File.separator;
            File f = new File(rPath + src);

            InputStream is = new FileInputStream(f);
            BufferedImage buff = ImageIO.read(is);
            int width = buff.getWidth();
            int height = buff.getHeight();
            is.close();
            
            ConvertCmd cmd = new ConvertCmd();
            IMOperation op = new IMOperation();
            op.addImage(rPath + src);
            op.resize(width, height);
            op.addRawArgs("-quality", "75"); // 图片品质 一般用 75
            op.addRawArgs("-strip");
            op.addImage(rPath + out);
            
            //linux下不要设置此值，不然会报错
            //cmd.setSearchPath("C:\\ImageMagick");
            cmd.run(op);
            return out;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 压缩图片，重新设置图片尺寸
     *
     * @param width
     *            宽度
     * @param height
     *            高度
     * @param src
     *            源图片地址
     * @param out
     *            目标地址
     * @param quality
     *            图片品质 一般用 75
     * @return
     */
    public static String resizeImage(int width, int height, String src, String out, String quality) {
        // 创建输出文件夹
        // FileUtil.createDir(out.substring(0, out.lastIndexOf(File.separator)));
    	String fullPathNoEndSeparator = FilenameUtils.getFullPathNoEndSeparator(out);
    	FileUtil.createDir(fullPathNoEndSeparator);
    	
        ConvertCmd cmd = new ConvertCmd();
        IMOperation op = new IMOperation();
        op.addImage(src);
        if (0 == width)
            op.resize(null, height);
        else if (0 == height)
            op.resize(width, null);
        else
            op.resize(width, height);//.gravity("center").extent(width, height);
        op.addRawArgs("-quality", quality);
        op.addRawArgs("-strip");
        op.addImage(out);
        // execute the operation
        try {
//        	cmd.setSearchPath("E:\\installation\\ImageMagick-6.9.2-Q8");
            cmd.run(op);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IM4JavaException e) {
            e.printStackTrace();
            return null;
        }
        return out;
    }

    /**
     * 根据坐标裁剪图片
     *
     * @param srcPath
     *            要裁剪图片的路径
     * @param newPath
     *            裁剪图片后的路径
     * @param x
     *            起始横坐标
     * @param y
     *            起始纵坐标
     * @param width
     *            剪切图片的宽度
     * @param height
     *            剪切图片的高度
     */
    public static void cutImage(String srcPath, String newPath, int x, int y, int width, int height) {
        IMOperation op = new IMOperation();
        op.addImage(srcPath);
        /**
         * width： 裁剪的宽度 height： 裁剪的高度 x： 裁剪的横坐标 y： 裁剪的挫坐标
         */
        op.crop(width, height, x, y);
        op.addImage(newPath);
        ConvertCmd convert = new ConvertCmd();

        // linux下不要设置此值，不然会报错
        // if (null != imageMagickPath) convert.setSearchPath(imageMagickPath);

        try {
            convert.run(op);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断图片扩展名是否正确
     *
     * @param imgExtend
     * @return
     */
    public static boolean isImgExtend(String imgExtend) {
        for (String iExtend : imgExtends) {
            if (iExtend.equals(imgExtend))
                return true;
        }
        return false;
    }

    /**
     * 判断是否是图片
     *
     * @param inputStream
     * @return
     */
    public static boolean isImgByInputStream(InputStream inputStream) {
        if (null == inputStream)
            return false;
        Image img = null;
        try {
            img = ImageIO.read(inputStream);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            img = null;
        }
        return false;
    }

    /**
     * 压缩图片{所有图片都转成jpg}
     *
     * @param src
     * @param widths
     * @param quality
     */
    public static void doResize(String src, String[] widths, String quality) {
        if (StringUtils.isBlank(src))
            return;
        for (String width : widths){
            ImageUtil.resizeImage(Integer.parseInt(width), 0, src, src + "." + width + src.substring(src.lastIndexOf(".")), quality);
        }
    }
    
    /**
     * base64字符串转化成图片
     * 对字节数组字符串进行Base64解码并生成图片
     * 
     * @param imgStr 图像数据
     * @return
     */
    public static boolean GenerateImage(String imgStr, String imgFilePath) {
        try {
            // Base64解码
            byte[] b = Base64.getDecoder().decode(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 图片转化成base64字符串
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * 
     * @param imgFilePath 待处理的图片存储位置
     * @return
     */
    public static String GetImageStr(String imgFilePath) {
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        return Base64.getEncoder().encodeToString(data);// 返回Base64编码过的字节数组字符串
    }
    
    /**
     * 根据图片路径复制一张新图片
     *
     * @param rootPath 源图片地址
     * @param newPath 新图片路径
     * @return
     */
    public static String copyImage(String rootPath, String newPath) {
        try {
            File f = new File(rootPath);

            InputStream is = new FileInputStream(f);
            BufferedImage buff = ImageIO.read(is);
            int width = buff.getWidth();
            int height = buff.getHeight();
            is.close();
            
            ConvertCmd cmd = new ConvertCmd();
            IMOperation op = new IMOperation();
            op.addImage(rootPath);
            op.resize(width, height);
            op.addRawArgs("-quality", "75"); // 图片品质 一般用 75
            op.addRawArgs("-strip");
            op.addImage(newPath);
            
            //linux下不要设置此值，不然会报错
            //cmd.setSearchPath("C:\\ImageMagick");
            cmd.run(op);
            return newPath;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 富文本中的 base64图片数据转存为 .jpg图片文件
     * 在富文本中替换为 链接地址
     * relativePath 存储位置，要求以“/” 结束，建议按时间分目录
     * 
     * @param content
     * @param rootPath 本地根目录
     * @param relativePath 存储位置，要求以“/” 结束
     * @param date
     *          当前时间
     * @return
     * @throws IOException 
     */
    public static String replaceBase64WithImgURL(String content, String rootPath, 
            String relativePath, Date date) throws IOException {
        if(StringUtils.isBlank(content)) {
            return content;
        }
        
        String img = "";
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(content);
        
        long times = date.getTime();
        int num = 0;
        String absolutePath = rootPath + relativePath;// 绝对路径
        File localFile = new File(absolutePath);
        if(!localFile.exists() || !localFile.isDirectory()) {
            localFile.mkdirs();
        }
        
        // 只能是find或matches后才能得到groups, 先匹配 img
        while(m_image.find()){
            // group() 返回匹配到的子字符串
            img = img + "," + m_image.group();
            Matcher m  = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while(m.find()){
                String base64_temp = m.group(1);
                if(base64_temp.contains("data:image/")) {
                    // 后缀
                    String suffix = base64_temp.substring(base64_temp.indexOf("/") + 1, base64_temp.indexOf(";"));
                    // 通过 "," 分隔为两部分，data:image/png;base64  和   base64图片数据
                    String image_base64 = base64_temp.split(",")[1];
                    // 生成图片名 // 保证目录存在
                    num = num + 1;
                    String fileName = times + "_" + num + "." + suffix;
                    // 如果重名，num不断加1
                    while(new File(absolutePath + fileName).exists()) {
                        num = num + 1;
                        fileName = times + "_" + num + "." + suffix;
                    }
                    
                    // base64字符串转化成图片
                    com.sky.support.utils.image.ImageUtil.GenerateImage(image_base64, absolutePath + fileName);
                    
                    try {
                        // 压缩存储大小，不改变尺寸
                        String outName = com.sky.support.utils.image.ImageUtil.reImage(absolutePath, fileName);
                        if(StringUtils.isNotBlank(outName)) {
                            fileName = outName;
                        }
                    } catch (Exception e) {
                    }
                    
                    int first = content.indexOf(base64_temp);
                    if(first > 0) {
                        StringBuffer content_buffer = new StringBuffer();
                        content_buffer.append(content.substring(0, first));
                        content_buffer.append("/" + relativePath + fileName);
                        content_buffer.append(content.substring(first + base64_temp.length()));
                        content = content_buffer.toString();
                    }
                }
            }
        }
        return content;
    }
}
