# vscode

## 简写

- `!`: 直接补齐 html5 框架

- `ul>li{这是第 $ 个li}*3`
    - `ul>li`: 表示 `<ul><li></li></ul>`
    - `li{xxx}`: 表示 `<li>xxx</li>`
    - `$`: 表示自动计数
    - `*3`: 表示自动生成3个

最终的效果为：
```html
<ul>
    <li>这是第 1 个li</li>
    <li>这是第 2 个li</li>
    <li>这是第 3 个li</li>
</ul>
```
