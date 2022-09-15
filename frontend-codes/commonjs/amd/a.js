define(["b.js"], function f(b) {
  var name = "aaa";
  var age = 15;
  console.log("b.name =", b.name);
  console.log("b.age =", b.getAge());
  return {
    name,
    getAge: () => age,
  };
});
