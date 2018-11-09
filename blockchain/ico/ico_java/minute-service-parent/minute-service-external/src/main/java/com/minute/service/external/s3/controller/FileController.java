package com.minute.service.external.s3.controller;

import com.minute.service.external.common.Constant;
import com.minute.service.external.s3.service.FileService;
import com.minute.service.external.s3.service.impl.FileSO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/get")
    public void getFile(@RequestParam(value = "fileUrl") String fileUrl, HttpServletResponse response) {

        if (StringUtils.isBlank(fileUrl)) {
            //TODO throw exception
        }

        FileSO fileSO = fileService.getFile(fileUrl);
        response.setContentType(fileSO.getContentType());

        InputStream fileInStream = fileSO.getFileInStream();
        int len = 0;
        byte [] buffer = new byte[512 * 1024]; //0.5M

        try {
            while ((len = fileInStream.read(buffer)) != -1) {
                response.getOutputStream().write(buffer, 0, len);
            }
        } catch (Exception e) {
            //TODO throw exception
        }

    }

    @PostMapping("/upload")
    public UploadFileResult uploadFile(@RequestParam(value = "file") MultipartFile file,
                                       @RequestParam(value = "accessPublic")  Boolean accessPublic) {
        UploadFileResult result = new UploadFileResult();

        try {
            InputStream fileInStream = file.getInputStream();

            Map<String, String> userMetaData = new TreeMap<String, String>();
            userMetaData.put(Constant.FILE_ORIGINAL_NAME, file.getOriginalFilename());
            userMetaData.put(Constant.CONTENT_TYPE, file.getContentType());

            String fileUrl = fileService
                    .uploadFile(accessPublic, fileInStream, file.getSize(), file.getContentType(), userMetaData);

            result.setUrl(fileUrl);
        } catch (Exception e) {
            //TODO log exception
            result.setCode("456");
            result.setMsg("upload file failed");
            e.printStackTrace();
        }

        //TODO 通过AOP实现通用异常处理
        result.setCode("200");
        result.setMsg("success");

        return result;
    }


}
