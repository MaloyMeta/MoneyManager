package ua.money_manager.MoneyManager.Transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/balance")
    public BigDecimal getBalance(Principal principal){
        User user = userService.getUserFromPrincipal(principal);
        return transactionService.calculateBalance(user);
    }

    @GetMapping(value = "/all")
    public List<Transaction> getAll(Principal principal){
        User user = userService.getUserFromPrincipal(principal);
        return transactionService.findAllByUser(user);
    }

    @GetMapping("/latest")
    public List<Transaction> getLatestTransactions(Principal principal,
                                                   @RequestParam(defaultValue = "5") int limit){
        User user = userService.getUserFromPrincipal(principal);
        return transactionService.findLatestByUser(user, limit);
    }

    @PostMapping(value = "/add")
    public Transaction add(@RequestBody Transaction transaction, Principal principal){
        System.out.println("Principal name: " + principal.getName());
        User user = userService.getUserFromPrincipal(principal);
        System.out.println("User from principal: " + user);
        transaction.setUser(user);
        return transactionService.save(transaction);
    }
    @PostMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id, Principal principal){
        User user = userService.getUserFromPrincipal(principal);
        transactionService.deleteByIdAndUser(id,user);
        return ResponseEntity.noContent().build();
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
        return transactionService.findByUserAndDateBetween(user, from, to);
    }


}
