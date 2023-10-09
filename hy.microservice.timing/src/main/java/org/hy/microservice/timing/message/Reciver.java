package org.hy.microservice.timing.message;

import org.hy.common.xml.SerializableDef;





/**
 * 消息的接收者
 *
 * @author      ZhengWei(HY)
 * @createDate  2021-02-06
 * @version     v1.0
 */
public class Reciver extends SerializableDef
{

    private static final long serialVersionUID = -523700887845896097L;
    
    /** 手机号（或openID、邮箱地址） */
    private String phone;
    
    /** 消息内容 */
    private String message;
    
    /** 标题（仅用于微信、邮件） */
    private String title;
    
    
    
    /**
     * 获取：手机号（或openID、邮箱地址）
     */
    public String getPhone()
    {
        return phone;
    }
    
    
    /**
     * 获取：消息内容
     */
    public String getMessage()
    {
        return message;
    }
    
    
    /**
     * 设置：手机号（或openID、邮箱地址）
     * 
     * @param phone
     */
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    
    /**
     * 设置：消息内容
     * 
     * @param message
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    
    /**
     * 获取：标题（仅用于微信、邮件）
     */
    public String getTitle()
    {
        return title;
    }
    
    
    /**
     * 设置：标题（仅用于微信、邮件）
     * 
     * @param title
     */
    public void setTitle(String title)
    {
        this.title = title;
    }
    
}
