import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      // 메인 날씨 대시보드
      path: '/',
      name: 'home',
      // Lazy Loading — 접근할 때 그때 불러옴
      component: () => import('../views/WeatherHomeView.vue'),
    },
    {
      // 서비스 소개 페이지
      path: '/about',
      name: 'about',
      component: () => import('../views/WeatherAboutView.vue'),
    },
    {
      // 도시 상세 페이지 — :cityId가 동적 파라미터
      // /weather/city_01 로 접근하면 cityId = 'city_01'
      path: '/weather/:cityId',
      name: 'weather-detail',
      component: () => import('../views/WeatherDetailView.vue'),
    },
    {
      // 본인 추가 — 날씨 통계 페이지
      path: '/stats',
      name: 'stats',
      component: () => import('../views/WeatherStatsView.vue'),
    },
    {
      // Catch-all — 위에서 매칭 안 된 모든 경로를 NotFound로
      // 반드시 routes 배열 맨 마지막에 위치해야 함
      path: '/:pathMatch(.*)*',
      name: 'not-found',
      component: () => import('../views/NotFoundView.vue'),
    },
  ],
})

export default router