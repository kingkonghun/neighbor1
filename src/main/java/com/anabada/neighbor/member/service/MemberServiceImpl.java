package com.anabada.neighbor.member.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.repository.MemberRepository;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.reply.service.ReplyService;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.repository.UsedRepository;
import com.anabada.neighbor.used.service.ImgDownService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final UsedRepository usedRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ImgDownService imgDownService;

    @Override
    public void save(Member member) {
        member.setMemberPWD(bCryptPasswordEncoder.encode(member.getMemberPWD()));
        member.setProviderId(member.getMemberEmail());
        member.setRole("ROLE_USER");
        memberRepository.save(member);
    }

    @Override
    public List<Used> myWrite(PrincipalDetails principalDetails, Criteria criteria) {
        List<Used> used = new ArrayList<>();
        long memberId = principalDetails.getMember().getMemberId(); // 시큐리티에서 memberId가져오기
        Map<String, Object> map = new HashMap<>();
        map.put("memberId",memberId);
        map.put("criteria",criteria);
        String memberName = principalDetails.getMember().getMemberName();
        List<Post> postList = memberRepository.findMyPost(map);//내가 작성한 post가져오기
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
                    .build();
            used.add(used1);
        }

        return used;
    }

    @Override
    public int getTotal(PrincipalDetails principalDetails) {//페이징
        long memberId=principalDetails.getMember().getMemberId();
        return memberRepository.getTotal(memberId);
    }

    @Override
    public Member myInfo(PrincipalDetails principalDetails) {//내정보
        long memberId = principalDetails.getMember().getMemberId();
        return memberRepository.findMyInfo(memberId);
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
}
