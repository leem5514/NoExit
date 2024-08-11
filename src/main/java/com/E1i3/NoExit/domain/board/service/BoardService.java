package com.E1i3.NoExit.domain.board.service;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.domain.BoardImage;
import com.E1i3.NoExit.domain.board.dto.BoardCreateReqDto;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.board.repository.BoardRepository;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.common.service.S3Service;
import com.E1i3.NoExit.domain.member.domain.Member;
import com.E1i3.NoExit.domain.member.repository.MemberRepository;
import com.E1i3.NoExit.domain.notification.service.NotificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final S3Service s3Service;
    private static final String BOARD_PREFIX = "board:";
    private static final String MEMBER_PREFIX = "member:";

    @Autowired
    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository,
		NotificationService notificationService, S3Service s3Service) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
		this.notificationService = notificationService;
        this.s3Service = s3Service;
	}

    @Autowired
    @Qualifier("4")
    private RedisTemplate<String, Object> boardRedisTemplate;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloud.aws.s3.folder.folderName5}")
    private String folder;



    public Board boardCreate(BoardCreateReqDto dto, List<MultipartFile> imgFiles) { // 게시글 생성
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("없는 회원입니다."));

        Board board = Board.builder()
                .member(member)
                .title(dto.getTitle())
                .content(dto.getContent())
                .boardType(dto.getBoardType())
                .build();

        for(MultipartFile f : imgFiles) {
            BoardImage img = BoardImage.builder()
                    .board(board)
                    .imageUrl(s3Service.uploadFile(f, "board"))
                    .build();
            board.getImgs().add(img);
        }


        boardRepository.save(board);
        return board;
    }


    public Page<BoardListResDto> boardList(Pageable pageable) { // 게시글 전체 조회
        Page<Board> boards = boardRepository.findByDelYN(pageable,DelYN.N);
//        Page<Board> boards = boardRepository.findAll(pageable);
        Page<BoardListResDto> boardListResDtos = boards.map(Board::fromEntity);
        return boardListResDtos;
    }

    public BoardDetailResDto boardDetail(Long id) { // 특정 게시글 조회
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
        if (board.getDelYN().equals(DelYN.Y)) {
            throw new IllegalArgumentException("cannot find board");
        }
        board.updateBoardHits(); // 조회수 +1
        return board.detailFromEntity();
    }


    public Board boardUpdate(Long id, BoardUpdateReqDto dto) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));

        if (!board.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 게시글만 수정할 수 있습니다.");
        }

        board.updateEntity(dto);
        return boardRepository.save(board);
    }

    public void boardDelete(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
//        boardRepository.delete(board);
        if (!board.getMember().getEmail().equals(email)) {
            throw new IllegalArgumentException("본인의 게시글만 삭제할 수 있습니다.");
        } else if (board.getDelYN().equals(DelYN.Y)) {
            throw new IllegalArgumentException("cannot delete board");
        }
        board.deleteEntity();
        boardRepository.save(board);
    }

    @Transactional
    public int boardUpdateLikes(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("존재하지 않는 이메일입니다."));
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));

        String key = BOARD_PREFIX + id + ":likesOrDislikes";
        String memberKey = MEMBER_PREFIX + member.getId() + ":likesOrDislikes:" + id;


        Boolean isAlreadyLikedOrDisliked = boardRedisTemplate.hasKey(memberKey);
        if(isAlreadyLikedOrDisliked != null && isAlreadyLikedOrDisliked) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
        }

        boardRedisTemplate.opsForValue().set(memberKey,true);
        boardRedisTemplate.opsForSet().add(key, member.getId());
        board.updateLikes();

        return board.getLikes();

    }


    @Transactional
    public int boardUpdateDislikes(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("존재하지 않는 이메일입니다."));
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));

        String key = "board:" + id + ":likesOrDislikes";
        String memberKey = "member:"+ member.getId() + ":likesOrDislikes:" + id;

        Boolean isAlreadyLikedOrDisliked = boardRedisTemplate.hasKey(memberKey);
        if(isAlreadyLikedOrDisliked != null && isAlreadyLikedOrDisliked) {
            throw new IllegalArgumentException("이미 싫어요를 누른 게시글입니다.");
        }

        boardRedisTemplate.opsForValue().set(memberKey,true);
        boardRedisTemplate.opsForSet().add(key, member.getId());
        board.updateDislikes();

        return board.getDislikes();

    }




//    public int boardUpdateLikes1(Long id) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("존재하지 않는 이메일입니다."));
//        Board board = boardRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
//        board.updateLikes();
////        board.updateLikes(member.getEmail());
//        boardRepository.save(board);
//        notificationService.notifyLikeBoard(board);
////        return board.getLikeMembers().size();
//        return board.getLikes();
//    }
//
//    public int boardUpdateDislikes1(Long id) {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("존재하지 않는 이메일입니다."));
//        Board board = boardRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
////        board.updateDislikes(member.getEmail());
//        board.updateDislikes();
//        boardRepository.save(board);
////        return board.getDislikeMembers().size();
//        return board.getDislikes();
//    }
}
