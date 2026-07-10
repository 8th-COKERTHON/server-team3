# Cokerthon

같이 사는 사람들끼리 집안일을 등록·분담하고, 완료 여부와 기여도를 추적하는 백엔드 서비스입니다.

## 기술 스택

- **Language**: Java 17
- **Framework**: Spring Boot 4.1.0 (Spring Framework 7)
- **Build Tool**: Gradle
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA (Hibernate)
- **Auth**: Spring Security (세션 기반 로그인)
- **API 문서화**: Springdoc OpenAPI (Swagger UI)
- **Infra**: Docker, Docker Compose
- **기타**: Lombok

## 실행 방법

### 1. Docker Compose로 실행 (권장)

```bash
docker compose -f docker-compose-local.yml up --build
```

- 앱: `http://localhost:8080`
- MySQL: `localhost:3306` (DB `cokerthon-local`, 계정 `root`/`root`)
- 컨테이너가 뜨면 Swagger UI에서 API를 확인할 수 있습니다: `http://localhost:8080/swagger-ui/index.html`

### 2. 로컬에서 직접 실행

MySQL을 별도로 띄운 뒤, 아래 환경변수를 지정하고 실행합니다.

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=cokerthon-local
export DB_USERNAME=root
export DB_PASSWORD=root

./gradlew bootRun
```

기본 프로필은 `local`이며, `src/main/resources/application.yml`을 참고하세요. 배포용 설정은 `application-prod.yml`에 있습니다.

## 주요 기능

### 인증 (Auth)
- 회원가입 / 로그인 / 로그아웃 (세션 기반)
- 로그인 응답에 소속 그룹 목록 포함

### 그룹 (Group)
- 그룹 생성 및 초대 코드 발급
- 초대 코드로 그룹 가입
- 그룹 멤버 목록 조회
- 그룹원별 주간 집안일 기여도 랭킹 리포트 조회

### 집안일 관리 (Chore)
- 기본 제공 집안일(카탈로그) 목록 조회
- 완전히 새로운 집안일 등록 (개별 항목으로 저장)
- 카탈로그 항목을 선택해 집안일 등록 (제목 수정 가능, 반복 주기 재지정)
- 집안일 진행 단계 관리: 예정 → 진행중 → 완료
  - 완료 처리 시 실제 수행자 지정 가능 (담당자와 다르면 담당자 감점, 수행자 가점)
- 날짜별 / 담당자별 / 캘린더(기간) 집안일 조회
- 담당자에게 처리 요청 보내기, 요청 알림(읽음 처리 포함) 조회

### 룰렛 (Roulette)
- 그룹 내 누적 기여도(총점)가 낮을수록 당첨 확률이 높은 역수 기반 룰렛으로 담당자 자동 배정
- 룰렛 지분(칸 비율) 및 배정 이력 조회

## API 문서

애플리케이션 실행 후 Swagger UI에서 전체 API 명세를 확인할 수 있습니다.

```
http://localhost:8080/swagger-ui/index.html
```