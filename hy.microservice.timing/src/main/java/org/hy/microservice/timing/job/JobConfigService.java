package org.hy.microservice.timing.job;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hy.common.Help;
import org.hy.common.PartitionMap;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.net.ClientSocketCluster;
import org.hy.common.thread.Job;
import org.hy.common.thread.Jobs;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.analyse.AnalyseBase;
import org.hy.microservice.common.BaseViewMode;
import org.hy.microservice.timing.jobHttp.XJobHttp;
import org.hy.microservice.timing.monitor.IJobUserDAO;
import org.hy.microservice.timing.monitor.JobUser;





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
    
    private static final Logger $Logger = new Logger(JobConfigService.class);

    
    
    @Xjava
    private IJobConfigDAO jobConfigDAO;
    
    @Xjava
    private IJobUserDAO   jobUserDAO;
    
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
     * 按ID或Code查询任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-09
     * @version     v1.0
     *
     * @return
     */
    @Override
    public List<JobConfigReport> queryByIDCodeForReport(JobConfig i_JobConfig)
    {
        List<JobConfigReport> v_Reports = new ArrayList<JobConfigReport>();
        JobConfig             v_JobDB   = this.jobConfigDAO.queryByIDCode(i_JobConfig);
        Job                   v_JobMM   = null;
        JobConfigReport       v_Report  = null;
        
        if ( v_JobDB == null )
        {
            if ( !Help.isNull(i_JobConfig.getCode()) )
            {
                v_JobMM = (Job) XJava.getObject(i_JobConfig.getCode());
                v_Report = this.toJobConfigReport(v_JobMM ,null);
                v_Reports.add(v_Report);
            }
            return v_Reports;
        }
        
        v_JobMM = (Job) XJava.getObject(v_JobDB.getCode());
        if ( v_JobMM == null )
        {
            return v_Reports;
        }
        
        v_Report = this.toJobConfigReport(v_JobMM ,v_JobDB);
        v_Report.setJobUsers(this.jobUserDAO.queryByJobID(v_JobDB.getId()));
        
        if ( !Help.isNull(v_Report.getJobUsers()) )
        {
            // 除了用户ID保留外，其它均不返回
            for (JobUser v_JobUser : v_Report.getJobUsers())
            {
                v_JobUser.setUserName(null);
                v_JobUser.setPhone(null);
                v_JobUser.setEmail(null);
                v_JobUser.setOpenID(null);
                v_JobUser.setCreateTime(null);
                v_JobUser.setUpdateTime(null);
                v_JobUser.setCreateUserID(null);
                v_JobUser.setUpdateUserID(null);
            }
        }
        
        v_Reports.add(v_Report);
        return v_Reports;
    }
    
    
    
    /**
     * 查询任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @param i_JobConfig  任务配置信息
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<JobConfigReport> queryList(JobConfig i_JobConfig)
    {
        List<JobConfigReport>         v_Reports  = new ArrayList<JobConfigReport>();
        Map<String ,Job>              v_JobsMM   = XJava.getObjects(Job.class ,false);
        List<JobConfig>               v_JobsDB   = this.jobConfigDAO.queryList(i_JobConfig);
        PartitionMap<String ,JobUser> v_JobsUser = this.jobUserDAO.queryByJobsOnlyHaveID();
        Map<String ,JobConfig>        v_JobsDM   = null;
        
        if ( !Help.isNull(v_JobsDB) )
        {
            v_JobsDM = (Map<String ,JobConfig>) Help.toMap(v_JobsDB ,"code");
        }
        
        for (Entry<String, Job> v_Item : v_JobsMM.entrySet())
        {
            JobConfig v_JobDB = null;
            Job       v_JobMM = v_Item.getValue();
            
            if ( !Help.isNull(v_JobsDM) )
            {
                v_JobDB = v_JobsDM.get(v_Item.getKey());
            }
            
            if ( i_JobConfig != null && !Help.isNull(i_JobConfig.getProjectID()) )
            {
                if ( v_JobDB == null )
                {
                    // 不用再判定数据库中的查询结果了，因为已在SQL条件中过滤了
                    continue;
                }
            }
            
            JobConfigReport v_Report = this.toJobConfigReport(v_JobMM ,v_JobDB);
            if ( v_JobDB != null )
            {
                v_Report.setJobUsers(v_JobsUser.get(v_JobDB.getId()));
            }
            
            // 为了网络传输性能，查询列表时，不传输日志
            v_Report.setRunLogs(null);
            v_Reports.add(v_Report);
        }
        
        Help.toSort(v_Reports ,"isEnabled DESC" ,"nextTime" ,"lastTime" ,"intervalType" ,"intervalLen NUMASC" ,"jobID");
        
        return v_Reports;
    }
    
    
    
    /**
     * 将XJava对象池中的与数据库中定时任务对象合并成JobConfigReport对外安全的暴露
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-25
     * @version     v1.0
     *
     * @param i_JobMM  XJava对象池中的定时任务对象
     * @param i_JobDB  数据库中的定时任务对象
     * @return
     */
    @Override
    public JobConfigReport toJobConfigReport(Job i_JobMM ,JobConfig i_JobDB)
    {
        JobConfigReport v_JobReport = null;
        
        if ( i_JobDB != null )
        {
            v_JobReport = new JobConfigReport(i_JobMM.getXJavaID() ,i_JobMM ,i_JobDB.getIsEnabled() == 1);
            v_JobReport.setReadOnly(false);
            v_JobReport.setId(               i_JobDB.getId());
            v_JobReport.setCode(             i_JobDB.getCode());
            v_JobReport.setProjectID(        i_JobDB.getProjectID());
            v_JobReport.setName(             i_JobDB.getName());
            v_JobReport.setIntervalTypeValue(i_JobDB.getIntervalType());
            v_JobReport.setXid(              i_JobDB.getXid());
            v_JobReport.setMethodName(       i_JobDB.getMethodName());
            v_JobReport.setCondition(        i_JobDB.getCondition());
            v_JobReport.setTryMaxCount(      i_JobDB.getTryMaxCount());
            v_JobReport.setTryIntervalLen(   i_JobDB.getTryIntervalLen());
            v_JobReport.setComment(          i_JobDB.getComment());
            v_JobReport.setIsEnabled(        i_JobDB.getIsEnabled());
            v_JobReport.setIsDel(            i_JobDB.getIsDel());
            v_JobReport.setCreateUserID(     i_JobDB.getCreateUserID());
            v_JobReport.setUpdateUserID(     i_JobDB.getUpdateUserID());
            v_JobReport.setUserID(           i_JobDB.getCreateUserID());
            v_JobReport.setCreateTime(       i_JobDB.getCreateTime());
            v_JobReport.setUpdateTime(       i_JobDB.getUpdateTime());
            v_JobReport.setStartTimes(       i_JobDB.toStartTimes());
            
            if ( Help.isNull(v_JobReport.getCloudServer()) )
            {
                Object v_XObject = XJava.getObject(v_JobReport.getXid() ,false);
                if ( v_XObject instanceof XJobHttp )
                {
                    XHttp v_XHttp = ((XJobHttp)v_XObject).getTaskHttp();
                    v_JobReport.setCloudType("H:" + v_XHttp.getIp() + ":" + v_XHttp.getPort());
                }
                else
                {
                    v_JobReport.setCloudType("");
                }
            }
            else
            {
                v_JobReport.setCloudType("S:" + v_JobReport.getCloudServer());
            }
        }
        
        if ( v_JobReport == null )
        {
            v_JobReport = new JobConfigReport(i_JobMM.getXJavaID() ,i_JobMM ,true);
        }
        
        if ( v_JobReport.getReadOnly() )
        {
            v_JobReport.setCode(             i_JobMM.getCode());
            v_JobReport.setName(             i_JobMM.getName());
            v_JobReport.setProjectID(        "");
            v_JobReport.setIntervalTypeValue(i_JobMM.getIntervalType());
            v_JobReport.setXid(              i_JobMM.getXid());
            v_JobReport.setMethodName(       i_JobMM.getMethodName());
            v_JobReport.setCondition(        i_JobMM.getCondition());
            v_JobReport.setTryMaxCount(      i_JobMM.getTryMaxCount());
            v_JobReport.setTryIntervalLen(   i_JobMM.getTryIntervalLen());
            v_JobReport.setComment(          i_JobMM.getComment());
            v_JobReport.setIsEnabled(        1);
            v_JobReport.setIsDel(            0);
            v_JobReport.setCreateUserID(     "msTiming");
            v_JobReport.setUpdateUserID(     "msTiming");
            v_JobReport.setUserID(           "msTiming");
            v_JobReport.setCreateTime(       BaseViewMode.$StartupTime);
            v_JobReport.setStartTimes(       i_JobMM.getStartTimes());
            v_JobReport.setCloudType("");
        }
        
        return v_JobReport;
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
            io_JobConfig.setId("JC" + StringHelp.getUUID());
            v_IsNew = true;
        }
        else
        {
            JobConfig v_Old = this.queryByCode(io_JobConfig.getCode());
            
            v_Old.setStartTimes(v_Old.toStartTimes());
            
            io_JobConfig.setProjectID(      Help.NVL(io_JobConfig.getProjectID()       ,v_Old.getProjectID()));
            io_JobConfig.setName(           Help.NVL(io_JobConfig.getName()            ,v_Old.getName()));
            io_JobConfig.setIntervalType(   Help.NVL(io_JobConfig.getIntervalType()    ,v_Old.getIntervalType()));
            io_JobConfig.setIntervalLen(    Help.NVL(io_JobConfig.getIntervalLen()     ,v_Old.getIntervalLen()));
            io_JobConfig.setStartTimes(     Help.NVL(io_JobConfig.getStartTimes()      ,v_Old.getStartTimes()));
            io_JobConfig.setXid(            Help.NVL(io_JobConfig.getXid()             ,v_Old.getXid()));
            io_JobConfig.setMethodName(     Help.NVL(io_JobConfig.getMethodName()      ,v_Old.getMethodName()));
            io_JobConfig.setCondition(      Help.NVL(io_JobConfig.getCondition()       ,v_Old.getCondition()));
            io_JobConfig.setTryMaxCount(    Help.NVL(io_JobConfig.getTryMaxCount()     ,v_Old.getTryMaxCount()));
            io_JobConfig.setTryIntervalLen( Help.NVL(io_JobConfig.getTryIntervalLen()  ,v_Old.getTryIntervalLen()));
            io_JobConfig.setComment(        Help.NVL(io_JobConfig.getComment()         ,v_Old.getComment()));
            // ""空字符串时，表示解除绑定
            // io_JobConfig.setCloudServerHost(Help.NVL(io_JobConfig.getCloudServerHost() ,v_Old.getCloudServerHost()));
        }
        
        io_JobConfig.setCreateUserID(Help.NVL(io_JobConfig.getCreateUserID() ,io_JobConfig.getUserID()));
        io_JobConfig.setUpdateUserID(Help.NVL(io_JobConfig.getUpdateUserID() ,io_JobConfig.getUserID()));
        io_JobConfig.setIsEnabled(   Help.NVL(io_JobConfig.getIsEnabled()    ,1));
        io_JobConfig.setIsDel(       Help.NVL(io_JobConfig.getIsDel()        ,0));
        io_JobConfig.setXJavaID(              io_JobConfig.getCode());
        
        boolean v_Ret = this.jobConfigDAO.save(io_JobConfig ,io_JobConfig.makeStartTimes() ,io_JobConfig.getJobUsers());
        if ( v_Ret )
        {
            Jobs v_Jobs = (Jobs) XJava.getObject("JOBS_MS_Common");
            Job  v_Job  = io_JobConfig.newJob();
            
            // 删除
            if ( !io_JobConfig.getIsDel().equals(0) || v_Job == null )
            {
                if ( !Help.isNull(io_JobConfig.getCodeOld()) )
                {
                    if ( XJava.getObject(io_JobConfig.getCodeOld() ,false) != null )
                    {
                        XJava.remove(io_JobConfig.getCodeOld());
                    }
                    delJobByJobs(v_Jobs ,io_JobConfig.getCodeOld());
                }
            }
            // 创建
            else if ( v_IsNew )
            {
                XJava.putObject(v_Job.getCode() ,v_Job);
                if ( io_JobConfig.getIsEnabled().equals(1) )
                {
                    v_Jobs.addJob(v_Job);
                }
            }
            // 修改
            else
            {
                // 防止修改Code，先删除原Code
                if ( !Help.isNull(io_JobConfig.getCodeOld()) )
                {
                    if ( XJava.getObject(io_JobConfig.getCodeOld() ,false) != null )
                    {
                        XJava.remove(io_JobConfig.getCodeOld());
                    }
                    delJobByJobs(v_Jobs ,io_JobConfig.getCodeOld());
                }
                
                // 重新添加前，先删除，再添加
                if ( !Help.isNull(io_JobConfig.getCode()) )
                {
                    if ( XJava.getObject(io_JobConfig.getCode() ,false) != null )
                    {
                        XJava.remove(io_JobConfig.getCode());
                    }
                    delJobByJobs(v_Jobs ,io_JobConfig.getCode());
                }
                
                v_Job.setCode(io_JobConfig.getXJavaID());
                XJava.putObject(v_Job.getCode() ,v_Job);
                if ( io_JobConfig.getIsEnabled().equals(1) )
                {
                    v_Jobs.addJob(v_Job);
                }
            }
            
            try
            {
                if ( v_Jobs.isDisasterRecovery() )
                {
                    ClientSocketCluster.sendCommands(v_Jobs.getDisasterRecoverys()
                                                    ,false
                                                    ,Jobs.$JOB_DisasterRecoverys_Timeout
                                                    ,"TimingClusterService"
                                                    ,"syncCluster"
                                                    ,new Object[] {io_JobConfig.getId() ,Help.NVL(io_JobConfig.getCodeOld() ,"-")}
                                                    ,true
                                                    ,"集群同步定时任务的信息");
                }
            }
            catch (Exception exce)
            {
                $Logger.error(exce);
            }
        }
        
        return v_Ret ? io_JobConfig : null;
    }
    
    
    
    public void syncCluster()
    {
        
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
    @Override
    public void delJobByJobs(Jobs i_Jobs ,String i_Code)
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
