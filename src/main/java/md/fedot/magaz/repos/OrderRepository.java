package md.fedot.magaz.repos;

import md.fedot.magaz.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
