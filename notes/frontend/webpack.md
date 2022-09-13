# webpack 使用

前端工程化开发的4个特点：

- 模块化
- 组件化
- 规范化
- 自动化

前端自动化开发的一种具体实现就是 webpack。

## 使用 webpack 步骤

1. 新建项目空白目录，运行 `npm init -y` 命令，初始化包管理配置文件 *package.json*
2. 新建 src 源代码目录
3. 新建 src -> index.html 首页和 src -> index.js 脚本文件
4. 初始化首页基本的结构
5. 运行 npm install jquery -S 命令，安装 jquery，其中 npm install 可以简写为 npm i， -S 是 --save 的简写
6. 通过 ES6 模块化的方法导入 jQuery，实现列表隔行变色效果

代码: *frontend-codes/change-rows-color*。

### 安装 webpack

`npm install webpack@5.42.1 webpack-cli@4.7.2 -D`，其中 -D 是 --save-dev 的简写

package.json 中 dependencies 和 devDependencies 的区别：

- dependencies: 开发和生产环境都需要的包
- devDependencies: 只在开发阶段用到的辅助开发的包，生产环境不用的包

### 配置 webpack

创建名为 webpack.config.js 的 webpack 配置文件，并初始化如下的基本配置

```js
module.exports = {
    mode: 'development'  // mode 用来指定构建模式，可选值有 development 和 production
}
```

