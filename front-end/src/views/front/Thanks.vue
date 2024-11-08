<template>
  <div class="main-content">
    <div class="thanks-container">
      <div class="thanks-message">Thank you for your participation! Have a nice day</div>
      <div><a href="/front/home" class="back-button">Back to Main</a></div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Thanks",
  data() {
    return {}
  },
  created() {

  },
  methods: {}
}
</script>

<style scoped>
.main-content {
  min-height: 80vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
}

.thanks-container {
  text-align: center;
  background: #ffffff;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  animation: fadeIn 1s ease-in-out;
}

.thanks-message {
  font-size: 36px;
  margin-bottom: 20px;
  color: #409EFF;
  font-weight: bold;
}

.back-button {
  font-size: 18px;
  color: #ffffff;
  background-color: #409EFF;
  padding: 10px 20px;
  border-radius: 5px;
  text-decoration: none;
  transition: background-color 0.3s;
}

.back-button:hover {
  background-color: #66b1ff;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
