var app;
if ("undefined" == typeof app) app = {};
app.a = (function () {
  let name = "aaa";
  let age = 10;
  return {
    getName: function () {
      return name;
    },
  };
})();
