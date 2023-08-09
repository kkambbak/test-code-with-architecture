package com.example.demo.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;

class UserTest {

	@Test
	@DisplayName("UserCreate 객체로 생성할 수 있다.")
	public void ut1() throws Exception {
		//given
		UserCreate userCreate = UserCreate.builder()
			.email("ys1@kakao.com")
			.nickname("ys1-n")
			.address("Pangyo")
			.build();
		//when
		User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));
		//then
		assertThat(user.getId()).isNull();
		assertThat(user.getEmail()).isEqualTo("ys1@kakao.com");
		assertThat(user.getNickname()).isEqualTo("ys1-n");
		assertThat(user.getAddress()).isEqualTo("Pangyo");
		assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	}

	@Test
	@DisplayName("UserUpdate 객체로 업데이트할 수 있다.")
	public void ut2() throws Exception {
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

		UserUpdate userUpdate = UserUpdate.builder()
			.nickname("ys1-n")
			.address("Pangyo")
			.build();

		//when

		user = user.update(userUpdate);

		//then
		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getEmail()).isEqualTo("ys1@naver.com");
		assertThat(user.getNickname()).isEqualTo("ys1-n");
		assertThat(user.getAddress()).isEqualTo("Pangyo");
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	}

	@Test
	@DisplayName("로그인을 할 수 있고, 로그인 시 마지막 로그인 시간이 변경된다.")
	public void ut3() throws Exception {
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
		user = user.login(new TestClockHolder(1678530673956L));

		//then
		assertThat(user.getLastLoginAt()).isEqualTo(1678530673956L);
	}

	@Test
	@DisplayName("유효한 인증 코드로 계정을 활성화할 수 있다.")
	public void ut4() throws Exception {
		//given
		User user = User.builder()
			.id(1L)
			.email("ys1@naver.com")
			.nickname("ys1")
			.address("Seoul")
			.status(UserStatus.PENDING)
			.lastLoginAt(100L)
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.build();
		//when
		user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

		//when
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
		//then
	}

	@Test
	@DisplayName("잘못된 인증 코드로 계정을 활성화 하려하면 에러를 던진다.")
	public void ut5() throws Exception {
		//given
		User user = User.builder()
			.id(1L)
			.email("ys1@naver.com")
			.nickname("ys1")
			.address("Seoul")
			.status(UserStatus.PENDING)
			.lastLoginAt(100L)
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.build();

		//when
		//then
		assertThatThrownBy(() -> user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab")
		).isInstanceOf(CertificationCodeNotMatchedException.class);
	}
}