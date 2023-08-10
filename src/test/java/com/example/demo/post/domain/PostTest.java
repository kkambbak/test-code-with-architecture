package com.example.demo.post.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.mock.TestClockHolder;
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
		Post post = Post.from(writer, postCreate, new TestClockHolder(1678530673956L));

		//then
		assertThat(post.getContent()).isEqualTo("helloworld");
		assertThat(post.getCreatedAt()).isEqualTo(1678530673956L);
		assertThat(post.getWriter().getEmail()).isEqualTo("ys1@naver.com");
		assertThat(post.getWriter().getNickname()).isEqualTo("ys1");
		assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
		assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(post.getWriter().getCertificationCode())
			.isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	}

	@Test
	@DisplayName("PostUpdate으로 게시물을 업데이트할 수 있다.")
	public void pt2() throws Exception {
		//given
		PostUpdate postUpdate = PostUpdate.builder()
			.content("foobar")
			.build();
		User writer = User.builder()
			.email("ys1@naver.com")
			.nickname("ys1")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.build();
		Post post1 = Post.builder()
			.id(1L)
			.content("helloworld")
			.createdAt(1678530673956L)
			.modifiedAt(0L)
			.writer(writer)
			.build();

		//when
		Post post = post1.update(postUpdate, new TestClockHolder(1678530673956L));

		//then
		assertThat(post.getContent()).isEqualTo("foobar");
		assertThat(post.getCreatedAt()).isEqualTo(1678530673956L);
	}
}