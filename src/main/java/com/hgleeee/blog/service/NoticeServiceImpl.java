package com.hgleeee.blog.service;

import com.hgleeee.blog.domain.Comment;
import com.hgleeee.blog.domain.Notice;
import com.hgleeee.blog.domain.User;
import com.hgleeee.blog.dto.response.NoticeResponseDto;
import com.hgleeee.blog.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public void createNotice(Comment comment) {
        User receiver = null;
        if (comment.getParentComment() == null) {
            receiver = comment.getPost().getUser();
        } else {
            receiver = comment.getParentComment().getUser();
        }
        if (equalSenderReceiver(comment, receiver)) {
            return;
        }
        noticeRepository.save(Notice.builder()
                .comment(comment)
                .receiver(receiver)
                .build());
    }

    private boolean equalSenderReceiver(Comment comment, User receiver) {
        return comment.getUser().getEmail().equals(receiver.getEmail());
    }

    @Override
    public List<NoticeResponseDto> getNoticesByReceiverEmail(String email) {
        return noticeRepository.getNoticesByReceiverEmail(email)
                .stream()
                .map(notice -> NoticeResponseDto.builder()
                        .postId(notice.getComment().getPost().getId())
                        .message(convertToMessage(notice))
                        .confirmed(notice.getConfirmed())
                        .build())
                .collect(Collectors.toList());
    }

    private String convertToMessage(Notice notice) {
        StringBuilder sb = new StringBuilder(notice.getComment().getUser().getName());
        sb.append("님이 ").append(notice.getReceiver().getName());
        if (notice.getComment().getParentComment() == null) {
            sb.append("님의 게시글에 댓글을 남겼습니다.");
        } else {
            sb.append("님의 댓글에 답글을 남겼습니다.");
        }
        return sb.toString();
    }
}
