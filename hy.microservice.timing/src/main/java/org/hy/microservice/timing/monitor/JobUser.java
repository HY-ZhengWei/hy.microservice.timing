package org.hy.microservice.timing.monitor;

import org.hy.microservice.common.BaseViewMode;





/**
 * 定时任务的责任人
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-27
 * @version     v1.0
 */
public class JobUser extends BaseViewMode
{

    private static final long serialVersionUID = 819077876608984173L;
    
    
    /** 用户ID */
    private String  id;
    
    /** 用户手机号 */
    private String  phone;
    
    /** 用户邮箱 */
    private String  email;
    
    /** 用户微信应用ID */
    private String  openID;
    
    /** 是否允许更新 */
    private Integer allowUpdate;

    
    
    public JobUser()
    {
        this.allowUpdate = 0;
    }
    
    
    /**
     * 获取：用户ID
     */
    public String getId()
    {
        return id;
    }

    
    /**
     * 设置：用户ID
     * 
     * @param i_Id 用户ID
     */
    public void setId(String i_Id)
    {
        this.id = i_Id;
    }

    
    /**
     * 获取：用户手机号
     */
    public String getPhone()
    {
        return phone;
    }

    
    /**
     * 设置：用户手机号
     * 
     * @param i_Phone 用户手机号
     */
    public void setPhone(String i_Phone)
    {
        this.phone = i_Phone;
    }

    
    /**
     * 获取：用户邮箱
     */
    public String getEmail()
    {
        return email;
    }

    
    /**
     * 设置：用户邮箱
     * 
     * @param i_Email 用户邮箱
     */
    public void setEmail(String i_Email)
    {
        this.email = i_Email;
    }

    
    /**
     * 获取：用户微信应用ID
     */
    public String getOpenID()
    {
        return openID;
    }

    
    /**
     * 设置：用户微信应用ID
     * 
     * @param i_OpenID 用户微信应用ID
     */
    public void setOpenID(String i_OpenID)
    {
        this.openID = i_OpenID;
    }


    /**
     * 获取：是否允许更新
     */
    public Integer getAllowUpdate()
    {
        return allowUpdate;
    }

    
    /**
     * 设置：是否允许更新
     * 
     * @param i_AllowUpdate 是否允许更新
     */
    public void setAllowUpdate(Integer i_AllowUpdate)
    {
        this.allowUpdate = i_AllowUpdate;
    }
    
}
