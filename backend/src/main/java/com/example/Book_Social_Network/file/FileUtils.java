package com.example.Book_Social_Network.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class FileUtils {
    public static byte[] readFileFromLocation(String fileUrl) {
        if(StringUtils.isBlank(fileUrl)){
            return null;
        }
        try {
            Path path = Paths.get(fileUrl);
            return  Files.readAllBytes(path);
        } catch (java.io.IOException e) {
            log.warn(e.getMessage());
            e.printStackTrace();
            return null;
        }

    }


}
