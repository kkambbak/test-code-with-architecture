package com.example.demo.post.controller.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class PostResponseTest {
	@Test
	@DisplayName("Post로 응답을 생성할 수 있다.")
	public void prt1() throws Exception {
		//given
		Post post = Post.builder()
			.content("helloworld")
			.writer(User.builder()
				.email("ys1@naver.com")
				.nickname("ys1")
				.address("Seoul")
				.status(UserStatus.ACTIVE)
				.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
				.build())
			.build();

		//when
		PostResponse postResponse = PostResponse.from(post);

		//then
		Assertions.assertThat(postResponse.getContent()).isEqualTo("helloworld");
		Assertions.assertThat(post.getWriter().getEmail()).isEqualTo("ys1@naver.com");
		Assertions.assertThat(post.getWriter().getNickname()).isEqualTo("ys1");
		Assertions.assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
	}
}