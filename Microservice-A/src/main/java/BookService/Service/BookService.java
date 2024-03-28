package BookService.Service;

import BookService.Entity.Book;
import BookService.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public boolean addBook(Book book) {
        if (bookRepository.findByTitle(book.getTitle()) != null) {
            return false;  // book already exists
        } else {
            bookRepository.save(book);
            return true;
        }
    }

    public boolean lookUpBook(Book book) {
        if (bookRepository.findByTitle(book.getTitle()) != null) {
            System.out.println(book.getTitle() + " found!");
            return true;
        } else {
            return false;
        }
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}
