package org.hy.microservice.timing.monitor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hy.common.Date;
import org.hy.common.ExpireMap;
import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.thread.Job;
import org.hy.common.thread.Jobs;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.timing.job.IJobConfigDAO;
import org.hy.microservice.timing.job.JobConfig;
import org.hy.microservice.timing.message.MessageService;





/**
 * 任务配置信息的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-10-08
 * @version     v1.0
 */
@Xjava
public class JobUserService implements IJobUserService ,Serializable
{
    
    private static final long serialVersionUID = 538775603292919172L;
    
    /** 向责任人发送告警消息的最后时间 */
    private static final ExpireMap<String ,Date> $MessageToUserLastTime = new ExpireMap<String ,Date>();

    
    
    @Xjava
    private IJobUserDAO    jobUserDAO;
    
    @Xjava
    private IJobConfigDAO  jobConfigDAO;
    
    @Xjava
    private MessageService messageService;
    
    @Xjava(ref="MS_Timing_Monitor_Signature")
    private Param          signature;
    
    @Xjava(ref="MS_Timing_Monitor_TimeLen")
    private Param          timeLen;
    
    @Xjava(ref="MS_Timing_Monitor_WaitTimeLen")
    private Param          waitTimeLen;
    
    
    
    /**
     * 查询所有责任人列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-07
     * @version     v1.0
     * 
     * @return
     */
    @Override
    public List<JobUser> queryAll()
    {
        return this.jobUserDAO.queryAll();
    }
    
    
    
    /**
     * 按责任人ID，查询责任人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-08
     * @version     v1.0
     * 
     * @param i_UserID  责任人的ID
     * @return
     */
    @Override
    public JobUser queryByUserID(String i_UserID)
    {
        return this.jobUserDAO.queryByUserID(i_UserID);
    }
    
    
    
    /**
     * 查询具体定时任务的责任人列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-07
     * @version     v1.0
     * 
     * @param i_JobID  定时任务的ID
     * @return
     */
    @Override
    public List<JobUser> queryByJobID(String i_JobID)
    {
        return this.jobUserDAO.queryByJobID(i_JobID);
    }
    
    
    
    /**
     * 新增、修改、逻辑删除任务责任人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-16
     * @version     v1.0
     *
     * @param io_JobUser  定时任务责任人
     * @return
     */
    @Override
    public JobUser save(JobUser io_JobUser)
    {
        if ( io_JobUser == null )
        {
            return null;
        }
        
        if ( Help.isNull(io_JobUser.getId()) )
        {
            io_JobUser.setId("JU" + StringHelp.getUUID());
        }
        
        io_JobUser.setCreateUserID(Help.NVL(io_JobUser.getCreateUserID() ,io_JobUser.getUserID()));
        io_JobUser.setUpdateUserID(Help.NVL(io_JobUser.getUpdateUserID() ,io_JobUser.getUserID()));
        io_JobUser.setIsDel(       Help.NVL(io_JobUser.getIsDel() ,0));
        
        boolean v_Ret = this.jobUserDAO.save(io_JobUser);
        if ( v_Ret )
        {
            return io_JobUser;
        }
        else
        {
            return null;
        }
    }
    
    
    
    /**
     * 监控定时任务，发送异常信息给责任人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-08
     * @version     v1.0
     *
     */
    @SuppressWarnings("unchecked")
    public void monitorJobTask()
    {
        Jobs v_Jobs = (Jobs) XJava.getObject("JOBS_MS_Common");
        if ( !v_Jobs.isMaster() )
        {
            return;
        }
        
        Map<String ,Job>       v_JobsMM  = XJava.getObjects(Job.class ,false);
        List<JobConfig>        v_JobsDB  = this.jobConfigDAO.queryList();
        Map<String ,JobConfig> v_JobsDM  = null;
        
        if ( !Help.isNull(v_JobsDB) )
        {
            v_JobsDM = (Map<String ,JobConfig>) Help.toMap(v_JobsDB ,"code");
        }
        if ( Help.isNull(v_JobsDM) )
        {
            return;
        }
        
        Date v_Now = new Date();
        for (Entry<String, Job> v_Item : v_JobsMM.entrySet())
        {
            Job       v_JobMM = v_Item.getValue();
            JobConfig v_JobDB = v_JobsDM.get(v_Item.getKey());
            
            if ( v_JobDB == null )
            {
                // 仅告警用户级的
                continue;
            }
            
            if ( v_JobDB.getIsEnabled() == null || !v_JobDB.getIsEnabled().equals(1) )
            {
                // 仅告警用户的定时任务
                continue;
            }
            
            if ( v_JobMM.getRunCount() <= 0 || v_JobMM.getRunOKCount() == v_JobMM.getRunCount() )
            {
                // 初步过滤有异常信息的
                continue;
            }
            
            List<JobUser> v_JobUsers = this.queryByJobID(v_JobDB.getId());
            if ( Help.isNull(v_JobUsers) )
            {
                // 过滤无责任人的
                continue;
            }
            
            Object [] v_RunLogs = v_JobMM.getRunLogs().toArray();
            if ( Help.isNull(v_RunLogs) )
            {
                // 尚未调度过的
                continue;
            }

            String v_LastRunLog = v_RunLogs[v_RunLogs.length - 1].toString();
            if ( v_LastRunLog.indexOf("对方接收成功") >= 0 )
            {
                // 过滤最后一次调度成功的
                continue;
            }
            
            String [] v_LastRunLogArr = v_LastRunLog.split("\\.");
            Date v_LastRunLogTime = new Date(v_LastRunLogArr[0]);
            if ( v_Now.differ(v_LastRunLogTime) <= 1000 * 60 * Integer.parseInt(waitTimeLen.getValue()) )
            {
                // 多长时间内刚发生的异常，暂时不告警，因为它还有尝试执行成功的机会
                continue;
            }
            
            String v_Message = this.signature.getValue() + "定时-告警：" + Help.NVL(v_JobDB.getCloudServer()) + v_JobDB.getTaskDesc() + Date.getNowTime().getYMDHM();
            for (JobUser v_JobUser : v_JobUsers)
            {
                String v_MTUserLastTimeID = v_JobDB.getCode() + ":" + v_JobUser.getId();
                if ( $MessageToUserLastTime.get(v_MTUserLastTimeID) != null )
                {
                    // 预防内时间内重复告警，对责任人造成心理影响
                    continue;
                }
                
                if ( !Help.isNull(v_JobUser.getPhone()) )
                {
                    this.messageService.sms(v_JobUser.getPhone() ,v_Message);
                }
                
                if ( !Help.isNull(v_JobUser.getEmail()) )
                {
                    this.messageService.mail(v_JobUser.getEmail() ,"定时-告警：" + v_JobDB.getCode() ,v_Message);
                }
                
                if ( !Help.isNull(v_JobUser.getOpenID()) )
                {
                    this.messageService.weixin(v_JobUser.getOpenID() ,"定时-告警：" + v_JobDB.getCode() ,v_Message);
                }
                
                $MessageToUserLastTime.put(v_MTUserLastTimeID ,new Date() ,Integer.parseInt(timeLen.getValue()) * 60);
            }
        }
    }
    
}
