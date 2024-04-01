package example.unit_test.domain.post.repository;

import java.util.List;
import java.util.Optional;

import example.unit_test.domain.post.model.Post;

public interface PostRepository {

	Post save(Post post);

	Optional<Post> findById(Long id);

	List<Post> findAll();

	void update(Post post);

	void delete(Post post);
}
