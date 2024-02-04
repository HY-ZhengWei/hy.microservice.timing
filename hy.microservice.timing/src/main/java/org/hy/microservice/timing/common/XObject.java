package org.hy.microservice.timing.common;

import org.hy.common.XJavaID;
import org.hy.microservice.timing.http.DBHttp;
import org.hy.microservice.timing.job.JobConfig;
import org.hy.microservice.timing.jobHttp.JobHttp;





/**
 * X对象
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-02
 * @version     v1.0
 */
public class XObject implements XJavaID
{
    
    /** X对象的类型：数据请求 */
    public static final String $Type_Http    = "XHttp";
    
    /** X对象的类型：定时任务请求 */
    public static final String $Type_JobHttp = "XJobHttp";
    
    /** X对象的类型：定时任务 */
    public static final String $Type_Job     = "XJob";
    
    
    
    /** 主键ID */
    private String  id;

    /** 逻辑ID */
    private String  xid;
    
    /** 注解说明 */
    private String  comment;
    
    /** 对象类型 */
    private String  objectType;
    
    
    
    public XObject()
    {
        
    }
    
    
    
    public XObject(DBHttp i_Http)
    {
        this.id         = i_Http.getId();
        this.xid        = i_Http.getXid();
        this.comment    = i_Http.getComment();
        this.objectType = $Type_Http;
    }
    
    
    
    public XObject(JobHttp i_JobHttp)
    {
        this.id         = i_JobHttp.getId();
        this.xid        = i_JobHttp.getXid();
        this.comment    = i_JobHttp.getComment();
        this.objectType = $Type_JobHttp;
    }
    
    
    
    public XObject(JobConfig i_JobConfig)
    {
        this.id         = i_JobConfig.getId();
        this.xid        = i_JobConfig.getCode();
        this.comment    = i_JobConfig.getComment();
        this.objectType = $Type_Job;
    }
    
    
    
    /**
     * 获取：主键ID
     */
    public String getId()
    {
        return id;
    }

    
    /**
     * 设置：主键ID
     * 
     * @param i_Id 主键ID
     */
    public void setId(String i_Id)
    {
        this.id = i_Id;
    }

    
    /**
     * 获取：逻辑ID
     */
    public String getXid()
    {
        return xid;
    }

    
    /**
     * 设置：逻辑ID
     * 
     * @param i_Xid 逻辑ID
     */
    public void setXid(String i_Xid)
    {
        this.xid = i_Xid;
    }

    
    
    /**
     * 获取：对象类型
     */
    public String getObjectType()
    {
        return objectType;
    }

    
    /**
     * 设置：对象类型
     * 
     * @param i_ObjectType 对象类型
     */
    public void setObjectType(String i_ObjectType)
    {
        this.objectType = i_ObjectType;
    }


    /**
     * 设置XJava池中对象的ID标识。此方法不用用户调用设置值，是自动的。
     * 
     * @param i_XJavaID
     */
    @Override
    public void setXJavaID(String i_XJavaID)
    {
        this.xid = i_XJavaID;
    }
    
    
    /**
     * 获取XJava池中对象的ID标识。
     * 
     * @return
     */
    @Override
    public String getXJavaID()
    {
        return this.xid;
    }

    
    /**
     * 获取：注解说明
     */
    @Override
    public String getComment()
    {
        return comment;
    }


    /**
     * 设置：注解说明
     * 
     * @param i_Comment 注解说明
     */
    @Override
    public void setComment(String i_Comment)
    {
        this.comment = i_Comment;
    }
    
}
