<script setup>
const welcomeMessage = 'Welcome to Skala-Vue'
</script>

<template>
  <div>
    <h2>{{ welcomeMessage }}</h2>
    <p>{{ welcomeMessage.toUpperCase() }}</p>
    <p>{{ 'Random: ' + Math.ceil(Math.random() * 100) }}</p>
  </div>
</template>