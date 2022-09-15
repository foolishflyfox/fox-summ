// name 和 age 都是 a.js 私有的
var age = 10;
module.exports.name = "aaa";
// exports 等价于 module.exports
exports.getAge = function () {
  return age;
};
