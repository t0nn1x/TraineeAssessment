package com.testapi.accounting.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for file storage operations.
 */

@Service
public class FileStorageService {
    private final Path fileStorageLocation = Paths.get("src/main/resources/static/photos");

    /**
     * Store a file in the file storage location.
     * 
     * @param file MultipartFile to be stored.
     * @return Name of the stored file.
     * @throws RuntimeException if there's an error during file storage.
     */
    
    public String storeFile (MultipartFile file) {
        try{
            // Check if the file's name contains invalid characters
            if (file.getOriginalFilename().contains("..")){
                throw new RuntimeException("Filename contains invalid path sequence " + file.getOriginalFilename());
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = fileStorageLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return file.getOriginalFilename();
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }
}
