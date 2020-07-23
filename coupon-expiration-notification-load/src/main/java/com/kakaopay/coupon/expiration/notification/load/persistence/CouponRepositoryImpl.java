package com.kakaopay.coupon.expiration.notification.load.persistence;

import static com.kakaopay.coupon.expiration.notification.load.persistence.CouponEntity.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CouponRepositoryImpl implements CouponRepository {
    private final DataSource dataSource;
    private final String tableName;

    public CouponRepositoryImpl(DataSource dataSource, String tableName) {
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    @Override
    public List<CouponEntity> findAllByStatusAndExpirationDateBetween(
            Status status, LocalDateTime startDate, LocalDateTime endDate) {
        String query = "SELECT * FROM " + tableName + " WHERE status = ? AND expiration_date BETWEEN ? AND ?";
        List<CouponEntity> couponEntities = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now();

        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, status.name());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(startDate));
            preparedStatement.setTimestamp(3, Timestamp.valueOf(endDate));
            System.out.println("Execution Query: " + preparedStatement.toString().replace(preparedStatement.getClass().getName() + ": ", ""));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                couponEntities.add(
                        CouponEntity
                                .builder()
                                .no(resultSet.getLong("no"))
                                .code(resultSet.getString("code"))
                                .createdDate(timestampToLocalDateTime(resultSet.getTimestamp("created_date")))
                                .publishedDate(timestampToLocalDateTime(resultSet.getTimestamp("published_date")))
                                .usedDate(timestampToLocalDateTime(resultSet.getTimestamp("used_date")))
                                .expirationDate(timestampToLocalDateTime(resultSet.getTimestamp("expiration_date")))
                                .status(Status.valueOf(resultSet.getString("status")))
                                .userNo(resultSet.getLong("user_no"))
                                .build());
            }
        } catch (SQLException exception) {
            System.out.println("Query Execution Fail");
            exception.printStackTrace();
        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Query execution takes " + ChronoUnit.MILLIS.between(start, end) +"ms");

        return couponEntities;
    }

    private LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
        return timestamp == null? null: timestamp.toLocalDateTime();
    }
}
