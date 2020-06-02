# 熔断监控仪表板
- name：hystrix-dashboard
- port：2800

## 使用说明
- 访问地址http://localhost:2800/hystrix
- 在打开的页面中，输入监控地址：

	E版本：http://localhost:2010/hystrix.stream 
	
	G版本：http://localhost:2010/actuator/hystrix.stream
- 点击下面的监控按钮，即可对service-ribbon服务进行监控