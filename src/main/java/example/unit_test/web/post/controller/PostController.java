package example.unit_test.web.post.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.unit_test.domain.post.dto.DeletePostCommand;
import example.unit_test.domain.post.dto.UpdatePostCommand;
import example.unit_test.domain.post.dto.UploadPostCommand;
import example.unit_test.domain.post.model.Post;
import example.unit_test.domain.post.service.PostService;
import example.unit_test.web.post.dto.PostInfoResponse;
import example.unit_test.web.post.dto.UpdatePostRequest;
import example.unit_test.web.post.dto.UploadPostRequest;
import example.unit_test.web.post.dto.UploadPostResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/posts")
@RestController
public class PostController {

	private final PostService postService;

	@PostMapping
	public ResponseEntity<UploadPostResponse> uploadPost(
		@RequestBody @Valid UploadPostRequest uploadRequest
	) {
		Post post = postService.upload(new UploadPostCommand(
			uploadRequest.writerId(),
			uploadRequest.title(),
			uploadRequest.content()
		));
		return ResponseEntity
			.created(URI.create("/api/posts/" + post.getId()))
			.body(UploadPostResponse.from(post));
	}

	@GetMapping("/{postId}")
	public PostInfoResponse getPost(@PathVariable Long postId) {
		Post post = postService.getById(postId);
		return PostInfoResponse.from(post);
	}

	@GetMapping
	public List<PostInfoResponse> findAllPosts() {
		return postService.findAll().stream()
			.map(PostInfoResponse::from)
			.toList();
	}

	@PatchMapping("/{postId}")
	public ResponseEntity<Void> updatePost(
		@PathVariable Long postId,
		@RequestParam Long requestUserId,
		@RequestBody UpdatePostRequest updatePostRequest
	) {
		postService.update(new UpdatePostCommand(
			requestUserId,
			postId,
			updatePostRequest.title(),
			updatePostRequest.content()
		));
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(
		@PathVariable Long postId,
		@RequestParam Long requestUserId
	) {
		postService.delete(new DeletePostCommand(requestUserId, postId));
		return ResponseEntity.noContent().build();
	}
}
