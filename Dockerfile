# 基于原作者的 Dockerfile 使用单阶段构建
FROM openjdk:8-jdk-alpine

# 安装 Maven 和更新包管理器
RUN apk update && apk add --no-cache maven

# 设置工作目录
WORKDIR /home/eladmin

# 复制所有项目文件到容器中
COPY . .

# 构建项目并运行应用
CMD ["sh", "-c", "mvn clean package -DskipTests && java -jar target/*.jar"]
