package org.hy.microservice.timing.jobHttp;

import org.hy.common.XJavaID;
import org.hy.microservice.common.BaseViewMode;
import org.hy.microservice.timing.http.DBHttp;





/**
 * 定时任务请求
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-03
 * @version     v1.0
 */
public class JobHttp extends BaseViewMode implements XJavaID
{

    private static final long serialVersionUID = 3840269704122773035L;
    
    
    
    /** 主键 */
    private String                     id;
    
    /** 逻辑ID */
    private String                     xid;
    
    /** 旧的逻辑ID */
    private String                     xidOld;
    
    /** 数据请求 */
    private DBHttp                     taskHttp;
    
    /** 获取Token的数据请求 */
    private DBHttp                     tokenHttp;
    
    /** 请求模板 */
    private String                     requestTemplate;
    
    /** 请求成功判定的关键字。即返回报文中包含什么时表示成功 */
    private String                     succeedKey;
    
    
    
    public JobHttp()
    {
        this.taskHttp  = new DBHttp();
        this.tokenHttp = new DBHttp();
    }
    
    
    /**
     * 获取：数据请求ID
     */
    public String getTaskHttpID()
    {
        return this.taskHttp.getId();
    }

    
    /**
     * 设置：数据请求ID
     * 
     * @param i_TaskHttpID 数据请求ID
     */
    public void setTaskHttpID(String i_TaskHttpID)
    {
        this.taskHttp.setId(i_TaskHttpID);
    }

    
    /**
     * 获取：获取Token的数据请求ID
     */
    public String getTokenHttpID()
    {
        return this.tokenHttp.getId();
    }

    
    /**
     * 设置：获取Token的数据请求ID
     * 
     * @param i_TokenHttpID 获取Token的数据请求ID
     */
    public void setTokenHttpID(String i_TokenHttpID)
    {
        this.tokenHttp.setId(i_TokenHttpID);
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
     * 获取：旧的逻辑ID
     */
    public String getXidOld()
    {
        return xidOld;
    }

    
    /**
     * 设置：旧的逻辑ID
     * 
     * @param i_XidOld 旧的逻辑ID
     */
    public void setXidOld(String i_XidOld)
    {
        this.xidOld = i_XidOld;
    }

    
    /**
     * 获取：数据请求
     */
    public DBHttp getTaskHttp()
    {
        return taskHttp;
    }

    
    /**
     * 设置：数据请求
     * 
     * @param i_TaskHttp 数据请求ID
     */
    public void setTaskHttp(DBHttp i_TaskHttp)
    {
        this.taskHttp = i_TaskHttp;
    }

    
    /**
     * 获取：获取Token的数据请求
     */
    public DBHttp getTokenHttp()
    {
        return tokenHttp;
    }

    
    /**
     * 设置：获取Token的数据请求
     * 
     * @param i_TokenHttp 获取Token的数据请求ID
     */
    public void setTokenHttp(DBHttp i_TokenHttp)
    {
        this.tokenHttp = i_TokenHttp;
    }

    
    /**
     * 获取：请求模板
     */
    public String getRequestTemplate()
    {
        return requestTemplate;
    }

    
    /**
     * 设置：请求模板
     * 
     * @param i_RequestTemplate 请求模板
     */
    public void setRequestTemplate(String i_RequestTemplate)
    {
        this.requestTemplate = i_RequestTemplate;
    }

    
    /**
     * 获取：请求成功判定的关键字。即返回报文中包含什么时表示成功
     */
    public String getSucceedKey()
    {
        return succeedKey;
    }

    
    /**
     * 设置：请求成功判定的关键字。即返回报文中包含什么时表示成功
     * 
     * @param i_SucceedKey 请求成功判定的关键字。即返回报文中包含什么时表示成功
     */
    public void setSucceedKey(String i_SucceedKey)
    {
        this.succeedKey = i_SucceedKey;
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
    
}
