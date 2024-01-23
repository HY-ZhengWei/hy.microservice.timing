package org.hy.microservice.timing.job;

import java.util.List;

import org.hy.common.Date;
import org.hy.common.thread.Job;
import org.hy.common.thread.JobReport;
import org.hy.microservice.timing.monitor.JobUser;

import com.fasterxml.jackson.annotation.JsonFormat;





/**
 * 定时任务监控用到的信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-14
 * @version     v1.0
 */
public class JobConfigReport extends JobReport
{

    private static final long serialVersionUID = -939222620021006306L;
    
    /** 主键 */
    private String  id;
    
    /** 项目ID */
    private String  projectID;
    
    /** 任务编号 */
    private String  code;
    
    /** 任务名称 */
    private String  name;
    
    /** 注释。可用于日志的输出等帮助性的信息 */
    private String  comment;
    
    /** 用户编号 */
    private String  userID;
    
    /** 用户名称 */
    private String  userName;
    
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date    createTime;
    
    /** 修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private Date    updateTime;
    
    /** 创建人编号 */
    private String  createUserID;
    
    /** 修改者编号 */
    private String  updateUserID;
    
    /** 删除标记。1删除；0未删除 */
    private Integer isDel;
    
    /** 任务开始时间组 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    private List<Date> startTimes;
    
    /** 间隔类型 */
    private Integer intervalTypeValue;
    
    /** 被执行任务对象的XJava标识 */
    private String xid;
    
    /** 被执行任务对象的方法名 */
    private String methodName;
    
    /** 允许执行的条件 */
    private String condition;
    
    /** 执行未成功时，尝试执行的最大次数 */
    private Integer tryMaxCount;
    
    /** 执行未成功时，每次尝试间隔时长（秒） */
    private Integer tryIntervalLen;
    
    /** 是否为只读模式。即配置在xml文件中，并非配置在数据库中的 */
    private Boolean readOnly;
    
    /** 是否启用。1启用；0停用 */
    private Integer isEnabled;
    
    /** 任务责任人 */
    private List<JobUser> jobUsers;
    
    
    
    public JobConfigReport(String i_JobID ,Job i_Job ,Boolean i_IsAddJobs)
    {
        super(i_JobID ,i_Job ,i_IsAddJobs);
        this.setReadOnly (true);
        this.setIsEnabled(1);
    }
    
    
    
    /**
     * 设置：创建时间
     * 
     * @param createTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    
    
    /**
     * 设置：修改时间
     * 
     * @param updateTime
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }
    
    
    /**
     * 获取：主键
     */
    public String getId()
    {
        return id;
    }

    
    /**
     * 设置：主键
     * 
     * @param i_Id 主键
     */
    public void setId(String i_Id)
    {
        this.id = i_Id;
    }
    

    /**
     * 获取：用户编号
     */
    public String getUserID()
    {
        return userID;
    }


    /**
     * 设置：用户编号
     * 
     * @param i_UserID 用户编号
     */
    public void setUserID(String i_UserID)
    {
        this.userID = i_UserID;
    }

    
    /**
     * 获取：用户名称
     */
    public String getUserName()
    {
        return userName;
    }

    
    /**
     * 设置：用户名称
     * 
     * @param i_UserName 用户名称
     */
    public void setUserName(String i_UserName)
    {
        this.userName = i_UserName;
    }

    
    /**
     * 获取：创建人编号
     */
    public String getCreateUserID()
    {
        return createUserID;
    }


    /**
     * 设置：创建人编号
     * 
     * @param i_CreateUserID 创建人编号
     */
    public void setCreateUserID(String i_CreateUserID)
    {
        this.createUserID = i_CreateUserID;
    }

    
    /**
     * 获取：修改者编号
     */
    public String getUpdateUserID()
    {
        return updateUserID;
    }


    /**
     * 设置：修改者编号
     * 
     * @param i_UpdateUserID 修改者编号
     */
    public void setUpdateUserID(String i_UpdateUserID)
    {
        this.updateUserID = i_UpdateUserID;
    }

    
    /**
     * 获取：删除标记。1删除；0未删除
     */
    public Integer getIsDel()
    {
        return isDel;
    }

    
    /**
     * 设置：删除标记。1删除；0未删除
     * 
     * @param i_IsDel 删除标记。1删除；0未删除
     */
    public void setIsDel(Integer i_IsDel)
    {
        this.isDel = i_IsDel;
    }

    
    /**
     * 获取：创建时间
     */
    public Date getCreateTime()
    {
        return createTime;
    }

    
    /**
     * 获取：修改时间
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }

    
    /**
     * 获取：任务编号
     */
    public String getCode()
    {
        return code;
    }

    
    /**
     * 设置：任务编号
     * 
     * @param i_Code
     */
    public void setCode(String i_Code)
    {
        this.code = i_Code;
    }
    
    
    
    /**
     * 获取：任务开始时间组
     */
    public List<Date> getStartTimes()
    {
        return startTimes;
    }

    
    /**
     * 设置：任务开始时间组
     * 
     * @param i_StartTimes 任务开始时间组
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone = "GMT+8")
    public void setStartTimes(List<Date> i_StartTimes)
    {
        this.startTimes = i_StartTimes;
    }



    /**
     * 获取：是否为只读模式。即配置在xml文件中，并非配置在数据库中的
     */
    public Boolean getReadOnly()
    {
        return readOnly;
    }

    
    /**
     * 设置：是否为只读模式。即配置在xml文件中，并非配置在数据库中的
     * 
     * @param i_ReadOnly 是否为只读模式。即配置在xml文件中，并非配置在数据库中的
     */
    public void setReadOnly(Boolean i_ReadOnly)
    {
        this.readOnly = i_ReadOnly;
    }

    
    /**
     * 获取：任务名称
     */
    public String getName()
    {
        return name;
    }

    
    /**
     * 设置：任务名称
     * 
     * @param i_Name 任务名称
     */
    public void setName(String i_Name)
    {
        this.name = i_Name;
    }

    
    /**
     * 获取：注释。可用于日志的输出等帮助性的信息
     */
    public String getComment()
    {
        return comment;
    }

    
    /**
     * 设置：注释。可用于日志的输出等帮助性的信息
     * 
     * @param i_Comment 注释。可用于日志的输出等帮助性的信息
     */
    public void setComment(String i_Comment)
    {
        this.comment = i_Comment;
    }


    /**
     * 保持统一含义，而重写
     *
     * @author      ZhengWei(HY)
     * @createDate  2023-09-14
     * @version     v1.0
     *
     * @return
     *
     * @see org.hy.common.thread.JobReport#getIntervalType()
     */
    @Override
    public String getIntervalType()
    {
        return this.intervalTypeValue + "";
    }
    
    
    /**
     * 获取间隔类型的名称
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-14
     * @version     v1.0
     *
     * @return
     */
    public String getIntervalTypeName()
    {
        return super.getIntervalType();
    }



    /**
     * 获取：间隔类型
     */
    public Integer getIntervalTypeValue()
    {
        return intervalTypeValue;
    }

    
    /**
     * 设置：间隔类型
     * 
     * @param i_IntervalTypeValue 间隔类型
     */
    public void setIntervalTypeValue(Integer i_IntervalTypeValue)
    {
        this.intervalTypeValue = i_IntervalTypeValue;
    }

    
    /**
     * 获取：被执行任务对象的XJava标识
     */
    public String getXid()
    {
        return xid;
    }

    
    /**
     * 设置：被执行任务对象的XJava标识
     * 
     * @param i_Xid 被执行任务对象的XJava标识
     */
    public void setXid(String i_Xid)
    {
        this.xid = i_Xid;
    }

    
    /**
     * 获取：被执行任务对象的方法名
     */
    public String getMethodName()
    {
        return methodName;
    }

    
    /**
     * 设置：被执行任务对象的方法名
     * 
     * @param i_MethodName 被执行任务对象的方法名
     */
    public void setMethodName(String i_MethodName)
    {
        this.methodName = i_MethodName;
    }

    
    /**
     * 获取：允许执行的条件
     */
    public String getCondition()
    {
        return condition;
    }

    
    /**
     * 设置：允许执行的条件
     * 
     * @param i_Condition 允许执行的条件
     */
    public void setCondition(String i_Condition)
    {
        this.condition = i_Condition;
    }

    
    /**
     * 获取：执行未成功时，尝试执行的最大次数
     */
    public Integer getTryMaxCount()
    {
        return tryMaxCount;
    }


    /**
     * 设置：执行未成功时，尝试执行的最大次数
     * 
     * @param i_TryMaxCount 执行未成功时，尝试执行的最大次数
     */
    public void setTryMaxCount(Integer i_TryMaxCount)
    {
        this.tryMaxCount = i_TryMaxCount;
    }

    
    /**
     * 获取：执行未成功时，每次尝试间隔时长（秒）
     */
    public Integer getTryIntervalLen()
    {
        return tryIntervalLen;
    }

    
    /**
     * 设置：执行未成功时，每次尝试间隔时长（秒）
     * 
     * @param i_TryIntervalLen 执行未成功时，每次尝试间隔时长（秒）
     */
    public void setTryIntervalLen(Integer i_TryIntervalLen)
    {
        this.tryIntervalLen = i_TryIntervalLen;
    }

    
    /**
     * 获取：是否启用。1启用；0停用
     */
    public Integer getIsEnabled()
    {
        return isEnabled;
    }

    
    /**
     * 设置：是否启用。1启用；0停用
     * 
     * @param i_IsEnabled 是否启用。1启用；0停用
     */
    public void setIsEnabled(Integer i_IsEnabled)
    {
        this.isEnabled = i_IsEnabled;
    }

    
    /**
     * 获取：任务责任人
     */
    public List<JobUser> getJobUsers()
    {
        return jobUsers;
    }

    
    /**
     * 设置：任务责任人
     * 
     * @param i_JobUsers 任务责任人
     */
    public void setJobUsers(List<JobUser> i_JobUsers)
    {
        this.jobUsers = i_JobUsers;
    }

    
    /**
     * 获取：项目ID
     */
    public String getProjectID()
    {
        return projectID;
    }

    
    /**
     * 设置：项目ID
     * 
     * @param i_ProjectID 项目ID
     */
    public void setProjectID(String i_ProjectID)
    {
        this.projectID = i_ProjectID;
    }
    
}
