import router from "./router";
import store from "./store";
import { getToken } from "@/utils/auth"; // get token from cookie
// import getPageTitle from '@/utils/get-page-title';

import { LoadingBar, Notice } from "view-design";

const whiteList = ["/login", "/auth-redirect"]; // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  LoadingBar.start();

  // set page title
  // document.title = getPageTitle(to.meta.title);

  const hasToken = getToken();

  if (hasToken) {
    if (to.path === "/login") {
      next({ path: "/" });
      LoadingBar.finish();
    } else {
      // 路由列表
      const routeList = store.getters.routeList;

      if (routeList) {
        next();
      } else {
        try {
          // 用户信息
          await store.dispatch("user/getUserInfo");

          // 路由列表
          let routeList = await store.dispatch("user/getUserRouteList");

          // 路由列表转换
          let accessRoutes = await store.dispatch("permission/generateRoutes", routeList);

          // 动态添加路由
          router.addRoutes(accessRoutes);

          // hack method to ensure that addRoutes is complete
          // set the replace: true, so the navigation will not leave a history record
          next({ ...to, replace: true });
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch("user/resetToken");
          Notice.error({
            title: error || "Has Error"
          });
          next(`/login?redirect=${to.path}`);
          LoadingBar.finish();
        }
      }
    }
  } else {
    /* has no token */
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next();
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`);
      LoadingBar.finish();
    }
  }
});

router.afterEach(() => {
  // finish progress bar
  LoadingBar.finish();
});
