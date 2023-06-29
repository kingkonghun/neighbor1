package com.anabada.neighbor.file.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    private final Path root = Paths.get("/Users", "upload_anabada");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("업로드 폴더를 생성할 수 없습니다.");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("해당 이름의 파일은 이미 존재합니다.");
            }
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Resource load(String fileName, String creaDate) {
        Path file = root.resolve(fileName + File.separator + creaDate);//여기서 현재날짜 경로문제 발생 해결해야함
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("파일을 찾을 수 없습니다.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }
    @Override
    public Resource load(String fileName) {

        Path file = root.resolve(fileName);//여기서 현재날짜 경로문제 발생 해결해야함
        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }else{
                throw new RuntimeException("파일을 찾을 수 없습니다.");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }
    }
}
