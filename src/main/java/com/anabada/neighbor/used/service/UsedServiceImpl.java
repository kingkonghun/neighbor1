package com.anabada.neighbor.used.service;

import com.anabada.neighbor.used.domain.*;
import com.anabada.neighbor.used.repository.UsedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsedServiceImpl implements UsedService{

    private final UsedRepository usedRepository;
    @Override
    public List<Used> list() {//글리스트
        List<Used> list = new ArrayList<>();
        List<Post> postList = usedRepository.postList();


        return list;
    }

    @Override
    public void write(Used used) {

    }

    @Override
    public void update(long postId) {

    }

    @Override
    public void delete(long postId) {

    }

    @Override
    public Used detail(long postId) {
        return null;
    }

    @Override
    public List<Img> images(long postId) {//사진
        return null;
    }
}
