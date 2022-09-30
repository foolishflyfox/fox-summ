# zeus-iot 在 centos 7 下的安装

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

但是安装的 tdengine 速度会非常慢。修复方法，卸载 tdengine 后重新安装：

- 通过 `rpm -qa | grep tdengine` 找到对应的安装文件
- 通过 `rpm -e --nodeps tdengine-2.2.0.2-3.x86_64` 卸载文件
- 通过 `rpm -ivh TDengine-server-2.4.0.30-Linux-x64.rpm` 安装 TDengine

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






