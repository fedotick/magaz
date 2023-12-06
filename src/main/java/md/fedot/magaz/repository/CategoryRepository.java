package md.fedot.magaz.repository;

import md.fedot.magaz.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
