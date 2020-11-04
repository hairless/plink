const path = require("path");
// const defaultSettings = require('./src/settings.js')

function resolve(dir) {
  return path.join(__dirname, dir);
}

// const name = defaultSettings.title || 'Vue Hairless Admin' // page title
const port = process.env.PORT || process.env.npm_config_port || 30011; // dev port

const chainWebpack = config => {
  config.resolve.alias.set("@", resolve("src"));
};
const node_env = process.env.NODE_ENV;

module.exports = {
  publicPath: node_env === "prod" ? "/" : "/",
  outputDir: "../plink-web/src/main/resources/static",
  devServer: {
    port: port,
    open: true,
    overlay: {
      warnings: false,
      errors: true
    },
    proxy: {
      "/user": {
        target: process.env.PROXY_USER_API_URL,
        changeOrigin: true,
        pathRewrite: {
          "^/api/user": "/"
        }
      },
      /* Job Inst Enum ... */
      "/mng": {
        target: process.env.PROXY_JOB_API_URL,
        changeOrigin: true,
        pathRewrite: {
          "^/mng": "/"
        }
      }
    }
  },
  chainWebpack
};
