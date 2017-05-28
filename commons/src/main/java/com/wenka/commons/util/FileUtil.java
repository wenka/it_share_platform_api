package com.wenka.commons.util;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/28.
 */
public class FileUtil {

    /**
     * 下载文件
     *
     * @param response         HttpServletResponse
     * @param saveUrl          文件保存路径
     * @param fileRealName     文件保存的真实名称
     * @param fileOriginalName 文件上传时的名称
     * @param contentType      文件类型
     */
    public static void download(HttpServletResponse response, String saveUrl, String fileRealName, String fileOriginalName, String contentType) {

        String filePath = saveUrl + "/" + fileRealName;

        try {
            InputStream inputStream = new FileInputStream(filePath);
            response.setContentType(contentType);
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileOriginalName + "\"");
            byte[] b = new byte[1024];
            int len;

            while ((len = inputStream.read(b)) > 0) {
                response.getOutputStream().write(b, 0, len);
            }

            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
