package com.example.jpaboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jpaboard.dto.BoardForm;
import com.example.jpaboard.entity.Article;
import com.example.jpaboard.entity.Board;
import com.example.jpaboard.repository.BoardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {
	@Autowired
	private BoardRepository boardRepository;
	
	@GetMapping("/board/delete")
	public String delete(@RequestParam long boardNo, RedirectAttributes rda) {
		Board board = boardRepository.findByBoardNo(boardNo).orElse(null);
		
		if(board == null) {
			rda.addFlashAttribute("msg", "삭제실패");
			return "redirect:/board/modifyBoard?boardNo="+boardNo;
		}
		
		boardRepository.delete(board);
		rda.addFlashAttribute("msg","삭제성공");
		return "redirect:/board/boardList";
	}
	
	@PostMapping("/board/addBoard")
	public String update(BoardForm boardForm) {
		Board board = boardForm.toEntity();
		boardRepository.save(board);
		return "redirect:/board/modifyBoard?boardNo="+boardForm.getBoardNo();
	}
	
	@GetMapping("/board/addBoard")
	public String add(Model model, @RequestParam long boardNo) {
		Board board = boardRepository.findByBoardNo(boardNo).orElse(null);
		model.addAttribute("board", board);
		return "board/addBoard";
	}
	
	@GetMapping("/board/modifyBoard")
	public String modify(Model model, @RequestParam long boardNo) {
		Board board = boardRepository.findByBoardNo(boardNo).orElse(null);
		model.addAttribute("board", board);
		return "board/modifyBoard";
	}
	
	@GetMapping("/board/boardList")
	public String boardList(Model model
						, @RequestParam(value = "currentPage", defaultValue = "0") int currentPage
						, @RequestParam(value = "rowPerPage", defaultValue = "10") int rowPerPage
						, @RequestParam(value = "word", defaultValue = "") String word) {
		Sort sort = Sort.by("title").ascending();
		
		
		PageRequest pageable = PageRequest.of(currentPage, rowPerPage, sort); // 0 페이지, 10개
		Page<Board> list = boardRepository.findByBoardTitleContaining(pageable, word);
		
		// Page의 추가 속성
		log.debug("list.getTotalElements(): "+ list.getTotalElements()); // 전체 행의 사이즈
		log.debug("list.getTotalPages(): "+ list.getTotalPages()); // 전체 페이지사이즈 lastPage
		log.debug("list.getNumber(): "+ list.getNumber()); // 현재 페이지
		log.debug("list.getSize(): "+ list.getSize()); // rowPerPage
		log.debug("list.isFirst(): "+ list.isFirst()); // 1페이지인지 : 이전링크유무
		log.debug("list.hasNext(): "+ list.hasNext()); // 다음이 있는지 : 다음링크유무
		
		
		
		model.addAttribute("list", list);
		model.addAttribute("prePage" , list.getNumber()-1);
		model.addAttribute("nextPage" , list.getNumber()+1);
		model.addAttribute("word", word);
		// redirect로 호출되었다면 + RedirectAttributes.attribute() 같이 포함
		return "board/boardList";
	}
	
	@GetMapping("/board/boardOne")
	public String newBoardForm() {
		return "board/boardOne";
	}
	
	@PostMapping("board/create")
	public String createBoard(BoardForm form) {
		System.out.println(form.toString());
		
		Board entity = form.toEntity();
		
		boardRepository.save(entity);
		return "redirect:/board/list";
	}
}
