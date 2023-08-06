package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.exception.ResourceNotFoundException;
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

			UserEntity result = userService.getById(2);
		}).isInstanceOf(ResourceNotFoundException.class);
	}
}