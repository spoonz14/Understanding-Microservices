package OrderService.Controller;
import OrderService.Entity.BookOrder;
import OrderService.Repository.OrderRepository;
import OrderService.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;
    private static final String BASE_URL = "http://localhost:8090/api/";

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("book/order/{title}")
    public ResponseEntity<String> placeOrderForBook(@PathVariable String title) {
        String bookUrl = BASE_URL + title; // Assuming BookService has an endpoint to get book details by title
        ResponseEntity<String> response = restTemplate.getForEntity(bookUrl, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            // Book found, place order logic
            BookOrder order = new BookOrder();
            order.setBookTitle(title);
            order.setReturnDate("07/21/2024");
            orderRepository.save(order);
            return ResponseEntity.ok("Order placed successfully!");
        } else {
            // Book not found
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("book/order/")
    public ResponseEntity<List<BookOrder>> getAllOrders() {
        List<BookOrder> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}

