package OrderService.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@RestController
public class OrderController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String BASE_URL = "http://bookservice-hostname:port"; // Replace with actual hostname and port

    @GetMapping("/order/book/{title}")
    public ResponseEntity<String> placeOrderForBook(@PathVariable String title) {
        String bookUrl = BASE_URL + title; // Assuming BookService has an endpoint to get book details by title
        ResponseEntity<String> response = restTemplate.getForEntity(bookUrl, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            // Book found, place order logic
            return ResponseEntity.ok("Order placed successfully!");
        } else {
            // Book not found
            return ResponseEntity.notFound().build();
        }
    }
}

