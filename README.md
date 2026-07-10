# COKERTHON-server-team3

# 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 4.1.0 (Spring Framework 7)
- **Build Tool**: Gradle
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA (Hibernate)
- **Auth**: Spring Security (세션 기반 로그인)
- **API 문서화**: Springdoc OpenAPI (Swagger UI)
- **Infra**: Docker, Docker Compose

# API 문서
[![Swagger UI](https://img.shields.io/badge/Swagger--UI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)](https://43.202.209.89.nip.io/swagger-ui/index.html)

# 실행 방법

## Docker Compose
```bash
# 실행
docker compose -f docker-compose-local.yml up -d

# 종료
docker compose -f docker-compose-local.yml down
```
## 로컬 직접 실행
MySQL을 별도로 띄운 뒤 실행합니다.

기본 프로필은 `local`이며, `src/main/resources/application.yml`을 참고해주세요.
```bash
# 실행
./gradlew bootRun
```

# 주요 기능
## 인증 (Auth)
- 회원가입 / 로그인 / 로그아웃 (세션 기반)
- 로그인 응답에 소속 그룹 목록 포함

## 그룹 (Group)
- 그룹 생성 및 초대 코드 발급
- 초대 코드로 그룹 가입
- 그룹원별 주간 집안일 기여도 랭킹 리포트 조회

## 집안일 관리 (Chore)
- 새로운 집안일 등록
- 카탈로그 항목을 선택해 집안일 등록
- 집안일 진행 단계 관리: 예정 → 진행중 → 완료
    - 완료 처리 시 실제 수행자 지정 가능 (담당자와 다르면 담당자 감점, 수행자 가점)
- 날짜별 / 담당자별 / 캘린더 집안일 조회
- 담당자에게 처리 요청 보내기
- 요청 알림 조회

## 룰렛 (Roulette)
- 그룹 내 누적 기여도가 낮을수록 당첨 확률이 높은 역수 기반 룰렛으로 담당자 자동 배정
<br>

# 역할 분담

| <img src="https://github.com/leehwx.png" width="100"> | <img src="https://github.com/Junseung-Ock.png" width="100"> |
|:---:|:---:|
| **해원** | **준승** |
| [leehwx](https://github.com/leehwx) | [Junseung-Ock](https://github.com/Junseung-Ock) |
| CI/CD 배포 <br> 인증 <br> 집안일 관리<br> | 집안일 룰렛<br>알림 |
