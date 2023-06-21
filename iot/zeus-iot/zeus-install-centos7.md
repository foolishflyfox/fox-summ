# zeus-iot 在 centos 7 下的安装

参考：

- [快速安装](https://zmops.io/docs/install_guide/quickinstall.html)
- [自定义安装](https://zmops.io/docs/install_guide/custominstall.html)

## 快速安装

```
$ curl -sL https://ghproxy.com/https://github.com/zmops/zeus-iot/raw/develop/docs/centos/install.sh | bash -s install
Step1: 初始化系统安装环境 ....  setenforce: SELinux is disabled
  [ OK ] 
Step2: 配置安装 YUM 源 ....    [ OK ] 
Step3: 安装 PostgreSQL ....    [ OK ] 
Step4: 编译安装 zabbix ....    [ OK ] 
Step5: 初始化 zabbix 数据库 ....    [ OK ] 
Step6: 启动 zabbix ....    [ OK ] 
Step7: 安装 zabbix-web ....    [ OK ] 
Step8: 启动 zabbix-web ....    [ OK ] 
Step9: 安装 taos 数据库 ....    [ OK ] 
zabbix 部分已安装成功，zeus iot 可以参照 www.zmops.com 官方文档自定义安装。

zabbix server 访问地址： http://<HostIP>/zabbix

登录用户名：Admin
登录密码：zabbix
```

但是安装的 tdengine 速度会非常慢。即使按下面的方式在卸载 tdengine 后重新安装，速度仍然很慢：

- 通过 `rpm -qa | grep tdengine` 找到对应的安装文件
- 通过 `rpm -e --nodeps tdengine-2.2.0.2-3.x86_64` 卸载文件
- 通过 `rpm -ivh TDengine-server-2.4.0.30-Linux-x64.rpm` 安装 TDengine

不过由于自定义安装会出现各种问题，在 centos 环境下还是建议使用快速安装的方式安装环境。

## 自定义安装

1. 修改磁盘空间，要求有 100G的空间
    1. 安装 gparted 用于硬盘扩容
        - `sudo yum install epel-release`
        - `sudo yum install gparted`
    2. 完成扩盘动作
2. 安装 tdengine
    1. 从 [官网](https://www.taosdata.com/all-downloads) 选择下载的安装包 TDengine-server-2.2.0.2-Linux-x64.deb
    2. 通过 `dpkg -i TDengine-server-2.2.0.2-Linux-x64.deb` 命令安装 TDengine
    3. 启动 TDengine: `systemctl start taosd`
    4. 通过 `taos` 命令即可进入 tdengine 数据库
3. 安装 PostgreSQL

```shell
# 安装 PostgreSQL 仓库源:

 sudo yum install -y https://download.postgresql.org/pub/repos/yum/reporpms/EL-7-x86_64/pgdg-redhat-repo-latest.noarch.rpm

# 安装 PostgreSQL:

 sudo yum install -y postgresql13-server

# 初始化数据库并启动:

 sudo /usr/pgsql-13/bin/postgresql-13-setup initdb
 sudo systemctl enable postgresql-13
 sudo systemctl start postgresql-13
```

4. 安装 zabbix

未完待续

## 编译 zeus-iot

1. 从 github 克隆仓库
    - `git clone https://github.com/zmops/zeus-iot.git`
    - `cd zeus-iot && git submodule update --init --recursive`
2. 通过命令 `mvn clean package -U -Dmaven.test.skip=true -Dos.detected.name=linux -Dos.detected.arch=x86_64` 进行编译，在编译 server-core 是会有关于 `JettyJsonHandler` 类的报错，解决方法为替换 maven 仓库中的 zeus-server-jetty-1.0.3-RELEASE.jar 文件。(结果测试，在 centos 7 上编译会出错，在 ubuntu、mac 上编译通过)
3. 生成的文件在 *dist* 中

## 初始化系统服务

- 进入到 `postgres` 用户下
- 创建 zeus-iot 数据库: `createdb -E Unicode -T template0 zeus-iot`
- 初始化数据库: `cat zeus-iot-bin/bin/sql/zeus-iot.sql | psql zeus-iot`

## 修改配置文件

zeus-iot 主要由 zeus-iot-server 和 webapp 两个服务造成，配置文件分别为 `./zeus-iot-bin/conf/application.yaml` 和 `./zeus-iot-bin/webapp/webapp.yaml`。

配置 zabbix token: zeus-iot 通过 zabbix token 与 zabbix api 进行权限认证，因此需要先在 zabbix 系统界面生成永久 token。

1. 登录 zabbix 管理界面进入 "User settings" ----- "API tokens" ----- "Create API token"
2. 创建名为 zeus 的 API token
3. 配置 zeus-iot 连接 zabbix token
4. 修改 webapp.yml，`vim ./zeus-iot-bin/webapp/webapp.yml`，将 zbxApiToken 改为刚刚创建的 token
5. 修改其他配置



