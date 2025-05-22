package ua.money_manager.MoneyManager.Transaction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.money_manager.MoneyManager.User.User;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);
    List<Transaction> findByUserAndType(User user, TransactionTypeEnum type);
    List<Transaction> findByUserAndDateBetween(User user, LocalDateTime from, LocalDateTime to);
    List<Transaction> findByUser(User user, Pageable pageable);
}
