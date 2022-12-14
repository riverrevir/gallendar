package com.gallendar.gradle.server.board.dto;

import com.gallendar.gradle.server.board.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardCreateRequest {

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;
    private String music;
    private String url;
    private MultipartFile photo;
    private String categoryTitle;
    private List<String> tags;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate created;
    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .music(music)
                .url(url)
                .created(created)
                .build();
    }
}