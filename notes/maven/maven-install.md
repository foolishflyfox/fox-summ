# maven install 的使用

结论：
- 一个自定义项目被 `mvn install` 后，才能被其他项目依赖，mvn 查找依赖时会先找 `~/.m2/repository` 中的内容
- 存在父项目的子模块，在子模块目录下执行 `mvn install` 的打包不能被引用
- 存在多个子模块的父项目，在父项目中执行 `mvn install` ，子模块也会被打包，并且可以被引用
- 直接可以通过 `vim xxx.jar` 编辑 jar 文件中的内容，例如为 MANIFEST.MF 设置 Main-Class
- 打包后的 jar 文件中，不包含被引用的其他包的 class 文件
- 默认 `mvn package` 打包成 jar 的文件也是不包含其他包的，因此存在 `java.lang.NoClassDefFoundError` 问题，通过 one-jar 插件，可以将依赖的jar 包放在打包后的 jar 包中(位于 lib 文件夹中)

## mvn install 命令

代码【maven / install-demo】

### 库项目创建

foo 项目没有子项目，在执行 `mvn install` 会将整个项目打包。

foo 的坐标为:
```xml
<groupId>com.bfh</groupId>
<artifactId>foo</artifactId>
<version>0.0.1</version>
```
在其中只有一个简单的类：`com.bfh.foo.Tools`。

在该项目下执行 `mvn install`，会在 *~/.m2/repository* 下生成如下文件：
```shell
$ tree com/bfh/foo 
com/bfh/foo
├── 0.0.1
│   ├── _remote.repositories
│   ├── foo-0.0.1.jar
│   └── foo-0.0.1.pom
└── maven-metadata-local.xml
```

### 使用库项目

创建 usage-lib 项目，配置 pom 文件，在其中引人 foo 项目：
```xml
    <dependencies>
        <dependency>
            <groupId>com.bfh</groupId>
            <artifactId>foo</artifactId>
            <version>0.0.1</version>
        </dependency>
    </dependencies>
```
在 usage-lib 中即可使用类 `com.bfh.foo.Tools`:
```java
package com.bfh.usagelib;

import com.bfh.foo.Tools;

public class Main {
    public static void main(String[] args) {
        Tools.say();
    }
}
```
输出为：`hello, fff`。

### 子项目执行 install

创建项目 bar，其中有两个模块 bar-sub-a 和 bar-sub-b，其中 bar-sub-a 指定了父模块，而bar-sub-b 没有，即在 bar-sub-a 的 pom.xml 中多了如下内容：
```xml
    <parent>
        <artifactId>bar</artifactId>
        <groupId>com.bfh</groupId>
        <version>0.0.1</version>
    </parent>
```
分别在子项目中执行 `mvn install`，发现 bar-sub-a 打包的 jar 不能使用，而 bar-sub-b 打包的 jar 文件可以使用。

**结论：存在父项目的子模块单独执行 mvn install 的打包不能被引用**

### 含多个子项目的父项目 install

创建项目 baz，其中创建子模块 baz-a，在父项目中执行 `mvn install`，会在 *~/.m2/repository/com/bfh* 生成两个文件夹: baz 和 baz-a。其他项目可以依赖 baz-a 模块。
