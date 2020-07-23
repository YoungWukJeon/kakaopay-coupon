# 쿠폰 서비스

## 요구 사항(목적)

- 사용자에게 할인, 선물 등 쿠폰을 제공하는 서비스를 개발
- 기능 명세에 대한 개발 및 테스트

## 기능 명세

- 필수 [상세내용](https://github.com/YoungWukJeon/kakaopay-coupon/tree/master/coupon-api)
    - [x] 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API를 구현
        - input: N
    - [x] 생성된 쿠폰 중 하나를 사용자에게 지급하는 API를 구현
        - output: 쿠폰 번호(XXXXX-XXXXXX-XXXXXXXX)
    - [x] 사용자에게 지급된 쿠폰을 조회하는 API를 구현
    - [x] 지급된 쿠폰 중 하나를 사용하는 API를 구현(쿠폰 재사용은 불가)
        - input : 쿠폰 번호
    - [x] 지급된 쿠폰 중 하나를 사용 취소하는 API를 구현(취소된 쿠폰 재사용 가능)
        - input : 쿠폰 번호
    - [x] 발급된 쿠폰 중 당일 만료된 전체 쿠폰 목록을 조회하는 API를 구현

- 선택 [상세내용](https://github.com/YoungWukJeon/kakaopay-coupon/tree/master/coupon-expiration-notification-load)
    - [x] 발급된 쿠폰 중 만료 3일전 사용자에게 메세지("쿠폰이 3일 후 만료됩니다.")를 발송하는 기능을 구현
        - 실제로 메세지를 발송하는 것이 아닌 stdout 등으로 출력

## 제약 사항

- 필수
    - [x] 기능 명세에 제시한 기능 개발
    - [x] 단위 테스트(Unit Test) 코드를 개발하여 각 기능을 검증
    - [x] 프로그래밍 언어는 자유롭게 선택
    - [x] 각 API의 HTTP Method들(GET | POST | PUT | DEL)은 자유롭게 선택
    - [x] README.md 파일을 추가하여, 개발 프레임워크, 문제해결 전략, 빌드 및 실행 방법을 기술
- 선택 [상세내용](https://github.com/YoungWukJeon/kakaopay-coupon/tree/master/coupon-api)
    - [x] API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능을 개발하고 각 API 호출시에 HTTP Header에 발급된 토큰을 가지고 호출
        - signup 계정 생성 API : ID, PW를 입력 받아 내부 DB에 계정을 저장하고 토큰을 생성하여 출력
            - 단, 패스워드는 안전한 방법으로 저장
        - signin 로그인 API : 입력으로 생성된 계정(ID, PW)으로 로그인 요청하면 토큰을 발급
    - [ ] 100억개 이상 쿠폰 저장 관리 가능하도록 구현
    - [ ] 10만개 이상 벌크 csv Import 기능
    - [ ] 대용량 트래픽(TPS 10K 이상)을 고려한 시스템 구현
    - [ ] 성능 테스트 결과 / 피드백

## 실행 방법

- 현재 Repository의 루트 경로에서 deploy 디렉터리 안에 들어간다.
- local 환경에서 api를 테스트한다면 
    - `java -Dspring.profiles.active=local -Duser.timezone=Asia/Seoul -jar coupon-api.jar` 실행
- dev 환경에서 api를 테스트한다면 
    - `docker-compose up -d`를 통해 mysql을 띄움
    - `java -Dspring.profiles.active=dev -Duser.timezone=Asia/Seoul -jar coupon-api.jar` 실행
- 기존 계정을 쓰던 새로운 계정을 만들던 한다.
- 여러 api를 테스트
    - 쿠폰은 생성을 하고 사용자에게 지급을 해야 만료일이 생긴다.
    - 현재 설정된 만료일은 지급일 기준 +3일
- 알림 발송 배치를 실행 (`java -jar coupon-expiration-notification-load.jar`)
    - 현재 날짜 기준으로 3일 후에 만료되는 쿠폰에 대해서 조회된다. 
    - 과제에서는 만료일과 알림일이 둘 다 3일로 지정했기 때문에 바로 조회가 된다.
- 만료일이 지났지만 아직 PUBLISHED 상태인 쿠폰을 EXPIRED로 바꿔주는 배치를 실행 (`java -jar coupon-expiration-load.jar`)
    - 과제에는 없는 내용
    - 데이터베이스에 임의로 데이터를 넣지 않는 이상 테스트하기 힘들다.

## 문제 해결 전략

- 랜덤한 쿠폰 번호 생성(추후 생성 전략 변경 고려)
    - `org.apache.commons:commons-lang3:3.4` 라이브러리를 이용해서 RandomString을 생성
    - 형식
        - 영문 대소문자, 숫자를 이용해 4자리 RandomString을 생성하고 이 과정을 5번 반복해서 dash("-")가 포함된 24자리 문자열 생성
        - OOOO-OOOO-OOOO-OOOO-OOOO
            - ex. Asdf-12da-CCaa-2Ada-zad2
    - 영문 대문자(26개) + 영문 소문자(26개) + 숫자(0~9, 10개)로 총 62개로 중복을 허용한 4자리 문자열을 만들면 `62 * 62 * 62 * 62 = 14,776,336` 경우의 문자열을 생성 가능
- 데이터베이스에 저장된 쿠폰에 Status 컬럼을 넣어서 상태 값에 따른 처리를 할 수 있도록 제어
    - Status 종류
        - **CREATED** : 쿠폰이 최초 생성됨
        - **PUBLISHED** : 쿠폰이 특정 사용자에게 지급됨(만료일이 지정됨)
        - **USED** : 쿠폰이 사용됨(재사용 불가, 취소하면 재사용 가능)
        - **EXPIRED** : 쿠폰이 사용되지 않고 만료됨(배치로 EXPIRED 상태로 만들 예정)
- 만료 3일전 알림 발송
    - 배치를 통해 데이터베이스에서 알림 대상이 되는 데이터를 조회한 후 Kafka Broker에 전송 (과제에서는 여기까지 구현)
    - 이 후 시나리오는 알림 발송기에서 Kafka Broker를 consume해서 사용자들에게 알림을 발송하는 것이다.

## 미해결 문제에 대한 고민들

- 100억개 이상 쿠폰 저장 관리 가능하도록 구현
    - 데이터가 정형적이고 영속성이 중요하기 때문에 관계형 데이터베이스를 과제 해결에서는 사용했다.
    - 하지만 row가 100억 개 이상이 된다고 하면 심각한 성능 저하가 예상된다.
    - 검색 속도와 영속성을 고려해서 메모리 기반의 데이터베이스 중 redis를 사용하는 것도 괜찮아 보인다.
- 10만개 이상 벌크 csv import 기술
    - 실시간 api로 벌크 import를 한다면 api 서버에 부하가 클 것으로 예상됨
    - 10만개 이상을 한번에 넣는 작업은 데이터베이스에도 부하가 클 수 있다.
        - 쿠폰은 삽입은 입력 순서가 중요한 작업이라고 생각되지 않기 때문에 여러 덩어리(ex. 1만개)로 나눠서 비동기 작업(CompletableFuture 등)을 최대한 활용하는게 좋아 보임.
    - 단순히 쿠폰 code만 csv 파일에 있다면 상관없지만 다른 부수적인 데이터가 들어있다면 정합성을 고려해야 한다.
        - coupon의 no는 기본키고 coupon의 code는 unique이기 때문에 data 충돌에 대해 전체 실패로 할 것인지 성공한 row와 실패한 row를 구분해야할지 고민이 필요해 보인다.
- 대용량 트래픽(TPS 10K 이상)을 고려한 시스템 구현
    - 서버를 수를 늘려서 적절한 부하 분산을 통해 트래픽을 줄일 수 있다.
    - 과제로 구현한 api는 어느 정도의 트래픽을 처리할 수 있을지 모른다.
    - 성능 테스트를 통해 고려해볼 상황이다.

## 고려 사항

- findByCode로 Database에서 해당 코드를 조회할 때, 해당 Entity의 Status가 PUBLISHED 일 경우에 ExpirationDate를 조회해서 만료일이 조회한 시점에 지났을 경우 Status를 EXPIRED로 변경
    - 이렇게 하면 배치로 EXPIRED로 만들 row가 줄어들 것으로 예상됨
- 충분한 성능 테스트를 진행하지 않았기 때문에 시스템 설계에 대해 다시 한 번 생각해 볼 필요가 있어 보인다.

## 개선 사항

- Open API에서도 Authorization이 적용 가능하도록 수정
- 몇 군데 빠진 테스트 코드
- 코드 리팩토링 (todo에 기재됨)
- JWT에 UserDto를 넣기 (현재는 userNo만 subject로 들어감)