import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
// ✅ [과제 7 추가] Element Plus + 아이콘 전역 등록
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// ✅ [과제 8 추가] 날씨 아이콘 폰트 라이브러리
import 'weather-icons/css/weather-icons.min.css'
// ✅ [과제 8 추가] 세계 도시 검색 결과 국기 배경용
import 'flag-icons/css/flag-icons.min.css'

import App from './App.vue'
import router from './router'
// ✅ [과제 6 추가] weatherStore import
import { useWeatherStore } from './stores/weatherStore'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

// ✅ [과제 7 추가] 아이콘 컴포넌트 전역 등록 — 각 파일에서 개별 import 없이 <Search /> 처럼 바로 사용
for (const [name, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(name, component)
}

// ✅ [과제 6 추가] 앱 시작 시 전체 도시 날씨 미리 조회
useWeatherStore().fetchAllWeather()

app.mount('#app')
