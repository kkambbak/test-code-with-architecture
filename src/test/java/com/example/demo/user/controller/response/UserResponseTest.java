package com.example.demo.user.controller.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class UserResponseTest {

	@Test
	@DisplayName("User로 응답을 생성 할 수 있다.")
	public void urt1() throws Exception {
		//given
		User user = User.builder()
			.id(1L)
			.email("ys1@naver.com")
			.nickname("ys1")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.lastLoginAt(100L)
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.build();

		//when
		UserResponse userResponse = UserResponse.from(user);

		//then
		Assertions.assertThat(userResponse.getId()).isEqualTo(1);
		Assertions.assertThat(userResponse.getEmail()).isEqualTo("ys1@naver.com");
		Assertions.assertThat(userResponse.getNickname()).isEqualTo("ys1");
		Assertions.assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
		Assertions.assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
	}
}