// 使用 ES6 语法导入 jquery
import $ from "jquery";

// 定义 jQuery 的入口函数
$(function () {
  // 实现奇偶行变色功能
  $("li:odd").css("background-color", "red");
  $("li:even").css("background-color", "pink");
});
