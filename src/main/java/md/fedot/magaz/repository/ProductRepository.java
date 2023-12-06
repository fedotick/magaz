package md.fedot.magaz.repository;

import jakarta.transaction.Transactional;
import md.fedot.magaz.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Product p WHERE p.category.id = :categoryId")
    void deleteAllByCategoryId(Long categoryId);

}
