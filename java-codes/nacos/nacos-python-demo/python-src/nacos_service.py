import threading
import requests
import time

# 自定义服务的信息
# class CustomService:
#     def __init__(self, name, ip, port):
#         self.name = name
#         self.ip = ip
#         self.port = port

# 参考 https://nacos.io/zh-cn/docs/open-api.html
# 服务启动流程
# 1. 通过 addService 添加多个自定义 python 服务
# 2. 通过 start 启动服务，NacosService 会自动向注册中心暴露服务并维持心跳
class NacosService:
    # 请求地址
    instancePath = "/nacos/v1/ns/instance"
    instanceBeatPath = "/nacos/v1/ns/instance/beat"

    def __init__(self, nacosIP, nacosPort):
        self.ip = nacosIP
        self.port = nacosPort
        self.instanceUrls = []
        self.instanceBeatUrls = []
        # 心跳周期，单位 s
        self.heatBeat = 5

    # def __init__(self, nacosIP):
    #     self.__init__(nacosIP, 8848)

    # def __init__(self):
    #     self.__init__("127.0.0.1")

    def addService(self, serviceName, serviceIP, servicePort):
        # customService = CustomService(serviceName, serviceIP, servicePort)
        self.instanceUrls.append("http://{}:{}{}?serviceName={}&ip={}&port={}".format(
            self.ip, self.port, NacosService.instancePath, serviceName, serviceIP, servicePort))
        self.instanceBeatUrls.append("http://{}:{}{}?serviceName={}&ip={}&port={}".format(
            self.ip, self.port, NacosService.instanceBeatPath, serviceName, serviceIP, servicePort))
    
    def keepAliveProcess(self):
        while True:
            for url in self.instanceBeatUrls:
                res = requests.put(url)
                print("心跳 {} 状态: {}".format(url, res.status_code))
                time.sleep(self.heatBeat)
    
    def start(self):
        for url in self.instanceUrls:
            res = requests.post(url)
            print("注册 {} 结果为 {}".format(url, res.status_code))
        threading.Timer(self.heatBeat, self.keepAliveProcess).start()

