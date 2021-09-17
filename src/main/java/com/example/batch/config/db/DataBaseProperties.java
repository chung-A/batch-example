package com.example.batch.config.db;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DataBaseProperties {
    private String driverClassName;
    private String url;
    private String userName;
    private String password;
    private boolean readOnly;
    private int minimumIdle;
    private int maximumPoolSize;
    private long timeOut;
}
