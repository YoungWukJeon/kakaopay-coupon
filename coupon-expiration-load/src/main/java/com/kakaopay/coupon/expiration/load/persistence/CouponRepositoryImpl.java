package com.kakaopay.coupon.expiration.load.persistence;

import static com.kakaopay.coupon.expiration.load.persistence.CouponEntity.Status;

import java.sql.*;
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
    public List<CouponEntity> findAllByStatusAndExpirationDateBefore(Status status, LocalDateTime now) {
        String query = "SELECT * FROM " + tableName + " WHERE status = ? and expiration_date <= ?";
        System.out.println("Execution Query: SELECT * FROM " + tableName +
                " WHERE status = '" + status + "' AND expiration_date <= '" + Timestamp.valueOf(now) + "'");
        List<CouponEntity> couponEntities = new ArrayList<> ();
        LocalDateTime start = LocalDateTime.now();

        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, status.name());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(now));
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

    @Override
    public Integer updateAllByStatusAndExpirationDateBefore(Status afterStatus, Status beforeStatus, LocalDateTime now) {
        String query = "UPDATE " + tableName + " SET status = ? WHERE status = ? and expiration_date <= ?";
        System.out.println("Execution Query: UPDATE " + tableName + " SET status = '" + afterStatus +
                "' WHERE status = '" + beforeStatus + "' AND expiration_date <= '" + Timestamp.valueOf(now) + "'");
        LocalDateTime start = LocalDateTime.now();
        int updateCount = 0;

        try {
            PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement(query);
            preparedStatement.setString(1, afterStatus.name());
            preparedStatement.setString(2, beforeStatus.name());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(now));
            updateCount = preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            System.out.println("Query Execution Fail");
            exception.printStackTrace();
        }
        LocalDateTime end = LocalDateTime.now();
        System.out.println("Query execution takes " + ChronoUnit.MILLIS.between(start, end) +"ms");
        return updateCount;
    }

    private LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
        return timestamp == null? null: timestamp.toLocalDateTime();
    }
}
