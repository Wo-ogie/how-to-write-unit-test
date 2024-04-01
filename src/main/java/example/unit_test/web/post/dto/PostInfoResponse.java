package example.unit_test.web.post.dto;

import example.unit_test.domain.post.model.Post;

public record PostInfoResponse(
	Long postId,
	Long writerId,
	String title,
	String content
) {
	public static PostInfoResponse from(Post post) {
		return new PostInfoResponse(
			post.getId(),
			post.getWriterId(),
			post.getTitle(),
			post.getContent()
		);
	}
}
