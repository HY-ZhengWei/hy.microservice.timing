package org.hy.microservice.timing.config;

import java.util.List;

import org.hy.common.net.common.ClientCluster;
import org.hy.common.thread.Jobs;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;





/**
 * 定时任务服务的初始化类（通过Job的方式初始化）
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-12
 * @version     v1.0
 */
@Xjava
public class TimingInit
{
    
    /**
     * 执行初始化
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     */
    public void execute()
    {
        // 注册：定时任务服务的灾备机制的心跳任务
        Jobs v_Jobs = (Jobs)XJava.getObject("JOBS_MS_Common");
        XJava.putObject(Jobs.$JOB_DisasterRecoverys_Check ,v_Jobs.createDisasterRecoveryJob());
        
        this.init_ClusterServers();
    }
    
    
    
    /**
     * 通过灾备机制的服务列表，自动生成集群监控列表。不再麻烦用户配置两次
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-12-14
     * @version     v1.0
     */
    @SuppressWarnings("unchecked")
    private void init_ClusterServers()
    {
        List<ClientCluster> v_DisasterRecoverys = (List<ClientCluster>)XJava.getObject("MS_Timing_DisasterRecoverys");
        XJava.putObject("ClusterServers" ,v_DisasterRecoverys);
    }
    
}
