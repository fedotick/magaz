package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import md.fedot.magaz.model.Order;
import md.fedot.magaz.model.Product;
import md.fedot.magaz.model.User;
import md.fedot.magaz.dto.OrderRequestDto;
import md.fedot.magaz.dto.OrderResponseDto;
import md.fedot.magaz.repository.OrderRepository;
import md.fedot.magaz.repository.ProductRepository;
import md.fedot.magaz.repository.UserRepository;
import md.fedot.magaz.exception.BadRequestException;
import md.fedot.magaz.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> mapToDTO(order, new OrderResponseDto()))
                .toList();
    }

    public OrderResponseDto getOrder(Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderResponseDto()))
                .orElseThrow(NotFoundException::new);
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = mapToEntity(orderRequestDto, new Order());
        return mapToDTO(orderRepository.save(order), new OrderResponseDto());
    }

    public OrderResponseDto updateOrder(Long id, OrderRequestDto orderRequestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderRequestDto, order);
        deleteOrder(id);
        return mapToDTO(orderRepository.save(order), new OrderResponseDto());
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order mapToEntity(OrderRequestDto orderRequestDto, Order order) {
        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(BadRequestException::new);
        order.setUser(user);

        if (orderRequestDto.getProductIds().isEmpty()) {
            throw new BadRequestException();
        }
        List<Product> products = orderRequestDto.getProductIds()
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

    public OrderResponseDto mapToDTO(Order order, OrderResponseDto orderResponseDto) {
        orderResponseDto.setId(order.getId());
        orderResponseDto.setUserId(order.getUser().getId());
        orderResponseDto.setProductIds(order.getProducts().stream().map(Product::getId).toList());
        orderResponseDto.setAmount(order.getAmount());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        orderResponseDto.setCreatedAt(order.getCreatedAt().format(formatter));
        return orderResponseDto;
    }

}
