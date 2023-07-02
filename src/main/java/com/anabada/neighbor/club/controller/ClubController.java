package com.anabada.neighbor.club.controller;

import com.anabada.neighbor.chat.service.ChattingService;
import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.ImageRequest;
import com.anabada.neighbor.club.domain.ImageResponse;
import com.anabada.neighbor.club.domain.*;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.service.ClubService;
import com.anabada.neighbor.club.service.ImageUtils;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.controller.ImageController;
import com.anabada.neighbor.file.domain.ImageInfo;
import com.anabada.neighbor.file.service.FilesStorageService;
import com.anabada.neighbor.used.domain.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ClubController {

    private final ClubService clubService;
    private final ImageUtils imageUtils;
    private final FilesStorageService storageService;
    private final ChattingService chattingService;


    @Autowired
    public ClubController(ClubService clubService, ImageUtils imageUtils, FilesStorageService storageService, ChattingService chattingService) {
        this.clubService = clubService;
        this.imageUtils = imageUtils;
        this.storageService = storageService;
        this.chattingService = chattingService;
    }



    @GetMapping("/clubList")
    public String clubList(Model model) {
        model.addAttribute("clubList", clubService.findClubList());
        model.addAttribute("hobby", clubService.findHobbyName());
        return "club/clubList";
    }


    //게시글 작성 페이지
    @GetMapping("/clubSave")
    public String clubSave(@RequestParam(value = "postId", required = false) Long postId
            , HttpSession session, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (postId != null) {//postId가 있으면 검색해서 정보 가져오기
            ClubResponse clubResponse = clubService.findClub(postId, principalDetails);
            model.addAttribute("club", clubResponse);
        } else {
            model.addAttribute("club", new ClubResponse());
        }
        return "club/clubSave";
    }

    @PostMapping("/clubSave")
    public String clubSave(ClubRequest clubRequest, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Post post = Post.builder()
                .memberId(principalDetails.getMember().getMemberId())
                .title(clubRequest.getTitle())
                .content(clubRequest.getContent())
                .postType("club")
                .build();
        long postId = clubService.savePost(post);
        if (postId == -1) {
            model.addAttribute("result", "글 등록실패!");
            return "club/clubSave";
        }
        Club club = Club.builder()
                .postId(postId)
                .memberId(post.getMemberId())
                .hobbyId(clubService.findHobbyId(clubRequest.getHobbyName()))
                .maxMan(clubRequest.getMaxMan())
                .build();
        if (clubService.saveClub(club) == 1) {
            chattingService.openRoom(postId, principalDetails, "club");
            List<ImageRequest> images = imageUtils.uploadImages(clubRequest.getImages());
            clubService.saveImages(postId, images);
            model.addAttribute("result", "글 등록성공!");//나중에 삭제
        } else {
            model.addAttribute("result", "글 등록실패!");//나중에 삭제
        }

        return "redirect:clubList";
    }

    @GetMapping("/clubDetail")
    public String clubDetail(@RequestParam(value = "postId", required = false) Long postId, Model model,
                             @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubResponse response = clubService.findClub(postId, principalDetails);
        List<ImageResponse> imageResponses = response.getImageResponseList();

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

        model.addAttribute("images", imageInfose);
        model.addAttribute("club", response);
        model.addAttribute("postId", postId);
        return "club/clubDetail";
    }

    @GetMapping("/clubRemove")
    public String clubRemove(Long postId) {
        clubService.deletePost(postId);
        return "redirect:clubList";
    }

    // 파일 리스트 조회
    @GetMapping("/posts/{postId}/images")
    @ResponseBody
    public List<ImageResponse> clubImage(@PathVariable Long postId) {
        return clubService.findAllImageByPostId(postId);
    }

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
                return clubService.findClub(postId,principalDetails ); // 가입성공시 클럽을 새로 조회
            }else{
                club.setClubJoinYn(-1);
                return club; // 가입 실패시 클럽을 새로조회하지않음
            }
        }else{
            if (clubService.deleteClubJoin(club, principalDetails) == 1) {
                clubService.updateNowMan(0, club.getClubId());
                return clubService.findClub(postId, principalDetails);// 탈퇴성공시 클럽을 새로 조회
            }else{
                return club; // 탈퇴 실패시 클럽을 새로 조회하지않음
            }
        }
    }

}
