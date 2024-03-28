package OrderService.Service;

import BookService.Entity.Book;
import OrderService.Entity.BookOrder;
import OrderService.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public boolean addBook(BookOrder order) {
        orderRepository.save(order);
        return true;
    }
    
    public List<BookOrder> getAllOrders() {
        return orderRepository.findAll();
    }
}
