package com.anabada.neighbor.file.service;

import com.anabada.neighbor.file.Repository.FileRepository;
import com.anabada.neighbor.file.domain.FileRequest;
import com.anabada.neighbor.file.domain.FileResponse;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Transactional
    @Override
    public int saveFiles(Long postId, List<FileRequest> files) {
        if (CollectionUtils.isEmpty(files)) {   //리스트의 길이가 0이면 0으로리턴
            return 0;
        }
        for (FileRequest file : files) {
            file.setPostId(postId);
            fileRepository.insertFile(file);
        }
        return 1;
    }

    @Override
    public List<FileResponse> findAllFileByPostId(Long postId) {
        return fileRepository.selectFilesByPostId(postId);
    }

    @Override
    public List<FileResponse> findAllFileByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList(); //비어있는 리스트 반환
        }
        List<FileResponse> result = new ArrayList<>();
        for (Long id : ids) {
            result.add(fileRepository.selectFileById(id));
        }
        return result;
    }

    @Override
    public FileResponse findFileById(Long id) {
        return fileRepository.selectFileById(id);
    }

    @Transactional
    @Override
    public void deleteAllFileByIds(List<FileResponse> fileResponses) {
        if (CollectionUtils.isEmpty(fileResponses)) {
            return;
        }
        for (FileResponse file : fileResponses) {
            fileRepository.deleteFileById(file.getId());
        }
    }

}
