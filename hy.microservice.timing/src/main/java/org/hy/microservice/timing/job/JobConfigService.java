package org.hy.microservice.timing.job;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.thread.Job;
import org.hy.common.thread.Jobs;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;





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
    
    
    
    /**
     * 查询任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @return
     */
    @Override
    public List<JobConfig> queryList()
    {
        return this.jobConfigDAO.queryList();
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
        io_JobConfig.setIsDel(Help.NVL(io_JobConfig.getIsDel() ,0));
        io_JobConfig.setXJavaID(io_JobConfig.getCode());
        
        boolean v_Ret = this.jobConfigDAO.save(io_JobConfig ,io_JobConfig.makeStartTimes());
        if ( v_Ret )
        {
            Jobs v_Jobs = (Jobs) XJava.getObject("JOBS_MS_Common");
            Job  v_Job  = new Job();
            
            v_Job.setXJavaID(       io_JobConfig.getXJavaID());
            v_Job.setCode   (       io_JobConfig.getCode());
            v_Job.setName   (       io_JobConfig.getName());
            v_Job.setIntervalType(  io_JobConfig.getIntervalType());
            v_Job.setIntervalLen(   io_JobConfig.getIntervalLen());
            v_Job.setXid(           io_JobConfig.getXid());
            v_Job.setMethodName(    io_JobConfig.getMethodName());
            v_Job.setCloudServer(   io_JobConfig.getCloudServer());
            v_Job.setCondition(     io_JobConfig.getCondition());
            v_Job.setTryMaxCount(   io_JobConfig.getTryMaxCount());
            v_Job.setTryIntervalLen(io_JobConfig.getTryIntervalLen());
            v_Job.setComment(       io_JobConfig.getComment());
            v_Job.setStartTimes(    io_JobConfig.getStartTimes());
            
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
