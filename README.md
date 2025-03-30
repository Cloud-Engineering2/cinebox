# cinebox

# 영화 예매 시스템

> **React + Spring Boot + AWS + Kubernetes 기반의 영화 예매 웹 애플리케이션**  
> CI/CD 자동화 및 클라우드 인프라 구축을 통해 운영 가능한 시스템 설계 및 구현

---

## 프로젝트 개요

이 프로젝트는 **영화 예매 서비스**를 위한 웹 애플리케이션으로,  
사용자(고객)와 관리자(운영자)의 기능을 모두 포함하며,  
AWS 기반 클라우드 인프라 위에서 자동화된 배포 환경을 갖추고 있습니다.

### 주요 목표
- 실제 상영 서비스처럼 영화 등록, 상영 시간 설정, 예매/결제 기능 제공
- REST API 기반 프론트/백 분리 구조
- CI/CD 자동화 및 GitOps 배포 실습

---

## 주요 기능

### 사용자 기능
- 영화 목록 조회
- 날짜별 상영 시간 선택
- 좌석 선택 및 예매
- 결제 연동
- 리뷰 작성 및 마이페이지 조회

### 관리자 기능
- 영화 등록/수정
- 상영관 및 상영 정보 설정
- 예매 내역 관리

---

## 인프라 설계
![image](https://github.com/user-attachments/assets/6cddcde4-b900-410e-a724-e2da8e2257e2)

## ERD 설계
![image](https://github.com/user-attachments/assets/39dacdf7-0757-490a-964d-62732a621b1d)

---

## 기술 스펙

- **Framework**: Spring Boot 3.4.2
- **Runtime**: JDK 17
- **ORM**: JPA
- **Library**: Maven
- **Database**: MySQL 8.0.4, Redis 7.1
- **API**: 영화진흥위원회(KOBIS), KMDB

---

## 프로젝트 실행 방법

해당 프로젝트를 추가로 개발 혹은 실행시켜보고 싶으신 경우 아래의 절차에 따라 진행해주세요

#### 1. 설정 파일 생성

```bash
touch ./src/main/resources/application.properties
```

먼저 `src/main/resources/application.properties` 파일을 아래 예시를 참고하여 작성해야 합니다.  
→ 민감한 정보(API 키, DB 비밀번호 등)는 실제 값으로 교체해주세요.  

[application.properties 예시](#applicationproperties-예시)

```properties
spring.application.name={ YOUR_APPLICATION_NAME }

# Primary DataSource
spring.datasource.primary.hikari.type=com.zaxxer.hikari.HikariDataSource
spring.datasource.primary.hikari.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.primary.hikari.username={ YOUR_DB_USERNAME }
spring.datasource.primary.hikari.password={ YOUR_DB_PASSWORD }
spring.datasource.primary.hikari.jdbc-url=jdbc:mysql://{YOUR_DB_HOST}:{YOUR_DB_PORT}/{YOUR_DB_NAME}

# Secondary DataSource
spring.datasource.secondary.hikari.type=com.zaxxer.hikari.HikariDataSource
spring.datasource.secondary.hikari.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.secondary.hikari.username={ YOUR_DB_USERNAME }
spring.datasource.secondary.hikari.password={ YOUR_DB_PASSWORD }
spring.datasource.secondary.hikari.jdbc-url=jdbc:mysql://{YOUR_DB_HOST}:{YOUR_DB_PORT}/{YOUR_DB_NAME}

# 공통 Hikari 설정
spring.datasource.hikari.pool-name=Hikari
spring.datasource.hikari.auto-commit=false

# Redis
spring.data.redis.host={ YOUR_REDIS_HOST }
spring.data.redis.port={ YOUR_REDIS_PORT }

# JPA
spring.jpa.generate-ddl=false
spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=false

# Multipart 설정
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=20MB

# AWS S3 설정
cloud.aws.credentials.access-key={ YOUR_AWS_CREDENTIALS_KEY }
cloud.aws.credentials.secret-key={ YOUR_AWS_SECRET_KEY }
cloud.aws.s3.bucket={ YOUR_S3_BUCKET_NAME }
cloud.aws.region.static={ YOUR_S3_REGION }
cloud.aws.stack.auto=false

# JWT 설정
security.jwt.secretkey=your-secret-key
security.jwt.accessTokenValidityInMilliseconds=3600000
security.jwt.refreshTokenValidityInMilliseconds=604800000

# Kakao 로그인 연동
spring.kakao.auth.client={ YOUR_KAKAO_AUTH_KEY }
spring.kakao.auth.redirect={ YOUR_REDIRECT_URL }

# Cookie Domain 설정
## If your env is localhost, it would be empty.
domain={ YOUR_DOMAIN }

# Spring Boot Actuator
management.endpoints.web.exposure.include=prometheus,health,metrics,info

# Logback
logging.config=classpath:logback-spring.xml
```

---

#### 2. 프로젝트 실행

```bash
./mvnw spring-boot:run
```
