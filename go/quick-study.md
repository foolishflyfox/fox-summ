# Go 语言

## go 变量声明

```go
package main

import "fmt"

// 全局变量不能使用 gA := 100
var gA = 100

func main() {
	// 方法一：声明一个变量，默认值为 0
	var a int
	fmt.Println("a = ", a)
	fmt.Printf("type of a = %T\n", a)
	// 方法二：声明一个变量，初始化一个值
	var b int = 10
	fmt.Println("b = ", b)
	fmt.Printf("type of b = %T\n", b)

	// 方法三：通过自动推断
	var c = 100
	fmt.Println("c = ", c)
	fmt.Printf("type of c = %T\n", c)

	// 方法四: 省去 var，最常用
	e := 1000.0
	fmt.Println("e = ", e)
	fmt.Printf("type of e = %T\n", e)

	fmt.Println("gA = ", gA)

	// 声明多个变量
	var xx, yy = 100, 200
	fmt.Println("xx =", xx, ", yy = ", yy)
	// 多行多变量声明
	var (
		vv int  = 100
		jj bool = true
	)
	fmt.Println("vv = ", vv, ", jj = ", jj)
}
```

## go 常量声明

```go
package main

import "fmt"

const (
	BEIJING  = iota //0, iota = 0
	SHANGHAI        //1, iota = 1
	SHENZHEN        //2, iota = 2
)
const (
	APPLE  = iota * 10 // 0, iota = 0
	BANANA             // 10, iota = 1
	PEAR               // 20, iota = 2
)
const (
	a, b = iota + 1, iota + 2 // 1, 2
	c, d                      // 2, 3
	e, f                      // 3, 4

	g, h // 4,5
	i, j // 5, 6
)

func main() {
	const length int = 10

	fmt.Println("length = ", 10)

	// length = 100 // 常量不允许修改

	fmt.Println("SHANGHAI = ", SHANGHAI)
	fmt.Println("BANANA = ", BANANA)

	fmt.Printf("a = %v, b = %v, c = %v, d = %v, e = %v, "+
		"f = %v, g = %v, h = %v, i = %v, j = %v\n", a, b, c, d, e, f, g, h, i, j)
}
```

## 函数声明

```go
package main

import "fmt"

func foo1(a string, b int) int {
	fmt.Println("a = ", a)
	fmt.Println("b = ", b)
	c := 100
	return c
}

func foo2(a string, b int) (int, int) {
	fmt.Println("a = ", a)
	fmt.Println("b = ", b)
	return 5, 6
}

func foo3() (r1 int, r2 int) {
	r1 = 111
	r2 = 222
	return
}

func foo4() (r1, r2 int) {
	r1 = 1111
	r2 = 2222
	return
}

func foo5() (r1, r2 int) {
	r1 = 1111
	r2 = 2222
	return
}

func main() {
	c := foo1("abc", 1)
	fmt.Println("c = ", c)

	r1, r2 := foo2("xxx", 2)
	fmt.Printf("foo2: r1 = %v, r2 = %v\n", r1, r2)

	r1, r2 = foo3()
	fmt.Printf("foo3: r1 = %v, r2 = %v\n", r1, r2)

	r1, r2 = foo4()
	fmt.Printf("foo4: r1 = %v, r2 = %v\n", r1, r2)
}
```



