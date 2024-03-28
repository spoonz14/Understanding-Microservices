package OrderService.Repository;

import OrderService.Entity.BookOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;

@Repository
public interface OrderRepository extends JpaRepository<BookOrder, Long> {

}
