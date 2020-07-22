package com.kakaopay.coupon.expiration.load.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;

import static com.kakaopay.coupon.expiration.load.persistence.CouponEntity.Status;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CouponRepositoryImplTest {
    @InjectMocks
    private CouponRepositoryImpl couponRepository;
    @Mock
    private Connection connection;
    @Mock
    private DataSource dataSource;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    CouponEntity couponEntity =
            CouponEntity
                    .builder()
                    .no(1L)
                    .code("test-code")
                    .createdDate(LocalDateTime.now())
                    .publishedDate(LocalDateTime.now())
                    .usedDate(LocalDateTime.now())
                    .expirationDate(LocalDateTime.now())
                    .status(Status.PUBLISHED)
                    .userNo(1L)
                    .build();

    @Test
    void expire시켜야할_목록_조회() throws SQLException {
        // given
        given(dataSource.getConnection())
                .willReturn(connection);
        given(connection.prepareStatement(anyString()))
                .willReturn(preparedStatement);
        given(preparedStatement.executeQuery())
                .willReturn(resultSet);
        given(resultSet.next())
                .willReturn(true)
                .willReturn(false);
        given(resultSet.getLong("no"))
                .willReturn(1L);
        given(resultSet.getString("code"))
                .willReturn("test-code");
        given(resultSet.getTimestamp("created_date"))
                .willReturn(Timestamp.valueOf(couponEntity.getCreatedDate()));
        given(resultSet.getTimestamp("published_date"))
                .willReturn(Timestamp.valueOf(couponEntity.getPublishedDate()));
        given(resultSet.getTimestamp("used_date"))
                .willReturn(Timestamp.valueOf(couponEntity.getUsedDate()));
        given(resultSet.getTimestamp("expiration_date"))
                .willReturn(Timestamp.valueOf(couponEntity.getExpirationDate()));
        given(resultSet.getString("status"))
                .willReturn("PUBLISHED");
        given(resultSet.getLong("user_no"))
                .willReturn(1L);
        List<CouponEntity> expectedCouponEntities = List.of(couponEntity);

        // when
        List<CouponEntity> couponEntities = couponRepository.findAllByStatusAndExpirationDateBefore(Status.PUBLISHED, couponEntity.getCreatedDate());

        // then
        assertEquals(expectedCouponEntities.size(), couponEntities.size());
        IntStream.range(0, expectedCouponEntities.size())
                .forEach(i -> {
                    assertEquals(expectedCouponEntities.get(i).getNo(), expectedCouponEntities.get(i).getNo());
                    assertEquals(expectedCouponEntities.get(i).getCode(), expectedCouponEntities.get(i).getCode());
                    assertEquals(expectedCouponEntities.get(i).getCreatedDate(), expectedCouponEntities.get(i).getCreatedDate());
                    assertEquals(expectedCouponEntities.get(i).getPublishedDate(), expectedCouponEntities.get(i).getPublishedDate());
                    assertEquals(expectedCouponEntities.get(i).getUsedDate(), expectedCouponEntities.get(i).getUsedDate());
                    assertEquals(expectedCouponEntities.get(i).getExpirationDate(), expectedCouponEntities.get(i).getExpirationDate());
                    assertEquals(expectedCouponEntities.get(i).getStatus(), expectedCouponEntities.get(i).getStatus());
                    assertEquals(expectedCouponEntities.get(i).getUserNo(), expectedCouponEntities.get(i).getUserNo());
                });
    }

    @Test
    void timestamp를_LocalDateTime으로_변환() {
        LocalDateTime now = LocalDateTime.now();
        Method method = ReflectionUtils.findMethod(CouponRepositoryImpl.class, "timestampToLocalDateTime", Timestamp.class).orElseThrow();
        LocalDateTime dateTime = (LocalDateTime) ReflectionUtils.invokeMethod(method, couponRepository, Timestamp.valueOf(now));
        assertEquals(now, dateTime);
    }
}