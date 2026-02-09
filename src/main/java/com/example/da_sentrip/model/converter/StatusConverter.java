package com.example.da_sentrip.model.converter;

import com.example.da_sentrip.model.enums.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusConverter implements AttributeConverter<Status, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Status status) {
        return status == null ? null : status == Status.ACTIVE ? 1 : 0;
    }

    @Override
    public Status convertToEntityAttribute(Integer value) {
        return value != null && value == 1
                ? Status.ACTIVE
                : Status.INACTIVE;
    }
}
