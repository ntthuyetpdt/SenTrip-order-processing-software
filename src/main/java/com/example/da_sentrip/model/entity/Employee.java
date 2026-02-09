package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "EMPLOYEES")
public class Employee extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", unique = true)
    private User user;

    @Column(name = "MNV")
    private String mnv;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name = "PHONE")
    private String phone;

    @Column(name="GENDER")
    private String gender;

    @Column(name="DATE")
    private LocalDateTime dateTime;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name ="JOIN_DATE")
    private LocalDateTime JoinDate;

    @Column(name ="CCCD")
    private String cccd;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name ="ACCOUNT_BANK")
    private String AccountBank;

}
