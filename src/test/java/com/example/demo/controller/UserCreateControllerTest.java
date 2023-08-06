package com.example.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.model.dto.UserCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserCreateControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private JavaMailSender mailSender;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@DisplayName("사용자는 회원가입을 할 수 있고, 회원가입시 사용자는 PENDING 상태가 된다.")
	void ucct1() throws Exception {
		//given
		UserCreateDto userCreateDto = UserCreateDto.builder()
			.email("ys1@kakao.com")
			.nickname("ys1-n")
			.address("Pangyo")
			.build();
		BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

		//then
		mockMvc.perform(
				post("/api/users")
					.header("EMAIL", "ys1@naver.com")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(userCreateDto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.email").value("ys1@kakao.com"))
			.andExpect(jsonPath("$.nickname").value("ys1-n"))
			.andExpect(jsonPath("$.status").value("PENDING"));
	}
}