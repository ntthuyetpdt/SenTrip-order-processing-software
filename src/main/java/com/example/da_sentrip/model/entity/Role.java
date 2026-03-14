package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "DIM_ROLE")
public class Role {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "ROLE_CODE")
    private Integer roleCode;

    @Column(name = "ROLE_NAME")
    private String roleName;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER)
    private List<RolePermission> rolePermissions = new ArrayList<>();
}