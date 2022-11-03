package com.hust.vitech.Controller;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {

        fileName = StringUtils.cleanPath(fileName);

        String prefixDirectory = "D:/Workspace/Vitech/vitech-admin/admin-vitech/src/assets/image";
        Path uploadPath = Paths.get(prefixDirectory + uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static void saveFiles(String uploadDir,
                                MultipartFile[] multipartFile) throws IOException {
        for (MultipartFile f:
             multipartFile) {
            saveFile( uploadDir, f.getOriginalFilename(), f);
        }
    }
}
