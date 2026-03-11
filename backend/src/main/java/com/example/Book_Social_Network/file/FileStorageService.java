package com.example.Book_Social_Network.file;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {
    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;


    public String saveFile(@NonNull MultipartFile sourceFile,

                           @NonNull Integer userId) {

        final  String fileUploadSubPath="users"+ File.separator+userId;
        return uploadFile(sourceFile,fileUploadSubPath);

    }

    private String uploadFile(@NonNull MultipartFile sourceFile,@NonNull String fileUploadSubPath) {
        final String finalUploadPath=fileUploadPath+File.separator+fileUploadSubPath;
        File targetFolder=new File(finalUploadPath);

        if(!targetFolder.exists()){
            boolean folderCreated=targetFolder.mkdirs();
            if(!folderCreated) {
                log.warn("Could not create directory {}",targetFolder.getAbsolutePath());
            }
        }

        final String fileExtention=getFileExtention(sourceFile.getOriginalFilename());
        String targerFilePath=finalUploadPath+ File.separator +System.currentTimeMillis()+"."+fileExtention;

        Path targetPath=Path.of(targerFilePath);
        try {
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File uploaded successfully");
            return targerFilePath;
        } catch (Exception e) {
            log.error("Error while saving file {} to path {}: {}",sourceFile.getOriginalFilename(),targerFilePath,e.getMessage());
            throw new RuntimeException("Failed to save file "+sourceFile.getOriginalFilename(),e);
        }

    }

    private String getFileExtention(String originalFilename) {
        if(originalFilename==null || !originalFilename.contains(".")) {
            return "";
        }
        int lastDotIndex=originalFilename.lastIndexOf(".");
        if(lastDotIndex==originalFilename.length()-1) {
            return "";
        }
        return originalFilename.substring(originalFilename.lastIndexOf(".")+1).toLowerCase();
    }
}
