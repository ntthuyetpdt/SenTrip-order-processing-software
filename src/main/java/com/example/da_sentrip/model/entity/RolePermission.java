package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "ROLE_PERMISSION")
public class RolePermission {

    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne
    @MapsId("roleId")
    @JoinColumn(name = "ROLE_ID")
    private Role role;

    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(name = "PERMISSION_ID")
    private Permission permission;

    @Column(name = "ACTION_DESC")
    private String actionDesc;
}