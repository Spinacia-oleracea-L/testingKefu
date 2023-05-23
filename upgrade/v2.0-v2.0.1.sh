#!/bin/bash 

# 结束tomcat
cd /mnt/tomcat8/bin/
./shutdown.sh
# 结束 root 用户 执行的所有 java 进程的命令（除了本身grep的这个之外）,免得没能成功结束tomcat
# 因为要重启，所以这个也不用结束了
# ps -ef | grep java | grep root | grep -v grep |awk '{print $2}' | xargs --no-run-if-empty kill -9


############支持包##############

# yunkefu
cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/yunkefu-*.jar
wget http://down.zvo.cn/yunkefu/v2.0/yunkefu-2.0.3.jar

# API
cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/yunkefu.plugin.api-*.jar
wget http://down.zvo.cn/yunkefu/plugin/api/yunkefu.plugin.api-1.5.jar

# wm
cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/wm-*.jar
wget http://down.zvo.cn/wangmarket/version/v6.2/wm-3.3.jar

# screenShare
if ls /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/yunkefu.plugin.screenShare-*.jar 2>&1;then
	cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
	rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/yunkefu.plugin.screenShare-*.jar
	wget http://down.zvo.cn/yunkefu/plugin/screenShare/yunkefu.plugin.screenShare-1.3.1.jar
fi

# kefujsCopyright
if ls /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/yunkefu.plugin.kefujsCopyright-*.jar 2>&1;then
	cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
	rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/yunkefu.plugin.kefujsCopyright-*.jar
	wget http://down.zvo.cn/yunkefu/plugin/kefujsCopyright/yunkefu.plugin.kefujsCopyright-1.3.2.jar
fi

# offlineWeixinNotification
if ls /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/yunkefu.plugin.kefujsCopyright-*.jar 2>&1;then
	cd /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/
	
	rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/yunkefu.plugin.offlineWeixinNotification-*.jar
	wget http://down.zvo.cn/yunkefu/plugin/offlineWeixinNotification/yunkefu.plugin.offlineWeixinNotification-1.3.2.jar
	
	rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/xnx3-weixin-*.jar
	wget http://down.zvo.cn/yunkefu/plugin/offlineWeixinNotification/xnx3-weixin-1.7.3.jar
	
	rm -rf /mnt/tomcat8/webapps/ROOT/WEB-INF/lib/http-*.jar
	wget http://down.zvo.cn/yunkefu/plugin/offlineWeixinNotification/http-1.1.jar
fi


##########################
# 启动tomcat，也就是--重启
reboot
