package com.E1i3.NoExit.domain.comment.service;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.repository.BoardRepository;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.comment.dto.CommentCreateReqDto;
import com.E1i3.NoExit.domain.comment.dto.CommentListResDto;
import com.E1i3.NoExit.domain.comment.dto.CommentUpdateReqDto;
import com.E1i3.NoExit.domain.comment.repository.CommentRepository;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.notification.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;


@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final NotificationService notificationService;
    private static final String COMMENT_PREFIX = "comment:";
    private static final String MEMBER_PREFIX = "member:";


    @Autowired
    public CommentService(CommentRepository commentRepository, MemberRepository memberRepository, BoardRepository boardRepository,
        NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.notificationService = notificationService;
    }

    @Autowired
    @Qualifier("5")
    private RedisTemplate<String, Object> commentRedisTemplate;




    public void commentCreate(CommentCreateReqDto dto) { // 댓굴 생성 보드아이디, 멤버아이디, 내용 받아옴

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("없는 회원입니다."));

        Board board = boardRepository.findById(dto.getBoardId()).orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다.")); // 보드 아이디로 보드 조회

        if(board.getDelYN().equals(DelYN.Y)) {
            throw new IllegalArgumentException("이미 삭제된 게시글입니다.");
        }

        Comment comment = Comment.builder()
                .board(board)
                .member(member)
                .contents(dto.getContents())
                .build();

        board.getComments().add(comment); // 게시글 댓글 목록에 추가

        commentRepository.save(comment);

        System.out.println("7 ok");
        notificationService.notifyComment(board, dto);  // 내가 쓴 게시글에 댓글 알림
    }






    public Page<CommentListResDto> commentList(Long id, Pageable pageable){ // 댓글 조회
        Board board = boardRepository.findById(id).orElseThrow(()->new EntityNotFoundException("게시글을 조회할 수 없습니다."));
        Page<Comment> comments = commentRepository.findByBoardAndDelYN(pageable, board, DelYN.N);
//        Page<CommentListResDto> commentListResDtos = comments.map(
//                a->a.fromEntity());
        Page<CommentListResDto> commentListResDtos = comments.map(Comment::fromEntity);
        return commentListResDtos;
    }







    public Comment commentUpdate(Long id, CommentUpdateReqDto dto) { // 댓글 수정
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("없는 회원입니다."));
        if (!member.getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 댓글만 수정할 수 있습니다.");
        }
        Comment comment = commentRepository.findById(id).orElseThrow(()->new EntityNotFoundException(" 찾을 수 없습니다."));
        comment.updateEntity(dto);
        return comment;
    }



    public void commentDelete(Long id) { // 댓글 삭제
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("없는 회원입니다."));
        if (!member.getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 댓글만 수정할 수 있습니다.");
        }
        Comment comment = commentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("찾을 수 없습니다."));
        Board board = boardRepository.findById(comment.getBoard().getId()).orElse(null);
        comment.deleteEntity();
//        commentRepository.delete(comment);
        boardRepository.save(board);

    }


    @Transactional
    public int commentUpdateLikes(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

        String likesKey = COMMENT_PREFIX + id + ":likes";
        String memberLikesKey = MEMBER_PREFIX + member.getId() + ":likes:" + id;

        Boolean isLiked = commentRedisTemplate.hasKey(memberLikesKey);

        if (isLiked != null && isLiked) {
            commentRedisTemplate.delete(memberLikesKey);
            commentRedisTemplate.opsForSet().remove(likesKey, member.getId());
            comment.updateLikes(false);
        } else {
            commentRedisTemplate.opsForValue().set(memberLikesKey, true);
            commentRedisTemplate.opsForSet().add(likesKey, member.getId());
            comment.updateLikes(true);
        }

        commentRepository.save(comment);

        return comment.getLikes();

    }


    @Transactional
    public boolean commentUpdateDislikes(Long id) {
        boolean value = false;

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 이메일입니다."));

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + id));

        String dislikesKey = COMMENT_PREFIX + id + ":dislikes";
        String memberDislikesKey = MEMBER_PREFIX + member.getId() + ":dislikes:" + id;

        Boolean isDisliked = commentRedisTemplate.hasKey(memberDislikesKey);

        if (isDisliked != null && isDisliked) {
            commentRedisTemplate.delete(memberDislikesKey);
            commentRedisTemplate.opsForSet().remove(dislikesKey, member.getId());
            comment.updateDislikes(false);
        } else {
            commentRedisTemplate.opsForValue().set(memberDislikesKey, true);
            commentRedisTemplate.opsForSet().add(dislikesKey, member.getId());
            comment.updateDislikes(true);
            value = true;
        }

        commentRepository.save(comment);

        return value;
    }

}

