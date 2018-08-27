# 基础镜像是java8 运行环境
FROM frolvlad/alpine-oraclejdk8:slim

#维护者的信息
MAINTAINER xiaw@mail.taiji.com.cn

#创建一个可以从本地主机或者其他容器挂载的挂载点，一般用来存放数据库和需要保持的数据等。
VOLUME /tmp

#向容器中复制文件
ADD eureka-server-0.0.1-SNAPSHOT.jar app.jar

#RUN bash -c 'touch /app.jar'

#容器启动后执行的命令，每个Dockerfile中只能有一个该指令，指定多个时，只有最后一个生效
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

#暴露的端口号
EXPOSE 2000