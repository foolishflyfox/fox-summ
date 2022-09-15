# commonjs 模块化

参考: [前端科普系列-CommonJS：不是前端却革命了前端](https://zhuanlan.zhihu.com/p/113009496?utm_id=0)

## 模块化的发展历史

### stage0: 全局变量阶段

代码 *commonjs/0-globalvar*。

```html
<!-- index.html -->
<!DOCTYPE html>
<script src="a.js"></script>
<script src="b.js"></script>
<script>
    console.log(name);
    console.log(age);
</script>
```
```js
// a.js
var name = "aaa";
var age = 10;
```
```js
// b.js
var name = "bbb";
var age = 11;
```
浏览器的控制台输出为：

```
bbb
11
```

如果我们交换 *index.html* 中引入 javascript 的顺序，输出将变为：

```
aaa
10
```

存在的问题：由于引入的变量都定义为了全局变量，因此必然存在全局变量被污染的情况，解决方案是引入类似命名空间的变量。

### stage1: 仿命名空间阶段

代码: *commonjs/1-namespace*。

```html
<!-- index.html -->
<script src="a.js"></script>
<script src="b.js"></script>

<script>
    console.log(app.a.name);
    console.log(app.a.age);
    console.log(app.b.name);
    console.log(app.b.age);
</script>
```

```js
// a.js
var app;
if ("undefined" == typeof app) app = {};

app.a = {};
app.a.name = "aaa";
app.a.age = 10;
```

```js
// b.js
var app;
if ("undefined" == typeof app) app = {};

app.b = {
  name: "bbb",
  age: 11,
};
```

执行结果为：
```
aaa
10
bbb
11
```
存在的问题：模块中定义的变量未添加权限控制，例如，我不希望 app.a.age 被修改，上述代码是做不到的，通过 `app.a.name = 100;` 即可修改。

### stage2: 控制访问权限阶段

代码: *commonjs/2-visitctl*。通过闭包限制变量访问，通过函数对外开放权限。

```html
<!-- index.html -->
<script src="a.js"></script>
<script src="b.js"></script>
<script>
    console.log(app.a.getName());
    console.log(app.b.getName());
</script>
```

```js
// a.js
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
```

```js
// b.js
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
```
a 模块只开放 getName 接口，b 模块只开放 getAge 接口。

## CommonJS 规范

CommonJS 是 js 的一种模块化方案。a.js 中定义的变量都是私有的，对其他的文件不可见。

CommonJS 中规定，每个模块内部有两个变量可以使用，`require` 和 `module`。

require 用来加载某个模块，module 代表当前模块，是一个对象，保存了当前模块的信息。exports 是 module 上的一个属性，保存了当前模块要导入的接口或者变量，使用 require 加载的某个模块获取到的值就是那个模块使用 exports 导出的值。

```js
// a.js
// name 和 age 都是 a.js 私有的
var age = 10;
module.exports.name = "aaa";
// exports 等价于 module.exports
exports.getAge = function () {
  return age;
};
```

```js
var aObj = require("./a.js");
console.log(aObj.name);
console.log(aObj.getAge());
```

通过 `node b.js` 的执行结果为：
```
aaa
10
```

注意，exports 是 module.exports 的引用，使用还是有所区别，例如：

```js
// c1.js
module.exports = {
  name: "c1c1c1",
  foo: function () {
    console.log("hello c1");
  },
};
```
```js
exports = {
  name: "c2c2c2",
  foo: function () {
    console.log("hello c2");
  },
};
```
```js
const c1 = require("./c1.js");
const c2 = require("./c2.js");

console.log(c1.name); // 输出 c1c1c1
c1.foo(); // 输出hello c1

console.log(c2.name); // 输出 undefined
c2.foo(); //  报错 TypeError: c2.foo is not a function
```

如果一个模块的对外接口只有单一的值，可以使用 module.exports 导出，例如：
```js
// e.js
module.exports = function () {
  console.log("hello, e.js");
};
```
```js
// f.js
var f = require("./e.js");

f();
```
执行结果为：
```
$ node f.js
hello, e.js
```

### CommonJS 的缓存功能

require 命令的基本功能是读入并执行一个 js 文件，然后返回该模块的 exports 对象，如果没有发现指定模块，会报错。

第一次加载某个模块时，Node.js 会缓存该模块，以后再加载时，直接从缓存中取出该模块的 module.export 属性返回。

```js
// g.js
var name = "morrain";
var age = 18;
exports.name = name;
exports.getAge = function () {
  return age;
};
```
```js
// h.js
var g1 = require("./g.js");
console.log(g1.name);
g1.name = "rename";
var g2 = require("./g.js");
console.log(g2.name);
```
执行结果为：
```
$ node h.js
morrain
rename
```

还有一点需要注意，CommonJS 模块的加载机制是，require 是被导出的值的拷贝。也就是说，一旦导出一个值，模块内部的变化就影响不到这个值。例如下面的代码：
```js
// i.js
var name = "iii";

exports.name = name;
exports.setName = function (s) {
  name = s;
};
exports.getName = function () {
  return name;
};
```
```js
// j.js
var i = require("./i.js");
console.log(i.name); //  iii
i.setName("ix");
console.log(i.name); //  iii，表名 exports 中的 name 没有改变
console.log(i.getName()); //  ix, 模块的内部变量已经改变了
```

### CommonJS 实现

因为 require、module、exports 都是 node.js 可以执行的，但是浏览器端不能识别，并且浏览器端通常使用的是一个合并后的 main.js 文件，由 webpack 等工程化软件自动生成，完成 CommonJS 的实现。

例如src下有以下两个文件：a.js 和 index.js。
```js
// a.js
exports.name = "fff";
```
```js
// index.js
var a = require("./a.js");
console.log(a.name);
```
则 webpack 在 development 模式下转换后生成的 dist/main.js 为：
```js
(() => {
    var webpackModules = ({
        "./src/a.js": ((wpModule, exports) => {
            eval("exports.name = \"fff\"");
        }),
        "./src/index.js": ((wpModule, exports, require) => {
            eval("var a = require(\"./src/a.js\");\nconsole.log(a.name);");
        })
    });
    var wpModuleCache = {};
    function wpRequire(moduleId) {
        var cacheModule = wpModuleCache[moduleId];
        if (cacheModule != undefined) {
            return cacheModule.exports;
        }
        var module = wpModuleCache[moduleId] = {
            exports: {}
        };
        // 通过递归调用实现依赖加载
        wpModuleCache[moduleId](module, module.exports, wpRequire);
        return cacheModule.exports;
    }
    // 载入 index.js
    var wpExports = wpRequire("./src/index.js");
})();
```

## 其他前端模块化的方案

CommonJS 的缺点：require 命令执行时会执行一个 js 文件，然后返回该模块的 exports 对象，这在服务器端可行，因为服务器端加载并执行一个文件的时间是可以忽略的，模块加载是同步的，但不适用与浏览器，浏览器每加载一个文件，就需要等 require 返回，影响性能。

为了解决这个问题，后面发展出众多的前端模块化规范，包括 CommonJS 大致有如下几种：

- JavaScript 模块化
    - 后端(Backend)
        - CommonJS: node.js
    - 前端(Frontend)
        - AMD: RequireJS
        - CMD: Sea.js
    - 后端+前端
        - ES6 Modules: ES6

### AMD (Asynchronous Module Definition)

RequireJS 是一个 js 文件和模块加载器，它非常适合在浏览器中使用，但它也可以用在其他 js 环境中，例如 Rhino 和 Node。使用 RequireJS 加载模块化脚本能提高代码的加载速度和质量。它解决了 CommonJS 规范不能用于浏览器端的问题，而 AMD 就是在 RequireJS 在推广过程中对模块定义的规范化产出。

下面是 AMD 规范的实现：
```html
<script src="https://cdn.bootcdn.net/ajax/libs/require.js/2.3.6/require.min.js"></script>
<script src="a.js"></script>
```
首先要在 html 文件中引入 require.js 工具库，就是这个库提供了定义模块、加载模块等功能。它提供了一个全局的 define 函数用来定义模块，所以在引入 require.js 后，再引入其他文件，都可以使用 define 来定义模块。
```js
define(id?, dependencies?, factory)
```
id: 可选参数，没有提供该参数，就使用 js 文件名(去除扩展名)。
dependencies: 可选参数，是一个数组，表示当前模块的依赖。
factory: 工厂方法，模块初始化要执行的函数或者对象，如果为函数，它应该只被执行一次，返回值便是模块要导出的值。如果是对象，此对象应该为模块的输出值。

RequireJS 已经在 2020.4 停止了更新。

### CMD
