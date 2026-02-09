package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "CUSTOMERS")
public class Customer extends  BaseCreatedEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", unique = true)
    private User user;

    @Column(name ="MKH")
    private String MaKH;

    @Column(name = "FULL_NAME")
    private String fullName;

    @Column(name ="DATE")
    private LocalDateTime dateTime;

    @Column(name = "PHONE")
    private String phone;

    @Column (name ="ADDRESS")
    private String address;

}
