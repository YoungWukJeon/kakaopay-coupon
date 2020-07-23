package com.kakaopay.coupon.expiration.notification.load.persistence;

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

import static com.kakaopay.coupon.expiration.notification.load.persistence.CouponEntity.Status;

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
                    .expirationDate(LocalDateTime.now().plusDays(3L))
                    .status(Status.PUBLISHED)
                    .userNo(1L)
                    .build();

    @Test
    void 만료기간이_3일남은_쿠폰_조회() throws SQLException {
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
                .willReturn(Timestamp.valueOf(couponEntity.getCreatedDate().plusDays(3L)));
        given(resultSet.getString("status"))
                .willReturn("PUBLISHED");
        given(resultSet.getLong("user_no"))
                .willReturn(1L);
        List<CouponEntity> expectedCouponEntities = List.of(couponEntity);

        // when
        List<CouponEntity> couponEntities =
                couponRepository.findAllByStatusAndExpirationDateBetween(
                        Status.PUBLISHED, LocalDateTime.now().plusDays(3L), LocalDateTime.now().plusDays(4L));

        // then
        assertEquals(expectedCouponEntities.size(), couponEntities.size());
        IntStream.range(0, expectedCouponEntities.size())
                .forEach(i -> {
                    assertEquals(expectedCouponEntities.get(i).getNo(), couponEntities.get(i).getNo());
                    assertEquals(expectedCouponEntities.get(i).getCode(), couponEntities.get(i).getCode());
                    assertEquals(expectedCouponEntities.get(i).getCreatedDate(), couponEntities.get(i).getCreatedDate());
                    assertEquals(expectedCouponEntities.get(i).getPublishedDate(), couponEntities.get(i).getPublishedDate());
                    assertEquals(expectedCouponEntities.get(i).getUsedDate(), couponEntities.get(i).getUsedDate());
                    assertEquals(expectedCouponEntities.get(i).getStatus(), couponEntities.get(i).getStatus());
                    assertEquals(expectedCouponEntities.get(i).getUserNo(), couponEntities.get(i).getUserNo());
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