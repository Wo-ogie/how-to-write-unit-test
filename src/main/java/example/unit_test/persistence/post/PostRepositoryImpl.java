package example.unit_test.persistence.post;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import example.unit_test.domain.post.model.Post;
import example.unit_test.domain.post.repository.PostRepository;

@Repository
public class PostRepositoryImpl implements PostRepository {
	@Override
	public Post save(Post post) {
		return null;
	}

	@Override
	public Optional<Post> findById(Long id) {
		return Optional.empty();
	}

	@Override
	public List<Post> findAll() {
		return List.of();
	}

	@Override
	public void update(Post post) {
	}

	@Override
	public void delete(Post post) {
	}
}
