package OrderService.Controller;
import BookService.Entity.Book;
import OrderService.Entity.BookOrder;
import OrderService.Repository.OrderRepository;
import OrderService.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
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

    // Method to view list of books in BookService
    @GetMapping("book/")
    public ResponseEntity<List<Book>> getAllBooks() {
        String bookServiceUrl = BASE_URL;
        ResponseEntity<List<BookService.Entity.Book>> response = restTemplate.exchange(
                bookServiceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {}
        );

        // Check if the response is successful
        if (response.getStatusCode().is2xxSuccessful()) {
            List<BookService.Entity.Book> books = response.getBody();
            return ResponseEntity.ok(books);
        } else {
            // Handle error if the request to BookService fails
            return ResponseEntity.status(response.getStatusCode()).body(null);
        }
    }

    @GetMapping("book/order/{title}")
    public ResponseEntity<String> placeOrderForBook(@PathVariable String title) {
        String bookUrl = BASE_URL + title;
        ResponseEntity<String> response = restTemplate.getForEntity(bookUrl, String.class);
        if (response.getStatusCode().is2xxSuccessful()) { // Book found

            // New order object
            BookOrder order = new BookOrder();
            order.setBookTitle(title); // Set title

            LocalDateTime currentTime = LocalDateTime.now(); // Using localdatetime module to get the return date


            // getting the month for the return date
            int month = currentTime.getMonthValue();
            String monthString = Integer.toString(month);

            // Return date will always be 2 weeks from time of order, therefore we are creating conditional statements to handle times where
            // the currrent day value + 14 will exceed the amount of days in that month
            int day = currentTime.getDayOfMonth();
            if (month == 1|| month == 3 || month == 5 || month == 7 || month == 8 || month == 10|| month == 12) {
                if (day <= 17) {
                    day = day + 14;  // Return time is always 2 weeks from now
                    String dayString = Integer.toString(day);
                } else {
                    month++;
                    day = day - 17;
                    String dayString = Integer.toString(day);
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                if (day <= 16) {
                    day = day + 14;
                    String dayString = Integer.toString(day);
                } else {
                    month++;
                    day = day - 16;
                    String dayString = Integer.toString(day);
                }
            } else  {
                if (day <= 14) {
                    day = day + 14;
                    String dayString = Integer.toString(day);
                } else {
                    month++;
                    day = day - 14;
                    String dayString = Integer.toString(day);
                }
            }

            // Getting the year for the return date
            int year = currentTime.getYear();
            String yearString = Integer.toString(year);
            // Save the return date
            order.setReturnDate(day + "/" + month + "/" + year);

            orderRepository.save(order); // Save the order
            return ResponseEntity.ok("Order placed successfully!");
        } else {
            // Book not found
            return ResponseEntity.notFound().build();
        }
    }

    // Method to view list of orders
    @GetMapping("book/order/")
    public ResponseEntity<List<BookOrder>> getAllOrders() {
        List<BookOrder> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}

