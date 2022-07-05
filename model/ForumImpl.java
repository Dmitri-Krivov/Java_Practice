package telran.forum.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

public class ForumImpl implements Forum {

	private Post[] posts;
	private int size;

	public ForumImpl() {
		posts = new Post[0];
	}

	@Override
	public String toString() {
		return "ForumImpl [posts=" + Arrays.toString(posts) + ", size=" + size + "]";
	}

	@Override
	public boolean addPost(Post post) {
		if (post == null || getPostById(post.getPostId()) != null) {
			return false;
		}
		int index = Arrays.binarySearch(posts, 0, size, post);
		index = index >= 0 ? index : -index - 1;
		Post[] newPosts = new Post[posts.length + 1];
		newPosts[index] = post;
		System.arraycopy(posts, 0, newPosts, 0, index);
		System.arraycopy(posts, index, newPosts, index + 1, posts.length - index);
		posts = newPosts;
		size++;
		return true;
	}

	@Override
	public boolean removePost(int postId) {
		Post pattern = new Post(null, postId, null, null);
		for (int i = 0; i < size; i++) {
			if (pattern.equals(posts[i])) {
				System.arraycopy(posts, i + 1, posts, i, size - i - 1);
				posts[--size] = null;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updatePost(int postId, String newContent) {
		Post post = getPostById(postId);
		if (post == null) {
			return false;
		}
		post.setContent(newContent);
		return true;
	}

	@Override
	public Post getPostById(int postId) {
		Post pattern = new Post(null, postId, null, null);
		for (int i = 0; i < size; i++) {
			if (posts[i].equals(pattern)) {
				return posts[i];
			}
		}
		return null;
	}

	@Override
	public Post[] getPostsByAuthor(String author) {
//		return findPostByPredicate(p->p.getAuthor()==author);
		int count = 0;
		for (int i = 0; i < size; i++) {
			if (posts[i].getAuthor().equals(author)) {
				count++;
			}
		}
		Post[] res = new Post[count];
		int j = 0;
		for (int i = 0; i < size; i++) {
			if (posts[i].getAuthor().equals(author)) {
				res[j++] = posts[i];
			}
		}
		return res;
	}

	@Override
	public Post[] getPostsByAuthor(String author, LocalDate dateFrom, LocalDate dateTo) {
		Post[] byAuthor1 = getPostsByAuthor(author);
		LocalDateTime[] byAuthor = new LocalDateTime[byAuthor1.length];
		for (int i = 0; i < byAuthor.length; i++) {
			byAuthor[i] = byAuthor1[i].date;
		}
		Post pattern = new Post(null, 0, null, null);
		pattern.setDate(dateFrom.atStartOfDay());

		int from = Arrays.binarySearch(byAuthor, 0, byAuthor.length, pattern.getDate());
		while (from >= 0) {
			from = Arrays.binarySearch(byAuthor, 0, from, pattern.getDate());
		}
		from = -from - 1;
		pattern.setDate(LocalDateTime.of(dateTo, LocalTime.MAX));
		int to = Arrays.binarySearch(byAuthor, 0, byAuthor.length, pattern.getDate());
		while (to >= 0) {
			to = Arrays.binarySearch(byAuthor, to + 1, byAuthor.length, pattern.getDate());
		}
		to = -to - 1;

		return Arrays.copyOfRange(byAuthor1, from, to);
	}

	@Override
	public int size() {
		return size;
	}

}
