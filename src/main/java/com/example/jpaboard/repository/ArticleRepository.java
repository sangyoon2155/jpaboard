package com.example.jpaboard.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.jpaboard.dto.ArticleForm;
import com.example.jpaboard.entity.Article;
import com.example.jpaboard.entity.ArticleMapping;

public interface ArticleRepository extends JpaRepository<Article, Long> {

	
	boolean ture = false;

	// CrudRepository : insert, select one, select all, update, delete
	
	// JpaRepository(CrudRepository 자식 인터페이스) : select limit, select order by, ....
	
	// findAll() : 원하는 컬럼만 가지고 오도록 .....
	Page<Article> findByTitleContaining(Pageable pageable, String word);
	
	@Query(nativeQuery = ture,
			value = "select min(id) minId, max(id) maxId, count(*) cnt "
					+ "from Article "
					+ "where title like :word")
	Map<String, Object> getMinMaxCount(String word);
}
