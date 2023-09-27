package org.hy.microservice.timing.cluster;

import org.hy.microservice.common.BaseViewMode;





/**
 * 任务服务的集群信息
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-26
 * @version     v1.0
 */
public class TimingClusterInfo extends BaseViewMode
{

    private static final long serialVersionUID = -8223538127793276825L;
    
    /** 云集群类型 */
    private String type;
    
    /** Master节点IP */
    private String ip;

    
    
    /**
     * 获取：云集群类型
     */
    public String getType()
    {
        return type;
    }

    
    /**
     * 设置：云集群类型
     * 
     * @param i_Type 云集群类型
     */
    public void setType(String i_Type)
    {
        this.type = i_Type;
    }

    
    /**
     * 获取：Master节点IP
     */
    public String getIp()
    {
        return ip;
    }

    
    /**
     * 设置：Master节点IP
     * 
     * @param i_Ip Master节点IP
     */
    public void setIp(String i_Ip)
    {
        this.ip = i_Ip;
    }
    
}
