package com.anabada.neighbor.member.service;

import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.repository.ClubRepository;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.service.FileService;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.reply.repository.ReplyRepository;
import com.anabada.neighbor.used.domain.Likes;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.repository.UsedRepository;
import com.anabada.neighbor.used.service.ImgDownService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final UsedRepository usedRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final FileService fileService;
    private final ImgDownService imgDownService;
    private final ReplyRepository replyRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClubRepository clubRepository;

    final String uploadPath = Paths.get(System.getProperty("user.home"), "upload_anabada","profile").toString();


    /**
     * 회원 가입
     */
    @Override
    public void save(Member member) {
        member.setMemberPWD(bCryptPasswordEncoder.encode(member.getMemberPWD()));
        member.setProviderId(member.getMemberEmail());
        member.setRole("ROLE_USER");
        memberRepository.save(member);
    }

    /**
     *내가 작성한 게시글 리스트 (Criteria 로 페이징)
     */
    @Override
    public List<Used> myUsedWrite(PrincipalDetails principalDetails, Criteria criteria) {
        List<Used> used = new ArrayList<>();
        long memberId = principalDetails.getMember().getMemberId(); // 시큐리티에서 memberId가져오기
        Map<String, Object> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("criteria",criteria);
        String memberName = principalDetails.getMember().getMemberName();
        List<Post> postList = memberRepository.findMyUsedWrite(map);//내가 작성한 post가져오기
        for (Post post : postList) {
                Product product = usedRepository.findProduct(post.getPostId());//postId로 product 가져오기
                String categoryName = usedRepository.findCategoryName(product.getCategoryId());//product로 가져온 카테고리id로 category이름가져오기
                Used used1 = Used.builder()
                        .postId(post.getPostId())
                        .memberName(memberName)
                        .title(post.getTitle())
                        .content(post.getContent())
                        .postType(post.getPostType())
                        .postDate(post.getPostDate())
                        .postUpdate(post.getPostUpdate())
                        .postView(post.getPostView())//작성한 글
                        .categoryName(categoryName)//카테고리
                        .fileResponseList(fileService.findAllFileByPostId(post.getPostId()))
                        .build();
                used.add(used1);
        }
        return used;
    }

    @Override
    public List<ClubResponse> myClubWrite(PrincipalDetails principalDetails, Criteria criteria) {
        List<ClubResponse> clubResponse = new ArrayList<>();
        long memberId = principalDetails.getMember().getMemberId(); // 시큐리티에서 memberId가져오기
        String memberName = principalDetails.getMember().getMemberName();
        Map<String, Object> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("criteria",criteria);

        List<Post> postList = memberRepository.findMyClubWrite(map);//내가 작성한 post가져오기
        for (Post post : postList) {
            Club club = clubRepository.selectClub(post.getPostId());
            String hobbyName = clubRepository.selectHobbyName(club.getHobbyId());//product로 가져온 카테고리id로 category이름가져오기
            ClubResponse clubResponse1 = ClubResponse.builder()
                    .postId(post.getPostId())
                    .memberName(memberName)
                    .title(post.getTitle())
                    .content(post.getContent())
                    .postType(post.getPostType())
                    .postDate(post.getPostDate())
                    .postUpdate(post.getPostUpdate())
                    .hobbyName(hobbyName)
                    .fileResponseList(fileService.findAllFileByPostId(post.getPostId()))
                    .postView(post.getPostView())//작성한 글
                    .build();
            clubResponse.add(clubResponse1);
        }
        return clubResponse;
    }



    /**
     *페이징을 위한 내가 쓴 글 총 갯수 가져오기
     */
    @Override
    public int getUsedTotal(long memberId) {//페이징
        return memberRepository.countMyUsedWrite(memberId);
    }

    @Override
    public int getClubTotal(long memberId) {
        return memberRepository.countMyClubWrite(memberId);
    }



    @Override
    public boolean emailCk(String memberEmail) {
        boolean check=false;
        int emailCheck = memberRepository.emailCheck(memberEmail);
        if (emailCheck != 1) {
            check = true;
        }
        return check;
    }

    /**
     * member 테이블에서 정보 가져오기
     */
    @Override
    public Member myInfo(long memberId) {//내정보
        Member member = null;
        member = memberRepository.findMyInfo(memberId);
        member.setMyWrite(memberRepository.countMyAllWrite(memberId));
        member.setMyReply(replyRepository.countMyReply(memberId));


        member.setMyLikesCount(memberRepository.countMyLikes(memberId));

        return  member;
    }

    @Override
    public String findProfileImg(long memberId) {//프로필사진 이름가져오기
        String profileImg = memberRepository.findProfileImg(memberId);
        return profileImg;
    }

    @Override
    public void downProfileImg(HttpServletResponse response, String profileImg) throws IOException {//프사다운
            imgDownService.downProfileImg(profileImg,response) ;
    }




    /**
     * member테이블
     * 개인정보 수정
     */
    @Override
    public void editInfo(Member member) {
        memberRepository.editInfo(member);
    }

    /**
     *관리자페이지에서 모든 member 가져오기
     */
    @Override
    public List<Member> findAllMember(Criteria criteria) {//관리자 모든 멤버 가져오기
        Map<String, Object> map = new HashMap<>();
        map.put("criteria", criteria);
        List<Member> member = memberRepository.findAllMember(map);
        for (Member member1 : member) {
            long total = getUsedTotal(member1.getMemberId());
            member1.setMyWrite(total);//사용자가 작성한 글의 숫자 가져오기
        }
        return member;
    }

    /**
     * 페이징을 위한 멤버의 총 수 가져오기
     * @return 멤버테이블의 count
     */
    @Override
    public int countMember() {
        return memberRepository.countMember();
    }

    /**
     * 비밀번호수정
     * @param oldPwd 원래 비밀번호
     * @param memberPWD 수정할 비밀번호
     */
    @Override
    public String editPwd(String oldPwd, String memberPWD,long memberId) {

        String msg="";
        String pwdCk=memberRepository.pwdCheck(memberId);//비밀번호 체크
        System.out.println("pwdCk = " + pwdCk);
        if (!passwordEncoder.matches(oldPwd,pwdCk)) {
             msg = "fail";
        }else{
            memberPWD = bCryptPasswordEncoder.encode(memberPWD);
            System.out.println("memberPWD = " + memberPWD);
            memberRepository.editPwd(memberPWD,memberId);
            msg = "pwdSuccess";
        }
        return msg;
    }

    @Override
    public void editPhoto(Member member) {
        System.out.println("member = " + member);
        try {
            File folderPath = new File(uploadPath);

            if (!folderPath.exists()) {
                try {
                    folderPath.mkdir();
                    System.out.println("폴더가 생성되었습니다.");
                } catch (Exception e) {
                    System.out.println("폴더를 생성할 수 없습니다: " + e.getMessage());
                }
            }

            MultipartFile file = member.getProfileImg();
            String uuid = UUID.randomUUID().toString();
            String profileImg = uuid+"_"+file.getOriginalFilename();

            String filePath = uploadPath+File.separator+profileImg;
            file.transferTo(new File(filePath));

            Map<String, Object> map = new HashMap<>();
            map.put("memberId",member.getMemberId());
            map.put("profileImg", profileImg);
            memberRepository.editProfileImg(map);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
