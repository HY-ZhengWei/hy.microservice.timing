package org.hy.microservice.timing.job;

import org.hy.common.Date;
import org.hy.microservice.common.BaseViewMode;





/**
 * 任务开始时间组
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-12
 * @version     v1.0
 */
public class JobStartTime extends BaseViewMode
{

    private static final long serialVersionUID = -7500880157651340971L;
    
    
    /** 任务ID */
    private String jobID;
    
    /** 任务开始时间 */
    private Date   startTime;

    
    
    /**
     * 获取：任务ID
     */
    public String getJobID()
    {
        return jobID;
    }

    
    /**
     * 设置：任务ID
     * 
     * @param i_JobID 任务ID
     */
    public void setJobID(String i_JobID)
    {
        this.jobID = i_JobID;
    }

    
    /**
     * 获取：任务开始时间
     */
    public Date getStartTime()
    {
        return startTime;
    }

    
    /**
     * 设置：任务开始时间
     * 
     * @param i_StartTime 任务开始时间
     */
    public void setStartTime(Date i_StartTime)
    {
        this.startTime = i_StartTime;
    }
    
}
