package com.saveondev.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    public static boolean writeFile(MultipartFile file) {
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            String UPLOADED_FOLDER = "//temp//";
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
