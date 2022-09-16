// webpack 打包常用配置
const path = require("path");
exports.mode = "development"; // 设置开发模式 production / development
exports.entry = path.join(__dirname, "src/index1.js"); // 设置入口文件
exports.output = {
  path: path.join(__dirname, "bundle"), // 设置输出目录
  filename: "m.js", // 设置输出文件名
};
exports.devServer = {
  static: "./", // 供 webpack-dev-server 使用(webpack server 命令),使之支持静态文件访问
};

