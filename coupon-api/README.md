# 쿠폰 API

## 구현한 기능

- 필수 문제
    1. 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API를 구현하세요.
        - input: N
        - API
            - GET /coupon/creation (단일 쿠폰 생성)
            - GET /coupon/creation/count/{count} (N개의 쿠폰 생성, N은 INTEGER)
        - Authority : ADMIN
    2. 생성된 쿠폰중 하나를 사용자에게 지급하는 API를 구현하세요.
        - output: 쿠폰번호(XXXXX-XXXXXX-XXXXXXXX)
        - API
            - PUT /coupon/user/{userNo} (userNo는 Long)
        - Authority : ADMIN
    3. 사용자에게 지급된 쿠폰을 조회하는 API를 구현하세요.
        - 문제에 명확히 기술되어 있지 않아서 특정 userNo를 통해 해당 사용자의 모든 쿠폰을 조회
        - API
            - GET /coupon/user/{userNo} (userNo는 Long)
        - Authority : ADMIN
    4. 지급된 쿠폰중 하나를 사용하는 API를 구현하세요. (쿠폰 재사용은 불가)
        - input : 쿠폰번호
        - API
            - PUT /coupon/{code}/status/{status} (code는 String, status는 'USED', status는 대문자로만 입력해야 함)
        - Authority : USER
            - JWT의 userNo와 해당 쿠폰의 userNo가 일치해야 함
    5. 지급된 쿠폰중 하나를 사용 취소하는 API를 구현하세요. (취소된 쿠폰 재사용 가능)
        - input : 쿠폰번호
        - API
            - PUT /coupon/{code}/status/{status} (code는 String, status는 'PUBLISHED', status는 대문자로만 입력해야 함)
        - Authority : USER
            - JWT의 userNo와 해당 쿠폰의 userNo가 일치해야 함
    6. 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회하는 API를 구현하세요.
        - API
            - GET /coupon/today/expiration
        - Authority : ADMIN
        
- 제약 사항(선택)
    - API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능을 개발하고 각 API 호출 시에 HTTP Header에 발급받은 토큰을 가지고 호출하세요.
    1. signup 계정생성 API: ID, PW를 입력 받아 내부 DB에 계정을 저장하고 토큰을 생성하여 출력한다.
        - 단, 패스워드는 안전한 방법으로 저장한다.
        - API
            - POST /auth/signup (Request Body에 application/json 형태로 id와 password를 입력)
            ```json
            {
                "id": "string",
                "password": "string"
            }
            ```
        - 임의로 ADMIN 권한을 가진 사용자를 만들게 하기 위해 QueryString에 'admin=true'를 줄 수 있다. (기본 값이 false)
            - POST /auth/signup?admin=true

    2. signin 로그인 API: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급한다.
        - API
            - POST /auth/signin (Request Body에 application/json 형태로 id와 password를 입력)
            ```json
            {
                "id": "string",
                "password": "string"
            }
            ```

## 개발 스펙

- 개발 언어 : Java 11 (OpenJDK 11)
- 빌드 도구 : Gradle 6.3
- 개발 프레임워크 : Spring Boot 2.3.1
    - Spring 관련 의존
        - Spring Data JPA
        - Spring Web(MVC)
        - Spring Doc OpenAPI
        - Spring Security
    - 테스트 프레임워크 : JUnit5
- local 환경 Database : H2
- dev 환경 Database : MySQL 5.7

## 빌드 및 실행 방법

- 루트 경로에서 gradlew로 빌드 후 결과물인 jar 파일 실행
- 빌드 명령 : `gradlew bootjar`
- local 환경 실행 명령 : `java -Dspring.profiles.active=local -Duser.timezone=Asia/Seoul -jar coupon-api.jar`
- dev 환경 실행 명령: `java -Dspring.profiles.active=dev -Duser.timezone=Asia/Seoul -jar coupon-api.jar`

## 개발 관련 테스트 링크

- H2-console(local 환경만 가능) : [localhost:8080/h2-console](http://localhost:8080/h2-console)
- OpenAPI : [localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- health 체크 : [localhost:8080/health](http://localhost:8080/health)

## 기능 테스트를 위한 사용자(user) 데이터
```
USER 권한 계정 : (no -> 1, id -> test, password -> pass)
ADMIN 권한 계정 : (no -> 2, id -> admin, password -> pass)
```

## Coupon API 사용할 때

- Request Header의 Authorization에 Bearer 'token' 형태를 추가해서 사용