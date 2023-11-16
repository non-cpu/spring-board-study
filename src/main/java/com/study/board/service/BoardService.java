package com.study.board.service;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void write(Board board, MultipartFile file) throws Exception {
        if (file != null) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String path = System.getProperty("user.dir") + "/src/main/resources/static/files";

            File saveFile = new File(path, fileName);

            file.transferTo(saveFile);

            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);
        }

        boardRepository.save(board);
    }

    public Page<Board> boardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Page<Board> boardSearchList(String keyword, Pageable pageable) {
        return boardRepository.findByTitleContaining(keyword, pageable);
    }

    public Board boardView(Long id) {
        return boardRepository.findById(id).orElseThrow();
    }

    public void boardDelete(Long id) {
        boardRepository.deleteById(id);
    }
}
