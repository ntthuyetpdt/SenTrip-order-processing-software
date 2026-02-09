package com.example.da_sentrip.repository.custom;


import java.util.Map;

public interface MenuCustomRepository {
    Map<String, Object> getMenuByEmail(String email, int page, int size);
}
