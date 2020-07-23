# 쿠폰 만료 상태 변경 배치

## 내용

- coupon 테이블에서 현재 시간 기준으로 만료 일자(expiration_date)가 지난 row에 대해 status를 'EXPIRED'로 변경하는 배치
- crontab이나 scheduler 등을 사용해서 주기적으로 실행
    - 이 프로젝트 내에서는 수동으로 실행해야 함(schedule이 안 걸려있음)

## 개발 스펙

- 개발 언어 : Java 11 (OpenJDK 11)
- 빌드 도구 : Gradle 6.3
    - 테스트 프레임워크 : JUnit5
- 개발 Database : MySQL 5.7

## 빌드 및 실행 방법

- 루트 경로에서 gradlew로 빌드 후 결과물인 jar 파일 실행
- 빌드 명령 : `gradlew jar`
- 실행 명령: `java -jar coupon-expiration-load.jar`
