package com.example.demo.post.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class PostTest {
	@Test
	@DisplayName("PostCreate으로 게시물을 만들 수 있다.")
	public void pt1() throws Exception {
		//given
		PostCreate postCreate = PostCreate.builder()
			.writerId(1)
			.content("helloworld")
			.build();
		User writer = User.builder()
			.email("ys1@naver.com")
			.nickname("ys1")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.build();

		//when
		Post post = Post.from(writer, postCreate);

		//then
		Assertions.assertThat(post.getContent()).isEqualTo("helloworld");
		Assertions.assertThat(post.getWriter().getEmail()).isEqualTo("ys1@naver.com");
		Assertions.assertThat(post.getWriter().getNickname()).isEqualTo("ys1");
		Assertions.assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
		Assertions.assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
		Assertions.assertThat(post.getWriter().getCertificationCode())
			.isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	}
}