package com.E1i3.NoExit.domain.board.service;

import com.E1i3.NoExit.domain.board.domain.Board;
import com.E1i3.NoExit.domain.board.dto.BoardCreateReqDto;
import com.E1i3.NoExit.domain.board.dto.BoardDetailResDto;
import com.E1i3.NoExit.domain.board.dto.BoardListResDto;
import com.E1i3.NoExit.domain.board.dto.BoardUpdateReqDto;
import com.E1i3.NoExit.domain.board.repository.BoardRepository;
import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.common.domain.DelYN;
import com.E1i3.NoExit.domain.common.service.RedisService;
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
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;
    private final S3Client s3Client;

    @Autowired
    public BoardService(BoardRepository boardRepository, MemberRepository memberRepository,
		NotificationService notificationService, S3Client s3Client) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
		this.notificationService = notificationService;
        this.s3Client = s3Client;
	}

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder}")
    private String folder;



    public Board boardCreate(BoardCreateReqDto dto) { // 게시글 생성
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()-> new EntityNotFoundException("없는 회원입니다."));

        List<MultipartFile> images = new ArrayList<>();
        images = dto.getBoardImages();
        Board board = null;

        try {
            board = boardRepository.save(dto.toEntity(member));
            for(MultipartFile img : images) {
                byte[] bytes = img.getBytes();
                String fileName = board.getId() + "_" + img.getOriginalFilename();
                Path path = Paths.get("C:/Users/Playdata1/Desktop/temp/", fileName);

                Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);//저 경로에 bytes(이미지파일)을 저장하겠다.
                PutObjectRequest putObjectRequest = PutObjectRequest
                        .builder()
                        .bucket(bucket)
                        .key(fileName)
                        .build();

                PutObjectResponse putObjectResponse
                        = s3Client.putObject(putObjectRequest, RequestBody.fromFile(path));

                String S3Path // a에서 값을 꺼내는 것. filename으로 찾아와라.  그럼 파일이 저장되어있는 s3 경로가 S3Path 변수에 저장된다.
                        = s3Client.utilities().getUrl(a -> a.bucket(bucket).key(fileName)).toExternalForm();
                board.updateImagePath(S3Path);
            }

        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다.");
        }
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

    public int boardUpdateLikes(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("존재하지 않는 이메일입니다."));
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
        board.updateLikes();
//        board.updateLikes(member.getEmail());
        boardRepository.save(board);
        notificationService.notifyLikeBoard(board);
//        return board.getLikeMembers().size();
        return board.getLikes();
    }

    public int boardUpdateDislikes(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("존재하지 않는 이메일입니다."));
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found with id: " + id));
//        board.updateDislikes(member.getEmail());
        board.updateDislikes();
        boardRepository.save(board);
//        return board.getDislikeMembers().size();
        return board.getDislikes();
    }
}
