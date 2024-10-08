# 使用 Maven 构建镜像
FROM maven:3.8.6-openjdk-8 AS build

# 设置工作目录
WORKDIR /app

# 复制 pom.xml
COPY pom.xml ./

# 复制其他源代码
COPY src ./src

# 构建项目
RUN mvn clean package -DskipTests

# 使用 OpenJDK 8 运行应用
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 从构建镜像中复制 JAR 文件
COPY --from=build /app/target/*.jar app.jar

# 暴露应用运行的端口
EXPOSE 9090

# 运行应用并激活 prod 配置文件
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
