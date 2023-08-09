package com.example.demo.user.controller.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class MyProfileResponseTest {
	@Test
	@DisplayName("User로 응답을 생성할 수 있다.")
	public void mprt1() throws Exception {

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
		MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

		//then
		Assertions.assertThat(myProfileResponse.getId()).isEqualTo(1);
		Assertions.assertThat(myProfileResponse.getEmail()).isEqualTo("ys1@naver.com");
		Assertions.assertThat(myProfileResponse.getNickname()).isEqualTo("ys1");
		Assertions.assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
		Assertions.assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
		Assertions.assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
	}
}