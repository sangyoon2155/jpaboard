package com.example.jpaboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.jpaboard.entity.Board;
import com.example.jpaboard.repository.BoardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class BoardController {
	@Autowired
	private BoardRepository boardRepository;
	
	@GetMapping("/board/boardList")
	public String boardList(Model model) {
		
		List<Board> boardList = boardRepository.findAll();
		model.addAttribute("boardList", boardList);
		return "/board/boardList";
	}
}
