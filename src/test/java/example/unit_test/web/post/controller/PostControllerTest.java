package example.unit_test.web.post.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import example.unit_test.domain.post.dto.DeletePostCommand;
import example.unit_test.domain.post.dto.UpdatePostCommand;
import example.unit_test.domain.post.dto.UploadPostCommand;
import example.unit_test.domain.post.model.Post;
import example.unit_test.domain.post.service.PostService;
import example.unit_test.web.post.dto.UpdatePostRequest;
import example.unit_test.web.post.dto.UploadPostRequest;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {

	@MockBean
	private PostService postService;

	private final MockMvc mvc;
	private final ObjectMapper mapper;

	@Autowired
	public PostControllerTest(MockMvc mvc, ObjectMapper mapper) {
		this.mvc = mvc;
		this.mapper = mapper;
	}

	@Test
	void 전체_게시글_목록을_조회한다() throws Exception {
		// given
		final Long WRITER_ID = 10L;
		List<Post> expectedResult = List.of(
			Post.withId(1L, WRITER_ID, "Test1", "This is..."),
			Post.withId(2L, WRITER_ID, "Test2", "This is...")
		);
		given(postService.findAll())
			.willReturn(expectedResult);

		// when & then
		mvc.perform(get("/api/posts"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.size()").value(expectedResult.size()));
		then(postService).should().findAll();
		then(postService).shouldHaveNoMoreInteractions();
	}

	@Test
	void 주어진_id로_게시글을_단건_조회한다() throws Exception {
		// given
		final Long POST_ID = 1L;
		Post expectedResult = Post.withId(POST_ID, 2L, "Test", "Contents..");
		given(postService.getById(POST_ID))
			.willReturn(expectedResult);

		// when & then
		mvc.perform(
				get("/api/posts/{postId}", POST_ID)
			).andExpect(status().isOk())
			.andExpect(jsonPath("$.postId").value(expectedResult.getId()));
		then(postService).should().getById(POST_ID);
		then(postService).shouldHaveNoMoreInteractions();
	}

	@Test
	void 신규_게시글을_업로드한다() throws Exception {
		// given
		final Long WRITER_ID = 1L;
		final Long EXPECTED_POST_ID = 2L;
		final String TITLE = "New Post";
		final String CONTENT = "Contents...";
		UploadPostRequest uploadPostRequest = new UploadPostRequest(WRITER_ID, TITLE, CONTENT);
		Post expectedResult = Post.withId(EXPECTED_POST_ID, WRITER_ID, TITLE, CONTENT);
		given(postService.upload(any(UploadPostCommand.class)))
			.willReturn(expectedResult);

		// when & then
		mvc.perform(
				post("/api/posts")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(uploadPostRequest))
			).andExpect(status().isCreated())
			.andExpect(jsonPath("$.postId").value(expectedResult.getId()));
		then(postService).should().upload(any(UploadPostCommand.class));
		then(postService).shouldHaveNoMoreInteractions();
	}

	@Test
	void 수정할_게시글_정보가_주어지고_게시글을_수정한다() throws Exception {
		// given
		final Long WRITER_ID = 1L;
		final Long POST_ID = 2L;
		UpdatePostRequest updatePostRequest = new UpdatePostRequest("Old title", "Old contents");
		willDoNothing()
			.given(postService).update(any(UpdatePostCommand.class));

		// when & then
		mvc.perform(
			patch("/api/posts/{postId}", POST_ID)
				.param("requestUserId", String.valueOf(WRITER_ID))
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(updatePostRequest))
		).andExpect(status().isNoContent());
		then(postService).should().update(any(UpdatePostCommand.class));
		then(postService).shouldHaveNoMoreInteractions();
	}

	@Test
	void 게시글을_삭제한다() throws Exception {
		// given
		final Long WRITER_ID = 1L;
		final Long POST_ID = 2L;
		willDoNothing()
			.given(postService).delete(any(DeletePostCommand.class));

		// when & then
		mvc.perform(
			delete("/api/posts/{postId}", POST_ID)
				.param("requestUserId", String.valueOf(WRITER_ID))
		).andExpect(status().isNoContent());
		then(postService).should().delete(any(DeletePostCommand.class));
		then(postService).shouldHaveNoMoreInteractions();
	}
}