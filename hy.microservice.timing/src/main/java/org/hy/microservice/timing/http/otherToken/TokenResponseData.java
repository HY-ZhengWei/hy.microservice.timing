package org.hy.microservice.timing.http.otherToken;

import org.hy.common.xml.SerializableDef;





/**
 * 访问Token的数据结构
 *
 * @author      ZhengWei(HY)
 * @createDate  2020-12-22
 * @version     v1.0
 */
public class TokenResponseData extends SerializableDef
{
    
    private static final long serialVersionUID = 2029220892157842421L;

    /** 访问Token */
    private String  data;

    
    
    /**
     * 获取访问Token
     * 
     * @return
     */
    public String getData()
    {
        return data;
    }

    
    /**
     * 设置访问Token
     * 
     * @param data
     */
    public void setData(String data)
    {
        this.data = data;
    }

}
