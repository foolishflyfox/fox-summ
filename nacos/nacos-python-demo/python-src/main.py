from nacos_service import NacosService

nacosService = NacosService('localhost', 8848)
nacosService.addService('test-service', 'localhost', 2100)
nacosService.start()
