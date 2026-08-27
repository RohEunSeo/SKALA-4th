# 과제 2 — Weather Composition

## 구현 내용
- ref(): searchQuery, selectedCityInfo, weatherList 반응형 상태 관리
- computed(): filteredWeatherList — 검색어 기반 도시 필터링
- watch(): selectedCityInfo 변경 시 콘솔 로그 출력
- watchEffect(): searchQuery 타이핑마다 자동 추적 및 콘솔 로그 출력
- 검색 결과 없을 때 안내 문구 표시

## 본인 추가 구현
- avgTemp computed: 현재 표시 중인 도시들의 평균 기온 실시간 계산 및 화면 표시