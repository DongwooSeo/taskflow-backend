package com.growmighty.taskflow.domain.auth.entity;

import com.growmighty.taskflow.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    private boolean enabled = true;

    public static User createUser(String username, String email, String password, String name) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.name = name;
        user.role = Role.USER;
        user.enabled = true;
        return user;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateName(String newName) {
        this.name = newName;
    }
} 