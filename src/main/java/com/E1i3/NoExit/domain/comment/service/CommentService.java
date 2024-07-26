package com.E1i3.NoExit.domain.comment.service;

import com.E1i3.NoExit.domain.comment.domain.Comment;
import com.E1i3.NoExit.domain.comment.dto.CommentCreateReqDto;
import com.E1i3.NoExit.domain.comment.dto.CommentListResDto;
import com.E1i3.NoExit.domain.comment.dto.CommentUpdateReqDto;
import com.E1i3.NoExit.domain.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


    @Transactional
    public void commentCreate(CommentCreateReqDto dto) { // 댓굴 생성
        Comment comment = dto.toEntity();
        commentRepository.save(comment);
    }






    public Page<CommentListResDto> commentList(Pageable pageable){ // 댓글 조회
        Page<Comment> comments = commentRepository.findByDelYN(pageable, "N");
        Page<CommentListResDto> commentListResDtos = comments.map(
                a->a.fromEntity());

        return commentListResDtos;
    }




    @Transactional
    public Comment commentUpdate(Long id, CommentUpdateReqDto dto) { // 댓글 수정
        Comment comment = commentRepository.findById(id).orElseThrow(()->new EntityNotFoundException(" 찾을 수 없습니다."));
        comment.updateEntity(dto);
        Comment updatedComment = commentRepository.save(comment);
        return updatedComment;
    }



    public void commentDelete(Long id) { // 댓글 삭제
        Comment comment = commentRepository.findById(id).orElseThrow(()->new EntityNotFoundException("찾을 수 없습니다."));
        comment.deleteEntity();
//        commentRepository.delete(comment);
    }
}

