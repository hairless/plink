<template>
  <span>
    <router-link class="logo" :to="{ name: 'Home' }">{{ title }}</router-link>
  </span>
</template>
<script>
import { title } from "@/settings";
export default {
  data() {
    return {
      title: title
    };
  }
};
</script>
<style scoped>
.logo {
  color: whitesmoke;
  font-size: 20px;
  font-weight: bold;
  /*animation: fireDiv 5s infinite;*/
}
@keyframes fireDiv {
  0% {
    text-shadow: 0 0 4px white, 0 -5px 4px #ff3, 2px -10px 6px rgba(255, 0, 0, 0.5), -2px -15px 11px #f80, 2px -25px 18px #f20;
  }
  25 % {
    text-shadow: 0 0 4px white, 2px -7px 6px #ff3, 2px -12px 8px rgba(255, 0, 0, 0.75), -3px -20px 11px #f80, 4px -30px 22px #f20;
  }
  50% {
    text-shadow: 0 0 4px white, 2px -10px 8px #ff3, 2px -14px 10px #ff0000, -4px -25px 11px #f80, 4px -35px 25px #f20;
  }
  75% {
    text-shadow: 0 0 4px white, 2px -7px 6px #ff3, 2px -12px 8px rgba(255, 0, 216, 0.52), -3px -20px 11px #f80, 4px -30px 22px #f20;
  } /*
  100% {
    text-shadow: 0 0 4px white, 0 -5px 4px #ff3, 2px -10px 6px #ff00fd,
    -2px -15px 11px #f80, 2px -25px 18px #f20;
  }*/
}
</style>
