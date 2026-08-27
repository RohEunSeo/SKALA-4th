import { ref } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

// 도시 목록 — id/한글명/영문 도시명(API 조회용) 매핑
// ✅ 판교(SKALA 캠퍼스)는 OpenWeather에 도시명으로 등록되어 있지 않아 좌표(coords)로 조회
const cities = [
  { id: 'city_00', name: '판교', coords: { lat: 37.3969908, lon: 127.1129974 } },
  { id: 'city_01', name: '서울', query: 'Seoul' },
  { id: 'city_02', name: '수원', query: 'Suwon' },
  { id: 'city_03', name: '부산', query: 'Busan' },
  { id: 'city_04', name: '대전', query: 'Daejeon' },
  { id: 'city_05', name: '제주', query: 'Jeju' },
]

const API_KEY = import.meta.env.VITE_OPENWEATHER_API_KEY
const BASE_URL = 'https://api.openweathermap.org/data/2.5/weather'
// ✅ 5일 예보(3시간 간격) — 상세 정보의 5일 예보 표시에 사용
const FORECAST_URL = 'https://api.openweathermap.org/data/2.5/forecast'

// ✅ 화면 표시 + 배경 전환에 같이 쓰는 날씨 분류 — API 설명 문구(예: "온흐림", "약한 비")는
// 종류가 너무 다양해서 그대로 쓰면 문구도 뜬금없고 배경 매핑도 거의 항상 빗나감.
// condition id(https://openweathermap.org/weather-conditions) + 풍속으로 4종류로 묶음.
const classifyWeather = (id, windSpeed) => {
  if (windSpeed >= 8) return '바람' // 초속 8m↑ — 체감상 뚜렷하게 바람 부는 날
  if (id >= 200 && id < 600) return '비' // 뇌우, 이슬비, 비
  if (id >= 600 && id < 800) return '흐림' // 눈, 안개/황사 등 대기 현상
  if (id === 800 || id === 801) return '맑음' // 맑음, 구름 조금
  return '흐림' // 802~804(구름 많음~흐림)
}

// ✅ [과제 8 추가] 분류(condition) + 낮/밤(API의 icon 코드 마지막 글자 d/n)로
// weather-icons 폰트의 아이콘 클래스명을 고른다.
const ICON_CLASS_MAP = {
  '맑음': { day: 'wi-day-sunny', night: 'wi-night-clear' },
  '흐림': { day: 'wi-day-cloudy', night: 'wi-night-alt-cloudy' },
  '바람': { day: 'wi-strong-wind', night: 'wi-strong-wind' },
  '비': { day: 'wi-day-rain', night: 'wi-night-alt-rain' },
}
export const getWeatherIconClass = (city) => {
  if (!city) return 'wi-na'
  const isDay = !city.icon || city.icon.endsWith('d')
  const entry = ICON_CLASS_MAP[city.condition]
  if (!entry) return 'wi-na'
  return isDay ? entry.day : entry.night
}

// axios 응답 하나를 카드에서 쓰는 형태로 정규화 (도시 목록 조회/세계 도시 검색 공통 사용)
const normalizeWeather = (res, { id, name }) => ({
  id,
  name,
  temp: Math.round(res.data.main.temp),
  status: res.data.weather[0].description,
  // ✅ 화면 표시 + 배경 전환 모두 이 값(맑음/흐림/바람/비)으로 판단
  condition: classifyWeather(res.data.weather[0].id, res.data.wind.speed),
  // weather-icons 아이콘 선택용 (낮/밤 판단)
  icon: res.data.weather[0].icon,
  // ✅ [과제 8 추가] flag-icons 국기 배경용 국가 코드(예: KR, JP)
  country: res.data.sys?.country ?? null,
  // 상세 페이지에서 쓰는 부가 정보
  humidity: res.data.main.humidity,
  wind: res.data.wind.speed,
  feel: Math.round(res.data.main.feels_like),
})

// ✅ [과제 8 추가] 3시간 간격 예보 리스트를 날짜별 1개(정오에 가장 가까운 값)로 요약
const summarizeForecast = (list) => {
  const byDate = new Map()
  for (const entry of list) {
    const date = entry.dt_txt.slice(0, 10) // "YYYY-MM-DD"
    const hour = Number(entry.dt_txt.slice(11, 13))
    const existing = byDate.get(date)
    if (!existing || Math.abs(hour - 12) < Math.abs(Number(existing.dt_txt.slice(11, 13)) - 12)) {
      byDate.set(date, entry)
    }
  }
  return Array.from(byDate.values())
    .slice(0, 5)
    .map((entry) => ({
      date: entry.dt_txt.slice(0, 10),
      temp: Math.round(entry.main.temp),
      condition: classifyWeather(entry.weather[0].id, entry.wind.speed),
      icon: entry.weather[0].icon,
    }))
}

export const useWeatherStore = defineStore('weather', () => {
  // state
  const weatherList = ref([])
  const isLoading = ref(false)
  const error = ref(null)

  // ✅ [과제 8 추가] 세계 도시 검색 상태
  const worldCityResult = ref(null)
  const isWorldSearchLoading = ref(false)
  const worldSearchError = ref(null)

  // ✅ [과제 8 추가] 5일 예보 상태 — 상세 정보를 열 때마다 그 도시 기준으로 새로 조회
  const forecast = ref([])
  const isForecastLoading = ref(false)
  const forecastError = ref(null)

  // actions — 전체 도시 날씨를 한번에 조회
  const fetchAllWeather = async () => {
    isLoading.value = true
    error.value = null
    try {
      const requests = cities.map((city) =>
        axios.get(BASE_URL, {
          params: {
            // 판교처럼 도시명 조회가 안 되는 곳은 좌표(lat/lon)로 요청
            ...(city.coords ? { lat: city.coords.lat, lon: city.coords.lon } : { q: city.query }),
            appid: API_KEY,
            units: 'metric',
            lang: 'kr',
          },
        }),
      )
      const responses = await Promise.all(requests)

      weatherList.value = responses.map((res, idx) =>
        normalizeWeather(res, { id: cities[idx].id, name: cities[idx].name }),
      )
    } catch (e) {
      error.value = e
    } finally {
      isLoading.value = false
    }
  }

  // ✅ [과제 8 추가] 세계 도시 검색 — 입력한 영문 도시명으로 실시간 조회
  const searchWorldCity = async (query) => {
    const trimmed = query.trim()
    if (!trimmed) return

    isWorldSearchLoading.value = true
    worldSearchError.value = null
    try {
      const res = await axios.get(BASE_URL, {
        params: { q: trimmed, appid: API_KEY, units: 'metric', lang: 'kr' },
      })
      worldCityResult.value = normalizeWeather(res, { id: 'world_search', name: res.data.name })
    } catch (e) {
      worldSearchError.value = e
      worldCityResult.value = null
    } finally {
      isWorldSearchLoading.value = false
    }
  }

  const clearWorldSearch = () => {
    worldCityResult.value = null
    worldSearchError.value = null
  }

  // ✅ [과제 8 추가] 등록된 6개 도시 중 하나의 5일 예보 조회 (상세보기 열 때 호출)
  const fetchForecast = async (cityId) => {
    const city = cities.find((c) => c.id === cityId)
    if (!city) {
      forecast.value = []
      return
    }
    isForecastLoading.value = true
    forecastError.value = null
    try {
      const res = await axios.get(FORECAST_URL, {
        params: {
          ...(city.coords ? { lat: city.coords.lat, lon: city.coords.lon } : { q: city.query }),
          appid: API_KEY,
          units: 'metric',
          lang: 'kr',
        },
      })
      forecast.value = summarizeForecast(res.data.list)
    } catch (e) {
      forecastError.value = e
      forecast.value = []
    } finally {
      isForecastLoading.value = false
    }
  }

  return {
    weatherList,
    isLoading,
    error,
    fetchAllWeather,
    worldCityResult,
    isWorldSearchLoading,
    worldSearchError,
    searchWorldCity,
    clearWorldSearch,
    forecast,
    isForecastLoading,
    forecastError,
    fetchForecast,
  }
})
