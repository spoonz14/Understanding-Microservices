package BookService.Service;

import BookService.Entity.Book;
import BookService.Entity.BookInfo;
import BookService.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public BookInfo lookUpBook(String title) {
        Book book = bookRepository.findByTitle(title);
        if (book != null) {
            int stock = book.getStock(); // fetch the stock from the Book entity
            return new BookInfo(book, stock);
        } else {
            return null;
        }
    }

    public Book findBookByTitle(String title) {
        Book book = bookRepository.findByTitle(title);
        return book;
    }

    public Book findBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return (Book) optionalBook.orElse(null);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public boolean deleteBook(Long id) {
        bookRepository.deleteById(id);
        return true;
    }
}
