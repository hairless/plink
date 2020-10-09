import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

/* ViewUI */
import ViewUI from "view-design";
import "view-design/dist/styles/iview.css";
import moment from "moment";

Vue.filter("dateFormat", function(
  dateStr: string,
  pattern: string = "YYYY-MM-DD HH:mm:ss"
) {
  let formatStr = moment(dateStr).format(pattern);
  if ("Invalid date" === formatStr) {
    return "";
  }
  return formatStr;
});

Vue.config.productionTip = false;
Vue.use(ViewUI);

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
