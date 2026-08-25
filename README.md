# SKALA 4th

<p>
  <img alt="Java" src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=openjdk&logoColor=white"/>
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/>
  <img alt="Python" src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white"/>
  <img alt="Vue.js" src="https://img.shields.io/badge/Vue.js-4FC08D?style=flat-square&logo=vuedotjs&logoColor=white"/>
  <img alt="JavaScript" src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black"/>
  <img alt="Docker" src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white"/>
  <img alt="Git" src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white"/>
</p>

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

### Git

<table>
<tr>
<td valign="top" width="50%">

**`git-profile-lab`**
<br/>
<img alt="Git" src="https://img.shields.io/badge/Git-F05032?style=flat-square&logo=git&logoColor=white"/>
<img alt="GitHub" src="https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white"/>
<br/>
Git 저장소 생성부터 커밋·브랜치·원격 push까지 기본기 실습

</td>
<td valign="top" width="50%"></td>
</tr>
</table>

### Frontend

<table>
<tr>
<td valign="top" width="50%">

**`todo-list`**
<br/>
<img alt="HTML5" src="https://img.shields.io/badge/HTML5-E34F26?style=flat-square&logo=html5&logoColor=white"/>
<img alt="CSS3" src="https://img.shields.io/badge/CSS3-1572B6?style=flat-square&logo=css3&logoColor=white"/>
<img alt="JavaScript" src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat-square&logo=javascript&logoColor=black"/>
<br/>
바닐라 HTML/CSS/JS로 구현한 Todo 리스트

</td>
<td valign="top" width="50%">

**`skala-vue`**
<br/>
<img alt="Vue.js" src="https://img.shields.io/badge/Vue.js-4FC08D?style=flat-square&logo=vuedotjs&logoColor=white"/>
<img alt="Vite" src="https://img.shields.io/badge/Vite-646CFF?style=flat-square&logo=vite&logoColor=white"/>
<br/>
Vue 3 + Vite 기반 SPA 실습 프로젝트

</td>
</tr>
</table>

### Backend

<table>
<tr>
<td valign="top" width="50%">

**`StockTrading` / `StockTrading2`**
<br/>
<img alt="Java" src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=openjdk&logoColor=white"/>
<img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/>
<img alt="Hibernate" src="https://img.shields.io/badge/JPA%2FHibernate-59666C?style=flat-square&logo=hibernate&logoColor=white"/>
<img alt="Gradle" src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=gradle&logoColor=white"/>
<br/>
Spring Boot + JPA 기반 주식 거래 시뮬레이션 API (day2 → day3 CRUD·분석 기능 확장)

</td>
<td valign="top" width="50%">

**`shopapi`**
<br/>
<img alt="Java" src="https://img.shields.io/badge/Java-007396?style=flat-square&logo=openjdk&logoColor=white"/>
<img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white"/>
<img alt="MyBatis" src="https://img.shields.io/badge/MyBatis-DC382D?style=flat-square"/>
<img alt="JWT" src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=jsonwebtokens&logoColor=white"/>
<img alt="H2" src="https://img.shields.io/badge/H2_Database-0000FF?style=flat-square"/>
<br/>
JWT 인증과 Swagger UI를 갖춘 SKALA 굿즈 쇼핑몰 REST API

</td>
</tr>
</table>

### ML

<table>
<tr>
<td valign="top" width="50%">

**`day1-async-data-pipeline`**
<br/>
<img alt="Python" src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white"/>
<img alt="httpx" src="https://img.shields.io/badge/httpx-0A9EDC?style=flat-square"/>
<img alt="Pandas" src="https://img.shields.io/badge/Pandas-150458?style=flat-square&logo=pandas&logoColor=white"/>
<img alt="Pydantic" src="https://img.shields.io/badge/Pydantic-E92063?style=flat-square&logo=pydantic&logoColor=white"/>
<img alt="Pytest" src="https://img.shields.io/badge/Pytest-0A9EDC?style=flat-square&logo=pytest&logoColor=white"/>
<br/>
asyncio + httpx 기반 비동기 데이터 파이프라인, Pandas/Parquet 변환 및 pytest 검증

</td>
<td valign="top" width="50%"></td>
</tr>
</table>

### AI

<table>
<tr>
<td valign="top" width="50%">

**`sllm-fine-tuning`**
<br/>
<img alt="Python" src="https://img.shields.io/badge/Python-3776AB?style=flat-square&logo=python&logoColor=white"/>
<img alt="PyTorch" src="https://img.shields.io/badge/PyTorch-EE4C2C?style=flat-square&logo=pytorch&logoColor=white"/>
<img alt="Hugging Face" src="https://img.shields.io/badge/Hugging_Face-FFD21E?style=flat-square&logo=huggingface&logoColor=black"/>
<img alt="Jupyter" src="https://img.shields.io/badge/Jupyter-F37626?style=flat-square&logo=jupyter&logoColor=white"/>
<br/>
Hugging Face sLLM 실행/비교, LoRA SFT로 Text-to-SQL 업무 규칙 파인튜닝

</td>
<td valign="top" width="50%">

**`llm-transformer-agent`**
<br/>
진행 예정 (플레이스홀더)

</td>
</tr>
</table>

### Cloud

<table>
<tr>
<td valign="top" width="50%">

**`docker-container/board`**
<br/>
<img alt="Docker" src="https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white"/>
<img alt="Docker Compose" src="https://img.shields.io/badge/Docker_Compose-2496ED?style=flat-square&logo=docker&logoColor=white"/>
<img alt="Flask" src="https://img.shields.io/badge/Flask-000000?style=flat-square&logo=flask&logoColor=white"/>
<img alt="Nginx" src="https://img.shields.io/badge/Nginx-009639?style=flat-square&logo=nginx&logoColor=white"/>
<br/>
게시판을 Web(Nginx)·WAS(Flask) 컨테이너로 분리한 Docker Compose 구성

</td>
<td valign="top" width="50%"></td>
</tr>
</table>

---

> 각 프로젝트별 실행 방법과 상세 내용은 하위 폴더의 README를 참고하세요.
