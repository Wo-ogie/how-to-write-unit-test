package example.unit_test.domain.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import example.unit_test.domain.post.dto.DeletePostCommand;
import example.unit_test.domain.post.dto.UpdatePostCommand;
import example.unit_test.domain.post.dto.UploadPostCommand;
import example.unit_test.domain.post.model.Post;
import example.unit_test.domain.post.repository.PostRepository;
import example.unit_test.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@InjectMocks
	private PostService sut;

	@Mock
	private UserRepository userRepository;
	@Mock
	private PostRepository postRepository;

	@Test
	void 전체_게시글_목록을_조회한다() {
		// given
		final Long WRITER_ID = 10L;
		List<Post> expectedResult = List.of(
			Post.withId(1L, WRITER_ID, "Test1", "This is..."),
			Post.withId(2L, WRITER_ID, "Test2", "This is...")
		);
		given(postRepository.findAll())
			.willReturn(expectedResult);

		// when
		List<Post> actualResult = sut.findAll();

		// then
		then(postRepository).should().findAll();
		then(postRepository).shouldHaveNoMoreInteractions();
		then(userRepository).shouldHaveNoInteractions();
		assertThatIterable(actualResult).isEqualTo(expectedResult);
	}

	@Test
	void 주어진_id로_게시글을_단건_조회한다() {
		// given
		final Long POST_ID = 1L;
		Post expectedResult = Post.withId(POST_ID, 2L, "Test", "Contents...");
		given(postRepository.findById(POST_ID))
			.willReturn(Optional.of(expectedResult));

		// when
		Post actualResult = sut.getById(POST_ID);

		// then
		then(postRepository).should().findById(POST_ID);
		then(postRepository).shouldHaveNoMoreInteractions();
		then(userRepository).shouldHaveNoInteractions();
		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	void 주어진_id로_게시글을_단건_조회한다_만약_존재하지_않는_id라면_예외가_발생한다() {
		// given
		final Long POST_ID = 1L;
		given(postRepository.findById(POST_ID))
			.willReturn(Optional.empty());

		// when
		Throwable ex = catchThrowable(() -> sut.getById(POST_ID));

		// then
		then(postRepository).should().findById(POST_ID);
		then(postRepository).shouldHaveNoMoreInteractions();
		then(userRepository).shouldHaveNoInteractions();
		assertThat(ex).isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void 게시글을_업로드한다() {
		// given
		final Long WRITER_ID = 1L;
		final Long EXPECTED_POST_ID = 2L;
		final String TITLE = "Test";
		final String CONTENT = "This is test...";
		UploadPostCommand uploadPostCommand = new UploadPostCommand(WRITER_ID, TITLE, CONTENT);
		Post expectedResult = Post.withId(EXPECTED_POST_ID, WRITER_ID, TITLE, CONTENT);
		given(userRepository.existsById(uploadPostCommand.writerId()))
			.willReturn(true);
		given(postRepository.save(any(Post.class)))
			.willReturn(expectedResult);

		// when
		Post actualResult = sut.upload(uploadPostCommand);

		// then
		then(userRepository).should().existsById(uploadPostCommand.writerId());
		then(postRepository).should().save(any(Post.class));
		then(userRepository).shouldHaveNoMoreInteractions();
		then(postRepository).shouldHaveNoMoreInteractions();
		assertThat(actualResult).isEqualTo(expectedResult);
	}

	@Test
	void 게시글을_업로드한다_만약_작성자_정보가_존재하지_않는다면_예외가_발생한다() {
		// given
		final Long WRITER_ID = 1L;
		UploadPostCommand uploadPostCommand = new UploadPostCommand(WRITER_ID, "Test", "Contents...");
		given(userRepository.existsById(WRITER_ID))
			.willReturn(false);

		// when
		Throwable ex = catchThrowable(() -> sut.upload(uploadPostCommand));

		// then
		then(userRepository).should().existsById(WRITER_ID);
		then(userRepository).shouldHaveNoMoreInteractions();
		then(postRepository).shouldHaveNoInteractions();
		assertThat(ex).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 수정할_게시글_정보가_주어지고_게시글을_수정한다() {
		// given
		final Long WRITER_ID = 1L;
		final Long POST_ID = 2L;
		UpdatePostCommand updatePostCommand = new UpdatePostCommand(WRITER_ID, POST_ID, "New title", "New Contents...");
		Post oldPost = Post.withId(POST_ID, WRITER_ID, "Old title", "Old Contents...");
		given(postRepository.findById(POST_ID))
			.willReturn(Optional.of(oldPost));
		willDoNothing()
			.given(postRepository).update(any(Post.class));

		// when
		sut.update(updatePostCommand);

		// then
		then(postRepository).should().findById(POST_ID);
		then(postRepository).should().update(any(Post.class));
		then(postRepository).shouldHaveNoMoreInteractions();
		then(userRepository).shouldHaveNoInteractions();
	}

	@Test
	void 게시글을_수정한다_만약_수정하려는_유저와_게시글_작성자가_다르다면_예외가_발생한다() {
		// given
		final Long WRITER_ID = 1L;
		final Long REQUEST_USER_ID = 2L;
		final Long POST_ID = 3L;
		UpdatePostCommand updatePostCommand = new UpdatePostCommand(REQUEST_USER_ID, POST_ID, "New", "New Content");
		Post oldPost = Post.withId(POST_ID, WRITER_ID, "Old title", "Old Contents...");
		given(postRepository.findById(POST_ID))
			.willReturn(Optional.of(oldPost));

		// when
		Throwable ex = catchThrowable(() -> sut.update(updatePostCommand));

		// then
		then(postRepository).should().findById(POST_ID);
		then(postRepository).shouldHaveNoMoreInteractions();
		then(userRepository).shouldHaveNoInteractions();
		assertThat(ex).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void 게시글을_삭제한다() {
		// given
		final Long WRITER_ID = 1L;
		final Long POST_ID = 2L;
		DeletePostCommand deletePostCommand = new DeletePostCommand(WRITER_ID, POST_ID);
		Post post = Post.withId(POST_ID, WRITER_ID, "Title", "Contents...");
		given(postRepository.findById(POST_ID))
			.willReturn(Optional.of(post));
		willDoNothing()
			.given(postRepository).delete(post);

		// when
		sut.delete(deletePostCommand);

		// then
		then(postRepository).should().findById(POST_ID);
		then(postRepository).should().delete(post);
		then(postRepository).shouldHaveNoMoreInteractions();
		then(userRepository).shouldHaveNoInteractions();
	}

	@Test
	void 게시글을_삭제한다_만약_삭제하려는_유저와_게시글_작성자가_다르다면_예외가_발생한다() {
		// given
		final Long WRITER_ID = 1L;
		final Long REQUEST_USER_ID = 2L;
		final Long POST_ID = 3L;
		DeletePostCommand deletePostCommand = new DeletePostCommand(REQUEST_USER_ID, POST_ID);
		Post post = Post.withId(POST_ID, WRITER_ID, "Title", "Contents...");
		given(postRepository.findById(POST_ID))
			.willReturn(Optional.of(post));

		// when
		Throwable ex = catchThrowable(() -> sut.delete(deletePostCommand));

		// then
		then(postRepository).should().findById(POST_ID);
		then(postRepository).shouldHaveNoMoreInteractions();
		then(userRepository).shouldHaveNoInteractions();
		assertThat(ex).isInstanceOf(IllegalArgumentException.class);
	}
}