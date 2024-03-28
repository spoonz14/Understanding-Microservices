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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;

    @PostMapping("/api/")
    public ResponseEntity<String> add(@RequestBody Book book) {
        if (bookService.addBook(book)) {
            return ResponseEntity.ok("Book added.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or email already exists.");
        }
    }
    @GetMapping("/api/")
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/api/{title}")
    public ResponseEntity<String> getBookById(@PathVariable String title) {
        String decodedTitle = URLDecoder.decode(title, StandardCharsets.UTF_8);
        BookInfo bookInfo = bookService.lookUpBook(title);
        if (bookInfo != null) {
            String responseMessage = "Book found: " + bookInfo.getBook().getTitle() + ", Stock: " + bookInfo.getStock();
            return ResponseEntity.ok(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found.");
        }
    }

    @PutMapping("/api/order/{id}")
    public ResponseEntity<String> placeOrder(@PathVariable Long id, @RequestBody Book updatedBook) {
        Book existingBook = bookService.findBookById(id);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        int stock = existingBook.getStock();
        stock--;
        // Update existing book
        existingBook.setStock(stock);

        // Save the changes
        bookRepository.save(existingBook);

        return ResponseEntity.ok("Order placed.");
    }

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

    @DeleteMapping("/api/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if (bookService.deleteBook(id)) {
            return ResponseEntity.ok("Book deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Book not found.");
        }
    }

}
