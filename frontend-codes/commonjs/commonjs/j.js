var i = require("./i.js");
console.log(i.name); //  iii
i.setName("ix");
console.log(i.name); //  iii，表名 exports 中的 name 没有改变
console.log(i.getName()); //  模块的内部变量已经改变了
