var name = "iii";

exports.name = name;
exports.setName = function (s) {
  name = s;
};
exports.getName = function () {
  return name;
};
