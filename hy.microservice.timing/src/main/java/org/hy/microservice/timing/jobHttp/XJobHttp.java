package org.hy.microservice.timing.jobHttp;

import java.io.Serializable;

import org.hy.common.Date;
import org.hy.common.Return;
import org.hy.common.XJavaID;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.log.Logger;





/**
 * 定时任务请求的XJava实例类。整合两个XHttp类。
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-03
 * @version     v1.0
 */
public class XJobHttp implements XJavaID ,Serializable
{
    
    private static final long serialVersionUID = 4710293651795675767L;

    private static final Logger $Logger        = new Logger(XJobHttp.class);
    
    public  static final int    $Succeed       = 200;
    
    
    /** 定时任务请求 */
    private JobHttp                    jobHttp;
    
    /** 数据请求 */
    private XHttp                      taskHttp;
    
    /** 获取Token的数据请求 */
    private XHttp                      tokenHttp;
    
    /** 是否在执行中 */
    private boolean                    isRunning;
    
    /** 执行开始时间 */
    private Date                       runStartTime;
    
    
    
    public XJobHttp(JobHttp i_JobHttp)
    {
        this.jobHttp = i_JobHttp;
    }
    
    
    
    /**
     * 执行数据推送
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-11-13
     * @version     v1.0
     *
     * @return
     */
    public Return<Object> execute()
    {
        return null;
    }
    
    
    /**
     * 获取：定时任务请求
     */
    public JobHttp getJobHttp()
    {
        return jobHttp;
    }

    
    /**
     * 设置：定时任务请求
     * 
     * @param i_JobHttp 定时任务请求
     */
    public void setJobHttp(JobHttp i_JobHttp)
    {
        this.jobHttp = i_JobHttp;
    }


    /**
     * 获取：数据请求
     */
    public XHttp getTaskHttp()
    {
        return taskHttp;
    }


    /**
     * 设置：数据请求
     * 
     * @param i_TaskHttp 数据请求
     */
    public void setTaskHttp(XHttp i_TaskHttp)
    {
        this.taskHttp = i_TaskHttp;
    }


    /**
     * 获取：获取Token的数据请求
     */
    public XHttp getTokenHttp()
    {
        return tokenHttp;
    }

    
    /**
     * 设置：获取Token的数据请求
     * 
     * @param i_TokenHttp 获取Token的数据请求
     */
    public void setTokenHttp(XHttp i_TokenHttp)
    {
        this.tokenHttp = i_TokenHttp;
    }


    /**
     * 设置XJava池中对象的ID标识。此方法不用用户调用设置值，是自动的。
     * 
     * @param i_XJavaID
     */
    @Override
    public void setXJavaID(String i_XJavaID)
    {
        this.jobHttp.setXid(i_XJavaID);
    }
    
    
    /**
     * 获取XJava池中对象的ID标识。
     * 
     * @return
     */
    @Override
    public String getXJavaID()
    {
        return this.jobHttp.getXid();
    }

    
    /**
     * 获取：注解说明
     */
    @Override
    public String getComment()
    {
        return this.jobHttp.getComment();
    }

    
    /**
     * 设置：注解说明
     * 
     * @param i_Comment 注解说明
     */
    @Override
    public void setComment(String i_Comment)
    {
        this.jobHttp.setComment(i_Comment);
    }
    
}
