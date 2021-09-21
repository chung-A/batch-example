package com.example.batch;

import com.example.batch.db.test.tables.Pay;
import com.example.batch.model.PayDto;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jooq.JooqTest;

import java.util.List;

import static org.jooq.impl.DSL.asterisk;

@JooqTest
class BatchApplicationTests {

    @Autowired
    private DSLContext dslContext;

    @Test
    void printSQL() {
        List<PayDto> list = dslContext.select(asterisk())
                .from(Pay.PAY.as("today"))
                .offset(0)
                .limit(10)
                .fetch().into(PayDto.class);

        for (PayDto pay : list) {
            System.out.println("pay = " + pay);
        }
    }

}
