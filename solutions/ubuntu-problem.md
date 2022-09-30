## ubuntu20.04

Q1: 执行 `apt-get install xxx` 总是显示 `‘The package needs to be reinstalled, but I can’t find an archive for it’`。
A1: 参考 https://www.jianshu.com/p/942c562065c8 ，解决方法：

```
//备份/var/lib/dpkg/status文件
sudo cp /var/lib/dpkg/status status.bak
//编辑该文件
sudo vim /var/lib/dpkg/status
//定位到出错的软件包，将该软件包的记录删除，保存，就完成了。
```

