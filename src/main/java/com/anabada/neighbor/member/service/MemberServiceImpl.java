package com.anabada.neighbor.member.service;

import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.member.domain.Profile;
import com.anabada.neighbor.member.repository.MemberRepository;
import com.anabada.neighbor.reply.domain.CarryReply;
import com.anabada.neighbor.reply.domain.Reply;
import com.anabada.neighbor.reply.service.ReplyService;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.domain.Product;
import com.anabada.neighbor.used.domain.Used;
import com.anabada.neighbor.used.repository.UsedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final UsedRepository usedRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ReplyService replyService;

    @Override
    public void save(Member member) {
        member.setMemberPWD(bCryptPasswordEncoder.encode(member.getMemberPWD()));
        member.setProviderId(member.getMemberEmail());
        member.setRole("ROLE_USER");
        memberRepository.save(member);
    }

    @Override
    public List<Used> myWrite(PrincipalDetails principalDetails) {
        List<Used> used = new ArrayList<>();
        long memberId = principalDetails.getMember().getMemberId(); // 시큐리티에서 memberId가져오기
        String memberName = principalDetails.getMember().getMemberName();
        List<Post> postList = memberRepository.findMyPost(memberId);//내가 작성한 post가져오기
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
}
