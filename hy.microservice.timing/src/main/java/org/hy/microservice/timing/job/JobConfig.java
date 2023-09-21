package org.hy.microservice.timing.job;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.thread.Job;

import com.fasterxml.jackson.annotation.JsonFormat;





/**
 * 任务配置
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-12
 * @version     v1.0
 */
public class JobConfig extends Job
{
    
    /** 主键 */
    private String  id;
    
    /** 旧的任务编号(即Job自身的旧XJavaID) */
    private String  codeOld;
    
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
    
    /** 是否启用。1启用；0停用 */
    private Integer isEnabled;
    
    /** 删除标记。1删除；0未删除 */
    private Integer isDel;
    
    /** 任务开始时间组 */
    private List<JobStartTime> jobStartTimes;
    
    
    
    public JobConfig()
    {
        super();
        this.jobStartTimes = new ArrayList<JobStartTime>();
    }
    
    
    
    /**
     * 从数据库中读取开始时间，转为Job对象结构的开始时间
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     *
     */
    public List<Date> toStartTimes()
    {
        this.getStartTimes().clear();
        if ( !Help.isNull(this.jobStartTimes) )
        {
            for (JobStartTime v_JobStartTime : this.jobStartTimes)
            {
                this.getStartTimes().add(v_JobStartTime.getStartTime());
            }
        }
        
        return this.getStartTimes();
    }
    
    
    
    /**
     * 创建出一个新的Job对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-14
     * @version     v1.0
     *
     * @return
     */
    public Job newJob()
    {
        Job  v_Job  = new Job();
        
        v_Job.setXJavaID(       this.getXJavaID());
        v_Job.setCode   (       this.getCode());
        v_Job.setName   (       this.getName());
        v_Job.setIntervalType(  this.getIntervalType());
        v_Job.setIntervalLen(   this.getIntervalLen());
        v_Job.setXid(           this.getXid());
        v_Job.setMethodName(    this.getMethodName());
        v_Job.setCloudServer(   this.getCloudServer());
        v_Job.setCondition(     this.getCondition());
        v_Job.setTryMaxCount(   this.getTryMaxCount());
        v_Job.setTryIntervalLen(this.getTryIntervalLen());
        v_Job.setComment(       this.getComment());
        v_Job.setStartTimes(    this.getStartTimes());
        
        return v_Job;
    }
    
    
    
    /**
     * 生成写库用的JobStartTime对象集合
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @return
     */
    public List<JobStartTime> makeStartTimes()
    {
        List<JobStartTime> v_StartTimes = new ArrayList<JobStartTime>();
        
        if ( !Help.isNull(this.getStartTimes()) && !Help.isNull(this.id) )
        {
            for (Date v_StartTime : this.getStartTimes())
            {
                JobStartTime v_JSTime = new JobStartTime();
                
                v_JSTime.setJobID(this.id);
                v_JSTime.setStartTime(v_StartTime);
                v_JSTime.setCreateUserID(Help.NVL(this.userID ,Help.NVL(this.createUserID ,this.updateUserID)));
                
                v_StartTimes.add(v_JSTime);
            }
        }
        
        return v_StartTimes;
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
    public String getJobID()
    {
        return id;
    }
    
    
    /**
     * 设置：主键
     * 
     * @param i_JobID 主键
     */
    public void setJobID(String i_JobID)
    {
        this.id = i_JobID;
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
     * 获取：旧的任务编号(即Job自身的旧XJavaID)
     */
    public String getCodeOld()
    {
        return codeOld;
    }

    
    /**
     * 设置：旧的任务编号(即Job自身的旧XJavaID)
     * 
     * @param i_CodeOld 旧的任务编号(即Job自身的旧XJavaID)
     */
    public void setCodeOld(String i_CodeOld)
    {
        this.codeOld = i_CodeOld;
    }
    
    
    /**
     * 获取：任务开始时间组
     */
    public List<JobStartTime> getJobStartTimes()
    {
        return jobStartTimes;
    }


    /**
     * 设置：任务开始时间组
     * 
     * @param i_JobStartTimes 任务开始时间组
     */
    public void setJobStartTimes(List<JobStartTime> i_JobStartTimes)
    {
        this.jobStartTimes = i_JobStartTimes;
    }
    
    
    /**
     * 设置：云计算服务器的地址端口。格式为：IP:Port。
     * 
     * 默认端口是：1721
     * 
     * @param i_CloudServer
     */
    public void setCloudServerHost(String i_CloudServer)
    {
        super.setCloudServer(i_CloudServer);
    }
    
    
    /**
     * 获取：云计算服务器的地址端口。格式为：IP:Port。
     * 
     * @param i_CloudServer
     */
    public String getCloudServerHost()
    {
        return super.getCloudServer();
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



    @Override
    public int compareTo(Job i_Other)
    {
        if ( i_Other == null )
        {
            return 1;
        }
        else
        {
            return this.getCode().compareTo(i_Other.getCode());
        }
    }
    
}
