package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board/write")
    public String boardWriteForm() {
        return "board_write";
    }

    @PostMapping("/board/write-do")
    public String boardWritePro(Model model, Board board, MultipartFile file) throws Exception {
        boardService.write(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault Pageable pageable, String searchKeyword) {
        Page<Board> list;

        if (searchKeyword == null) list = boardService.boardList(pageable);
        else list = boardService.boardSearchList(searchKeyword, pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;
        int endPage = list.getTotalPages();

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", 1);
        model.addAttribute("endPage", endPage);

        return "board_list";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, Long id) {
        model.addAttribute("board", boardService.boardView(id));

        return "board_view";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Long id) {
        boardService.boardDelete(id);

        return "redirect:/board/list";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.boardView(id));

        return "board_modify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable Long id, Board board, MultipartFile file) throws Exception {
        board.setId(id);

        boardService.write(board, file);

        return "redirect:/board/list";
    }
}
