# hy.microservice.timing 定时任务微服务


### 定时任务的监控界面
访问网址格式为：http://IP:Port/服务名/analyses/analyseObject?Job=Y
![image](hy.microservice.timing/images/Jobs.png)


### 定时任务灾备双活集群（支持云任务）：总统模式
1. 定时任务的灾备集群由2台或多台服务组成。

2. 多台定时任务服务器工作时，在同一时间，只有一台Master服务有权执行任务。其它服务为Slave服务，没有执行任务的权限。

3. 当原Master50.89宕机时，50.241自动接管执行任务的权限，成为新的Master服务。

4. 当原Master50.89只时暂时无法访问时（如网络原因），服务本是正常时，50.241先自动接管执行任务的权限，暂时成为新的Master服务，当50.89网络恢复时，再将执行权限还给50.89。

5. 多台定时任务服务间，每1分钟监测一次心跳。

6. 当连续心跳3次（数量可配置）均发现原Master服务异常时，才进行权限转移动作。

7. 为了保证Master服务的性能，只有其它Slave服务对整体集群做心跳监测。Master服务不再对其它Slave服务做心跳监测。

下面的组网图，为某项目的实战组网结构。
![image](hy.microservice.timing/images/定时任务灾备双活集群.png)



### 定时任务灾备双活集群服务的监控页面
![image](hy.microservice.timing/images/定时任务灾备双活集群.监控页面.png)