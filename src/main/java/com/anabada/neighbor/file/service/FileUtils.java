package com.anabada.neighbor.file.service;

import com.anabada.neighbor.file.controller.FileController;
import com.anabada.neighbor.file.domain.FileInfo;
import com.anabada.neighbor.file.domain.FileRequest;
import com.anabada.neighbor.file.domain.FileResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileUtils {
    private final String uploadPath = Paths.get(System.getProperty("user.home"), "upload_anabada").toString();

    /**
     * 다중 파일 업로드
     * @param multipartFileList - 파일 객체 List
     * @return DB에 저장할 파일 정보 List
     */
    public List<FileRequest> uploadFiles(final List<MultipartFile> multipartFileList) {
        List<FileRequest> files = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            if (multipartFile.isEmpty()) {
                continue; //리스트안의 이미지가 없을경우 건너뛰기
            }
            files.add(uploadFile(multipartFile));
        }
        return files;
    }

    /**
     * 단일 파일 업로드
     * @param multipartFile - 파일 객체
     * @return DB에 저장할 파일 정보
     */
    private FileRequest uploadFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null; //업로드된 파일이 비어 있는지, 즉 멀티파트 형식에서 파일이 선택되지 않았는지 또는 선택한 파일에 내용이 없는지 여부를 반환한다.
        }
        //getOriginalFilename 클라이언트 파일시스템의 원래 파일 이름을 반환한다.
        String saveName = generateSaveFilename(multipartFile.getOriginalFilename());
        //현재 컴퓨터의 날짜형식을 yyMMdd 로 String 변환해 가져온다.
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String uploadPath = getUploadPath(today) + File.separator + saveName;
        File uploadFile = new File(uploadPath);
        try {
            multipartFile.transferTo(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return  FileRequest.builder()
                .originalName(multipartFile.getOriginalFilename())
                .saveName(saveName)
                .size(multipartFile.getSize())
                .build();
    }

//    /**
//     * 프로필 사진 업로드
//     * @param multipartFile - 파일 객체
//     * @return DB에 저장할 파일 정보
//     */
//    private FileRequest uploadProfileFile(MultipartFile multipartFile) {
//        if (multipartFile.isEmpty()) {
//            return null; //업로드된 파일이 비어 있는지, 즉 멀티파트 형식에서 파일이 선택되지 않았는지 또는 선택한 파일에 내용이 없는지 여부를 반환한다.
//        }
//        //getOriginalFilename 클라이언트 파일시스템의 원래 파일 이름을 반환한다.
//        String saveName = generateSaveFilename(multipartFile.getOriginalFilename());
//        //현재 컴퓨터의 날짜형식을 yyMMdd 로 String 변환해 가져온다.
//        String profile = "profile";
//        String uploadPath = getUploadPath(profile) + File.separator + saveName;
//        File uploadFile = new File(uploadPath);
//        try {
//            multipartFile.transferTo(uploadFile);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return  FileRequest.builder()
//                .originalName(multipartFile.getOriginalFilename())
//                .saveName(saveName)
//                .size(multipartFile.getSize())
//                .build();
//    }





    /**
     * 컴퓨터에 저장된 실제 이미지 로드
     * @param saveName 실제 저장된 파일명
     * @param addPath 추가경로 (날짜, 프로필폴더 등)
     * @return 이미지 리소스
     */
    public Resource load(String saveName, String addPath) {
        Path file = Paths.get(uploadPath + File.separator + addPath + File.separator + saveName);
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

    /**
     * 파일 삭제 (from Disk)
     * @param files - 삭제할 파일 정보 List
     */
    public void deleteFiles(final List<FileResponse> files) {
        if (CollectionUtils.isEmpty(files)) {
            return ;    // 리스트가 비어있으면 그냥 리턴
        }
        for (FileResponse file : files) {
            String uploadedDate = file.getCreatedDate().toLocalDate()
                    .format(DateTimeFormatter.ofPattern("yyMMdd"));
            deleteFile(uploadedDate, file.getSaveName());
        }
    }

    /**
     * 파일 삭제 (from Disk)
     * @param addPath  추가 경로
     * @param fileName  파일명
     */
    private void deleteFile(final String addPath, final String fileName) {
        String filePath = Paths.get(uploadPath, addPath, fileName).toString();
        deleteFile(filePath);
    }

    /**
     * 파일 삭제 (from Disk)
     * @param filePath 파일 경로
     */
    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean delete(String saveName, String addPath){
        Path file = Paths.get(uploadPath + File.separator + addPath + File.separator + saveName);
        try {
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    /**
     *  실제 디스크에 저장되는 파일명 생성
     *  uuid 32자리의 랜덤문자열 + extension 업로드 한 파일의 확장자를 담아
     *  uuid + "." + extension 파일명을 리턴함.
     * @param originalFilename 원본 파일명
     * @return  디스크에 저장할 파일명
     */
    private String generateSaveFilename(final String originalFilename) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");//랜덤uuid생성 중간에 있는 "-"들은 replaceAll을 이용해 제거
        String extension = StringUtils.getFilenameExtension(originalFilename);//업로드한 파일의 확장자명을 가져옴
        return uuid + "." + extension;
    }

    /**
     * 업로드 경로 반환
     * @return 업로드 경로
     */
    private String getUploadPath() {
        return makeDirectories(uploadPath);
    }

    /**
     * 업로드 경로 반환
     * @param addPath - 추가 경로
     * @return 업로드 경로
     */
    private String getUploadPath(final String addPath) {
        return makeDirectories(uploadPath + File.separator + addPath);
    }


    /**
     * 업로드 폴더(디렉토리) 생성
     * @param path - 업로드경로
     * @return 업로드 경로
     */
    private String makeDirectories(final String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getPath();
    }

    /**
     * 파일정보(이름, 가상url) 가져오기
     * @param fileResponse DB에 저장된 파일정보 리스트
     * @return 가상의 파일정보가 담긴 객체
     */
    public List<FileInfo> getFileInfo(List<FileResponse> fileResponse) {
        // 파일정보 리스트 생성
        List<FileInfo> result = new ArrayList<>();

        for (FileResponse file : fileResponse) {
            //파일정보를 getImage 컨트롤러로 보내 생성
            FileInfo fileInfo = FileInfo.builder()
                    // 파일원본이름
                    .name(file.getOriginalName())
                    .id(file.getId())
                    .originalName(file.getOriginalName())
                    // 실제 저장된 이름과 디렉토리 이름을 보내 가상의 url 을 생성
                    .url(MvcUriComponentsBuilder
                            .fromMethodName(FileController.class, "getImage"
                                    , file.getSaveName(), file.getCreatedDate().format(DateTimeFormatter.ofPattern("yyMMdd"))).build().toString())
                    .build();
            result.add(fileInfo);
        }
        return result;
    }



}
