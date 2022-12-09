package com.gallendar.gradle.server.board.service;

import com.gallendar.gradle.server.board.dto.BoardCreateRequestDto;
import com.gallendar.gradle.server.board.dto.BoardUpdateRequestDto;
import com.gallendar.gradle.server.board.entity.Board;
import com.gallendar.gradle.server.board.repository.BoardRepository;
import com.gallendar.gradle.server.board.repository.BoardRepositoryCustomImpl;
import com.gallendar.gradle.server.category.domain.Category;
import com.gallendar.gradle.server.category.domain.CategoryRepository;
import com.gallendar.gradle.server.common.CustomException;
import com.gallendar.gradle.server.global.auth.jwt.JwtUtils;
import com.gallendar.gradle.server.members.domain.Members;
import com.gallendar.gradle.server.members.domain.MembersRepository;
import com.gallendar.gradle.server.photo.entity.Photo;
import com.gallendar.gradle.server.photo.repository.PhotoRepository;
import com.gallendar.gradle.server.photo.service.S3UploadService;
import com.gallendar.gradle.server.tags.domain.*;
import com.gallendar.gradle.server.tags.domain.BoardTags;
import com.gallendar.gradle.server.tags.type.TagStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.gallendar.gradle.server.common.ErrorCode.MEMBER_NOT_FOUND;
import static com.gallendar.gradle.server.common.ErrorCode.POST_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MembersRepository membersRepository;
    private final S3UploadService photoService;
    private final PhotoRepository photoRepository;
    private final BoardTagsRepository boardTagsRepository;
    private final TagsRepository tagsRepository;
    private final CategoryRepository categoryRepository;
    private final BoardRepositoryCustomImpl boardRepositoryCustom;
    private final JwtUtils jwtUtils;

    /* 게시글 저장 */
    @Transactional
    public void save(BoardCreateRequestDto requestDto, String token) throws IOException {
        final String memberId = jwtUtils.getMemberIdFromToken(token);
        Members members = membersRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        final String fileName = UUID.randomUUID() + "-" + requestDto.getPhoto().getOriginalFilename();
        final String path = photoService.upload(requestDto.getPhoto());
        Photo photo = Photo.builder().fileName(fileName).path(path).build();
        photoRepository.save(photo);

        if (!categoryRepository.existsByCategoryTitle(requestDto.getCategoryTitle())) {
            Category category = Category.builder()
                    .categoryTitle(requestDto.getCategoryTitle()).build();
            categoryRepository.save(category);
        }

        Category category = categoryRepository.findByCategoryTitle(requestDto.getCategoryTitle());
        Board board = requestDto.toEntity();
        board.setMembers(members);
        board.setPhoto(photo);
        board.setCategory(category);
        boardRepository.save(board);

        if (!requestDto.getTags().isEmpty()) {
            requestDto.getTags().forEach(m -> {
                BoardTags boardTags = new BoardTags();
                Tags tags = Tags.builder()
                        .tagsMember(m)
                        .tagStatus(TagStatus.alert)
                        .build();
                boardTags.setBoard(board);
                boardTags.setTags(tags);
                boardTagsRepository.save(boardTags);
                tagsRepository.save(tags);
            });
        }
    }

    /* 게시글 수정 */
    @Transactional
    public void update(Long boardId, BoardUpdateRequestDto requestDto, String token) throws IOException {
        final String memberId = jwtUtils.getMemberIdFromToken(token);
        Members members = membersRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        List<Board> oldTagsMember = boardRepositoryCustom.findByTagMembers(boardId);
        List<String> oldTagsList = new ArrayList<>();
        List<String> newTagsMember = requestDto.getTags();

        oldTagsMember.forEach(board1 -> {
            board1.getBoardTags().forEach(boardTags -> {
                oldTagsList.add(boardTags.getTags().getTagsMember());
            });
        });

        List<String> oldNoneMatchList = oldTagsList.stream().filter(o -> newTagsMember.stream().noneMatch(Predicate.isEqual(o))).collect(Collectors.toList());
        List<String> newNoneMatchList = newTagsMember.stream().filter(n -> oldTagsList.stream().noneMatch(Predicate.isEqual(n))).collect(Collectors.toList());
        oldNoneMatchList.forEach(o -> {
            Tags tags = tagsRepository.findByTagsMember(o);
            if (tags.getStatus().equals(TagStatus.alert)) {
                tags.changeStatus(TagStatus.deleteTag);
            }
        });
        newNoneMatchList.forEach(n -> {
            BoardTags boardTags = new BoardTags();
            Tags tags = Tags.builder()
                    .tagsMember(n)
                    .tagStatus(TagStatus.alert)
                    .build();
            boardTags.setBoard(board);
            boardTags.setTags(tags);

            boardTagsRepository.save(boardTags);
            tagsRepository.save(tags);
        });

        if (!categoryRepository.existsByCategoryTitle(requestDto.getCategoryTitle())) {
            Category category = Category.builder()
                    .categoryTitle(requestDto.getCategoryTitle()).build();
            categoryRepository.save(category);
        }

        Category category = categoryRepository.findByCategoryTitle(requestDto.getCategoryTitle());
        board.setCategory(category);

        if (!requestDto.getPhoto().isEmpty()) {
            final String fileName = UUID.randomUUID() + "-" + requestDto.getPhoto().getOriginalFilename();
            final String path = photoService.upload(requestDto.getPhoto());
            Photo photo = Photo.builder().fileName(fileName).path(path).build();
            board.setPhoto(photo);
            photoRepository.save(photo);
        }
        board.update(requestDto.getTitle(), requestDto.getContent(), requestDto.getMusic(), requestDto.getUrl(), requestDto.getCreated());
    }

    /* 게시글 삭제 */
    @Transactional
    public void delete(Long boardId, String token) {
        final String memberId = jwtUtils.getMemberIdFromToken(token);
        Members members = membersRepository.findById(memberId).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        board.getBoardTags().forEach(boardTag -> {
            if (boardTag.getTags().getStatus() == TagStatus.alert) {
                boardTag.getTags().changeStatus(TagStatus.delete);
            }
        });

        final int count = boardRepositoryCustom.findByCategoryCount(board.getCategory().getCategoryId());
        if (count == 1) {
            Category category = categoryRepository.findByCategoryTitle(board.getCategory().getCategoryTitle());
            categoryRepository.delete(category);
        }
        boardRepository.delete(board);
    }
}