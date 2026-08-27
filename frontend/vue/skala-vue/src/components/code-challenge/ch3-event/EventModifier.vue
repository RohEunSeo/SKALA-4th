<script setup>

// ── .prevent 예시용 함수 ──────────────────────────────────────
const handleLink = () => {
  // .prevent 수식어가 e.preventDefault()를 대신 호출해줌
  // 그래서 <a> 태그의 기본 동작(링크 이동)이 막히고 이 함수만 실행됨
  alert('수식어 덕분에 네이버로 이동하지 않고 함수만 실행됩니다!')
}

// ── .stop 예시용 함수들 ──────────────────────────────────────
const handleBox = () => {
  // 부모 div에 걸린 클릭 이벤트
  // 자식 버튼 중 .stop이 없는 버튼을 클릭하면 버블링으로 여기까지 올라와서 실행됨
  alert('부모 박스가 클릭되었습니다!')
}

const handleChild1 = () => {
  // .stop 없는 버튼 → 이 함수 실행 후 부모로 버블링됨 (handleBox도 실행)
  alert('1번 자식 클릭! (부모로 버블링 발생)')
}

const handleChild2 = () => {
  // .stop 있는 버튼 → 이 함수만 실행, 부모로 버블링 안 됨
  alert('2번 자식(나만 켜짐) 클릭! (버블링 차단됨)')
}
</script>

<template>
  <div style="padding: 20px">
    <h2>이벤트 수식어(Modifiers) 학습</h2>

    <!-- ① .prevent : 태그의 기본 동작을 막고 함수만 실행 -->
    <!-- <a> 태그는 기본적으로 클릭하면 href로 이동하는 동작이 있음 -->
    <!-- @click.prevent 를 붙이면 그 기본 동작이 막히고 handleLink만 실행됨 -->
    <h3>1) .prevent (기본 동작 막기)</h3>
    <a href="https://www.naver.com" @click.prevent="handleLink">네이버 링크</a>

    <br /><br />

    <!-- ② .stop : 이벤트 버블링 차단 -->
    <!-- 부모 div에 @click이 걸려있고, 자식 버튼에도 @click이 걸려있음 -->
    <!-- 버블링이란: 자식 클릭 → 이벤트가 부모로도 전파되는 현상 -->
    <h3>2) .stop (이벤트 버블링 막기)</h3>
    <div @click="handleBox" style="padding: 20px; background-color: #eee">
      <p>부모 영역 (클릭 시 alert 발동)</p>

      <!-- .stop 없음 → handleChild1 실행 후 버블링으로 handleBox도 실행됨 -->
      <button @click="handleChild1">버블링 발생 버튼</button>

      &nbsp;

      <!-- .stop 있음 → handleChild2만 실행, 부모 handleBox는 실행 안 됨 -->
      <button @click.stop="handleChild2">버블링 차단 버튼</button>
    </div>
  </div>
</template>