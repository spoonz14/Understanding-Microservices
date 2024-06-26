package BookService.Controller;


import BookService.Entity.Book;
import BookService.Entity.BookInfo;
import BookService.Repository.BookRepository;
import BookService.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    private RestTemplate restTemplate;

    // URL to communicate with Microservice-B aka OrderService
    private static final String EXTERNAL_SERVICE_URL = "http://localhost:8081/api/";

    // Method to communicate with the external service
    public ResponseEntity<String> callExternalService() {
        ResponseEntity<String> response = restTemplate.getForEntity(EXTERNAL_SERVICE_URL, String.class);
        // Process the response from the external service
        return response;
    }

    // Instantiations
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;

    // Post method to add a new book
    @PostMapping("/api/")
    public ResponseEntity<String> add(@RequestBody Book book) {
        if (bookService.addBook(book)) {
            return ResponseEntity.ok("Book added.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or email already exists.");
        }
    }
    // get method to see all books
    @GetMapping("/api/")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
    // Get method to see book by title
    @GetMapping("/api/{title}")
    public ResponseEntity<String> getBookByTitle(@PathVariable String title) {
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        Book book = bookService.findBookByTitle(title);

        if (book != null) {
            String responseMessage = "Book found: " + book.getTitle() + ", Stock: " + book.getStock();
            int stock = book.getStock();
            stock--;
            book.setStock(stock);
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found.");
        }
    }

    // Method to update the stock number
    @PutMapping("/api/order/{title}")
    public ResponseEntity<String> placeOrder(@PathVariable String title, @RequestBody Book updatedBook) {
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        Book existingBook = bookService.findBookByTitle(title);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        int stock = existingBook.getStock();
        if (stock == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book out of stock.");
        } else {
            stock--;
            // Update existing book
            existingBook.setStock(stock);

            // Save the changes
            bookRepository.save(existingBook);

            return ResponseEntity.ok("Order placed.");
        }
    }

    // Method to update the stock number
    @PutMapping("/api/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book existingBook = bookService.findBookById(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        // Update existing book
        existingBook.setStock(updatedBook.getStock());

        // Save the changes
        bookRepository.save(existingBook);

        return ResponseEntity.ok("Book updated.");
    }

    // Method to delete book
    @DeleteMapping("/api/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.ok("Book deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found.");
        }
    }

}
