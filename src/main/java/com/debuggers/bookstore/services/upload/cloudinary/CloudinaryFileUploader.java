package com.debuggers.bookstore.services.upload.cloudinary;

import com.cloudinary.*;

import com.cloudinary.utils.ObjectUtils;
import com.debuggers.bookstore.services.upload.FileUploader;
import com.debuggers.bookstore.services.upload.FileUploaderException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class CloudinaryFileUploader implements FileUploader {
    final private Map uploadDataMap = ObjectUtils.asMap();

    final private Map authenticationDataMap = ObjectUtils.asMap(
            "cloud_name", "bookberries",
            "api_key", "916994977651899",
            "api_secret", "UfwNGuw3Ugp-87sTaCDeRZUlb20");

    private File file;

    private Cloudinary cloudinary = new Cloudinary(authenticationDataMap);

    @Override
    public String upload(File file) throws FileUploaderException {
        this.file = file;
        return uploadFile();
    }

    @Override
    public String upload(File file, String destination) throws FileUploaderException {
        this.file = file;
        this.uploadDataMap.put("public_id", destination);
        return uploadFile();
    }

    private String uploadFile() throws FileUploaderException {
        Map uploadResult = null;
        try {
            uploadResult = cloudinary.uploader().upload(file, uploadDataMap);
        } catch (IOException e) {
            throw new FileUploaderException(e);
        }
        return uploadResult.get("url").toString();
    }

}
