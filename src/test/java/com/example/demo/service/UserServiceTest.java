package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
	@Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})

class UserServiceTest {
	@Autowired
	private UserService userService;
	@MockBean
	private JavaMailSender mailSender;

	@Test
	@DisplayName("getByEmail은 ACTIVE 상태인 유저를 찾아올 수 있다.")
	void ust1() throws Exception{
	    //given
	    String email = "ys1@naver.com";

	    //when
		UserEntity result = userService.getByEmail(email);

	    //then
		Assertions.assertThat(result.getNickname()).isEqualTo("ys1");
	}

	@Test
	@DisplayName("getByEmail은 PENDING 상태인 유저를 찾아올 수 없다.")
	void ust2() throws Exception{
		//given
		String email = "ys2@naver.com";

		//when
		//then
		assertThatThrownBy(() -> {

			UserEntity result = userService.getByEmail(email);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	@DisplayName("getById은 ACTIVE 상태인 유저를 찾아올 수 있다.")
	void ust3() throws Exception{

		//when
		UserEntity result = userService.getById(1);

		//then
		Assertions.assertThat(result.getNickname()).isEqualTo("ys1");
	}

	@Test
	@DisplayName("getById은 PENDING 상태인 유저를 찾아올 수 없다.")
	void ust4() throws Exception{

		//then
		assertThatThrownBy(() -> {

			userService.getById(2);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	@DisplayName("userCreateDto를 이용하여 유저를 생성할 수 있다.")
	void ust5() throws Exception{
		//given
		UserCreateDto userCreateDto = UserCreateDto.builder()
			.email("ys3@naver.com")
			.address("Gyeonggi")
			.nickname("ys1-k")
			.build();
		BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

		//when
		UserEntity result = userService.create(userCreateDto);

		//then
		Assertions.assertThat(result.getId()).isNotNull();
		Assertions.assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
		//Assertions.assertThat(result.getCertificationCode()).isEqualTo("??"); //FIXME
	}

	@Test
	@DisplayName("userUpdateDto를 이용하여 유저를 수정할 수 있다.")
	void ust6() throws Exception{
		//given
		UserUpdateDto userUpdateDto = UserUpdateDto.builder()
			.address("Incheon")
			.nickname("ys1-n")
			.build();

		//when
		userService.update(1, userUpdateDto);

		//then
		UserEntity userEntity = userService.getById(1);
		Assertions.assertThat(userEntity.getId()).isNotNull();
		Assertions.assertThat(userEntity.getAddress()).isEqualTo("Incheon");
		Assertions.assertThat(userEntity.getNickname()).isEqualTo("ys1-n");
	}

	@Test
	@DisplayName("user를 로그인 시키면 마지막 로그인 시간이 변경된다.")
	void ust7() throws Exception{

		//when
		userService.login(1);

		//then
		UserEntity userEntity = userService.getById(1);
		Assertions.assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
		//Assertions.assertThat(result.getCertificationCode()).isEqualTo("??"); //FIXME
	}

	@Test
	@DisplayName("PENDING 상태의 사용자는 인증코드로 ACTIVE 시킬 수 있다.")
	void ust8() throws Exception{

		//when
		userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

		//then
		UserEntity userEntity = userService.getById(2);
		Assertions.assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	@DisplayName("PENDING 상태의 사용자는 잘못된 인증코드를 받으면 에러를 던진다.")
	void ust9() throws Exception{

		//when
		//then
		assertThatThrownBy(() -> {
			userService.verifyEmail(2, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab");
		}).isInstanceOf(CertificationCodeNotMatchedException.class);
	}
}