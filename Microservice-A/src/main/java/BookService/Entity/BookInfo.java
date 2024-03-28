package BookService.Entity;


import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class BookInfo {
    private Book book;
//    private String title;
    private int stock;

    public BookInfo(Book book, int stock) {
        this.book = book;
//        this.title = title;
        this.stock = stock ;
    }
}
