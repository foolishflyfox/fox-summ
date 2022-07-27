# python 接入 nacos 为 Java 提供微服务

【代码 nacos / nacos-python-demo】

参考：[Python发布微服务到注册中心Nacos](https://blog.csdn.net/m0_37892044/article/details/124167874)

## python 环境准备

Python 需要安装以下库：

- flask
- requests

如果是 python3，可以用 `pip3 install flask` 命令安装 flask。

## python 服务编写

python 向 nacos 注册服务的代码参见: *nacos/nacos-python-demo/python-src/nacos_service.py* 。

python 服务代码参见: *nacos/nacos-python-demo/python-src/test-service.py*

先后启动 *test-service.py*、*main.py* 即将 python 微服务集成到 nacos 中。

## Java 编写

根据 python 定义 feign 接口。如果 python 返回的是一个 json 字符串，java 端需要根据返回类型定义 bean。

