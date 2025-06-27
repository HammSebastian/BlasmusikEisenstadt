/**
 * @author Sebastian Hamm
 * @filename ImageDao.java
 * @created 6/25/25, Wednesday
 */

package at.sebastianhamm.kapelle_eisenstadt.dao;

import at.sebastianhamm.kapelle_eisenstadt.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageDao extends JpaRepository<Image, Long> {

    List<Image> findImagesByPath(String path);

    List<Image> findImagesByCategory(String category);

    List<Image> findImagesByAuthor(String imageAuthor);

    Optional<Image> findImageByPath(String path);

    Optional<Image> findImageByCategory(String category);

    Optional<Image> findImageByAuthor(String author);
}
