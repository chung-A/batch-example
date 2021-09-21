package com.example.batch.config.db.interceptors;

import com.example.batch.config.utils.TimeUtils;
import com.mysql.cj.MysqlConnection;
import com.mysql.cj.Query;
import com.mysql.cj.interceptors.QueryInterceptor;
import com.mysql.cj.log.Log;
import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.protocol.ServerSession;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
public class CustomQueryInterceptor implements QueryInterceptor {

    private LocalDateTime queryStartAt;

    @Override
    public QueryInterceptor init(MysqlConnection conn, Properties props, Log log) {
        return this;
    }

    @Override
    public <T extends Resultset> T preProcess(Supplier<String> sql, Query interceptedQuery) {
        this.queryStartAt = LocalDateTime.now();
        return null;
    }

    @Override
    public boolean executeTopLevelOnly() {
        return false;
    }

    @Override
    public void destroy() {

    }

    @Override
    public <T extends Resultset> T postProcess(Supplier<String> sql, Query interceptedQuery, T originalResultSet, ServerSession serverSession) {
        long second = TimeUtils.getAverageTime(queryStartAt, LocalDateTime.now(), 1, TimeUnit.SECONDS);
        String query = (sql.get() != null) ? sql.get() : interceptedQuery.toString();

        // 지정한 시간 보다 오래 수행되었다면 잡는다!!!
        if (second > 10) {
            log.warn("slow query detected!!! query time is {}, sql query is {}", second, query);
        }
//        log.info("query time is {} (executed query = {})", second, query1);
        return null;
    }
}
