package com.wenka.web.controller;

import com.wenka.domain.model.AttachmentBag;
import com.wenka.domain.service.AttachmentBagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by 文卡<wkwenka@gmail.com> on 2017/5/17.
 */
@RestController
@RequestMapping("/attachmentBag")
public class AttachmentBagController extends BaseController {

    @Autowired
    private AttachmentBagService attachmentBagService;

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void save(@RequestBody AttachmentBag attachmentBag) {
        attachmentBag.setCreator(this.currentUser);
        this.attachmentBagService.save(attachmentBag);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<AttachmentBag> list(@RequestParam(required = false) String parm,
                                    @RequestParam(required = false) AttachmentBag.FileType fileType,
                                    @RequestParam(required = false) String userId) {
        if (StringUtils.isBlank(StringUtils.trimToEmpty(userId))) {
            userId = this.currentUserId;
        }
        return this.attachmentBagService.list(parm, fileType, userId);
    }

    @RequestMapping(value = "/listSize", method = RequestMethod.GET)
    public long listSize(@RequestParam(required = false) String parm,
                     @RequestParam(required = false) AttachmentBag.FileType fileType,
                     @RequestParam(required = false) String userId) {
        if (StringUtils.isBlank(StringUtils.trimToEmpty(userId))) {
            userId = this.currentUserId;
        }
        return this.attachmentBagService.listSize(parm, fileType, userId);
    }
}
