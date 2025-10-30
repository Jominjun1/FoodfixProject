<div align="center">

![FoodfixProject Logo](https://github.com/Jominjun1/FoodfixProject/assets/116476333/6618c55f-f7af-4a78-a6a7-c91459636c78)

# 🍽️ FoodfixProject

**2024 Capstone Project - 웹 부문 장려상**  
포장/예약 주문을 보다 편리하게, 효율적인 매장 관리를 위한 웹/앱 서비스

[![Platform](https://img.shields.io/badge/Platform-Web%20%7C%20iOS%20%7C%20Android-blue)](https://github.com/Jominjun1/FoodfixProject)
[![Tech Stack](https://img.shields.io/badge/Tech%20Stack-React%20%7C%20Spring%20Boot%20%7C%20Swift%20%7C%20Kotlin-green)](https://github.com/Jominjun1/FoodfixProject)
[![License](https://img.shields.io/badge/License-MIT-yellow)](https://github.com/Jominjun1/FoodfixProject)

<img src="https://github.com/Jominjun1/FoodfixProject/assets/116476333/e626defd-a8d1-4025-9f72-37d309fba5e8" width="400" height="600" alt="FoodfixProject Demo">

</div>

## 📋 목차
- [프로젝트 개요](#프로젝트-개요)
- [팀원 소개](#팀원-소개)
- [주요 기능](#주요-기능)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [설치 및 실행](#설치-및-실행)
- [API 문서](#api-문서)
- [시연 영상](#시연-영상)
- [기대 효과](#기대-효과)
- [프로젝트 비교](#프로젝트-비교)

## 🎯 프로젝트 개요

### 문제 정의
현대 사회에서 모임이나 약속 시 메뉴 선택으로 인한 시간 낭비와 매장 대기 시간이 주요 문제로 지적되고 있습니다.

### 해결 방안
**FoodfixProject**는 다음과 같은 솔루션을 제공합니다:
- 🚀 **즉시 포장 주문**: 앱을 통한 편리한 주문 시스템
- 📅 **예약 시스템**: 효율적인 예약 관리
- 📊 **실시간 관리**: 웹 기반 매장 관리 시스템
- 📈 **매출 분석**: 데이터 기반 비즈니스 인사이트

## 👥 팀원 소개

| 역할 | 이름 | 학번 | 담당 기술 | 이메일 | 코드 링크 |
|------|------|------|-----------|--------|-----------|
| **Backend** | 조민준 | 1971281 | Spring Boot | simpers@naver.com | [링크](https://github.com/Jominjun1/FoodfixProject/tree/spring) |
| **Frontend Web** | 김규리 | 2071222 | React | kimgyulee00@gmail.com | [링크](https://github.com/Jominjun1/FoodfixProject/tree/frontend_react) |
| **Frontend iOS** | 노현민 | 1871085 | Swift | miryumin@hansung.ac.kr | [링크](https://github.com/Jominjun1/FoodfixProject/tree/frontend_ios) |
| **Frontend Android** | 최명진 | 1971277 | Kotlin | cmgjn1881@naver.com | [링크](https://github.com/Jominjun1/FoodfixProject/tree/frontend_android) |

<img src="https://github.com/Jominjun1/FoodfixProject/assets/116476333/294f0e5c-df35-408d-8d63-4c04a0e902f0" width="800" height="800" alt="Team Introduction">

## ✨ 주요 기능

### 📱 사용자 서비스 (모바일 앱)
- **매장 검색 및 필터링**: 위치, 메뉴, 평점 기반 검색
- **실시간 주문**: 포장 주문 및 예약 시스템
- **메뉴 상세 정보**: 가격, 설명, 이미지 제공
- **주문 상태 추적**: 실시간 주문 진행 상황 확인

### 💻 매장 관리 서비스 (웹)
- **주문 관리**: 실시간 주문 접수 및 상태 관리
- **예약 관리**: 예약 현황 및 일정 관리
- **메뉴 관리**: 메뉴 등록, 수정, 삭제
- **매출 분석**: 일별/월별 매출 통계 및 분석

## 🛠️ 기술 스택

### Frontend
- **Web**: React.js, HTML5, CSS3
- **iOS**: Swift, UIKit
- **Android**: Kotlin, Android SDK

### Backend
- **Framework**: Spring Boot
- **Database**: MySQL
- **ORM**: Spring Data JPA
- **API**: RESTful API

### Development Tools
- **IDE**: Visual Studio Code, IntelliJ IDEA, Android Studio, Xcode
- **API Testing**: Postman
- **Version Control**: Git

![Architecture](https://github.com/Jominjun1/FoodfixProject/assets/116476333/4801c477-7d0d-476e-8715-5563c172c35a)

## 📁 프로젝트 구조

### 🖥️ 웹 사용자 기능 구성도

```
📊 매장 관리 시스템 (Web)
├── 🔐 로그인/인증
│   ├── 매장 관리자 로그인
│   ├── 세션 관리
│   └── 권한 관리
│
├── 📋 주문 관리
│   ├── 실시간 주문 접수
│   ├── 주문 상태 관리 (접수 → 조리중 → 완료)
│   ├── 주문 내역 조회
│   ├── 주문 취소/수정
│   └── 주문 알림 설정
│
├── 📅 예약 관리
│   ├── 예약 현황 조회
│   ├── 예약 승인/거절
│   ├── 예약 일정 관리
│   ├── 예약 변경/취소
│   └── 예약 알림 발송
│
├── 🍽️ 메뉴 관리
│   ├── 메뉴 등록/수정/삭제
│   ├── 메뉴 카테고리 관리
│   ├── 가격 설정
│   ├── 메뉴 이미지 업로드
│   ├── 메뉴 설명 관리
│   └── 품절/판매 중지 설정
│
├── 🏪 매장 정보 관리
│   ├── 매장 기본 정보 설정
│   ├── 영업 시간 관리
│   ├── 휴무일 설정
│   ├── 매장 위치 정보
│   └── 연락처 관리
│
└── ⚙️ 시스템 설정
    ├── 알림 설정
    ├── 결제 수단 관리
    ├── 배달 설정
    └── 시스템 로그 관리
```

### 📱 앱 사용자 기능 구성도

```
🍽️ 푸드픽스 앱 (Mobile)
├── 🔐 사용자 인증
│   ├── 회원가입/로그인
│   ├── 소셜 로그인 (카카오, 네이버)
│   ├── 비밀번호 찾기
│   └── 자동 로그인
│
├── 🗺️ 매장 검색 & 필터링
│   ├── 현재 위치 기반 검색
│   ├── 키워드 검색
│   ├── 필터링 옵션
│   │   ├── 거리별 정렬
│   │   ├── 평점별 정렬
│   │   ├── 음식 카테고리별 필터
│   │   └── 영업 상태별 필터
│   └── 즐겨찾기 매장 관리
│
├── 🏪 매장 상세 정보
│   ├── 매장 기본 정보
│   │   ├── 매장명, 주소, 연락처
│   │   ├── 영업시간, 휴무일
│   │   ├── 평점 및 리뷰
│   │   └── 매장 사진
│   ├── 메뉴 목록
│   │   ├── 메뉴 카테고리별 분류
│   │   ├── 메뉴 상세 정보
│   │   ├── 가격 및 설명
│   │   └── 메뉴 이미지
│   └── 리뷰 및 평점
│       ├── 고객 리뷰 조회
│       ├── 평점 작성
│       └── 리뷰 사진 업로드
│
├── 🛒 주문 시스템
│   ├── 장바구니 관리
│   │   ├── 메뉴 추가/삭제
│   │   ├── 수량 변경
│   │   └── 장바구니 비우기
│   ├── 주문 옵션 설정
│   │   ├── 포장/매장식사 선택
│   │   ├── 요청사항 입력
│   │   └── 결제 수단 선택
│   ├── 주문 진행
│   │   ├── 주문 확인
│   │   ├── 결제 처리
│   │   └── 주문 완료
│   └── 주문 상태 추적
│       ├── 실시간 주문 상태 확인
│       ├── 예상 완료 시간
│       └── 픽업 알림
│
├── 📅 예약 시스템
│   ├── 예약 가능 시간 확인
│   ├── 예약 정보 입력
│   │   ├── 날짜 및 시간 선택
│   │   ├── 인원 수 선택
│   │   ├── 요청사항 입력
│   │   └── 연락처 확인
│   ├── 예약 확정
│   └── 예약 관리
│       ├── 예약 내역 조회
│       ├── 예약 변경/취소
│       └── 예약 알림
│
├── 👤 사용자 프로필
│   ├── 개인정보 관리
│   │   ├── 프로필 정보 수정
│   │   ├── 연락처 관리
│   │   └── 배송지 관리
│   ├── 주문 내역
│   │   ├── 전체 주문 내역
│   │   ├── 주문 상세 정보
│   │   └── 주문 재주문
│   ├── 예약 내역
│   │   ├── 전체 예약 내역
│   │   ├── 예약 상세 정보
│   │   └── 예약 재예약
│   └── 즐겨찾기
│       ├── 즐겨찾기 매장 목록
│       ├── 즐겨찾기 메뉴 목록
│       └── 즐겨찾기 관리
│
└──⚙️ 앱 설정
    ├── 알림 설정
       ├── 주문 상태 알림
       ├── 예약 알림
       └── 마케팅 알림
```

## 🚀 설치 및 실행

### Prerequisites
- Node.js (v16 이상)
- Java 11 이상
- MySQL 8.0
- Android Studio / Xcode

### Backend 실행
```bash
# Backend 브랜치로 이동
git checkout backend

# 의존성 설치 및 실행
./gradlew build
./gradlew bootRun
```

### Frontend Web 실행
```bash
# Frontend Web 브랜치로 이동
git checkout frontend_web

# 의존성 설치
npm install

# 개발 서버 실행
npm start
```

### Mobile App 실행
- **iOS**: Xcode에서 프로젝트 열기 후 시뮬레이터 실행
- **Android**: Android Studio에서 프로젝트 열기 후 에뮬레이터 실행

## 📚 API 문서

<details>
<summary><b>푸드픽스 API 명세서</b></summary>

![API Documentation](https://github.com/Jominjun1/FoodfixProject/assets/116476333/50945216-4325-4297-9848-1f607d8c732e)

</details>

## 🎬 시연 영상

| 플랫폼 | 담당자 | 시연 영상 |
|--------|--------|-----------|
| **Web** | 김규리 | [📺 시연 영상 보기](https://youtu.be/Te7HCASK7oM) |
| **iOS** | 노현민 | [📺 시연 영상 보기](https://www.youtube.com/watch?v=iKVNI1_gh0Q) |
| **Android** | 최명진 | [📺 시연 영상 보기](https://youtu.be/1W3Jt--FotI?si=y0werGD-1l_KI9Vq) |

## 📸 작품 소개

<div align="center">
<img src="https://github.com/Jominjun1/FoodfixProject/assets/116476333/7d22e396-f5f3-4751-8e7f-e284cd673a78" width="450" height="700" alt="App Screenshot 1">
<img src="https://github.com/Jominjun1/FoodfixProject/assets/116476333/9b20081c-dbd4-4553-9e5e-d02b1a8ab611" width="450" height="700" alt="App Screenshot 2">
</div>

## 🎯 기대 효과

### 🕒 시간 절약
- **기존**: 매장에서 오랜 시간 대기
- **개선**: 앱을 통한 사전 주문으로 즉시 수령

### ��️ 선택의 편의성
- **기존**: 만족하는 매장 찾기 어려움
- **개선**: 다양한 매장 정보와 메뉴 사전 확인

### 📊 매장 관리 효율성
- **기존**: 주문 순서 혼선 및 예약 정보 오류
- **개선**: 웹 기반 실시간 주문/예약 관리 시스템

## 📊 프로젝트 비교

![Project Comparison](https://github.com/Jominjun1/FoodfixProject/assets/116476333/bb5fc391-b5bc-4fe3-ae70-f609b5f64f0f)

### 참고 프로젝트
- **최우수**: [Aloha Project](https://github.com/capstone-aloha)
- **우수1**: [TeamCookCaps](https://github.com/TeamCookCaps)
- **우수2**: [Godi00 Capstone](https://github.com/godi00/capstone)

---

<div align="center">

**🍽️ FoodfixProject** - 더 나은 식사 경험을 위한 혁신적인 솔루션

[![GitHub](https://img.shields.io/badge/GitHub-Repository-black?style=for-the-badge&logo=github)](https://github.com/Jominjun1/FoodfixProject)

</div>

