define(function (require, exports, module) {
  var name = "bbb";
  var age = 19;
  console.log("b.name =", name);
  exports.name = name;
  exports.getAge = () => age;
});
