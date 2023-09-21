package org.hy.microservice.timing.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.thread.Job;
import org.hy.common.thread.Jobs;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.plugins.analyse.AnalyseBase;





/**
 * 任务配置信息的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-12
 * @version     v1.0
 */
@Xjava
public class JobConfigService implements IJobConfigService ,Serializable
{
    
    private static final long serialVersionUID = 1688409498515125449L;

    @Xjava
    private IJobConfigDAO jobConfigDAO;
    
    @Xjava
    private AnalyseBase   analyseBase;
    
    
    
    /**
     * 查询云主机的通讯IP和端口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     * 
     * @return
     */
    @Override
    public List<Param> queryCloudServers()
    {
        return this.jobConfigDAO.queryCloudServers();
    }
    
    
    
    /**
     * 查询任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<JobConfigReport> queryList()
    {
        List<JobConfigReport>  v_Reports         = new ArrayList<JobConfigReport>();
        Map<String ,Job>       v_JobsMM          = XJava.getObjects(Job.class ,false);
        List<JobConfig>        v_JobsDB          = this.jobConfigDAO.queryList();
        Map<String ,JobConfig> v_JobsDM          = null;
        Date                   v_ServerStartTime = new Date(this.analyseBase.analyseCluster_Info().getStartTime());
        
        if ( !Help.isNull(v_JobsDB) )
        {
            v_JobsDM = (Map<String ,JobConfig>) Help.toMap(v_JobsDB ,"code");
        }
        
        for (Entry<String, Job> v_Item : v_JobsMM.entrySet())
        {
            JobConfigReport v_JobReport = new JobConfigReport(v_Item.getKey() ,v_Item.getValue());
            
            if ( !Help.isNull(v_JobsDM) )
            {
                JobConfig v_JobDB = v_JobsDM.get(v_JobReport.getJobID());
                if ( v_JobDB != null )
                {
                    v_JobReport.setReadOnly(false);
                    v_JobReport.setId(               v_JobDB.getId());
                    v_JobReport.setCode(             v_JobDB.getCode());
                    v_JobReport.setName(             v_JobDB.getName());
                    v_JobReport.setIntervalTypeValue(v_JobDB.getIntervalType());
                    v_JobReport.setXid(              v_JobDB.getXid());
                    v_JobReport.setMethodName(       v_JobDB.getMethodName());
                    v_JobReport.setCondition(        v_JobDB.getCondition());
                    v_JobReport.setTryMaxCount(      v_JobDB.getTryMaxCount());
                    v_JobReport.setTryIntervalLen(   v_JobDB.getTryIntervalLen());
                    v_JobReport.setComment(          v_JobDB.getComment());
                    v_JobReport.setIsEnabled(        v_JobDB.getIsEnabled());
                    v_JobReport.setIsDel(            v_JobDB.getIsDel());
                    v_JobReport.setCreateUserID(     v_JobDB.getCreateUserID());
                    v_JobReport.setCreateTime(       v_JobDB.getCreateTime());
                    v_JobReport.setUpdateTime(       v_JobDB.getUpdateTime());
                    v_JobReport.setStartTimes(       v_JobDB.toStartTimes());
                }
            }
            
            if ( v_JobReport.getReadOnly() )
            {
                Job v_JobMM = v_JobsMM.get(v_JobReport.getJobID());
                if ( v_JobMM != null )
                {
                    v_JobReport.setCode(             v_JobMM.getCode());
                    v_JobReport.setName(             v_JobMM.getName());
                    v_JobReport.setIntervalTypeValue(v_JobMM.getIntervalType());
                    v_JobReport.setXid(              v_JobMM.getXid());
                    v_JobReport.setMethodName(       v_JobMM.getMethodName());
                    v_JobReport.setCondition(        v_JobMM.getCondition());
                    v_JobReport.setTryMaxCount(      v_JobMM.getTryMaxCount());
                    v_JobReport.setTryIntervalLen(   v_JobMM.getTryIntervalLen());
                    v_JobReport.setComment(          v_JobMM.getComment());
                    v_JobReport.setIsEnabled(        1);
                    v_JobReport.setIsDel(            0);
                    v_JobReport.setCreateUserID(     "msTiming");
                    v_JobReport.setCreateTime(       v_ServerStartTime);
                    v_JobReport.setStartTimes(       v_JobMM.getStartTimes());
                }
            }
            
            v_Reports.add(v_JobReport);
        }
        
        Help.toSort(v_Reports ,"nextTime" ,"lastTime" ,"intervalType" ,"intervalLen NUMASC" ,"jobID");
        
        return v_Reports;
    }
    
    
    
    /**
     * 按XID查询任务配置信息
     * 
     * 注意：使用查询缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     * 
     * @param i_ID  任务配置信息ID
     *
     * @return
     */
    @Override
    public JobConfig queryByID(String i_ID)
    {
        JobConfig v_JobConfig = new JobConfig();
        v_JobConfig.setId(i_ID);
        
        return this.queryByIDCode(v_JobConfig);
    }
    
    
    
    /**
     * 按Code查询任务配置信息
     * 
     * 注意：使用查询缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     * 
     * @param i_Code  任务配置编号
     *
     * @return
     */
    @Override
    public JobConfig queryByCode(String i_Code)
    {
        JobConfig v_JobConfig = new JobConfig();
        v_JobConfig.setCode(i_Code);
        
        return this.queryByIDCode(v_JobConfig);
    }
    
    
    
    /**
     * 按ID或Code查询任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     * 
     * @param i_JobConfig  任务配置信息
     * @return
     */
    @Override
    public JobConfig queryByIDCode(JobConfig i_JobConfig)
    {
        return this.jobConfigDAO.queryByIDCode(i_JobConfig);
    }
    
    
    
    /**
     * 新增、修改、逻辑删除任务配置信息
     * 
     *  {
     *      "code": "Job_Test_001",
     *      "name": "测试",
     *      "xid": "JobConfigService",
     *      "methodName": "queryList",
     *      "intervalType": 60,
     *      "intervalLen": 1,
     *      "startTime": "2023-09-12 00:00:00",
     *      "createUserID": "ZhengWei"
     *  }
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @param io_JobConfig  任务配置信息对象
     * @return
     */
    @Override
    public JobConfig save(JobConfig io_JobConfig)
    {
        if ( io_JobConfig == null )
        {
            return null;
        }
        
        boolean v_IsNew = false;
        if ( Help.isNull(io_JobConfig.getId()) )
        {
            io_JobConfig.setId(StringHelp.getUUID());
            v_IsNew = true;
        }
        
        io_JobConfig.setCreateUserID(Help.NVL(io_JobConfig.getCreateUserID() ,io_JobConfig.getUserID()));
        io_JobConfig.setIsEnabled(Help.NVL(io_JobConfig.getIsEnabled() ,1));
        io_JobConfig.setIsDel(Help.NVL(io_JobConfig.getIsDel() ,0));
        io_JobConfig.setXJavaID(io_JobConfig.getCode());
        
        boolean v_Ret = this.jobConfigDAO.save(io_JobConfig ,io_JobConfig.makeStartTimes());
        if ( v_Ret && io_JobConfig.getIsEnabled() == 1 )
        {
            Jobs v_Jobs = (Jobs) XJava.getObject("JOBS_MS_Common");
            Job  v_Job  = io_JobConfig.newJob();
            
            if ( v_IsNew )
            {
                v_Jobs.addJob(v_Job);
                XJava.putObject(v_Job.getCode() ,v_Job);
            }
            else if ( !io_JobConfig.getIsDel().equals(0) )
            {
                if ( !Help.isNull(io_JobConfig.getCodeOld()) )
                {
                    v_Job.setCode(io_JobConfig.getCodeOld());
                }
                XJava.remove(v_Job.getCode());
                delJobByJobs(v_Jobs ,v_Job.getCode());
            }
            else
            {
                if ( !Help.isNull(io_JobConfig.getCodeOld()) )
                {
                    v_Job.setCode(io_JobConfig.getCodeOld());
                }
                XJava.remove(v_Job.getCode());
                delJobByJobs(v_Jobs ,v_Job.getCode());
                
                v_Job.setCode(io_JobConfig.getXJavaID());
                v_Jobs.addJob(v_Job);
                XJava.putObject(v_Job.getCode() ,v_Job);
            }
        }
        
        return v_Ret ? io_JobConfig : null;
    }
    
    
    
    /**
     * 删除定时任务池中的任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     *
     * @param i_Jobs   定时任务池
     * @param i_Code   要删除任务编号
     */
    private void delJobByJobs(Jobs i_Jobs ,String i_Code)
    {
        Iterator<Job> v_JobList = i_Jobs.getJobs();
        while ( v_JobList.hasNext() )
        {
            Job v_Item = v_JobList.next();
            if ( v_Item.getCode().equals(i_Code) )
            {
                i_Jobs.delJob(v_Item);
                break;
            }
        }
    }
    
}
