package telran.album.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.function.Predicate;

public class AlbumImpl implements Album {
	private Photo[] photos;
	private int size;

	public AlbumImpl(int capacity) {
		photos = new Photo[capacity];
	}

	@Override
	public boolean addPhoto(Photo photo) {
		if (size == photos.length || photo == null
				|| getPhotoFromAlbum(photo.getPhotoId(), photo.getAlbomId()) != null) {
			return false;
		}
		int index = Arrays.binarySearch(photos, 0, size, photo);
		index = index < 0 ? -index - 1 : index;
		System.arraycopy(photos, index, photos, index + 1, size - index);
		photos[index] = photo;
		size++;
		return true;
	}

	@Override
	public boolean removePhoto(int photoId, int albomId) {
		Photo pattern = new Photo(albomId, photoId, null, null, null);
		for (int i = 0; i < size; i++) {
			if (pattern.equals(photos[i])) {
				System.arraycopy(photos, i + 1, photos, i, size - i - 1);
				photos[--size] = null;
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean updatePhoto(int photoId, int albomId, String url) {
		Photo photo = getPhotoFromAlbum(photoId, albomId);
		if (photo == null) {
			return false;
		}
		photo.setUrl(url);
		return true;
	}

	@Override
	public Photo getPhotoFromAlbum(int photoId, int albomId) {
		Photo pattern = new Photo(albomId, photoId, null, null, null);
		for (int i = 0; i < size; i++) {
			if (photos[i].equals(pattern)) {
				return photos[i];
			}
		}
		return null;
	}

	@Override
	public Photo[] getAllPhotoFromAlbum(int albomId) {
		return findPhotosByPredicate(p -> p.getAlbomId() == albomId);
	}

	@Override
	public Photo[] getPhotoBeetwenDate(LocalDate dateFrom, LocalDate dateTo) {
		
//		decision with Predicate
//		return findPhotosByPredicate(p -> p.getDate().isAfter(dateFrom.atStartOfDay())
//				&& p.getDate().isBefore(LocalDateTime.of(dateTo, LocalTime.MAX)));
		
		Photo pattern = new Photo(0, 0, null, null, dateFrom.atStartOfDay());
		int from = Arrays.binarySearch(photos, 0, size, pattern);
		while(from>=0) {
			from=Arrays.binarySearch(photos, 0, from, pattern);
		}
		from =  -from - 1 ;
		pattern = new Photo(0, 0, null, null, LocalDateTime.of(dateTo, LocalTime.MAX));
		int to = Arrays.binarySearch(photos, 0, size, pattern);
		while(to>=0) {
			to = Arrays.binarySearch(photos, to+1, size, pattern);
		}
		to =  -to - 1 ;

		return Arrays.copyOfRange(photos, from, to);
	}

	@Override
	public int size() {
		return size;
	}

	private Photo[] findPhotosByPredicate(Predicate<Photo> predicate) {
		Photo[] res = new Photo[size];
		int j = 0;
		for (int i = 0; i < size; i++) {
			if (predicate.test(photos[i])) {
				res[j++] = photos[i];
			}
		}
		return Arrays.copyOf(res, j);
	}

}
