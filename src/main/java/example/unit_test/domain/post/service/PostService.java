package example.unit_test.domain.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import example.unit_test.domain.post.dto.DeletePostCommand;
import example.unit_test.domain.post.dto.UpdatePostCommand;
import example.unit_test.domain.post.dto.UploadPostCommand;
import example.unit_test.domain.post.model.Post;
import example.unit_test.domain.post.repository.PostRepository;
import example.unit_test.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {

	private final UserRepository userRepository;
	private final PostRepository postRepository;

	@Transactional
	public Post upload(UploadPostCommand uploadPostCommand) {
		if (!userRepository.existsById(uploadPostCommand.writerId())) {
			throw new IllegalArgumentException("유저 정보가 존재하지 않습니다.");
		}
		return postRepository.save(Post.withoutId(
			uploadPostCommand.writerId(),
			uploadPostCommand.title(),
			uploadPostCommand.content()
		));
	}

	@Transactional(readOnly = true)
	public Post getById(Long id) {
		return postRepository.findById(id).orElseThrow();
	}

	@Transactional(readOnly = true)
	public List<Post> findAll() {
		return postRepository.findAll();
	}

	@Transactional
	public void update(UpdatePostCommand updatePostCommand) {
		Post post = getById(updatePostCommand.postId());
		post.verifyUpdateOrDeletePermission(updatePostCommand.requestUserId());
		post.updateTitle(updatePostCommand.title());
		post.updateContent(updatePostCommand.content());
		postRepository.update(post);
	}

	@Transactional
	public void delete(DeletePostCommand deletePostCommand) {
		Post post = getById(deletePostCommand.postId());
		post.verifyUpdateOrDeletePermission(deletePostCommand.requestUserId());
		postRepository.delete(post);
	}
}
