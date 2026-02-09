package com.example.da_sentrip.model.entity;

import com.example.da_sentrip.model.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "GMAIL")
    private String gmail;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE", nullable = false)
    private Role role;

    @Column(name = "STATUS")
    private Status status;

}
