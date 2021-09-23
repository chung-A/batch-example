package com.example.batch.reader.jdbc;

import com.example.batch.model.Pay;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PayRowMapper implements RowMapper<Pay> {

    @Override
    public Pay mapRow(ResultSet rs, int rowNum) throws SQLException {
        Pay pay = new Pay(
                rs.getLong("id"),
                rs.getLong("amount"),
                rs.getString("tx_name"),
                rs.getString("tx_date_time")
        );

        return pay;
    }
}
