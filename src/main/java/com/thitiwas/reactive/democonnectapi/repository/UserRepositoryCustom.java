package com.thitiwas.reactive.democonnectapi.repository;

import com.thitiwas.reactive.democonnectapi.entity.UserEntity;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@Slf4j
public class UserRepositoryCustom {

    private final DatabaseClient databaseClient;

    @Autowired
    public UserRepositoryCustom(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Mono<UserEntity> findByTelno(String telno) {
        String sql = "select *\n" +
                "from user\n" +
                "where telno = :telNo";
        return databaseClient.sql(sql)
                .bind("telNo", telno)
                .map(this::mapToEntity)
                .one();

    }

    private UserEntity mapToEntity(Row row, RowMetadata rowMetadata) {
        return UserEntity.builder()
                .id((Long) row.get("id"))
                .email((String) row.get("email"))
                .loginExpired((LocalDateTime) row.get("login_expired"))
                .accessToken((String) row.get("access_token"))
                .createBy((Long) row.get("create_by"))
                .createDate((LocalDateTime) row.get("create_date"))
                .password((String) row.get("password"))
                .isConfirm(((Byte) row.get("is_confirm")).intValue() == 1)
                .isDelete(((Byte) row.get("is_delete")).intValue() == 1)
                .build();
    }
}
