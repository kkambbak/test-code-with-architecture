package com.example.demo.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
	@Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserJpaRepository userJpaRepository;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("사용자는 특정 유저의 정보를 개인정보는 소거된 채 전달받을 수 있다.")
	void uct1() throws Exception {
		//then
		mockMvc.perform(get("/api/users/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.email").value("ys1@naver.com"))
			.andExpect(jsonPath("$.nickname").value("ys1"))
			.andExpect(jsonPath("$.status").value("ACTIVE"))
			.andExpect(jsonPath("$.address").doesNotExist());
	}

	@Test
	@DisplayName("사용자는 존재하지 않는 유저의 아이디로 api 호출할 경우 404 응답을 받는다.")
	void uct2() throws Exception {
		//then
		mockMvc.perform(get("/api/users/12345678"))
			.andExpect(status().isNotFound())
			.andExpect(content().string("Users에서 ID 12345678를 찾을 수 없습니다."));
	}

	@Test
	@DisplayName("사용자는 인증 코드로 계정을 활성화시킬 수 있다.")
	void uct3() throws Exception {
		//then
		mockMvc.perform(
				get("/api/users/2/verify")
					.queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
			.andExpect(status().isFound());

		UserEntity userEntity = userJpaRepository.findById(2L).get();
		Assertions.assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	@DisplayName("사용자는 내 정보를 불러올 때 개인정보인 주소도 갖고 올 수 있다.")
	void uct4() throws Exception {
		//then
		mockMvc.perform(
				get("/api/users/me")
					.header("EMAIL", "ys1@naver.com"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.email").value("ys1@naver.com"))
			.andExpect(jsonPath("$.nickname").value("ys1"))
			.andExpect(jsonPath("$.address").value("Seoul"))
			.andExpect(jsonPath("$.status").value("ACTIVE"));
	}

	@Test
	@DisplayName("사용자는 내 정보를 수정할 수 있다.")
	void uct5() throws Exception {

		//given
		UserUpdate userUpdate = UserUpdate.builder()
			.nickname("ys1-n")
			.address("Pangyo")
			.build();

		//then
		mockMvc.perform(
				put("/api/users/me")
					.header("EMAIL", "ys1@naver.com")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(userUpdate)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.email").value("ys1@naver.com"))
			.andExpect(jsonPath("$.nickname").value("ys1-n"))
			.andExpect(jsonPath("$.address").value("Pangyo"))
			.andExpect(jsonPath("$.status").value("ACTIVE"));
	}

	@Test
	@DisplayName("사용자는 인증 코드가 일치하지 않을 경우 권한 없음 에러를 내려준다.")
	void uct6() throws Exception {
		//then
		mockMvc.perform(
				get("/api/users/2/verify")
					.queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
			.andExpect(status().isForbidden());
	}
}