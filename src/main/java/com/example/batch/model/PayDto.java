package com.example.batch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import java.time.LocalDateTime;

@ToString
public class PayDto {

    private Long id;
    private Long amount;
    private String txName;
    private LocalDateTime txDateTime;

}
