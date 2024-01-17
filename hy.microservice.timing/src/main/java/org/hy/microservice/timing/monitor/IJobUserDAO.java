package org.hy.microservice.timing.monitor;

import java.util.List;

import org.hy.common.PartitionMap;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 定时任务的责任人的操作DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-10-07
 * @version     v1.0
 */
@Xjava(id="JobUserDAO" ,value=XType.XSQL)
public interface IJobUserDAO
{
    
    /**
     * 查询所有责任人列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-07
     * @version     v1.0
     * 
     * @return
     */
    @Xsql("XSQL_Timing_JobUser_Query_All")
    public List<JobUser> queryAll();
    
    
    
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
    @Xsql(id="XSQL_Timing_JobUser_Query_All" ,returnOne=true)
    public JobUser queryByUserID(@Xparam("id") String i_UserID);
    
    
    
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
    @Xsql(id="XSQL_Timing_JobUser_Query_ByJobID" ,returnOne=true)
    public List<JobUser> queryByJobID(@Xparam("jobID") String i_JobID);
    
    
    
    /**
     * 查询所有定时任务的责任人列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-17
     * @version     v1.0
     * 
     * @return
     */
    @Xsql("XSQL_Timing_JobUser_Query_ByJobID")
    public PartitionMap<String ,JobUser> queryByJobs();
    
    
    
    /**
     * 查询所有定时任务的责任人列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-17
     * @version     v1.0
     * 
     * @return
     */
    @Xsql("XSQL_Timing_JobUser_Query_OnlyHaveID")
    public PartitionMap<String ,JobUser> queryByJobsOnlyHaveID();
    

    
    /**
     * 新增、修改、逻辑删除任务责任人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-16
     * @version     v1.0
     *
     * @param i_JobUser 任务责任人
     * @return
     */
    @Xsql("GXSQL_Timing_JobUser_Save")
    public boolean save(JobUser i_JobUser);
    
}
