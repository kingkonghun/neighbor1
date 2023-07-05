package com.anabada.neighbor.club.service;

import com.anabada.neighbor.club.domain.ClubRequest;
import com.anabada.neighbor.club.domain.ClubResponse;
import com.anabada.neighbor.club.domain.Message;
import com.anabada.neighbor.club.domain.entity.Club;
import com.anabada.neighbor.club.domain.entity.Hobby;
import com.anabada.neighbor.club.repository.ClubRepository;
import com.anabada.neighbor.config.auth.PrincipalDetails;
import com.anabada.neighbor.file.domain.FileResponse;
import com.anabada.neighbor.file.service.FileService;
import com.anabada.neighbor.file.service.FileUtils;
import com.anabada.neighbor.member.domain.Member;
import com.anabada.neighbor.page.Criteria;
import com.anabada.neighbor.used.domain.Likes;
import com.anabada.neighbor.used.domain.Post;
import com.anabada.neighbor.used.repository.UsedRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ClubServiceImpl implements ClubService {
    private final ClubRepository clubRepository;
    private final FileService fileService;
    private final UsedRepository usedRepository;
    private final FileUtils fileUtils;

    public ClubServiceImpl(ClubRepository clubRepository, FileService fileService, UsedRepository usedRepository, FileUtils fileUtils) {
        this.clubRepository = clubRepository;
        this.fileService = fileService;
        this.usedRepository = usedRepository;
        this.fileUtils = fileUtils;
    }

    @Transactional
    @Override/*클럽세이브*/
    public int saveClub(Club club) {//post,club 등록
        if (clubRepository.insertClub(club) == 1) {//db등록 실패시 0으로반환
            return 1;
        }
        return 0;
    }
    @Transactional
    @Override
    public long savePost(Post post) {
        if (clubRepository.insertPost(post) == 1){
            return post.getPostId();
        }//게시글이 성공적으로 등록되었으면 postId 반환 실패하였으면 -1반환
        return -1;
    }

    @Override
    public ClubResponse findClub(long postId, PrincipalDetails principalDetails) {
        Post post = clubRepository.selectPost(postId);
        Member postMember = clubRepository.selectMember(post.getMemberId());//글작성자의 정보
        int replyCount = usedRepository.findReplyCount(post.getPostId()); // postId 로 댓글 갯수 가져오기
        int likesCount = usedRepository.findLikesCount(post.getPostId()); // postId 로 좋아요 갯수 가져오기
        int likesCheck = 0; // 초기화

        Member member;
        if (principalDetails != null) { // 현재 로그인한 상태라면
            likesCheck = usedRepository.likesCheck(Likes.builder() // 좋아요를 누른 게시물인지 확인
                    .postId(postId)
                    .memberId(principalDetails.getMember().getMemberId())
                    .build());
            member = principalDetails.getMember(); // 글을 보러온 사용자의 정보
        }else {
            member = Member.builder().memberId(-2).build();
        }

        String[] splitString = postMember.getAddress().split(" ");
        String address = splitString[0] + " " + splitString[1];

        Club club = clubRepository.selectClub(postId);
//        System.out.println("클럽아이디" + club.getClubId()+ "멤버아이디 : " + member.getMemberId());
        return ClubResponse.builder()
                .clubId(club.getClubId())
                .clubJoinYn(clubRepository.selectClubJoinIdByMemberId(club.getClubId(), member.getMemberId()) == null ? 0 : 1) // 클럽에 가입되어있으면 1 아니면 0
                .postId(post.getPostId())
                .postUpdate(post.getPostUpdate())
                .memberId(postMember.getMemberId())
                .memberName(postMember.getMemberName())
                .title(post.getTitle())
                .content(post.getContent())
                .hobbyName(clubRepository.selectHobbyName(club.getHobbyId()))
                .score(postMember.getScore())
                .postView(post.getPostView())
                .replyCount(replyCount)
                .likesCount(likesCount)
                .likesCheck(likesCheck)
                .fileResponseList(fileService.findAllFileByPostId(postId))//여기까지완성
                .maxMan(club.getMaxMan())
                .nowMan(club.getNowMan())
                .address(address)
                .postType(post.getPostType())
                .build();
    }


    @Override
    public Post clubRequestToPost(ClubRequest clubRequest
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Post post = Post.builder()
                .memberId(principalDetails.getMember().getMemberId())
                .title(clubRequest.getTitle())
                .content(clubRequest.getContent())
                .postType("club")
                .build();
        return post;
    }

    @Override
    public Post clubRequestToPost(ClubRequest clubRequest, Long postId, PrincipalDetails principalDetails) {
        return Post.builder()
                .postId(postId)
                .memberId(principalDetails.getMember().getMemberId())
                .title(clubRequest.getTitle())
                .content(clubRequest.getContent())
                .postType("club")
                .build();
    }

    @Override
    public Club clubRequestToClub(ClubRequest clubRequest
            , @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Club club = Club.builder()
                .postId(clubRequest.getPostId())
                .memberId(principalDetails.getMember().getMemberId())
                .hobbyId(findHobbyId(clubRequest.getHobbyName()))
                .maxMan(clubRequest.getMaxMan())
                .build();
        return club;
    }

    @Override
    public Message updatePost(Post post) {
        Message message = new Message();
        if (clubRepository.updatePost(post) == 1){
            message.setMessage("게시물 업데이트 성공");
            message.setSuccess(1);
        }

        return message;
    }

    @Override
    public Message updateClub(Club clubRequest, ClubResponse clubResponse) {
        Message message = new Message();
        if (clubRequest.getMaxMan() < clubResponse.getNowMan()) {
            message.setMessage("현재 가입한 인원보다 최대 인원수를 내릴 수 없습니다.");
            message.setSuccess(0);
            return message;
        }
        if(clubRepository.updateClub(clubRequest) == 1){
            message.setMessage("모임 업데이트 성공.");
            message.setSuccess(1);
        }
        return message;
    }

    @Override
    public long deletePost(long postId) {
        clubRepository.deletePost(postId);
        return postId;
    }

    @Override
    public Long findHobbyId(String hobbyName) {
        return clubRepository.selectHobbyId(hobbyName);
    }

    @Override
    public List<ClubResponse> findClubList(int num, long hobbyId, String search, String listType, long postId) {
        List<ClubResponse> result = new ArrayList<>(); //반환해줄 리스트생성
        List<Post> postList = clubRepository.selectPostList(); //foreach돌릴 postlist생성j

        List<Club> clubList = null;
        if (hobbyId != 0) { // hobbyId 가 0이 아니면 즉 카테고리를 선택했다면
            clubList = clubRepository.selectHobbyClubList(hobbyId); // 해당 취미 리스트 가져오기
        }else { // hobbyId 가 0 즉 전체모임이라면
            clubList = clubRepository.selectClubList(); // 전체 리스트 가져오기
        }

        for (Club club : clubList) {
            Post post = clubRepository.selectPost(club.getPostId()); // postId 로 해당하는 post 튜플 가져오기

            // 비슷한 모임 리스트를 가져올 때 현재 보고있는 게시물은 빼고 가져오기 위함
            if (post.getPostId() == postId || !post.getPostType().equals("club")) { // 가져온 post 의 postId 가 파라미터로 받은 postId 와 같거나 postType 이 club이 아니라면
                continue;
            }

            Member member = clubRepository.selectMember(club.getMemberId());
            int replyCount = usedRepository.findReplyCount(post.getPostId()); // 게시물의 댓글 갯수
            int likesCount = usedRepository.findLikesCount(post.getPostId()); // 게시물의 좋아요 갯수

            String[] splitString = member.getAddress().split(" ");
            String address = splitString[0] + " " + splitString[1]; // 주소를 원하는 만큼 자르기

            ClubResponse temp = ClubResponse.builder()
                    .postId(post.getPostId())
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .hobbyName(clubRepository.selectHobbyName(club.getHobbyId()))
                    .score(member.getScore())
                    .maxMan(club.getMaxMan())
                    .nowMan(club.getNowMan())
                    .memberIdList(clubRepository.findMemberIdInClub(club.getClubId()))
                    .fileResponseList(fileService.findAllFileByPostId(post.getPostId()))//여기까지완성
                    .address(address)
                    .replyCount(replyCount)
                    .likesCount(likesCount)
                    .postView(post.getPostView())
                    .postUpdate(post.getPostUpdate())
                    .build();
            if (temp.getTitle().indexOf(search) != -1 || temp.getContent().indexOf(search) != -1) { // 검색 조건
                result.add(temp);
            }
//            result.add(temp);
        }
        Comparator<ClubResponse> comparator = (use1, use2) -> Long.valueOf( // 정렬
                        use1.getPostUpdate().getTime())
                .compareTo(use2.getPostUpdate().getTime());
        Collections.sort(result, comparator.reversed());

        if (listType.equals("similarList")) {  // listType 이 similarList 라면(비슷한 모임 보기)
            return result.subList(0, Math.min(result.size(), 4)); // 같은 hobbyId 를 가지고 있는 게시물 목록을 4개만 자르기
        }

        if(num >= result.size()){ // 게시물 더보기 처리
            return null;
        }
        return result.subList(num,Math.min(result.size(),num+6)); // 6개씩 끊어서 목록으로 가져가기
//        return result;
    }

    @Override
    public int checkPost(ClubRequest clubRequest) {//clubPost 정상값인지 체크 나중에memberId추가해야함
        if (clubRequest.getTitle() == null ||
                clubRequest.getContent() == null ||
                clubRequest.getHobbyName() == null ||
                clubRequest.getMaxMan() == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public Long findClubJoinByMemberId(ClubResponse club, Long memberId) {
        return clubRepository.selectClubJoinIdByMemberId(club.getClubId(), memberId);
    }

    /**
     * 모임 가입하기
     * @param club 모임정보
     * @param principalDetails 사용자정보
     * @return 가득찼을때 -1 성공시 1 실패시 0
     */
    @Override
    public int joinClubJoin(ClubResponse club, PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getMemberId();
        if (club.getMaxMan() == club.getNowMan()) {
            return -1; // 모임 최대인원이 가득찼을때 -1 리턴
        }
        return clubRepository.insertClubJoin(club.getClubId(), memberId, club.getPostId());
    }

    @Override
    public int deleteClubJoin(ClubResponse club, PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getMemberId();
        return clubRepository.deleteClubJoin(club.getClubId(), memberId);
    }

    @Override
    public void updateNowMan(int num, Long clubId) {
        if (num == 1){
            clubRepository.updateNowManPlus(clubId);
        }else{
            clubRepository.updateNowManMinus(clubId);
        }
    }

    @Override
    public List<Hobby> findHobbyName() {
        return clubRepository.findHobbyName();
    }

    @Override
    public void updatePostView(Long postId) {
        usedRepository.updatePostView(postId);
    }

    @Override
    public List<ClubResponse> mainList() { // 메인화면 리스트(최근 일주일 조회수 기준)
        List<ClubResponse> result = new ArrayList<>();
        List<Post> postList = clubRepository.selectHotPostList();
        for (Post post : postList) {
            Club club = clubRepository.selectClub(post.getPostId());
            Member member = clubRepository.selectMember(post.getMemberId());
            int replyCount = usedRepository.findReplyCount(post.getPostId());
            int likesCount = usedRepository.findLikesCount(post.getPostId());
            String[] splitString = member.getAddress().split(" ");
            String address = splitString[0] + " " + splitString[1];
            ClubResponse temp = ClubResponse.builder()
                    .postId(post.getPostId())
                    .memberIdList(clubRepository.findMemberIdInClub(club.getClubId()))
                    .memberId(member.getMemberId())
                    .memberName(member.getMemberName())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .hobbyName(clubRepository.selectHobbyName(club.getHobbyId()))
                    .fileResponseList(fileService.findAllFileByPostId(post.getPostId()))
                    .score(member.getScore())
                    .maxMan(club.getMaxMan())
                    .nowMan(club.getNowMan())
                    .address(address)
                    .replyCount(replyCount)
                    .likesCount(likesCount)
                    .postView(post.getPostView())
                    .postUpdate(post.getPostUpdate())
                    .build();
            result.add(temp);
        }
        return result;
    }

    @Override
    public List<ClubResponse> likePost(long memberId, Criteria criteria) {
        List<ClubResponse> clubList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("criteria", criteria);
        List<Likes> likesList = usedRepository.findLikePosts(map);//좋아요 누른 게시글 긁기

        for (Likes likes : likesList) {
            long postId = likes.getPostId();//좋아요 누른 게시글 ID
            Post post = clubRepository.selectPost(postId);
            if (post.getPostType().equals("club")) {
                ClubResponse clubResponse = ClubResponse.builder()
                        .postId(postId)
                        .title(post.getTitle())
                        .content(post.getContent())
                        .postView(post.getPostView())
                        .likesCount(usedRepository.findLikesCount(postId))
                        .build();
                clubList.add(clubResponse);
            }
        }
        return clubList;
    }

    @Override
    public int countMyClubLikePost(long memberId) {
        List<Post> postList = clubRepository.findPostId(memberId);
        List<String> postTypeList = new ArrayList<>();
        for (Post post : postList) {
            long postId = post.getPostId();
            String postType = clubRepository.findMyClubLikePostType(postId);//포스트타입
            if (postType.equals("club")) {
                postTypeList.add(postType);
            }
        }
        int total = postTypeList.size();
        return total;
    }
}
