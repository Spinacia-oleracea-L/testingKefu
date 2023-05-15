#!/bin/bash 

# 结束tomcat
cd /mnt/tomcat8/bin/
./shutdown.sh
# 结束 root 用户 执行的所有 java 进程的命令（除了本身grep的这个之外）,免得没能成功结束tomcat
# 因为要重启，所以这个也不用结束了
# ps -ef | grep java | grep root | grep -v grep |awk '{print $2}' | xargs --no-run-if-empty kill -9


############支持包##############

# wangmarket
cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/wangmarket-*.jar
wget http://down.zvo.cn/wangmarket/version/v6.2/wangmarket-6.1.3.jar

# wm
cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/wm-*.jar
wget http://down.zvo.cn/wangmarket/version/v6.2/wm-3.1.jar


# templateCenter
if ls /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/wangmarket.plugin.templateCenter-*.jar 2>&1;then
	cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
	rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/wangmarket.plugin.templateCenter-*.jar
	wget http://down.zvo.cn/wangmarket/plugin/templateCenter/wangmarket.plugin.templateCenter-1.5.jar
fi

# siteapi
if ls /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/wangmarket.plugin.siteapi-*.jar 2>&1;then
	cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
	rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/wangmarket.plugin.siteapi-*.jar
	wget http://down.zvo.cn/wangmarket/plugin/siteapi/wangmarket.plugin.siteapi-2.1.jar
fi

# HtmlVisualEditor 可视化html编辑
cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/wangmarket.plugin.HtmlVisualEditor-*.jar
wget http://down.zvo.cn/wangmarket/plugin/HtmlVisualEditor/wangmarket.plugin.HtmlVisualEditor-3.0.1.jar


##########################
# 启动tomcat，也就是--重启
reboot
