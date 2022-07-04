package telran.album.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.album.model.*;

class AlbumTest {

	Album albums;
	LocalDateTime now = LocalDateTime.now();
	Photo[] ph = new Photo[6];
	Comparator<Photo> comp = (p1, p2) -> {
		int res = Integer.compare(p1.getAlbomId(), p2.getAlbomId());
		return res != 0 ? res : Integer.compare(p1.getPhotoId(), p2.getPhotoId());
	};

	@BeforeEach
	void setUp() throws Exception {
		albums = new AlbumImpl(7);
		ph[0] = new Photo(1, 1, "title1", "url1", now.minusDays(7));
		ph[1] = new Photo(1, 2, "title2", "url2", now.minusDays(5).toLocalDate().atStartOfDay());
		ph[2] = new Photo(1, 3, "title3", "url3", now.minusDays(5).toLocalDate().atStartOfDay());
		ph[3] = new Photo(2, 1, "title1", "url1", now.minusDays(5).toLocalDate().atStartOfDay());
		ph[4] = new Photo(2, 4, "title4", "url4", LocalDateTime.of(now.minusDays(2).toLocalDate(), LocalTime.MAX));
		ph[5] = new Photo(1, 4, "title4", "url4", LocalDateTime.of(now.minusDays(2).toLocalDate(), LocalTime.MAX));
		for (int i = 0; i < ph.length - 1; i++) {
			albums.addPhoto(ph[i]);
		}
	}

	@Test
	void testAddPhoto() {
		assertTrue(albums.addPhoto(ph[5]));
		assertEquals(ph[5], albums.getPhotoFromAlbum(4, 1));
		assertEquals(6, albums.size());
		assertFalse(albums.addPhoto(ph[5]));
	}

	@Test
	void testRemovePhoto() {
		assertFalse(albums.removePhoto(4, 1));
		assertTrue(albums.removePhoto(1, 1));
		assertNull(albums.getPhotoFromAlbum(1, 1));
		assertEquals(4, albums.size());
	}

	@Test
	void testUpdatePhoto() {
		assertTrue(albums.updatePhoto(1, 1, "newUrl"));
		assertEquals("newUrl", albums.getPhotoFromAlbum(1, 1).getUrl());
	}

	@Test
	void testGetPhotoFromAlbum() {
		assertEquals(ph[0], albums.getPhotoFromAlbum(1, 1));
		assertNull(albums.getPhotoFromAlbum(4, 1));
	}

	@Test
	void testGetAllPhotoFromAlbum() {
		Photo[] actuals = albums.getAllPhotoFromAlbum(2);
		Arrays.sort(actuals, comp);
		Photo[] expecteds = { ph[3], ph[4] };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	void testGetPhotoBeetwenDate() {
		LocalDate ld = now.toLocalDate();
		albums.addPhoto(ph[5]);
		Photo[] actuals = albums.getPhotoBeetwenDate(ld.minusDays(5), ld.minusDays(2));
		Arrays.sort(actuals, comp);
		Photo[] expecteds = { ph[1], ph[2], ph[5], ph[3], ph[4] };
		assertArrayEquals(expecteds, actuals);
	}

	@Test
	void testSize() {
		assertEquals(5, albums.size());
	}
}
