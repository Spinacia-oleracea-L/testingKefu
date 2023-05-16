#!/bin/bash 

# 结束tomcat
cd /mnt/tomcat8/bin/
./shutdown.sh
# 结束 root 用户 执行的所有 java 进程的命令（除了本身grep的这个之外）,免得没能成功结束tomcat
# 因为要重启，所以这个也不用结束了
# ps -ef | grep java | grep root | grep -v grep |awk '{print $2}' | xargs --no-run-if-empty kill -9


############支持包##############

cd ~
mkdir 123


##########################
# 启动tomcat，也就是--重启
reboot
