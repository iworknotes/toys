package org.com.utils;

import com.sky.entity.common.Result;
import com.sky.support.BaseLog;
import com.sky.support.date.CDateUtil;
import com.sky.support.utils.file.FileUtil;
import com.sky.support.utils.string.MD5Util;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2016/1/26 0026.
 */
public class FileUpload extends BaseLog {

    /**
     * 获取文件路径
     * 
     * @param request
     * @param hashPath
     * @param image_ROOT_PATH com.sky.utils.admin.Constant.IMAGE_ROOT_PATH
     * @param filenetpath
     * @param filedirpath
     */
    public static void getAndMakePath(HttpServletRequest request, int hashPath, String image_ROOT_PATH,
    		StringBuffer filenetpath, StringBuffer filedirpath) {
        String path = File.separator + CDateUtil.getdateformat("yyyy") + File.separator + CDateUtil.getdateformat("MM")
                + File.separator + CDateUtil.getdateformat("dd") + "_" + CDateUtil.getdateformat("HH") + File.separator
                + hashPath + File.separator;
        String filepath = image_ROOT_PATH + path;
        String dirpath = FileUtil.getDir(request, filepath);
        FileUtil.mkdir(new File(dirpath));
        filenetpath.append(filepath);
        filedirpath.append(dirpath);
    }

    /**
     * @param request
     * @param imgpath
     * @return
     */
    public Result uploadHeadPortrait(HttpServletRequest request, String imgpath) {
        String imgName = System.currentTimeMillis() + "";
        if (StringUtils.isNotEmpty(imgpath) && StringUtils.isNotEmpty(imgName)) {
            // 图片扩展名
            String fileExtendName = imgpath.substring(imgpath.indexOf('/') + 1, imgpath.indexOf(';'));
            String fileName = MD5Util.string2MD5(imgName + new Random(new Date().getTime())) + "." + fileExtendName;
            StringBuffer filenetpath = new StringBuffer();
            StringBuffer filedirpath = new StringBuffer();
            FileUtil.getAndMakePath(request, 123, filenetpath, filedirpath);
            String filenetsrc = (filenetpath.toString() + fileName).replaceAll("\\\\", "\\/");
            return FileUtil.base64ToFile((imgpath.substring(imgpath.indexOf("base64") + 7)),
                    filedirpath.toString() + fileName, filenetsrc);
        } else {
            Result result = new Result(1, "操作失败");
            result.setMsg("不允许上传空文件");
            return result;
        }

    }

}
