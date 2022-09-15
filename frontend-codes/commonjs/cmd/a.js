define(function (require, exports, module) {
  var name = "aaa";
  var age = 18;
  console.log("a.name =", name);
  exports.name = name;
  exports.getAge = () => age;
});
