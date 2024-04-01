package example.unit_test.domain.post.model;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
public class Post {
	private Long id;    // 게시글 id
	private Long writerId;    // 게시글 작성자 id
	private String title;    // 게시글 제목
	private String content;    // 게시글 내용

	public static Post withId(@NonNull Long id, @NonNull Long writerId, @NonNull String title,
		@NonNull String content) {
		return new Post(id, writerId, title, content);
	}

	public static Post withoutId(@NonNull Long writerId, @NonNull String title, @NonNull String content) {
		return new Post(null, writerId, title, content);
	}

	public void updateTitle(@Nullable String title) {
		if (StringUtils.hasText(title)) {
			this.title = title;
		}
	}

	public void updateContent(@Nullable String content) {
		if (StringUtils.hasText(content)) {
			this.content = content;
		}
	}

	public void verifyUpdateOrDeletePermission(Long userId) {
		if (!this.writerId.equals(userId)) {
			throw new IllegalArgumentException("게시물을 수정/삭제할 수 있는 권한이 없습니다. 게시물은 작성자만 수정/삭제할 수 있습니다.");
		}
	}
}