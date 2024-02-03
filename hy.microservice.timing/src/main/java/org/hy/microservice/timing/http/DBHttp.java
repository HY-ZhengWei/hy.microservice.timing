package org.hy.microservice.timing.http;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.XJavaID;
import org.hy.microservice.common.BaseViewMode;





/**
 * 数据Http请求。对应XHttp类。
 * 
 * @author      ZhengWei(HY)
 * @createDate  2023-10-19
 * @version     v1.0
 */
public class DBHttp extends BaseViewMode implements XJavaID
{

    private static final long serialVersionUID = 3518432671898673187L;
    
    
    /** 主键 */
    private String                     id;
    
    /** 逻辑ID。即XHttp的XID */
    private String                     xid;
    
    /** 旧的逻辑ID */
    private String                     xidOld;
    
    /** 请求协议类型(http、https) */
    private String                     protocol;
    
    /** HTTP主机IP地址(此属性也可以是域名，如www.sina.com) */
    private String                     ip;
    
    /** HTTP主机端口(默认为0，表示可以不明确说明访问端口) */
    private Integer                    port;
    
    /** HTTP请求地址。不含"http://IP:Port/"。不含请求参数  */
    private String                     url;
    
    /** 请求类型。1:get方式(默认值)  2:post方式 */
    private Integer                    requestType;
    
    /** 请求内容类型(默认:text/html) */
    private String                     contentType;
    
    /** 请求字符集名称(默认:UTF-8) */
    private String                     charset;
    
    /** 连接超时（单位：毫秒）。零值：表示永远不超时 */
    private Integer                    connectTimeout;
    
    /** 读取数据超时时长（单位：毫秒）。零值：表示永远不超时 */
    private Integer                    readTimeout;
    
    /** 代理服务器的IP */
    private String                     proxyHost;
    
    /** 代理服务器的端口 */
    private Integer                    proxyPort;
    
    /**
     * 请求参数转义类型。
     * 0: isToUnicode=false   isEncode=false
     * 1: isToUnicode=true    isEncode=false
     * 2: isToUnicode=false   isEncode=true
     */
    private Integer                    encodeType;
    
    /** HTTP请求参数 */
    private List<DBHttpParam>          httpParams;
    
    /** 任务请求的Json格式的参数，它将被转为对象 */
    private String                     executeParams;
    
    
    
    public DBHttp()
    {
        this.httpParams = new ArrayList<DBHttpParam>();
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
     * 获取：逻辑ID。即XHttp的XID
     */
    public String getXid()
    {
        return xid;
    }


    
    /**
     * 设置：逻辑ID。即XHttp的XID
     * 
     * @param i_Xid 逻辑ID。即XHttp的XID
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
     * 获取：请求协议类型(http、https)
     */
    public String getProtocol()
    {
        return protocol;
    }


    
    /**
     * 设置：请求协议类型(http、https)
     * 
     * @param i_Protocol 请求协议类型(http、https)
     */
    public void setProtocol(String i_Protocol)
    {
        this.protocol = i_Protocol;
    }


    
    /**
     * 获取：HTTP主机IP地址(此属性也可以是域名，如www.sina.com)
     */
    public String getIp()
    {
        return ip;
    }


    
    /**
     * 设置：HTTP主机IP地址(此属性也可以是域名，如www.sina.com)
     * 
     * @param i_Ip HTTP主机IP地址(此属性也可以是域名，如www.sina.com)
     */
    public void setIp(String i_Ip)
    {
        this.ip = i_Ip;
    }


    
    /**
     * 获取：HTTP主机端口(默认为0，表示可以不明确说明访问端口)
     */
    public Integer getPort()
    {
        return port;
    }


    
    /**
     * 设置：HTTP主机端口(默认为0，表示可以不明确说明访问端口)
     * 
     * @param i_Port HTTP主机端口(默认为0，表示可以不明确说明访问端口)
     */
    public void setPort(Integer i_Port)
    {
        this.port = i_Port;
    }


    
    /**
     * 获取：HTTP请求地址。不含"http://IP:Port/"。不含请求参数
     */
    public String getUrl()
    {
        return url;
    }


    
    /**
     * 设置：HTTP请求地址。不含"http://IP:Port/"。不含请求参数
     * 
     * @param i_Url HTTP请求地址。不含"http://IP:Port/"。不含请求参数
     */
    public void setUrl(String i_Url)
    {
        this.url = i_Url;
    }


    
    /**
     * 获取：请求类型。1:get方式(默认值)  2:post方式
     */
    public Integer getRequestType()
    {
        return requestType;
    }


    
    /**
     * 设置：请求类型。1:get方式(默认值)  2:post方式
     * 
     * @param i_RequestType 请求类型。1:get方式(默认值)  2:post方式
     */
    public void setRequestType(Integer i_RequestType)
    {
        this.requestType = i_RequestType;
    }


    
    /**
     * 获取：请求内容类型(默认:text/html)
     */
    public String getContentType()
    {
        return contentType;
    }


    
    /**
     * 设置：请求内容类型(默认:text/html)
     * 
     * @param i_ContentType 请求内容类型(默认:text/html)
     */
    public void setContentType(String i_ContentType)
    {
        this.contentType = i_ContentType;
    }


    
    /**
     * 获取：请求字符集名称(默认:UTF-8)
     */
    public String getCharset()
    {
        return charset;
    }


    
    /**
     * 设置：请求字符集名称(默认:UTF-8)
     * 
     * @param i_Charset 请求字符集名称(默认:UTF-8)
     */
    public void setCharset(String i_Charset)
    {
        this.charset = i_Charset;
    }


    
    /**
     * 获取：连接超时（单位：毫秒）。零值：表示永远不超时
     */
    public Integer getConnectTimeout()
    {
        return connectTimeout;
    }


    
    /**
     * 设置：连接超时（单位：毫秒）。零值：表示永远不超时
     * 
     * @param i_ConnectTimeout 连接超时（单位：毫秒）。零值：表示永远不超时
     */
    public void setConnectTimeout(Integer i_ConnectTimeout)
    {
        this.connectTimeout = i_ConnectTimeout;
    }


    
    /**
     * 获取：读取数据超时时长（单位：毫秒）。零值：表示永远不超时
     */
    public Integer getReadTimeout()
    {
        return readTimeout;
    }


    
    /**
     * 设置：读取数据超时时长（单位：毫秒）。零值：表示永远不超时
     * 
     * @param i_ReadTimeout 读取数据超时时长（单位：毫秒）。零值：表示永远不超时
     */
    public void setReadTimeout(Integer i_ReadTimeout)
    {
        this.readTimeout = i_ReadTimeout;
    }


    
    /**
     * 获取：代理服务器的IP
     */
    public String getProxyHost()
    {
        return proxyHost;
    }


    
    /**
     * 设置：代理服务器的IP
     * 
     * @param i_ProxyHost 代理服务器的IP
     */
    public void setProxyHost(String i_ProxyHost)
    {
        this.proxyHost = i_ProxyHost;
    }


    
    /**
     * 获取：代理服务器的端口
     */
    public Integer getProxyPort()
    {
        return proxyPort;
    }


    
    /**
     * 设置：代理服务器的端口
     * 
     * @param i_ProxyPort 代理服务器的端口
     */
    public void setProxyPort(Integer i_ProxyPort)
    {
        this.proxyPort = i_ProxyPort;
    }


    
    /**
     * 获取：请求参数转义类型。
     * 0: isToUnicode=false   isEncode=false
     * 1: isToUnicode=true    isEncode=false
     * 2: isToUnicode=false   isEncode=true
     */
    public Integer getEncodeType()
    {
        return encodeType;
    }


    
    /**
     * 设置：请求参数转义类型。
     * 0: isToUnicode=false   isEncode=false
     * 1: isToUnicode=true    isEncode=false
     * 2: isToUnicode=false   isEncode=true
     * 
     * @param i_EncodeType
     */
    public void setEncodeType(Integer i_EncodeType)
    {
        this.encodeType = i_EncodeType;
    }


    
    /**
     * 获取：HTTP请求参数
     */
    public List<DBHttpParam> getHttpParams()
    {
        return httpParams;
    }


    
    /**
     * 设置：HTTP请求参数
     * 
     * @param i_HttpParams HTTP请求参数
     */
    public void setHttpParams(List<DBHttpParam> i_HttpParams)
    {
        this.httpParams = i_HttpParams;
    }

    
    /**
     * 获取：任务请求的Json格式的参数，它将被转为对象
     */
    public String getExecuteParams()
    {
        return executeParams;
    }


    
    /**
     * 设置：任务请求的Json格式的参数，它将被转为对象
     * 
     * @param i_ExecuteParams 任务请求的Json格式的参数，它将被转为对象
     */
    public void setExecuteParams(String i_ExecuteParams)
    {
        this.executeParams = i_ExecuteParams;
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
