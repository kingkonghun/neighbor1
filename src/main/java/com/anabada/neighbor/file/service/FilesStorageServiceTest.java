package com.anabada.neighbor.file.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FilesStorageServiceTest {
    public void init();

    public void save(MultipartFile file);

    public Resource load(String fileName, String createDate);

    public Resource load(String fileName);

    public void deleteAll();

    public Stream<Path> loadAll();

}
