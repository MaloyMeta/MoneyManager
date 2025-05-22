package ua.money_manager.MoneyManager.Transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ua.money_manager.MoneyManager.User.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class TransactionService {
    private final TransactionRepository transactionRepository;

    public List<Transaction> findAllByUser(User user){
        return transactionRepository.findAllByUser(user);
    }

    public List<Transaction> findByUserAndType(User user, TransactionTypeEnum type) {
        return transactionRepository.findByUserAndType(user, type);
    }

    public List<Transaction> findByUserAndDateBetween(User user, LocalDateTime from, LocalDateTime to){
        return transactionRepository.findByUserAndDateBetween(user, from, to);
    }

    public void deleteByIdAndUser(int id, User user){
        Transaction transaction = transactionRepository.findById((long)id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        if(!transaction.getUser().getId().equals(user.getId())){
            throw new RuntimeException("Unathorized");
        }
        transactionRepository.delete(transaction);
    }

    public List<Transaction> findLatestByUser(User user, int limit){
        Pageable pageable = PageRequest.of(0, limit, Sort.by("date").descending());
        return transactionRepository.findByUser(user, pageable);
    }

    public BigDecimal calculateBalance(User user) {
        List<Transaction> transactions = findAllByUser(user);
        BigDecimal balance = BigDecimal.ZERO;

        for(Transaction transaction : transactions){
            if(transaction.getType() == TransactionTypeEnum.INCOME){
                balance = balance.add(transaction.getAmount());
            } else {
                balance = balance.subtract(transaction.getAmount());
            }
        }
        return balance;
    }

    public Transaction save(Transaction transaction){
        return transactionRepository.save(transaction);
    }
}
