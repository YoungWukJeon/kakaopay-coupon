# 쿠폰 만료일 3일전 알림 배치

## 구현한 문제

- 선택 문제
    1. 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송하는 기능을 구현하 세요. (실제 메세지를 발송하는것이 아닌 stdout 등으로 출력하시면 됩니다.) 
- coupon 테이블에서 status가 'PUBLISHED'이고 만료일(expiration_date)가 현재 시간 기준 오늘 00시~23시59분59초 사이면 조회됨
- 조회된 데이터는 Kafka Producer에 의해 Kafka Broker로 전달됨

## 개발 스펙

- 개발 언어 : Java 11 (OpenJDK 11)
- 빌드 도구 : Gradle 6.3
- 개발 프레임워크
    - 테스트 프레임워크 : JUnit5
- 개발 Database : MySQL 5.7
- 알림 발송처 : kafka:2.12-2.0.1, topic: 'notification'

## 빌드 및 실행 방법

- 루트 경로에서 gradlew로 빌드 후 결과물인 jar 파일 실행
- 빌드 명령 : `gradlew bootjar`
- 실행 명령: `java -jar coupon-expiration-notification-load.jar`