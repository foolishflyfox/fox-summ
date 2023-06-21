# ThingsPanel 

ThingsPanel 技术栈：

- 前端：
    - js框架: 2.6.12
- 后端
    - 语言: go
    - 框架: beego
- 通讯
    - mqtt broker: gmqtt
- 数据库
    - 结构化数据库: postgresSQL
    - 时序数据库: timescaledb

定位：帮助老手节省时间，帮助新手降低门槛。通过插件化让开发更为快速。

Q: 什么是 mqtt broker，gmqtt 是什么?

## ThingsPanel 工作流程

数据接入：

应用管理: 以设备插件的形式提供，相同数据输入形式的设备可以用相同的插件(可能设备不一样)。设备插件的开发量与设备数据的复杂度有关，例如 3D 数据的设备开发周期会比较长。

插件做好之后，数据发送到 broker 上面。broker 是什么？

broker 中的数据会自动进入可视化、数据管理中。

自动化对数据进行处理，例如根据数据进行告警，可以配置告警策略。还可以根据数据 进行控制操作，可以配置控制策略。

数据管理：
    - 可视化
        - 实时数据图表
        - 报表功能
    - 输出
        - 自行开发接口

协议兼容：TCP、MQTT

## 插件开发

### 温度插件实例

复制一个原有插件。

编写配置：
```yaml
# config.yaml
humiture:
  type: 'app'
  name: 'humiture'
  # 设备名称
  device: 'humiture 传感器'
  description: '插件'
  widgets:
    humiture_line:
      name: 'humiture曲线'
      description: '曲线插件图表'
      receiver: ''
      template: 'huminiture-line'
      fields:
        temp:
          name: '温度'
          type: 3
          symbol: ''
        hum:
          name: '湿度'
          type: 3
          symbol: ''
```

开发图表组件(本质上是修改 vue 项目  )。