package com.example.jpaboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("loginName", "구디");
		// System.out.println(model.getAttribute("loginName"));
		log.debug("loginName:" +model.getAttribute("loginName"));	
		return "home";
	}
}
