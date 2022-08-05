# shell 编程之 if

主要对 springboot starter 中的mvnw 进行分析总结的经验。

## if 语句

参考: 

- https://blog.csdn.net/weixin_39665379/article/details/111171632

### if 的语法结构

if 的单分支语法为：

```shell
if [ 条件 ] ; then
    代码块
fi
```

if 的多分支语法为:

```shell
if [ 条件 ] ; then
    代码块1
elif
    代码块2
else
    代码块3
fi
```

### 条件

- 文件测试
    - `-e`: 测试是否存在
    - `-f`: 测试是否为文件
    - `-d`: 测试是否为目录(directory)
    - `-r`: 测试是否有读权限
    - `-w`: 测试是否有写权限
    - `-x`: 测试是否可执行
- 整数比较
    - `-eq`: 测试是否等于
    - `-ne`: 测试是否不等于
    - `-gt`: 测试是否大于
    - `-lt`: 测试是否小于
    - `-le`: 测试是否小于等于
    - `-ge`: 测试是否大于等于
- 字符串比较
    - `=`: 测试是否相同
    - `!=`: 测试是否不同
    - `-z`: 测试字符串是否为空
- 逻辑比较
    - `&&`: 与，例如 `if [ 5 -eq 5 ] && [ 1 -eq 1 ]; then echo abc; fi`
    - `||`: 或，例如 `if [ 1 -eq 5 ] || [ 1 -eq 1 ]; then echo abc; fi`
    - `!`: 非，例如 `if [ ! 1 -eq 5 ]; then echo abc; fi`

下面的脚本对上述文件条件进行测试：
```shell
#!/bin/sh

fileTypeTest() {
	path=$1
	if [ ! -e $path ]; then
		echo "$path isn't exist"
	elif [ -f $path ]; then
		echo "$path is file"
	elif [ -d $path ]; then
		echo "$path is directory"
	else
		echo "$path is other file"
	fi
}

fileModeTest() {
	path=$1
	a="'"
	if [ -r $path ]; then
		a="${a}r"
	fi
	if [ -w $path ]; then
		a="${a}w"
	fi
	if [ -x $path ]; then
		a="${a}x"
	fi
	a="${a}'"
	echo $path mode is $a
}

rm -rf a b c d

fileTypeTest ./a # 因为 a 没有创建，输出 "./a isn't exist"

touch b 
fileTypeTest ./b # 因为创建了 b，输出 "./b is file"

mkfifo c
fileTypeTest c # 因为 c 是管道文件，测试 -f 为 false，输出 "c is other file"

mkdir d
fileTypeTest d # 因为 d 是文件夹，输出 "d is directory"

chmod 0 b # 将 b 的权限清空

fileModeTest b # 因为 b 没有任何权限，输出为 "b mode is ''"

chmod u+r b
fileModeTest b # 添加 read 权限后，输出为 "b mode is 'r'"

chmod u+wx b
fileModeTest b # 添加了写和执行权限后，输出为 "b mode is 'rwx'"
```
