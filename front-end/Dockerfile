# 使用 Nginx 基础镜像
FROM nginx:alpine

# 设置工作目录（可选）
WORKDIR /usr/share/nginx/html

# 复制 Nginx 配置文件到镜像中
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 复制构建后的前端文件到 Nginx 的文档根目录
COPY dist /usr/share/nginx/html

# 设置环境变量（可选）
ENV NODE_ENV=production

# 添加健康检查（可选）
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost/ || exit 1

# 暴露 Nginx 的 80 端口
EXPOSE 80

# 启动 Nginx
CMD ["nginx", "-g", "daemon off;"]
