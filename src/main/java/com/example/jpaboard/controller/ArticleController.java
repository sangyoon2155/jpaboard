package com.example.jpaboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.jpaboard.dto.ArticleForm;
import com.example.jpaboard.entity.Article;
import com.example.jpaboard.repository.ArticleRepository;

@Controller
public class ArticleController {
	@Autowired // 의존성주입
	private ArticleRepository articlerepository;
	
	@GetMapping("/articles/new") // doGet()
	public String newArticleForm() {
		return "articles/new"; // forward
	}
	
	@PostMapping("articles/create") // doPost()
	public String createArticle(ArticleForm form) { // @ReqeustParam, DTO(커맨드객체)
		System.out.println(form.toString());
		
		// DTO -> Entity
		
		Article entity = form.toEntity();
		
		
		articlerepository.save(entity); // 레파지토리 호출할때는 Entity가 필요
		return "redirect:/"; // article/list로 리다이렉트 "redirect:/article/list"
	}
}
