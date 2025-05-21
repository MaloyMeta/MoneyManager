package ua.money_manager.MoneyManager.Transaction;

import jakarta.persistence.*;
import lombok.*;
import ua.money_manager.MoneyManager.User.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private BigDecimal amount;

    private String description;

    @Column(nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "type_id", nullable = false)
    private TransactionTypeEnum type;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}
