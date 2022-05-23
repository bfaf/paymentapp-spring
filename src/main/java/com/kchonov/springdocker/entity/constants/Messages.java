package com.kchonov.springdocker.entity.constants;

/**
 *
 * @author Krasi
 */
public class Messages {
    public static final String FILE_UPLOAD_SUCCESSFUL = "File uploaded successfully";
    public static final String FILE_NOT_A_CSV = "File is not in CSV format";
    public static final String FILE_UPLOAD_ERROR(String filename) {
        return String.format("Could not upload the file: %s!", filename);
    };
}
