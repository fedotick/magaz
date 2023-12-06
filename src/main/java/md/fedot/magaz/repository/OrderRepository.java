package md.fedot.magaz.repository;

import md.fedot.magaz.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
