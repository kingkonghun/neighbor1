package com.anabada.neighbor.club.controller;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.service.ChattingService;
import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.file.domain.FileRequest;
import com.anabada.neighbor.file.domain.FileResponse;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.file.service.FileService;
import com.anabada.neighbor.file.service.FileUtils;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.domain.FileInfo;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.service.UsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClubController {

    private final ClubService clubService;
    private final ChattingService chattingService;
    private final UsedService usedService;
    private final FileUtils fileUtils;
    private final FileService fileService;

    public ClubController(ClubService clubService, ImageUtils imageUtils, FilesStorageService storageService, ChattingService chattingService, UsedService usedService) {
    public ClubController(ClubService clubService, ChattingService chattingService, FileUtils fileUtils, FileService fileService) {
        this.clubService = clubService;
        this.chattingService = chattingService;
        this.usedService = usedService;
        this.fileUtils = fileUtils;
        this.fileService = fileService;
    }

    @GetMapping("/clubList")
    public String clubList(Model model, @RequestParam(value = "num", defaultValue = "0") int num, @RequestParam(value = "hobbyName", defaultValue = "전체모임") String hobbyName, @RequestParam(value = "search", defaultValue = "") String search) {
        Long hobbyId = clubService.findHobbyId(hobbyName);
        if (hobbyId == null) {
            hobbyId = 0L;
        }
        model.addAttribute("clubList", clubService.findClubList(num, hobbyId, search, "list", 0));
        model.addAttribute("hobby", clubService.findHobbyName());
        model.addAttribute("search", search);
        model.addAttribute("hobbyName", hobbyName);
        return num <= 0 ? "club/clubList" : "club/clubListPlus";
    }


//    //게시글 작성 페이지
//    @GetMapping("/clubSave")
//    public String clubSave(@RequestParam(value = "postId", required = false) Long postId
//            , HttpSession session, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        if (postId != null) {//postId가 있으면 검색해서 정보 가져오기
//            ClubResponse clubResponse = clubService.findClub(postId, principalDetails);
//            model.addAttribute("club", clubResponse);
//        } else {
//            model.addAttribute("club", new ClubResponse());
//        }
//        return "club/clubSave";
//    }

    @PostMapping("/clubSave")
    public String clubSave(ClubRequest clubRequest, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 게시글 객체에 담기
        Post post = Post.builder()
                .memberId(principalDetails.getMember().getMemberId())
                .title(clubRequest.getTitle())
                .content(clubRequest.getContent())
                .postType("club")
                .build();
        long postId = clubService.savePost(post);
        //게시물 작성 실패시 postId가 -1로 반환되어 작성실패 메세지 리턴
        if (postId == -1) {
            model.addAttribute("result", "글 등록실패!");
            return "club/clubSave";
        }
        //게시물 작성 성공시 모임 등록
        Club club = Club.builder()
                .postId(postId)
                .memberId(post.getMemberId())
                .hobbyId(clubService.findHobbyId(clubRequest.getHobbyName()))
                .maxMan(clubRequest.getMaxMan())
                .build();
        //게시물 작성 성공시 채팅방 생성 및 이미지 작성
        if (clubService.saveClub(club) == 1) {
            chattingService.openRoom(postId, principalDetails, "club");
            List<FileRequest> images = fileUtils.uploadFiles(clubRequest.getImages());
            //이미지 업로드 성공시 DB에 이미지정보 저장
            fileService.saveFiles(postId, images);
            model.addAttribute("result", "글 등록성공!");//나중에 삭제
        } else {
            model.addAttribute("result", "글 등록실패!");//나중에 삭제
        }

        return "redirect:clubList";
    }

    @GetMapping("/clubDetail")
    public String clubDetail(@RequestParam(value = "postId", required = false) Long postId, Model model,
                             HttpServletRequest request, HttpServletResponse response,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubResponse response = clubService.findClub(postId, principalDetails);
        List<FileResponse> files = response.getFileResponseList();
        List<FileInfo> fileInfoList = fileUtils.getFileInfo(files);
        model.addAttribute("images", fileInfoList);
        model.addAttribute("club", response);
        ClubResponse clubResponse = clubService.findClub(postId, principalDetails);
        List<ImageResponse> imageResponses = clubResponse.getImageResponseList();

        List<ImageInfo> imageInfose = new ArrayList<>();
        for (ImageResponse image : imageResponses) {
            ImageInfo imageInfo = ImageInfo.builder()
                    .name(image.getOrigName())
                    .url(MvcUriComponentsBuilder
                            .fromMethodName(ImageController.class, "getImage"
                            , image.getSaveName(), image.getCreaDate().format(DateTimeFormatter.ofPattern("yyMMdd"))).build().toString())
                    .build();
            imageInfose.add(imageInfo);
        }
        System.out.println(imageInfose);

        Cookie[] cookies = request.getCookies(); //쿠키 가져오기

        Cookie viewCookie = null;

        if (cookies != null && cookies.length > 0) { //가져온 쿠키가 있으면
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cookie" + postId)) { //해당하는 게시물의 쿠키가 있으면
                    viewCookie = cookie; //viewCookie에 저장
                }
            }
        }
        if (viewCookie == null) { //쿠키가 없으면
            Cookie newCookie = new Cookie("cookie" + postId, String.valueOf(postId)); //해당하는 게시물의 새로운 쿠키 생성
            response.addCookie(newCookie); //쿠키 등록
            clubService.updatePostView(postId); //postId로 post 테이블에서 해당하는 튜플의 조회수 증가
        }

        model.addAttribute("images", imageInfose);
        model.addAttribute("club", clubResponse);
        model.addAttribute("postId", postId);
        model.addAttribute("hobby", clubService.findHobbyName());
        model.addAttribute("similarList", clubService.findClubList(0, clubService.findHobbyId(clubResponse.getHobbyName()), "", "similarList", postId));
        model.addAttribute("roomId", chattingService.findRoomId(postId));
        model.addAttribute("reportType", usedService.reportType());
        return "club/clubDetail";
    }

    @GetMapping("/club/update")
    @ResponseBody
    public ClubResponse update(@RequestParam(value = "postId") Long postId, Model model
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        //  로그인 안했을때
        if (principalDetails == null) {
//            return "redirect:clubDetail?postId=" + postId;
            return null;
        }
        ClubResponse clubResponse = clubService.findClub(postId, principalDetails);
        //  게시글 작성자가 아닐때
        if (principalDetails.getMember().getMemberId() != clubResponse.getMemberId()) {
//            return "redirect:clubDetail?postId=" + postId;
            return null;
        }
        model.addAttribute("club", clubResponse);
        return clubResponse;
    }

//    @PostMapping("/club/update")
//    public String update(ClubRequest clubRequest, Model model
//            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        //  로그인 안했을때
//        if (principalDetails == null) {
//            return "redirect:clubDetail?postId=" + postId;
//        }
//        ClubResponse clubResponse = clubService.findClub(postId, principalDetails);
//        //  게시글 작성자가 아닐때
//        if (principalDetails.getMember().getMemberId() != clubResponse.getMemberId()) {
//            return "redirect:clubDetail?postId=" + postId;
//        }
//        clubService.updatePost()
//    }

    @GetMapping("/clubRemove")
    public String clubRemove(@RequestParam(value = "postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:clubDetail?postId=" + postId;
        }
        //게시글삭제
        clubService.deletePost(postId);
        //파일 리스트 가져오기
        List<FileResponse> fileResponseList = fileService.findAllFileByPostId(postId);
        //이미지 Disk 에서 삭제
        fileUtils.deleteFiles(fileResponseList);
        //이미지 DB 에서 삭제
        fileService.deleteAllFileByIds(fileResponseList);
        return "redirect:clubList";
    }

//    // 파일 리스트 조회
//    @GetMapping("/posts/{postId}/images")
//    @ResponseBody
//    public List<FileResponse> clubImage(@PathVariable Long postId) {
//        return clubService.findAllImageByPostId(postId);
//    }

    /**
     * 모임가입
     * @param postId 게시글아이디
     * @param principalDetails 사용자 정보
     * @return ResponseBody(Ajax)
     */
    @PostMapping("/club/join")
    @ResponseBody
    public ClubResponse join(Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        long memberId = principalDetails.getMember().getMemberId();
        ClubResponse club = clubService.findClub(postId,principalDetails);
        if (club.getMemberId() == memberId) {//클럽글을 작성한 본인은 가입탈퇴 불가
            return null;
        }
        Long clubJoinId = clubService.findClubJoinByMemberId(club, memberId);

        if (clubJoinId == null) {//가입한적 없으면 join 있으면 delete
            if (clubService.joinClubJoin(club, principalDetails) == 1) { //인원이 꽉차 가입실패시 -1 반환
                clubService.updateNowMan(1, club.getClubId());

                chattingService.chatJoin(postId, principalDetails);

                return clubService.findClub(postId,principalDetails); // 가입성공시 클럽을 새로 조회
            }else{
                club.setClubJoinYn(-1);
                return club; // 가입 실패시 클럽을 새로조회하지않음
            }
        }else{
            if (clubService.deleteClubJoin(club, principalDetails) == 1) {
                clubService.updateNowMan(0, club.getClubId());

                chattingService.chatOut(Chat.builder()
                        .roomId(chattingService.findRoomId(postId))
                        .type("club")
                        .build(), principalDetails);

                return clubService.findClub(postId, principalDetails);// 탈퇴성공시 클럽을 새로 조회
            }else{
                return club; // 탈퇴 실패시 클럽을 새로 조회하지않음
            }
        }
    }

}
