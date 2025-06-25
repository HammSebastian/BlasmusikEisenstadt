/**
 * @author Sebastian Hamm
 * @filename ImageDao.java
 * @created 6/25/25, Wednesday
 */

package at.sebastianhamm.kapelle_eisenstadt.dao;

import at.sebastianhamm.kapelle_eisenstadt.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageDao extends JpaRepository<Image, Long> {
    
    Image findImageByImagePath(String imagePath);
    
    List<Image> findImagesByImageCategory(String imageCategory);
    
    List<Image> findImagesByImageAuthor(String imageAuthor);
}
