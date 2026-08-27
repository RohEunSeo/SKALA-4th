<script setup>
// props: 부모로부터 city 객체 하나를 받아서 표시
const props = defineProps({
  city: {
    type: Object,
    required: true,
  },
})

// emits: 카드 클릭 → select-card, 상세보기 클릭 → click-detail 을 부모에게 올림
const emit = defineEmits(['select-card', 'click-detail'])

// 카드 클릭 시 부모에게 city 객체 전달
const handleSelectCard = () => {
  emit('select-card', props.city)
}

// 상세보기 버튼 클릭 시 부모에게 city 객체 전달
// .stop은 부모 template에서 처리 안 하고 여기서 직접 처리
const handleClickDetail = () => {
  emit('click-detail', props.city)
}
</script>

<template>
  <!-- 카드 클릭 → select-card emit -->
  <div class="weather-card" @click="handleSelectCard">
    <div class="card-info">
      <p class="city-name">{{ city.name }} ({{ city.status }})</p>
      <p class="city-temp">현재 기온: {{ city.temp }}°C</p>
      <span v-if="city.temp >= 25" class="label hot">🔥 더움</span>
      <span v-else class="label cool">❄️ 선선함</span>
    </div>

    <!-- 상세보기 클릭 → click-detail emit -->
    <!-- @click.stop으로 카드 클릭 이벤트(select-card)가 같이 발생하는 것을 막음 -->
    <button @click.stop="handleClickDetail" class="detail-btn">
      상세보기
    </button>
  </div>
</template>

<style scoped>
.weather-card {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  margin-bottom: 12px;
  background: rgba(255, 255, 255, 0.15);
  border: 1px solid rgba(255, 255, 255, 0.35);
  border-radius: 16px;
  cursor: pointer;
  transition: background 0.2s, transform 0.15s;
}

.weather-card:hover {
  background: rgba(255, 255, 255, 0.25);
  transform: translateY(-2px);
}

.city-name {
  color: white;
  font-weight: 700;
  font-size: 16px;
  margin-bottom: 4px;
  text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

.city-temp {
  color: rgba(255, 255, 255, 0.95);
  font-size: 13px;
  margin-bottom: 10px;
}

.label {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

.hot { background: rgba(220, 80, 60, 0.85); color: white; }
.cool { background: rgba(60, 140, 220, 0.85); color: white; }

.detail-btn {
  padding: 8px 16px;
  background: rgba(255, 255, 255, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  color: white;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.2s;
}

.detail-btn:hover { background: rgba(255, 255, 255, 0.4); }
</style>