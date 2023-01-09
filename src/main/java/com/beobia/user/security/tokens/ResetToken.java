package com.beobia.user.security.tokens;

import com.beobia.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ResetToken {

    @SequenceGenerator(
            name = "reset_token_sequence",
            sequenceName = "reset_token_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "reset_token_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private com.beobia.user.entity.User user;
    private LocalDateTime usedAt;

    public ResetToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, LocalDateTime confirmedAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.usedAt = confirmedAt;
        this.user = user;
    }
}