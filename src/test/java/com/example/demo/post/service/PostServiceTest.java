package com.example.demo.post.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class PostServiceTest {

	private PostService postService;

	@BeforeEach
	void init() {
		FakeMailSender fakeMailSender = new FakeMailSender();
		FakePostRepository fakePostRepository = new FakePostRepository();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();
		this.postService = PostService.builder()
			.postRepository(fakePostRepository)
			.userRepository(fakeUserRepository)
			.clockHolder(new TestClockHolder(1678530673956L))
			.build();

		User user1 = User.builder()
			.id(1L)
			.email("ys1@naver.com")
			.nickname("ys1")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.build();
		User user2 = User.builder()
			.id(2L)
			.email("ys2@naver.com")
			.nickname("ys2")
			.address("Seoul")
			.status(UserStatus.PENDING)
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.build();

		fakeUserRepository.save(user1);
		fakeUserRepository.save(user2);

		fakePostRepository.save(Post.builder()
			.id(1L)
			.content("helloworld")
			.createdAt(1678530673956L)
			.modifiedAt(0L)
			.writer(user1)
			.build());
	}

	@Test
	@DisplayName("getById는_존재하는_게시물을_내려준다")
	void pst1() {
		// given
		// when
		Post result = postService.getById(1);

		// then
		assertThat(result.getContent()).isEqualTo("helloworld");
		assertThat(result.getWriter().getEmail()).isEqualTo("ys1@naver.com");
	}

	@Test
	@DisplayName("postCreateDto_를_이용하여_게시물을_생성할_수_있다")
	void pst2() {
		// given
		PostCreate postCreate = PostCreate.builder()
			.writerId(1)
			.content("foobar")
			.build();

		// when
		Post result = postService.create(postCreate);

		// then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getContent()).isEqualTo("foobar");
		assertThat(result.getCreatedAt()).isEqualTo(1678530673956L);
	}

	@Test
	@DisplayName("postUpdateDto_를_이용하여_게시물을_수정할_수_있다")
	void pst3() {
		// given
		PostUpdate postUpdate = PostUpdate.builder()
			.content("hello world :)")
			.build();

		// when
		postService.update(1, postUpdate);

		// then
		Post post = postService.getById(1);
		assertThat(post.getContent()).isEqualTo("hello world :)");
		assertThat(post.getModifiedAt()).isEqualTo(1678530673956L);
	}
}