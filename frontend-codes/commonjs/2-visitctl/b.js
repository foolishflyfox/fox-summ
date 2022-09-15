var app;
if ("undefined" == typeof app) app = {};
app.b = (function () {
  let name = "bbb";
  let age = 10;
  return {
    getAge: function () {
      return age;
    },
  };
})();
