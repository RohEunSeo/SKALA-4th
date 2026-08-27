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

// ✅ 배경 전환용 날씨 상태 분류 — API 설명 문구(예: "온흐림", "약한 비")는
// 종류가 너무 다양해서 그대로 쓰면 배경 매핑이 거의 항상 빗나감.
// OpenWeather의 condition id(https://openweathermap.org/weather-conditions)로 4종류로 묶음.
const mapConditionCode = (id) => {
  if (id >= 200 && id < 600) return '비' // 뇌우, 이슬비, 비
  if (id >= 600 && id < 800) return '흐림' // 눈, 안개/황사 등 대기 현상
  if (id === 800 || id === 801) return '맑음' // 맑음, 구름 조금
  if (id === 802) return '구름' // 구름 보통
  return '흐림' // 803(구름 많음), 804(흐림) 등
}

export const useWeatherStore = defineStore('weather', () => {
  // state
  const weatherList = ref([])
  const isLoading = ref(false)
  const error = ref(null)

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

      weatherList.value = responses.map((res, idx) => ({
        id: cities[idx].id,
        name: cities[idx].name,
        temp: Math.round(res.data.main.temp),
        status: res.data.weather[0].description,
        // ✅ 배경 전환은 설명 문구 대신 이 값(맑음/구름/비/흐림)으로 판단
        condition: mapConditionCode(res.data.weather[0].id),
        // 상세 페이지(WeatherDetailView)에서 쓰는 부가 정보
        humidity: res.data.main.humidity,
        wind: res.data.wind.speed,
        feel: Math.round(res.data.main.feels_like),
      }))
    } catch (e) {
      error.value = e
    } finally {
      isLoading.value = false
    }
  }

  return { weatherList, isLoading, error, fetchAllWeather }
})
