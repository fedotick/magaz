package md.fedot.magaz.repos;

import md.fedot.magaz.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
