package com.example.demo.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.repository.PostEntity;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
	@Sql(value = "/sql/post-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {

	@Autowired
	private PostService postService;

	@Test
	@DisplayName("getById는_존재하는_게시물을_내려준다")
	void pst1() {
		// given
		// when
		PostEntity result = postService.getById(1);

		// then
		assertThat(result.getContent()).isEqualTo("helloworld");
		assertThat(result.getWriter().getEmail()).isEqualTo("ys1@naver.com");
	}

	@Test
	@DisplayName("postCreateDto_를_이용하여_게시물을_생성할_수_있다")
	void pst2() {
		// given
		PostCreateDto postCreateDto = PostCreateDto.builder()
			.writerId(1)
			.content("foobar")
			.build();

		// when
		PostEntity result = postService.create(postCreateDto);

		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getContent()).isEqualTo("foobar");
		assertThat(result.getCreatedAt()).isGreaterThan(0);
	}

	@Test
	@DisplayName("postUpdateDto_를_이용하여_게시물을_수정할_수_있다")
	void pst3() {
		// given
		PostUpdateDto postUpdateDto = PostUpdateDto.builder()
			.content("hello world :)")
			.build();

		// when
		postService.update(1, postUpdateDto);

		// then
		PostEntity postEntity= postService.getById(1);
		assertThat(postEntity.getContent()).isEqualTo("hello world :)");
		assertThat(postEntity.getModifiedAt()).isGreaterThan(0);
	}
}