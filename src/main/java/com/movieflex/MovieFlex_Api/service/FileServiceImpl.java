package com.movieflex.MovieFlex_Api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImpl implements FileService{
    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        //get file name
        String fileName = file.getOriginalFilename();
        //to get file path
        String filepath = path + File.separator + fileName;
//        create the file
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }
        //copy the file or upload file to path

        Files.copy(file.getInputStream(), Paths.get(filepath));

        return fileName;
    }

    @Override
    public InputStream getResourceFileAsStream(String path, String filename) throws FileNotFoundException {
        String filepath = path + File.separator + filename;

        return new FileInputStream(filepath);
    }
}
