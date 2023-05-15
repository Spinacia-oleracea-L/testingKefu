版本更新控制

## 文件说明
#### version.list
版本列表，每行都代表一个版本升级，从几升级到几。格式为: 

````
v当前版本号-v要升级到的版本号,
````

比如:

````
v5.6.16-v5.6.17,
````

则是表示从5.6.16升级到5.6.17版本  
注意最后还有个, 主要是用于java内避免不识别换行，给个区分
#### vxxx-vxxxx.sh
具体哪个版本更新到哪个版本的linux更新命令。  
跟 version.list 中的每行都是一一对应的  
注意，**这个是仅仅适用于CentOS7.4的**  

## 升级时的逻辑

比如当前版本是 v5.6.16 ，检测当前是否有新版本的方式是先拉取 https://gitee.com/leimingyun/wangmarket_deploy/raw/master/upgrade/version.list ，判断其中是否存在 v5.6.16-vxxxx  这一行，如果存在，那证明当前版本存在更新的shell，那下一步就拉取 https://gitee.com/leimingyun/wangmarket_deploy/raw/master/upgrade/v5.6.16-vxxxx.sh 在服务器上执行了。  
  
注意，升级时应该对服务器最备份，以保障数据安全。  