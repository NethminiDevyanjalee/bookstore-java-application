package com.debuggers.bookstore.services.upload;

import java.io.File;

public interface FileUploader {

    String upload(File file) throws FileUploaderException;

    String upload(File file,String destination) throws FileUploaderException;

}
