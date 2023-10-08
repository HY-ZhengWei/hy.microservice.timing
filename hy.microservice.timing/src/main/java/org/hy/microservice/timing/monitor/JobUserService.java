package org.hy.microservice.timing.monitor;

import java.io.Serializable;
import java.util.List;

import org.hy.common.xml.annotation.Xjava;





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

    
    
    @Xjava
    private IJobUserDAO jobUserDAO;
    
    
    
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
    
}
