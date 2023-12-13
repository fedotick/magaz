package md.fedot.magaz.repository;

import md.fedot.magaz.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("UPDATE Product p SET p.category.id = NULL WHERE p.category.id = :categoryId")
    void updateAllCategoryIdToNullByCategoryId(Long categoryId);

}
