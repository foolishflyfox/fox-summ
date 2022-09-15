var g1 = require("./g.js");
console.log(g1.name);
g1.name = "rename";
var g2 = require("./g.js");
console.log(g2.name);
