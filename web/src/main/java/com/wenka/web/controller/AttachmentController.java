package com.wenka.web.controller;

import com.wenka.domain.model.Attachment;
import com.wenka.domain.service.AttachmentService;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.ServletContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author 文卡<wkwenka@gmail.com>  on 17-4-5.
 */
@RestController
public class AttachmentController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${upload.save.dir}")
    private String uploads_dir;
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    ServletContext context;


    @RequestMapping(
            value = {"/uploads"},
            method = {RequestMethod.POST}
    )
    public Set<Attachment> handleFormUpload(MultipartHttpServletRequest request) {
        String thumbPath = StringUtils.stripEnd(this.uploads_dir, "/") + "/thumb";
        File ufd = new File(this.uploads_dir);
        File tfd = new File(thumbPath);
        if (!ufd.exists()) {
            ufd.mkdirs();
        }

        if (!tfd.exists()) {
            tfd.mkdirs();
        }

        ArrayList oldNames = new ArrayList();
        ArrayList fileTypes = new ArrayList();
        ArrayList fileNames = new ArrayList();
        Iterator attachmentSet = request.getFiles("files").iterator();

        while (attachmentSet.hasNext()) {
            MultipartFile i = (MultipartFile) attachmentSet.next();
            if (!i.isEmpty()) {
                String attachment = i.getOriginalFilename();
                String ext = StringUtils.trimToEmpty(FilenameUtils.getExtension(attachment));
                String[] ext_t = StringUtils.split(ext, "?");
                if (ext_t != null && ext_t.length > 1) {
                    ext = ext_t[0];
                }

                String fileUID = UUID.randomUUID().toString();
                String newFileName = fileUID + "." + ext;
                oldNames.add(attachment);
                fileTypes.add(i.getContentType());
                fileNames.add(newFileName);
                File newFile;
                BufferedImage bufferedImage = null;

                try {
                    newFile = new File(this.uploads_dir, newFileName);
                    FileUtils.copyInputStreamToFile(i.getInputStream(), newFile);
                    bufferedImage = ImageIO.read(newFile);
                } catch (Exception var19) {
                    this.logger.info(this.getClass().getSimpleName(), var19.getCause());
                }

                if (bufferedImage != null) {
                    try {
                        BufferedImage e = Thumbnails.of(new BufferedImage[]{bufferedImage}).size(96, 96).asBufferedImage();
                        Thumbnails.of(new BufferedImage[]{e}).sourceRegion(Positions.CENTER, 96, 96).size(96, 96).keepAspectRatio(false).toFile(new File(thumbPath, newFileName));
                    } catch (IOException var18) {
                        this.logger.info(this.getClass().getSimpleName(), var18.getCause());
                    }
                }
            }
        }

        if (oldNames.size() == fileTypes.size() && oldNames.size() == fileNames.size() && fileTypes.size() == fileNames.size()) {
            HashSet var20 = new HashSet();

            for (int var21 = 0; var21 < oldNames.size(); ++var21) {
                Attachment var22 = new Attachment();
                var22.setOriginalName((String) oldNames.get(var21));
                var22.setContentType((String) fileTypes.get(var21));
                var22.setRealName((String) fileNames.get(var21));
                this.attachmentService.save(var22);
                var20.add(var22);
            }

            return var20;
        } else {
            throw new RuntimeException("上传失败");
        }
    }

    private boolean isImage(File file) {
        boolean flag = false;

        try {
            ImageInputStream is = ImageIO.createImageInputStream(file);
            if (null == is) {
                return flag;
            }

            is.close();
            flag = true;
        } catch (Exception var4) {
            ;
        }

        return flag;
    }

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public Attachment upload(MultipartHttpServletRequest request) throws UnsupportedEncodingException {
        String thumbPath = StringUtils.stripEnd(this.uploads_dir, "/") + "/thumb";
        File ufd = new File(this.uploads_dir);
        File tfd = new File(thumbPath);
        if (!ufd.exists()) {
            ufd.mkdirs();
        }

        if (!tfd.exists()) {
            tfd.mkdirs();
        }


        MultipartFile file = request.getFile("file");
        if (file != null){
            String contentType = file.getContentType();
            String originalFilename = new String(file.getOriginalFilename().getBytes(),"UTF-8");

            String ext = StringUtils.trimToEmpty(FilenameUtils.getExtension(originalFilename));
            String[] ext_t = StringUtils.split(ext, "?");
            if (ext_t != null && ext_t.length > 1) {
                ext = ext_t[0];
            }
            String newFileName = UUID.randomUUID().toString()  + "." + ext;

            File newFile;
            try {
                newFile = new File(this.uploads_dir, newFileName);
                FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Attachment attachment = new Attachment();

            attachment.setRealName(newFileName);
            attachment.setContentType(contentType);
            attachment.setOriginalName(URLDecoder.decode(originalFilename,"utf-8"));

            attachmentService.save(attachment);

            return attachment;
        }

        return null;
    }
}
