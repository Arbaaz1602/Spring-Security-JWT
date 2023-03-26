package com.project.testcreate;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.project.entity.User;
import com.project.repository.UserRepository;
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)

public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;
	
	@Test
	public void testCreateUser()
	{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("test2023");
		
		User user = new User("arbaaz@gmail.com",password);
		User savedUser = userRepository.save(user);
		
		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getId()).isGreaterThanOrEqualTo(0);
	}
	
	
}
