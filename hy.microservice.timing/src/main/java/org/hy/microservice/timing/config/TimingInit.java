package org.hy.microservice.timing.config;

import java.util.Iterator;
import java.util.List;

import org.hy.common.Execute;
import org.hy.common.Help;
import org.hy.common.net.common.ClientCluster;
import org.hy.common.thread.Job;
import org.hy.common.thread.Jobs;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.timing.job.IJobConfigDAO;
import org.hy.microservice.timing.job.JobConfig;





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
    
    @Xjava
    private IJobConfigDAO jobConfigDAO;
    
    
    
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
        
        new Execute(this ,"addJobs").start();
    }
    
    
    
    public void addJobs()
    {
        Jobs            v_Jobs   = (Jobs)XJava.getObject("JOBS_MS_Common");
        List<JobConfig> v_JobsDB = this.jobConfigDAO.queryList();
        
        if ( !Help.isNull(v_JobsDB) )
        {
            for (JobConfig v_JobDB : v_JobsDB)
            {
                Job v_Job = v_JobDB.newJob();
                XJava.putObject(v_Job.getCode() ,v_Job);
                
                // 先删除，后添加，预防本方法被重复执行
                Iterator<Job> v_JobList = v_Jobs.getJobs();
                while ( v_JobList.hasNext() )
                {
                    Job v_Item = v_JobList.next();
                    if ( v_Item.getCode().equals(v_Job.getCode()) )
                    {
                        v_Jobs.delJob(v_Item);
                        break;
                    }
                }
                
                if ( v_JobDB.getIsEnabled() != null && v_JobDB.getIsEnabled().equals(1) )
                {
                    v_Jobs.addJob(v_Job);
                }
            }
        }
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
