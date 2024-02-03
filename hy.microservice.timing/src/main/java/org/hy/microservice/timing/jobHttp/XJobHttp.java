package org.hy.microservice.timing.jobHttp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.StringHelp;
import org.hy.common.XJavaID;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XHttpParam;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.timing.http.otherToken.TokenResponseData;





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
     * @return  返回是否请求成功。
     *          Return.paramObj  真实完整的请求URL，及请求报文
     *          Return.paramStr  保存响应信息
     *          Return.paramInt  保存响应异常类型代码
     *          Return.exception 保存异常信息
     */
    public Return<Object> execute()
    {
        synchronized ( this )
        {
            if ( this.isRunning )
            {
                return new Return<Object>(false).setParamStr("正在运行中，上次执行时间为：" + this.runStartTime.getFull());
            }
            
            this.isRunning    = true;
            this.runStartTime = new Date();
        }
        
        Return<Object> v_Ret = null;
        try
        {
            v_Ret = this.executeTaskHttp();
        }
        catch (Exception exce)
        {
            v_Ret.setParamInt(-900).setParamStr(exce.getMessage());
            $Logger.error(exce);
        }
        finally
        {
            this.isRunning = false;
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 执行数据请求的发送
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @return  返回是否请求成功。
     *          Return.paramObj  真实完整的请求URL，及请求报文
     *          Return.paramStr  保存响应信息
     *          Return.paramInt  保存响应异常类型代码
     *          Return.exception 保存异常信息
     */
    @SuppressWarnings("unchecked")
    private Return<Object> executeTaskHttp()
    {
        Map<String ,Object> v_TaskHttpUrlData = new HashMap<String ,Object>();
        String              v_TokenValue      = "";
        if ( this.tokenHttp != null )
        {
            // {"code":"200","message":"成功","data":{"data":"addeba19eede4a97916d79c5c209e62a"}}
            Return<Object> v_TokenRet = (Return<Object>) this.tokenHttp.request();
            
            if ( v_TokenRet !=null && v_TokenRet.booleanValue() && !Help.isNull(v_TokenRet.getParamStr()) )
            {
                XJSON v_XJson = new XJSON();
                try
                {
                    TokenResponseData v_Data = (TokenResponseData)v_XJson.toJava(v_TokenRet.getParamStr() ,"data" ,TokenResponseData.class);
                    if ( v_Data != null )
                    {
                        if ( !Help.isNull(v_Data.getData()) )
                        {
                            v_TokenValue = v_Data.getData();
                        }
                    }
                }
                catch (Exception e)
                {
                    $Logger.error(e);
                    return v_TokenRet.set(false).setException(e);
                }
            }
            
            if ( !Help.isNull(this.taskHttp.getParams()) )
            {
                for (XHttpParam v_UrlParam : this.taskHttp.getParams())
                {
                    if ( JobHttp.$Template_Token.equals(v_UrlParam.getParamValue()) )
                    {
                        v_TaskHttpUrlData.put(v_UrlParam.getUrlParamName() ,v_TokenValue);
                    }
                }
            }
        }
        
        Map<String ,Object> v_TemplateParams = new HashMap<String ,Object>();
        v_TemplateParams.put(JobHttp.$Template_Token     ,v_TokenValue);
        v_TemplateParams.put(JobHttp.$Template_Timestamp ,Date.getNowTime().getTime());
        v_TemplateParams.put(JobHttp.$Template_Sign      ,"");
        
        String v_TaskHttpBodyData = StringHelp.replaceAll(this.jobHttp.getRequestTemplate() ,v_TemplateParams);
        Return<Object> v_TaskHttpRet = (Return<Object>) this.taskHttp.request(v_TaskHttpUrlData ,v_TaskHttpBodyData);
        v_TaskHttpRet.setParamObj(v_TaskHttpRet.getParamObj() + "\r\n\r\n" + v_TaskHttpBodyData);
        
        if ( v_TaskHttpRet.booleanValue() )
        {
            // 判定业务逻辑上是否有成功
            if ( !Help.isNull(this.getJobHttp().getSucceedKey()) )
            {
                if ( Help.isNull(v_TaskHttpRet.getParamStr()) )
                {
                    v_TaskHttpRet.set(false).setParamInt(-191).setParamStr("返回信息为空，与成功标记不符");
                }
                else if ( v_TaskHttpRet.getParamStr().indexOf(this.getJobHttp().getSucceedKey()) < 0 )
                {
                    v_TaskHttpRet.set(false).setParamInt(-192).setParamStr(v_TaskHttpRet.getParamStr() + "\r\n\r\n返回信息不包含成功标记");
                }
            }
        }
        
        return v_TaskHttpRet;
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
