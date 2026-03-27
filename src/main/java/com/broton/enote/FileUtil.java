package com.broton.enote;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Author ：ZhangRY
 * @Time ：2024-07-12 08:51:57
 * @Description ：file类型完美转换为multipartFile类型
 */
public class FileUtil {

    public static MultipartFile fileToMultipartFile(File file) {
        FileItem fileItem = createFileItem(file);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    public static FileItem createFileItem(File file) {

        DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory(16, null);
        Path path = Paths.get(file.getAbsolutePath());
        String contentType;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileItem fileItem = diskFileItemFactory.createItem("file", contentType, true, file.getName());

        int read = 0;
        byte[] buffer = new byte[1024];

        try {
            FileInputStream is = new FileInputStream(file);
            OutputStream os = fileItem.getOutputStream();
            while ((read = is.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, read);
            }
            Files.copy(file.toPath(), os);
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileItem;
    }
}