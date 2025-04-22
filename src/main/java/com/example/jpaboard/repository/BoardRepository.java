package com.example.jpaboard.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jpaboard.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Integer> {
	List<Board> findAll();
}
