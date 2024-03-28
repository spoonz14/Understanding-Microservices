package BookService.Controller;


import BookService.Entity.Book;
import BookService.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/api/add")
    public ResponseEntity<String> add(@RequestBody Book book) {
        if (bookService.addBook(book)) {
            return ResponseEntity.ok("Book added.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username or email already exists.");
        }
    }

    @GetMapping("/api/all")
    public ResponseEntity<List<Book>> getAllUsers() {
        List<Book> users = bookService.getAllBooks();
        return ResponseEntity.ok(users);
    }

}
