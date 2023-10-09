package org.hy.microservice.timing.cluster;

import java.io.Serializable;

import org.hy.common.Help;
import org.hy.common.thread.Job;
import org.hy.common.thread.Jobs;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.timing.job.IJobConfigService;
import org.hy.microservice.timing.job.JobConfig;





/**
 * 任务服务的集群相关的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-25
 * @version     v1.0
 */
@Xjava
public class TimingClusterService implements ITimingClusterService ,Serializable
{
    private static final long serialVersionUID = 7204415807002894605L;
    
    private static final Logger $Logger = new Logger(TimingClusterService.class);
    
    
    
    @Xjava
    private IJobConfigService jobConfigService;
    
    

    /**
     * 集群同步定时任务的信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-25
     * @version     v1.0
     *
     * @param i_JobID       任务ID
     * @param i_JobOldCode  旧的任务编号
     * @return              执行是否成功
     */
    @Override
    public boolean syncCluster(String i_JobID ,String i_JobOldCode)
    {
        $Logger.info(i_JobID ,i_JobOldCode);
        
        if ( Help.isNull(i_JobID) )
        {
            return false;
        }
        
        Jobs v_Jobs = (Jobs) XJava.getObject("JOBS_MS_Common");
        
        // 防止修改Code，先删除原Code
        if ( !Help.isNull(i_JobOldCode) )
        {
            if ( XJava.getObject(i_JobOldCode ,false) != null )
            {
                XJava.remove(i_JobOldCode);
            }
            this.jobConfigService.delJobByJobs(v_Jobs ,i_JobOldCode);
        }
        
        JobConfig v_JobDB = this.jobConfigService.queryByID(i_JobID);
        if ( v_JobDB == null )
        {
            return true;
        }
        
        // 重新添加前，先删除，再添加
        if ( !Help.isNull(v_JobDB.getCode()) )
        {
            if ( XJava.getObject(v_JobDB.getCode() ,false) != null )
            {
                XJava.remove(v_JobDB.getCode());
            }
            this.jobConfigService.delJobByJobs(v_Jobs ,v_JobDB.getCode());
        }
        
        v_JobDB.setStartTimes(v_JobDB.toStartTimes());
        Job v_Job = v_JobDB.newJob();
        XJava.putObject(v_Job.getCode() ,v_Job);
        
        if ( v_JobDB.getIsEnabled() != null && v_JobDB.getIsEnabled().equals(1) )
        {
            v_Jobs.addJob(v_Job);
        }
        
        return true;
    }
    
}
