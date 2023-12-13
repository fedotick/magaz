package md.fedot.magaz.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<OrderResponseDto> getAllOrders() {
        log.info("Getting all orders...");

        List<OrderResponseDto> orderResponseDtos = orderRepository.findAll()
                .stream()
                .map(OrderResponseDto::new)
                .toList();

        log.info("Found {} orders", orderResponseDtos.size());

        return orderResponseDtos;
    }

    public OrderResponseDto getOrder(Long id) {
        log.info("Getting order with ID: " + id);

        OrderResponseDto orderResponseDto = orderRepository.findById(id)
                .map(OrderResponseDto::new)
                .orElseThrow(() -> {
                    log.warn("Order not found");
                    return new NotFoundException("Order not found");
                });

        log.info("Found order: " + orderResponseDto);

        return orderResponseDto;
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("Creating order with data:" + orderRequestDto);

        Order order = mapToEntity(orderRequestDto, new Order());

        try {
            Order savedOrder = orderRepository.save(order);
            log.info("Order created with ID: " + savedOrder.getId());
        } catch (Exception e) {
            log.warn("Order was not created: " + e.getMessage());
        }

        return new OrderResponseDto(order);
    }

    public OrderResponseDto updateOrder(Long id, OrderRequestDto orderRequestDto) {
        log.info("Updating order with ID: " + id);

        orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Order not found");
                    return new NotFoundException("Order not found");
                });

        deleteOrder(id);

        return createOrder(orderRequestDto);
    }

    public void deleteOrder(Long id) {
        log.info("Deleting order with ID: " + id);

        orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Order not found");
                    return new NotFoundException("Order not found");
                });

        try {
            orderRepository.deleteById(id);
            log.info("Order deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting order: " + e.getMessage());
        }
    }

    public Order mapToEntity(OrderRequestDto orderRequestDto, Order order) {
        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> {
                    log.warn("User is not found");
                    return new BadRequestException("User is not found");
                });
        order.setUser(user);

        if (orderRequestDto.getProductIds().isEmpty()) {
            log.warn("There are no goods in the order");
            throw new BadRequestException("There are no goods in the order");
        }

        List<Product> products = orderRequestDto.getProductIds()
                .stream()
                .map(productId -> productRepository.findById(productId)
                        .orElseThrow(() -> {
                            log.warn("Product not found");
                            return new BadRequestException("Product not found");
                        }))
                .toList();
        order.setProducts(products);

        BigDecimal amount = products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setAmount(amount);

        return order;
    }

}
