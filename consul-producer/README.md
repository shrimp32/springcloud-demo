# 注册到consul的服务——服务提供者

## 访问 http://localhost:8081/producer

## 启动consul服务

#### 以开发模式启动

```
# 开发模式启动Consul代理
consul agent -dev
# 查看集群成员
consul members
# 查看
curl localhost:8500/v1/catalog/nodes
#使用DNS协议查看节点信息:
dig @127.0.0.1 -p 8600 consul-server-1.node.consul
```

#### 以server模式运行

```
consul agent -server -bootstrap-expect 1 -data-dir=/Users/xiaw/tmp/consul -node=consul-server-1  -bind=127.0.0.1 -client=0.0.0.0 -ui
```

- `-server` ： 定义agent运行在server模式
- `-bootstrap-expect` ：在一个datacenter中期望提供的server节点数目，当该值提供的时候，consul一直等到达到指定sever数目的时候才会引导整个集群，该标记不能和bootstrap共用
- `-data-dir`：data存放的目录
- `-bind`：该地址用来在集群内部的通讯，集群内的所有节点到地址都必须是可达的，默认是0.0.0.0
- `-node`：节点在集群中的名称，在一个集群中必须是唯一的，默认是该节点的主机名
- `-ui`： 可以访问UI界面
- `-rejoin`：使consul忽略先前的离开，在再次启动后仍旧尝试加入集群中。
- `-config-dir`：配置文件目录，里面所有以.json结尾的文件都会被加载
- `-client`：consul服务侦听地址，这个地址提供HTTP、DNS、RPC等服务，默认是127.0.0.1所以不对外提供服务，如果你要对外提供服务改成0.0.0.0

访问 http://localhost:8500

## 注意
consul中的yaml配置信息，不可以用tab代替空格
