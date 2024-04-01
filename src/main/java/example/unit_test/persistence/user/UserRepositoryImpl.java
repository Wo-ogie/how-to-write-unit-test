package example.unit_test.persistence.user;

import org.springframework.stereotype.Repository;

import example.unit_test.domain.user.repository.UserRepository;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@Override
	public boolean existsById(Long id) {
		return false;
	}
}
