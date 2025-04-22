package com.example.jpaboard.controller;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpaboard.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer>{
	// member_id 중복검사
	// 로그인하는 추상매서드
	boolean existsByMemberId(String memberId);
	
	Member findByMemberIdAndMemberPw(String memberId, String memberPw);
	
	Member findByMemberId(String memberId);
}
