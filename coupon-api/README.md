# 쿠폰 API

## 개발 스펙

- 개발 언어 : Java 11 (OpenJDK 11)
- 빌드 도구 : Gradle 6.3
- 개발 프레임워크 : Spring Boot 2.3.1
    - Spring 관련 의존
        - Spring Data JPA
        - Spring Web(MVC)
        - Spring Doc OpenAPI
    - 테스트 프레임워크 : JUnit5
- 로컬 개발 Database : H2

## 빌드 및 실행 방법

- 루트 경로에서 gradlew로 빌드 후 결과물인 jar 파일 실행
- 빌드 명령 : `gradlew bootjar`
- 실행 명령: `java -jar coupon-api.jar`

## 개발 관련 테스트 링크

- H2-console : [localhost:8080/h2-console](http://localhost:8080/h2-console)
- OpenAPI : [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)