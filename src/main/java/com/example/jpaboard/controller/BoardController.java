package com.example.jpaboard.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.jpaboard.dto.BoardForm;
import com.example.jpaboard.entity.Board;
import com.example.jpaboard.repository.BoardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {
	@Autowired
	BoardRepository boardRepository;
	
	@GetMapping("/board/boardList")
	public String boardList(Model model
						, @RequestParam(value = "currentPage", defaultValue = "0") int currentPage
						, @RequestParam(value = "rowPerPage", defaultValue = "10") int rowPerPage
						, @RequestParam(value = "word", defaultValue = "") String word) {
		
		// boardNo으로 정렬
		Sort sort = Sort.by("boardNo").descending();
		
		// 페이징
		PageRequest pageable = PageRequest.of(currentPage, rowPerPage, sort); // 현재 페이지, 컬럼 개수, 정렬 옵션
		
		// findByBoardTitleContaining(페이징 옵션, 특정 단어)
		Page<Board> list = boardRepository.findByBoardTitleContaining(pageable, word);

		log.debug("디버깅 : " + list.toString());
		
		// 넘기기
		model.addAttribute("list", list);						// 게시글 리스트
		model.addAttribute("prePage", list.getNumber()-1);		// 이전 페이지
		model.addAttribute("currentPage", list.getNumber());	// 현재 페이지
		model.addAttribute("nextPage", list.getNumber()+1);		// 다음 페이지
		model.addAttribute("word", word);						// 특정 단어
		model.addAttribute("hasNext",list.hasNext());			// 다음 페이지가 있는지
		model.addAttribute("isFirst",list.isFirst());			// 처음 페이지 인지
		
		return "board/boardList";
	}


	
	@GetMapping("/board/boardOne")
	public String boardOne(@RequestParam int boardNo, Model model) {
		Board board = boardRepository.findById(boardNo).orElse(null);
		model.addAttribute("board", board);
		return "/board/boardOne";
	}
	
	@PostMapping("/board/boardList")
	public String boardList() {
		
		return "redirect:/board/boardList";
	}
	
	@GetMapping("/board/addBoard")
	public String addBoard() {
		
		return "/board/addBoard";
	}
	
	@PostMapping("/board/createBoard")
	public String createBoard(BoardForm boardForm) {
		Board board = boardForm.toEntity();
		boardRepository.save(board);
		return "redirect:/board/boardList";
	}
	
	@GetMapping("/board/modifyBoard")
	public String modifyBoard(@RequestParam int boardNo, Model model) {
		Board board = boardRepository.findById(boardNo).orElse(null);
		model.addAttribute("board", board);
		return "/board/modifyBoard";
	}
	
	@PostMapping("/board/updateBoard")
	public String updateBoard(BoardForm boardForm) {
		Board board = boardForm.toEntity();
		boardRepository.save(board);
		return "redirect:/board/boardList";
	}
	
	@GetMapping("/board/deleteBoard")
	public String deleteBoard(@RequestParam int boardNo) {
		Board board = boardRepository.findById(boardNo).orElse(null);
//		boardRepository.delete(board);
		boardRepository.deleteById(boardNo);
		return "redirect:/board/boardList";
	}
	
}
