package com.example.jpaboard.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.jpaboard.dto.MemberForm;
import com.example.jpaboard.dto.MemberList;
import com.example.jpaboard.entity.Member;
import com.example.jpaboard.util.SHA256Util;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MemberController {
	@Autowired
	MemberRepository memberRepository;
	
	// 회원탈퇴
	@GetMapping("/member/removeMember")
	public String removeMember(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/member/login";
		}
		return "member/removeMember";
	}
	
	@PostMapping("/member/removeMember")
	public String removeMember(MemberForm memberForm, RedirectAttributes rda) {
		
		Member member = memberRepository.findByMemberIdAndMemberPw(
		        memberForm.getMemberId(), SHA256Util.encoding(memberForm.getMemberPw())
		    );
	 
		if (member == null) {
	        rda.addFlashAttribute("msg", "현재 비밀번호가 일치하지 않습니다.");
	        return "redirect:/member/removeMember";
	    }
		
		memberRepository.delete(member);

		rda.addFlashAttribute("msg", "회원 탈퇴가 완료되었습니다.");
		
		return "redirect:/member/joinMember";
	}
	
	
	// 비밀번호수정
	@GetMapping("/member/modifyMemberPw")
	public String modifyMemberPw(HttpSession session) {
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/member/login";
		} 
		return "member/modifyMemberPw";
		
	}
	
	@PostMapping("/member/modifyMemberPw")
	public String modifyMemberPw(MemberForm memberForm, RedirectAttributes rda) {
		
		 Member member = memberRepository.findByMemberIdAndMemberPw(
			        memberForm.getMemberId(), SHA256Util.encoding(memberForm.getMemberPw())
			    );
		 
		 if (member == null) {
		        rda.addFlashAttribute("msg", "현재 비밀번호가 일치하지 않습니다.");
		        return "redirect:/member/modifyMemberPw";
		    }
		
		String newPw = SHA256Util.encoding(memberForm.getNewPw());
		
		member.setMemberPw(newPw);
		
		memberRepository.save(member);

		rda.addFlashAttribute("msg", "비밀번호가 성공적으로 변경되었습니다.");
		
		return "redirect:/member/memberList";
	}
	
	
	
	// 회원목록
	@GetMapping("/member/memberList")
	public String memberList(HttpSession session, Model model) {
		// session 인증/인가 검사
		if(session.getAttribute("loginMember") == null) {
			return "redirect:/member/login";
		} 
		
		List<MemberList> members = memberRepository.findAllBy();
	    model.addAttribute("memberList", members);
		// 사용자 목록 + 페이징 + id 검색
		// Page<Member> = memberRepository.findByMemberIdContaining(Pageable pageable, String word);
		
		return "member/memberList";
		
	}
	
	// 로그인
	@GetMapping("/member/login")
	public String login() {
		return "member/login";
	}
	// 로그인 액션
	@PostMapping("/member/login")
	public String login(HttpSession session, MemberForm memberForm, RedirectAttributes rda) {
		// pw 암호화
		memberForm.setMemberPw(SHA256Util.encoding(memberForm.getMemberPw()));
		// 로그인 확인 매서드
		Member loginMember = memberRepository.findByMemberIdAndMemberPw(memberForm.getMemberId(), memberForm.getMemberPw());
		if(loginMember == null) {
			log.debug("로그인 실패");
			rda.addFlashAttribute("msg", "로그인 실패");
			return "redirect:/member/login";
		}
		
		// 로그인 성공코드 구현
		log.debug("로그인 성공");
		session.setAttribute("loginMember", loginMember); // ISSUE : pw정보까지 세션에 저장
		return "redirect:/member/memberList";
	}
	
	
	@GetMapping("/member/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/member/login";
	}
	
	// 회원가입 + member_id 중복확인
	@GetMapping("/member/joinMember")
	public String joinMember() {
		return "member/joinMember";
	}
	
	@PostMapping("/member/joinMember")
	public String joinMember(MemberForm memberForm, RedirectAttributes rda) {
		// memberForm.getMemberID() DB에 존재하는지?
		
		log.debug(memberForm.toString());
		log.debug("ismemberId : "+memberRepository.existsByMemberId(memberForm.getMemberId()));
		if(memberRepository.existsByMemberId(memberForm.getMemberId())) {
			rda.addFlashAttribute("msg", memberForm.getMemberId()+ "ID가 이미 존재합니다.");
			return "redirect:/member/joinMember";
		}
		// false이면 회원가입 진행
		// memberForm.getMemberPw()값을 SHA-256방식으로 암호화
		memberForm.setMemberPw(SHA256Util.encoding(memberForm.getMemberPw()));
		
		Member member = memberForm.toEntity();
		memberRepository.save(member); // entity저장 -> 최종커밋시 -> 테이블에 행이 추가(insert)
		
		return "redirect:/member/login";
	}
	
	
	
	
	
	// 회원정보수정
	
	// 회원목록
	
	// 회원탈퇴
}
