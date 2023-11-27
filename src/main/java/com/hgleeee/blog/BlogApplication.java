package com.hgleeee.blog;

import com.hgleeee.blog.domain.*;
import com.hgleeee.blog.repository.CategoryRepository;
import com.hgleeee.blog.repository.CommentRepository;
import com.hgleeee.blog.repository.PostRepository;
import com.hgleeee.blog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class BlogApplication {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CommentRepository commentRepository;

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    @PostConstruct
    void postConstruct() {
        User user = userRepository.save(User.builder()
                .name("lee")
                .email("test@test.com")
                .role(Role.ADMIN)
                .build());

        Category category = categoryRepository.save(Category.builder()
                .order(0)
                .code("100")
                .level(1)
                .name("임의")
                .build());

        postRepository.saveAll(
                IntStream.range(0, 200)
                        .mapToObj((i) -> Post.builder()
                                .user(user)
                                .title("hello " + i + "번째 제목입니다.")
                                .content("<p>" + i + " 내용이라구요</p>")
                                .category(category)
                                .build())
                        .collect(Collectors.toList()));

        commentRepository.saveAll(
                IntStream.range(0, 200)
                        .mapToObj((i) -> Comment.builder()
                                .user(user)
                                .content(i + " 댓글이라구요")
                                .post(postRepository.findById(198L).get())
                                .build())
                        .collect(Collectors.toList()));
    }

}
