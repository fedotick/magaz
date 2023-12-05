package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import md.fedot.magaz.domain.Order;
import md.fedot.magaz.domain.Product;
import md.fedot.magaz.domain.User;
import md.fedot.magaz.model.OrderRequestDTO;
import md.fedot.magaz.model.OrderResponseDTO;
import md.fedot.magaz.repos.OrderRepository;
import md.fedot.magaz.repos.ProductRepository;
import md.fedot.magaz.repos.UserRepository;
import md.fedot.magaz.util.BadRequestException;
import md.fedot.magaz.util.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public OrderResponseDTO create(OrderRequestDTO orderRequestDTO) {
        Order order = mapToEntity(orderRequestDTO, new Order());
        return mapToDTO(orderRepository.save(order), new OrderResponseDTO());
    }

    public List<OrderResponseDTO> getAll() {
        return orderRepository.findAll().stream()
                .map(order -> mapToDTO(order, new OrderResponseDTO()))
                .toList();
    }

    public OrderResponseDTO get(Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderResponseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public OrderResponseDTO update(Long id, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderRequestDTO, order);
        delete(id);
        return mapToDTO(orderRepository.save(order), new OrderResponseDTO());
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }

    public Order mapToEntity(OrderRequestDTO orderRequestDTO, Order order) {
        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(BadRequestException::new);
        order.setUser(user);

        if (orderRequestDTO.getProductIds().isEmpty()) {
            throw new BadRequestException();
        }
        List<Product> products = orderRequestDTO.getProductIds()
                .stream()
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(BadRequestException::new))
                .toList();
        order.setProducts(products);

        BigDecimal amount = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setAmount(amount);

        return order;
    }

    public OrderResponseDTO mapToDTO(Order order, OrderResponseDTO orderResponseDTO) {
        orderResponseDTO.setId(order.getId());
        orderResponseDTO.setUserId(order.getUser().getId());
        orderResponseDTO.setProductIds(order.getProducts().stream().map(Product::getId).toList());
        orderResponseDTO.setAmount(order.getAmount());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        orderResponseDTO.setCreatedAt(order.getCreatedAt().format(formatter));
        return orderResponseDTO;
    }

}
