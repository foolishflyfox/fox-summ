# 常用信息

## 文件管理规范

- 自定义软件的安装位置：所有的自定义软件(通过下载后解压得到的可执行文件)放置在 */usr/local/software/bin* 下，如果软件附带其他的文件形成文件夹，则该文件夹放置在 */usr/local/software* 下，并将可执行文件软链接到 */usr/local/software/bin* 下。
- 自定义软件必须带上版本信息。
- */usr/local/software* 文件夹必须保持整洁，不能有解压文件。

对于有的不能使用软链接的情况(例如nacos，会根据相对路径找jar文件)，则只能将路径加入到 PATH 中了。