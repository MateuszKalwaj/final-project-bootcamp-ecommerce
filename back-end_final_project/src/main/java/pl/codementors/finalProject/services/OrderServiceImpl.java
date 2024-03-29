package pl.codementors.finalProject.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.codementors.finalProject.models.Cart;
import pl.codementors.finalProject.models.LocalUser;
import pl.codementors.finalProject.models.Order;
import pl.codementors.finalProject.models.Product;
import pl.codementors.finalProject.repo.CartRepository;
import pl.codementors.finalProject.repo.LocalUserRepository;
import pl.codementors.finalProject.repo.OrderRepository;
import pl.codementors.finalProject.repo.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class OrderServiceImpl implements OrderService {



    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private LocalUserRepository localUserRepository;

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findOne(id);
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders =  StreamSupport.stream(orderRepository.findAll().spliterator(),false).collect(Collectors.toList());
        return orders;
    }

    @Override
    public Order addOrder(Long cartid, Order sentOrder) {
        //create new order
        Order order = new Order();
        Cart cart = cartRepository.findOne(cartid);
        order.setBuyer(cart.getBuyer());
        order.setValue(cart.getCartValue());
        order.setAddress(sentOrder.getAddress());
        List<Product> itemList = new ArrayList<>(cart.getProducts());
        order.setItems(itemList);
        orderRepository.save(order);
        cart.getProducts().clear();
        //clear cart after order
        cart.setCartValue(0D);
        cartRepository.save(cart);
        //set products as unavailable
        for (Product item : itemList) {
            item.setAvailable(false);
            productRepository.save(item);
        }
        return order;
    }

    @Override
    public List<Order> findByBuyer(Long id) {
        LocalUser buyer = localUserRepository.findOne(id);
        return orderRepository.findOrderByBuyer(buyer);
    }

    @Override
    public void deleteOrder (Long id) {
        orderRepository.delete(id);
    }


    public OrderServiceImpl() {
    }

}
