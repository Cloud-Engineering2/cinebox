# cinebox

# 영화 예매 시스템

> **React + Spring Boot + AWS + Kubernetes 기반의 영화 예매 웹 애플리케이션**  
> CI/CD 자동화 및 클라우드 인프라 구축을 통해 운영 가능한 시스템 설계 및 구현

---

## 프로젝트 개요

이 프로젝트는 **영화 예매 서비스**를 위한 웹 애플리케이션으로,  
사용자(고객)와 관리자(운영자)의 기능을 모두 포함하며,  
**AWS 기반 클라우드 인프라** 위에서 자동화된 배포 환경을 갖추고 있습니다.

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

## 기술 스택

| 분류 | 기술 |
|------|------|
| **Frontend** | React, React Router, MUI, Axios, React Calendar, React Toastify |
| **Backend** | Spring Boot 3.4.2, **JDK 17**, **JPA (ORM)**, **MySQL**, **Redis**, Maven |
| **API 연동** | 영화진흥위원회 API (KOBIS), KMDB |
| **인증/보안** | OAuth2.0, JWT (Access/Refresh Token) |
| **Infra & DevOps** | AWS (EKS, RDS, S3, Route 53, ELB, IAM, VPC), Docker, GitHub Actions, Terraform, ArgoCD, Prometheus, Grafana |

---

## 프로젝트 실행 방법

### 방법 1: 웹에서 바로 접속하기

> 배포된 프로젝트는 아래 주소에서 바로 확인할 수 있습니다.

🔗 [https://cine-box.store](https://cine-box.store)

---

### 방법 2: 로컬에서 직접 실행하기

#### 프론트엔드 (React)

```bash
# 1. 레포지토리 클론
git clone [레포지토리 주소]
cd frontend

# 2. 패키지 설치
npm install

# 3. 개발 서버 실행
npm start
```

## ERD 설계
![image](https://github.com/user-attachments/assets/39dacdf7-0757-490a-964d-62732a621b1d)

## 인프라 설계
![image](https://github.com/user-attachments/assets/4154f749-444f-48e3-9a1c-1a1691f140dd)


