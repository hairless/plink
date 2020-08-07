<template>
  <div style="float: right">
    <a-tooltip placement="left">
      <template slot="title">
        <a href="http://wiki.beijingyuanxin.com/pages/viewpage.action?pageId=5079608" target="_blank" style="color: #ecffeb">项目文档</a>
      </template>
      <a href="http://wiki.beijingyuanxin.com/pages/viewpage.action?pageId=5079608" target="_blank"><a-icon type="question" style="margin-right: 20px;font-size: 18px; color: #ff0000;" spin/></a>
    </a-tooltip>
    <a-icon type="user" style="font-size: 18px; color: #a6f5ff;" />
    <a-dropdown>
      <a class="ant-dropdown-link" @click="e => e.preventDefault()" style="font-size: 18px; color: whitesmoke"> {{ userInfo.nickName }} <a-icon type="down" /> </a>
      <a-menu slot="overlay">
        <a-menu-item key="1" v-on:click="onProfile">
          个人配置
        </a-menu-item>
        <a-menu-item key="2" @click="onLogout">
          注销
        </a-menu-item>
      </a-menu>
    </a-dropdown>
  </div>
</template>
<script>
export default {
  data() {
    return {
      userInfo: {
        nickName: this.$store.state.user.nickName
      }
    };
  },
  methods: {
    onProfile() {},
    onLogout() {
      this.$store
        .dispatch("user/logout")
        .then(() => {
          this.$Notice.success({
            title: "注销成功!",
            duration: 1
          });
          this.$router.push({ path: "/" });
        })
        .catch(err => {
          this.$Notice.error({
            title: err.msg || "注销失败!",
            duration: 3
          });
        });
    }
  }
};
</script>
