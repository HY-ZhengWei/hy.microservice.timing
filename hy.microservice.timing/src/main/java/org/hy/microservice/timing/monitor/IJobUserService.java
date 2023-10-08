package org.hy.microservice.timing.monitor;

import java.util.List;





/**
 * 任务配置信息的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-10-08
 * @version     v1.0
 */
public interface IJobUserService
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
    public JobUser queryByUserID(String i_UserID);
    
    
    
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
    public List<JobUser> queryByJobID(String i_JobID);
    
}
