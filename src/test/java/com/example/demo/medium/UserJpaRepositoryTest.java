package com.example.demo.medium;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;

@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
class UserJpaRepositoryTest {

	@Autowired
	private UserJpaRepository userJpaRepository;

	@Test
	@DisplayName("findByIdAndStatus로_유저_데이터를_찾아올_수_있다.")
	public void UR2() throws Exception {

		//when
		Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

		//then
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	@DisplayName("findByIdAndStatus는 데이터가 없으면 Optinoal empty를 내려준다.")
	public void UR3() throws Exception{

		//when
		Optional<UserEntity> result = userJpaRepository.findByIdAndStatus(1, UserStatus.PENDING);

		//then
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	@DisplayName("findByEmailAndStatus로_유저_데이터를_찾아올_수_있다.")
	public void UR4() throws Exception{

		//when
		Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("ys1@naver.com", UserStatus.ACTIVE);

		//then
		assertThat(result.isPresent()).isTrue();
	}

	@Test
	@DisplayName("findByIdAndStatus는 데이터가 없으면 Optinoal empty를 내려준다.")
	public void UR5() throws Exception{

		//when
		Optional<UserEntity> result = userJpaRepository.findByEmailAndStatus("ys1@naver.com", UserStatus.PENDING);

		//then
		assertThat(result.isEmpty()).isTrue();
	}
}