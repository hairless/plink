<template>
  <div :style="background" class="background-class">
    <a-row type="flex" justify="space-around" align="middle">
      <a-col :span="6">
        <div ref="login" class="login-div">
          <a-form-model ref="ruleForm" :model="ruleUserFormData" :rules="rules" v-bind="layout">
            <div align="center" class="login-title">
              {{ title }}
            </div>
            <a-form-model-item has-feedback prop="username">
              <a-input v-model="ruleUserFormData.username" type="text" autocomplete="off" @keyup.enter.native="handleSubmit">
                <a-icon slot="prefix" type="user" style="color:rgba(99,255,185,0.7)" />
              </a-input>
            </a-form-model-item>
            <a-form-model-item has-feedback prop="password">
              <a-input v-model="ruleUserFormData.password" type="password" autocomplete="off" @keyup.enter.native="handleSubmit">
                <a-icon slot="prefix" type="lock" style="color:rgba(99,255,185,0.7)" />
              </a-input>
            </a-form-model-item>
            <a-form-model-item :wrapper-col="{ span: 24 }">
              <a-button type="primary" @click="handleSubmit" style="width: 100%" :disabled="disabledLogin">
                登录
              </a-button>
            </a-form-model-item>
          </a-form-model>
        </div>
      </a-col>
    </a-row>
  </div>
</template>

<script>
let h = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
let w = window.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
import { title } from "@/settings";
export default {
  data() {
    let validateUsername = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Please input the username !"));
      } else {
        callback();
      }
    };
    let validatePassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("Please input the password !"));
      } else {
        callback();
      }
    };
    return {
      title: title,
      ruleUserFormData: {
        username: "",
        password: ""
      },
      rules: {
        username: [{ validator: validateUsername, trigger: "change" }],
        password: [{ validator: validatePassword, trigger: "change" }]
      },
      layout: {
        wrapperCol: { span: 24 }
      },
      disabledLogin: false,
      fileSerialNumber: 1
    };
  },
  computed: {
    background: function() {
      return {
        backgroundImage: "url(" + require("../../../assets/img/background/" + this.fileSerialNumber + ".jpg") + ")",
        height: h + "px"
      };
    }
  },
  methods: {
    handleSubmit() {
      this.$store
        .dispatch("user/login", this.ruleUserFormData)
        .then(() => {
          this.$Notice.success({
            title: "欢迎回来 !",
            duration: 1
          });
          this.$router.push({ path: "/" });
        })
        .catch(err => {
          this.$Notice.error({
            title: err.msg || "登录失败 !",
            duration: 3
          });
        });
    },
    handleReset() {
      this.userInfo.username = "";
      this.userInfo.password = "";
    }
  },
  created() {
    this.fileSerialNumber = Math.floor(Math.random() * 10 + 1);
  },
  mounted() {
    let l = w / 2 - this.$refs.login.offsetWidth / 2;
    let t = h / 2 - this.$refs.login.offsetHeight / 2;
    this.$refs.login.style.left = l + "px";
    this.$refs.login.style.top = t + "px";
  }
};
</script>

<style scoped>
.background-class {
  /*width:800px;*/
  /*max-width:100%;*/
  /*display:flex;*/
  /*align-items:center;*/
  /*justify-content:center;*/
  background-size: cover;
  background-repeat: no-repeat;
  /*object-fit: cover;*/
}
.login-div {
  width: 300px;
  position: fixed;
}
.login-title {
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: bold;
  color: #1890ff;
  animation: fireDiv 5s infinite;
}
@keyframes fireDiv {
  0% {
    text-shadow: 0 0 4px white, 0 -5px 4px #ff3, 2px -10px 6px #fffa6e, -2px -15px 11px #f80, 2px -25px 18px #f20;
  }
  25% {
    text-shadow: 0 0 4px white, 2px -7px 6px #ff3, 2px -12px 8px #fc74ff, -3px -20px 11px #f80, 4px -30px 22px #f20;
  }
  50% {
    text-shadow: 0 0 4px white, 2px -10px 8px #ff3, 2px -14px 10px #cf2eff, -4px -25px 11px #f80, 4px -35px 25px #f20;
  }
  75% {
    text-shadow: 0 0 4px white, 2px -7px 6px #ff3, 2px -12px 8px rgb(255, 0, 0), -3px -20px 11px #f80, 4px -30px 22px #f20;
  } /*
  100% {
    text-shadow: 0 0 4px white, 0 -5px 4px #ff3, 2px -10px 6px #ff0000,
      -2px -15px 11px #f80, 2px -25px 18px #f20;
  }*/
}
</style>
