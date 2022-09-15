const c1 = require("./c1.js");
const c2 = require("./c2.js");

console.log(c1.name); // 输出 c1c1c1
c1.foo(); // 输出hello c1

console.log(c2.name); // 输出 undefined
c2.foo(); //  报错 TypeError: c2.foo is not a function
