package com.example.demo.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;

class UserServiceTest {

	private UserService userService;

	@BeforeEach
	void init() {
		FakeMailSender fakeMailSender = new FakeMailSender();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();
		this.userService = UserService.builder()
			.uuidHolder(new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
			.clockHolder(new TestClockHolder(1678530673956L))
			.userRepository(fakeUserRepository)
			.certificationService(new CertificationService(fakeMailSender))
			.build();
		fakeUserRepository.save(User.builder()
			.id(1L)
			.email("ys1@naver.com")
			.nickname("ys1")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.build());
		fakeUserRepository.save(User.builder()
			.id(2L)
			.email("ys2@naver.com")
			.nickname("ys2")
			.address("Seoul")
			.status(UserStatus.PENDING)
			.certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
			.build());
	}

	@Test
	@DisplayName("getByEmail은 ACTIVE 상태인 유저를 찾아올 수 있다.")
	void ust1() throws Exception {
		//given
		String email = "ys1@naver.com";

		//when
		User result = userService.getByEmail(email);

		//then
		Assertions.assertThat(result.getNickname()).isEqualTo("ys1");
	}

	@Test
	@DisplayName("getByEmail은 PENDING 상태인 유저를 찾아올 수 없다.")
	void ust2() throws Exception {
		//given
		String email = "ys2@naver.com";

		//when
		//then
		assertThatThrownBy(() -> {

			userService.getByEmail(email);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	@DisplayName("getById은 ACTIVE 상태인 유저를 찾아올 수 있다.")
	void ust3() throws Exception {

		//when
		User result = userService.getById(1);

		//then
		Assertions.assertThat(result.getNickname()).isEqualTo("ys1");
	}

	@Test
	@DisplayName("getById은 PENDING 상태인 유저를 찾아올 수 없다.")
	void ust4() throws Exception {

		//then
		assertThatThrownBy(() -> {

			userService.getById(2);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	@DisplayName("userCreateDto를 이용하여 유저를 생성할 수 있다.")
	void ust5() throws Exception {
		//given
		UserCreate userCreate = UserCreate.builder()
			.email("ys3@naver.com")
			.address("Gyeonggi")
			.nickname("ys1-k")
			.build();

		//when
		User result = userService.create(userCreate);

		//then
		Assertions.assertThat(result.getId()).isNotNull();
		Assertions.assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
		Assertions.assertThat(result.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
	}

	@Test
	@DisplayName("userUpdateDto를 이용하여 유저를 수정할 수 있다.")
	void ust6() throws Exception {
		//given
		UserUpdate userUpdate = UserUpdate.builder()
			.address("Incheon")
			.nickname("ys1-n")
			.build();

		//when
		userService.update(1, userUpdate);

		//then
		User user = userService.getById(1);
		Assertions.assertThat(user.getId()).isNotNull();
		Assertions.assertThat(user.getAddress()).isEqualTo("Incheon");
		Assertions.assertThat(user.getNickname()).isEqualTo("ys1-n");
	}

	@Test
	@DisplayName("user를 로그인 시키면 마지막 로그인 시간이 변경된다.")
	void ust7() throws Exception {

		//when
		userService.login(1);

		//then
		User user = userService.getById(1);
		Assertions.assertThat(user.getLastLoginAt()).isGreaterThan(0L);
	}

	@Test
	@DisplayName("PENDING 상태의 사용자는 인증코드로 ACTIVE 시킬 수 있다.")
	void ust8() throws Exception {

		//when
		userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

		//then
		User user = userService.getById(2);
		Assertions.assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	@DisplayName("PENDING 상태의 사용자는 잘못된 인증코드를 받으면 에러를 던진다.")
	void ust9() throws Exception {

		//when
		//then
		assertThatThrownBy(() -> {
			userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
		}).isInstanceOf(CertificationCodeNotMatchedException.class);
	}
}