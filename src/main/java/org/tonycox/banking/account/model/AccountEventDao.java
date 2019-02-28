package org.tonycox.banking.account.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AccountEventDao {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "amount")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private AccountEventType eventType;
    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Version
    private Long version;
}
