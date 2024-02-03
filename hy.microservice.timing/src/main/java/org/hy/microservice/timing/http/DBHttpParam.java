package org.hy.microservice.timing.http;

import org.hy.microservice.common.BaseViewMode;





/**
 * 数据Http请求参数。对应XHttpParam类。
 * 
 * @author      ZhengWei(HY)
 * @createDate  2023-10-19
 * @version     v1.0
 */
public class DBHttpParam extends BaseViewMode
{

    private static final long serialVersionUID = 2343943378329324506L;
    
    
    /** Http请求的ID */
    private String  httpID;
    
    /** Http请求的XID（仅内存有效） */
    private String  httpXID;
    
    /**
     * XJava标记的参数名称
     */
    private String  paramName;
    
    /**
     * 参数值(非必选)(默认值)
     * 
     * 当此字段有值时，外界还对此字段输入其它值，则以外界为准。
     * 否则，已此值为准。相当于"默认值"的概念
     */
    private String  paramValue;

    
    
    /**
     * 获取：Http请求的ID
     */
    public String getHttpID()
    {
        return httpID;
    }

    
    /**
     * 设置：Http请求的ID
     * 
     * @param i_HttpID Http请求的ID
     */
    public void setHttpID(String i_HttpID)
    {
        this.httpID = i_HttpID;
    }
    
    
    /**
     * 获取：Http请求的XID（仅内存有效）
     */
    public String getHttpXID()
    {
        return httpXID;
    }

    
    /**
     * 设置：Http请求的XID（仅内存有效）
     * 
     * @param i_HttpXID Http请求的XID（仅内存有效）
     */
    public void setHttpXID(String i_HttpXID)
    {
        this.httpXID = i_HttpXID;
    }


    /**
     * 获取：XJava标记的参数名称
     */
    public String getParamName()
    {
        return paramName;
    }

    
    /**
     * 设置：XJava标记的参数名称
     * 
     * @param i_ParamName XJava标记的参数名称
     */
    public void setParamName(String i_ParamName)
    {
        this.paramName = i_ParamName;
    }

    
    /**
     * 获取：参数值(非必选)(默认值)
     * 
     * 当此字段有值时，外界还对此字段输入其它值，则以外界为准。
     * 否则，已此值为准。相当于"默认值"的概念
     */
    public String getParamValue()
    {
        return paramValue;
    }

    
    /**
     * 设置：参数值(非必选)(默认值)
     * 
     * 当此字段有值时，外界还对此字段输入其它值，则以外界为准。
     * 否则，已此值为准。相当于"默认值"的概念
     * 
     * @param i_ParamValue
     */
    public void setParamValue(String i_ParamValue)
    {
        this.paramValue = i_ParamValue;
    }
    
}
