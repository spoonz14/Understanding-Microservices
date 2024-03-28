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

    // method to save a book to the database
    public boolean addBook(Book book) {
        if (bookRepository.findByTitle(book.getTitle()) != null) {
            return false;  // book already exists
        } else {
            bookRepository.save(book);
            return true;
        }
    }

    // method to look up a book by its title, and return the number left in stock
    public BookInfo lookUpBook(String title) {
        Book book = bookRepository.findByTitle(title);
        if (book != null) {
            int stock = book.getStock(); // fetch the stock from the Book entity
            return new BookInfo(book, stock);
        } else {
            return null;
        }
    }

    // method to find a book by its title without returning the stock
    public Book findBookByTitle(String title) {
        Book book = bookRepository.findByTitle(title);
        return book;
    }

    // method to look up book by id
    public Book findBookById(Long id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return (Book) optionalBook.orElse(null);
    }

    // display all books
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // delete books
    public boolean deleteBook(Long id) {
        bookRepository.deleteById(id);
        return true;
    }
}
