## 应用说明
- eueka server：服务注册中心
  - port：2000
- hello:示例应用
  - port：2001
- cloud-config：配置中心
  - port：3000
  - 配置文件的映射规则
    - 文件名：{application}-{profile}.properties
    - /{application}/{profile}[/{label}]
     /{application}-{profile}.yml
     /{label}/{application}-{profile}.yml
     /{application}-{profile}.properties
     /{label}/{application}-{profile}.properties
    - {label}对应git上的分支名
    