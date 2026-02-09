package com.example.da_sentrip.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "data_source")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Lob
    @Column(name = "DATA")
    private byte[] data;

    @Column(name = "IMAGE_URL")
    private String imageUrl;

    @PostPersist
    private void onPostPersist() {
        if (this.imageUrl == null) {
            this.imageUrl = "/api/main/public/image/" + this.id;
        }
    }
}
