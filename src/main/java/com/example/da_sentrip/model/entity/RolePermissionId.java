package com.example.da_sentrip.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class RolePermissionId implements Serializable {

    @Column(name = "ROLE_ID")
    private Long roleId;

    @Column(name = "PERMISSION_ID")
    private Long permissionId;
}