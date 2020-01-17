// vue.config.js
module.exports = {
  publicPath: "/",
  devServer: {
    port: 8081,
    proxy: {
      "/mng": {
        target: "http://127.0.0.1:8666",
        changeOrigin: true
      }
    }
  }
};
