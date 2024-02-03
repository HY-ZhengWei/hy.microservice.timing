package org.hy.microservice.timing.http;

import java.io.Serializable;

import org.hy.common.Help;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XHttpParam;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.ConfigToJavaDefault;





/**
 * 数据Http请求的XJava对象实例：XHttp类型
 * 
 * @author      ZhengWei(HY)
 * @createDate  2023-10-19
 * @version     v1.0
 */
@Xjava
public class DBHttpToJava_XHttp extends ConfigToJavaDefault<DBHttp ,XHttp> implements Serializable
{
    
    private static final long serialVersionUID = -1729078820505520994L;

    private static Logger $Logger = new Logger(DBHttpToJava_XHttp.class);
    
    
    
    @Override
    protected XHttp newObject(DBHttp i_Config)
    {
        if ( Help.isNull(i_Config.getIp()) )
        {
            $Logger.error(i_Config.getXJavaID() + "数据请求域名为空");
            return null;
        }
        
        if ( Help.isNull(i_Config.getUrl()) )
        {
            $Logger.error(i_Config.getXJavaID() + "数据请求资源地址为空");
            return null;
        }
        
        XHttp v_XHttp = new XHttp();
        
        v_XHttp.setXJavaID( i_Config.getXJavaID());
        v_XHttp.setComment( i_Config.getComment());
        v_XHttp.setProtocol(i_Config.getProtocol());
        v_XHttp.setIp(      i_Config.getIp());
        v_XHttp.setUrl(     i_Config.getUrl());
        
        if ( i_Config.getPort() != null )
        {
            v_XHttp.setPort(i_Config.getPort());
        }
        
        if ( i_Config.getRequestType() != null )
        {
            v_XHttp.setRequestType(i_Config.getRequestType());
        }
        
        if ( !Help.isNull(i_Config.getContentType()) )
        {
            v_XHttp.setContentType(i_Config.getContentType());
        }
        
        if ( !Help.isNull(i_Config.getCharset()) )
        {
            v_XHttp.setCharset(i_Config.getCharset());
        }
        
        if ( !Help.isNull(i_Config.getConnectTimeout()) )
        {
            v_XHttp.setConnectTimeout(i_Config.getConnectTimeout());
        }
        
        if ( !Help.isNull(i_Config.getReadTimeout()) )
        {
            v_XHttp.setReadTimeout(i_Config.getReadTimeout());
        }
        
        if ( !Help.isNull(i_Config.getProxyHost()) )
        {
            v_XHttp.setProxyHost(i_Config.getProxyHost());
        }
        
        if ( !Help.isNull(i_Config.getProxyPort()) )
        {
            v_XHttp.setProxyPort(i_Config.getProxyPort());
        }
        
        if ( i_Config.getEncodeType() == null || i_Config.getEncodeType() == 0 )
        {
            v_XHttp.setEncode(false);
            v_XHttp.setToUnicode(false);
        }
        else if ( i_Config.getEncodeType() == 1 )
        {
            v_XHttp.setToUnicode(true);
        }
        else if ( i_Config.getEncodeType() == 2 )
        {
            v_XHttp.setEncode(true);
        }
        else
        {
            $Logger.error(i_Config.getXJavaID() + "数据请求参数转义类型" + i_Config.getEncodeType() + "非法");
            return null;
        }
        
        if ( !Help.isNull(i_Config.getHttpParams()) )
        {
            for (DBHttpParam v_Param : i_Config.getHttpParams())
            {
                if ( Help.isNull(v_Param.getParamName()) )
                {
                    $Logger.error(i_Config.getXJavaID() + "数据请求参数名称为空");
                    return null;
                }
                
                XHttpParam v_XHttpParam = new XHttpParam();
                
                v_XHttpParam.setParamName(v_Param.getParamName());
                
                if ( !Help.isNull(v_Param.getParamValue()) )
                {
                    v_XHttpParam.setParamValue(v_Param.getParamValue());
                }
                
                v_XHttp.setAddParam(v_XHttpParam);
            }
        }
        
        return v_XHttp;
    }

}
