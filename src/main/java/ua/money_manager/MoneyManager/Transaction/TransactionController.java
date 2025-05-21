package ua.money_manager.MoneyManager.Transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ua.money_manager.MoneyManager.User.User;
import ua.money_manager.MoneyManager.User.UserService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;

    @GetMapping
    public List<Transaction> getAll(Principal principal){
        User user = userService.getUserFromPrincipal(principal);
        return transactionService.findAllByUser(user);
    }

    @PostMapping
    public Transaction add(@RequestBody Transaction transaction, Principal principal){
        User user = userService.getUserFromPrincipal(principal);
        transaction.setUser(user);
        return transactionService.save(transaction);
    }

    @GetMapping(value = "/type/{type}")
    public List<Transaction> getByType(@PathVariable TransactionTypeEnum type, Principal principal){
        User user = userService.getUserFromPrincipal(principal);
        return transactionService.findByUserAndType(user, type);
    }

    @GetMapping(value = "/filter")
    public List<Transaction> getByDate(
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to,
            Principal principal
            ){
        User user = userService.getUserFromPrincipal(principal);
        return transactionService.findByUserAndDateBefore(user, from, to);
    }

    @GetMapping(value = "/balance")
    public BigDecimal getBalance(Principal principal){
        User user = userService.getUserFromPrincipal(principal);
        return transactionService.calculateBalance(user);
    }
}
