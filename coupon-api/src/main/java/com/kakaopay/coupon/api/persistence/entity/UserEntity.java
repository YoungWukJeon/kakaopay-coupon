package com.kakaopay.coupon.api.persistence.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(name = "unique_id", columnNames = "id")
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Long no;

    @Column(name = "id", length = 20, nullable = false, unique = true)
    private String id;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "salt", length = 40, nullable = false)
    private String salt;

    @Builder
    public UserEntity(Long no, String id, String password, String salt) {
        this.no = no;
        this.id = id;
        this.password = password;
        this.salt = salt;
    }
}
