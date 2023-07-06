package com.anabada.neighbor.club.controller;

import com.anabada.neighbor.chat.domain.Chat;
import com.anabada.neighbor.chat.service.ChattingService;
import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.Message;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.domain.FileInfo;
import com.anabada.neighbor.file.domain.FileRequest;
import com.anabada.neighbor.file.domain.FileResponse;
import com.anabada.neighbor.file.service.FileService;
import com.anabada.neighbor.file.service.FileUtils;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.service.UsedService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClubController {

    private final ClubService clubService;
    private final ChattingService chattingService;
    private final UsedService usedService;
    private final FileUtils fileUtils;
    private final FileService fileService;

    public ClubController(ClubService clubService, ChattingService chattingService, UsedService usedService, FileUtils fileUtils, FileService fileService) {
        this.clubService = clubService;
        this.chattingService = chattingService;
        this.usedService = usedService;
        this.fileUtils = fileUtils;
        this.fileService = fileService;
    }

    @PostMapping("/clubSave")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String clubSave(ClubRequest clubRequest, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 게시글 객체에 담기
        Post post = clubService.clubRequestToPost(clubRequest, principalDetails);
        // 포스트 저장하고 postId clubRequest 에 저장
        long postId = clubService.savePost(post);
        clubRequest.setPostId(postId);
        //게시물 작성 실패시 postId가 -1로 반환되어 작성실패 메세지 리턴
        if (postId == -1) {
            model.addAttribute("result", "글 등록실패!");
            return "redirect:clubList";
        }
        //게시물 작성 성공시 모임 등록
        Club club = clubService.clubRequestToClub(clubRequest, principalDetails);
        //게시물 작성 성공시 채팅방 생성 및 이미지 작성
        if (clubService.saveClub(club) == 1) {
            chattingService.openRoom(postId, principalDetails, "club");
            // Disk 에 이미지 저장
            List<FileRequest> images = fileUtils.uploadFiles(clubRequest.getImages());
            //이미지 업로드 성공시 DB에 이미지정보 저장
            fileService.saveFiles(postId, images);
            model.addAttribute("result", "글 등록성공!");//나중에 삭제
        } else {
            model.addAttribute("result", "글 등록실패!");//나중에 삭제
        }
        return "redirect:clubList";
    }

    @GetMapping("/clubList")
    public String clubList(Model model, @RequestParam(value = "num", defaultValue = "0") int num, @RequestParam(value = "hobbyName", defaultValue = "전체모임") String hobbyName, @RequestParam(value = "search", defaultValue = "") String search) {
        Long hobbyId = clubService.findHobbyId(hobbyName);
        if (hobbyId == null) {
            hobbyId = 0L;
        }
        List<ClubResponse> list = clubService.findClubList(num, hobbyId, search, "list", 0);
        if (list != null) {
            for (ClubResponse clubResponse : list) {
                List<FileInfo> fileInfoList = fileUtils.getFileInfo(clubResponse.getFileResponseList());
                clubResponse.setFileInfo(fileInfoList.get(0));
            }
        }


        model.addAttribute("clubList", list);
        model.addAttribute("hobby", clubService.findHobbyName());
        model.addAttribute("search", search);
        model.addAttribute("hobbyName", hobbyName);
        return num <= 0 ? "club/clubList" : "club/clubListPlus";
    }

    @GetMapping("/clubDetail")
    public String clubDetail(@RequestParam(value = "postId", required = false) Long postId, Model model,
                             HttpServletRequest request, HttpServletResponse response,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubResponse clubResponse = clubService.findClub(postId, principalDetails);
        List<ClubResponse> similarList = clubService.findClubList(0, clubService.findHobbyId(clubResponse.getHobbyName()), "", "similarList", postId);
        List<FileResponse> files = clubResponse.getFileResponseList();
        List<FileInfo> fileInfoList = fileUtils.getFileInfo(files);

        if (similarList != null) {
            for (ClubResponse clubResponse1 : similarList) {
                List<FileInfo> fileInfoList1 = fileUtils.getFileInfo(clubResponse1.getFileResponseList());
                clubResponse1.setFileInfo(fileInfoList1.get(0));
            }
        }

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

        model.addAttribute("images", fileInfoList);
        model.addAttribute("club", clubResponse);
        model.addAttribute("postId", postId);
        model.addAttribute("hobby", clubService.findHobbyName());
        model.addAttribute("similarList", similarList );
        model.addAttribute("roomId", chattingService.findRoomId(postId));
        model.addAttribute("reportType", usedService.reportType());
        return clubResponse.getPostType().equals("del") ? "redirect:club/delPost" : "club/clubDetail";
    }

//    @GetMapping("/club/update")
//    @ResponseBody
//    public ClubResponse update(@RequestParam(value = "postId") Long postId, Model model
//            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
//        //  로그인 안했을때
//        if (principalDetails == null) {
////            return "redirect:clubDetail?postId=" + postId;
//            return null;
//        }
//        ClubResponse clubResponse = clubService.findClub(postId, principalDetails);
//        //  게시글 작성자가 아닐때
//        if (principalDetails.getMember().getMemberId() != clubResponse.getMemberId()) {
////            return "redirect:clubDetail?postId=" + postId;
//            return null;
//        }
//        model.addAttribute("club", clubResponse);
//        System.out.println(clubResponse.getFileResponseList());
//        return clubResponse;
//    }

    @PostMapping("/club/update")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String update(ClubRequest clubRequest, Model model
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        //  로그인 안했을때
        if (principalDetails == null) {
            return "redirect:clubDetail?postId=" + clubRequest.getPostId();
        }
        // postId 가져오기
        long postId = clubRequest.getPostId();
        // 저장되어있는 클럽가져오기
        ClubResponse clubResponse = clubService.findClub(clubRequest.getPostId(), principalDetails);
        //  게시글 작성자가 아닐때 리턴
        if (principalDetails.getMember().getMemberId() != clubResponse.getMemberId()) {
            return "redirect:clubDetail?postId=" + clubRequest.getPostId();
        }
        // 클럽으로 반환 후 업데이트
        Club club = clubService.clubRequestToClub(clubRequest, principalDetails);
        Message message = clubService.updateClub(club, clubResponse);
        // 클럽 업데이트 성공 했을 때
        if (message.getSuccess() == 1) {
            message.setSuccess(0); // 성공 0으로 초기화
            Post post = clubService.clubRequestToPost(clubRequest,postId, principalDetails);
            message = clubService.updatePost(post);
            // 포스트로 변환 후 업데이트
        }
        // 포스트 업데이트 성공했을 때
        if (message.getSuccess() == 1) {
            message.setSuccess(0);
            List<FileResponse> filResponse = fileService.findAllFileByPostId(clubResponse.getPostId());
            // 이미지 Disk 삭제
            fileUtils.deleteFiles(filResponse);
            // 이미지 DB 삭제
            fileService.deleteAllFileByIds(filResponse);
            // 사용자가 업로드한 이미지
            List<MultipartFile> multipartFileList = clubRequest.getImages();
            // 이미지 Disk 저장
            List<FileRequest> fileRequests = fileUtils.uploadFiles(multipartFileList);
            // 이미지 DB 저장
            fileService.saveFiles(clubRequest.getPostId(), fileRequests);


        }
        return "redirect:/clubList";
    }

    @PostMapping("/clubRemove")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String clubRemove(@RequestParam(value = "postId") Long postId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null) {
            return "redirect:clubDetail?postId=" + postId;
        }
        //게시글삭제
        clubService.deletePost(postId);
        //파일 리스트 가져오기
        List<FileResponse> fileResponseList = fileService.findAllFileByPostId(postId);
        // 모임에서 이미지 삭제하면 안되기때문에 주석처리 * 나중에 삭제처리하고 이미지가 없으면 기본이미지로 보이게 하기
        //이미지 Disk 에서 삭제
//        fileUtils.deleteFiles(fileResponseList);
        //이미지 DB 에서 삭제
//        fileService.deleteAllFileByIds(fileResponseList);
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
                        .build(), principalDetails.getMember().getMemberId());

                return clubService.findClub(postId, principalDetails);// 탈퇴성공시 클럽을 새로 조회
            }else{
                return club; // 탈퇴 실패시 클럽을 새로 조회하지않음
            }
        }
    }

    @GetMapping("club/delPost")
    @ResponseBody
    public String delPost() {
        return "<h1>삭제된 게시물입니다.</h1>";
    }
}
