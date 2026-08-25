# SKALA 4th

SK AI Camp **SKALA 4기** 교육 과정에서 진행한 실습·과제 코드를 모아두는 기록용 저장소입니다.
분야별(AI, Backend, Cloud, Frontend, ML, Git)로 폴더를 나누어 정리했습니다.

## 📁 디렉토리 구조

```
SKALA-4th/
├── AI/
│   ├── llm-transformer-agent/     # LLM·Transformer·Agent 실습 (진행 예정)
│   └── sllm-fine-tuning/          # sLLM 실행/비교 및 LoRA SFT 파인튜닝 실습 노트북
│
├── backend/springboot/
│   ├── StockTrading/               # day2 - JPA 기반 주식 거래 API (Repository/Service/Controller)
│   ├── StockTrading2/              # day3 - CRUD 8종 + 포트폴리오 분석 API 5종
│   └── shopapi/                    # SKALA 굿즈 쇼핑몰 REST API (JWT 인증, Swagger, H2)
│
├── cloud/docker-container/
│   └── board/                      # Docker Compose로 구성한 게시판 (Nginx Web + Flask WAS)
│
├── frontend/
│   ├── html-css-js/todo-list/      # 바닐라 JS로 구현한 Todo 리스트
│   └── skala-vue/                  # Vue 3 + Vite 실습 프로젝트
│
├── github/
│   └── git-profile-lab/            # Git/GitHub 기본 사용법 실습 (커밋, 브랜치, 원격 저장소)
│
└── ML/
    └── day1-async-data-pipeline/   # asyncio 기반 데이터 파이프라인 실습
```

## 🗂️ 프로젝트별 요약

| 분야 | 프로젝트 | 설명 |
|---|---|---|
| AI | `sllm-fine-tuning` | Hugging Face sLLM 실행/비교, LoRA SFT로 Text-to-SQL 업무 규칙 파인튜닝 |
| Backend | `StockTrading` / `StockTrading2` | Spring Boot + JPA 기반 주식 거래 시뮬레이션 API |
| Backend | `shopapi` | Spring Boot 굿즈 쇼핑몰 API (JWT, Swagger UI, H2 인메모리 DB) |
| Cloud | `docker-container/board` | 게시판을 Web/WAS 컨테이너로 분리한 Docker Compose 구성 |
| Frontend | `todo-list` | HTML/CSS/JS 바닐라 Todo 앱 |
| Frontend | `skala-vue` | Vue 3 + Vite 기반 SPA 실습 |
| Git | `git-profile-lab` | Git 저장소 생성부터 원격 push까지 기본기 실습 |
| ML | `day1-async-data-pipeline` | Python asyncio 기반 비동기 데이터 파이프라인 |

## 🛠️ 기술 스택

`Java · Spring Boot · JPA/MyBatis` · `Python · FastAPI/Flask · asyncio` · `Vue 3 · Vite` · `HTML/CSS/JS` · `Docker · Docker Compose` · `Git/GitHub`

---

> 각 프로젝트별 실행 방법과 상세 내용은 하위 폴더의 README를 참고하세요.
