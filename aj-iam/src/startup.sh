#!/bin/bash

process_name="aj-iam-server.jar"
log_file="message.log"

# 1. 查找进程 ID（使用 -w 精确匹配）
pid=$(jps -l | grep -w "$process_name" | awk '{print $1}')

# 2. 如果存在旧进程，执行优雅停机并等待
if [ -n "$pid" ]; then
    echo "发现旧进程 (PID: $pid)，正在优雅停止..."
    kill -15 "$pid"

    # 等待旧进程退出，最多等待 30 秒
    timeout=30
    while [ $timeout -gt 0 ]; do
        if ! kill -0 "$pid" 2>/dev/null; then
            echo "旧进程已正常停止。"
            break
        fi
        sleep 1
        ((timeout--))
    done

    # 超时仍未退出，强制杀死
    if [ $timeout -eq 0 ]; then
        echo "优雅停机超时，强制杀死进程 $pid"
        kill -9 "$pid"
    fi
else
    echo "没有找到正在运行的进程 $process_name"
fi

# 3. 启动新程序
echo "正在启动新程序..."
nohup java -Xms512m -Xmx512m -jar ./$process_name > "$log_file" 2>&1 &
tail -f message.log
new_pid=$!
echo "程序已启动，新的 PID 为：$new_pid"

# 4. 提示查看日志（不阻塞脚本）
echo "您可以使用以下命令查看启动日志："
echo "tail -f $log_file"
