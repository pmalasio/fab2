package eu.cehj.cdb2.common.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageService.class);

    private final Path rootLocation = Paths.get("uploads");

    public void store(final MultipartFile file) throws IOException {
        try (InputStream is = file.getInputStream()) {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        }catch(final FileAlreadyExistsException e) {
            final String fileName = file.getOriginalFilename();
            LOGGER.warn(String.format("File '%s' already exists: deleting it", fileName));
            this.deleteFile(fileName);
            throw e;
        }
    }

    public Resource loadFile(final String filename) throws IOException {
        final Path file = this.rootLocation.resolve(filename);
        final Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        }
        return null;
    }

    public void deleteFile(final String fileName) throws IOException{
        Files.deleteIfExists(this.rootLocation.resolve(fileName));
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(this.rootLocation.toFile());
    }

    @PostConstruct
    public void init() throws IOException {
        Files.createDirectories(this.rootLocation);
    }
}
