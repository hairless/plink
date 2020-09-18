const getters = {
  token: state => state.user.token,
  userName: state => state.user.userName,
  nickName: state => state.user.nickName,
  moduleId: state => state.user.moduleId,
  routeList: state => state.user.routeList,
  topMenuList: state => state.user.topMenuList,
  leftMenuList: state => state.user.leftMenuList
};

export default getters;
