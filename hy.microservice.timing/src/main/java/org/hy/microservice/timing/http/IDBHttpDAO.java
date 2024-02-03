package org.hy.microservice.timing.http;

import java.util.List;
import java.util.Map;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 数据Http请求的操作DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-10-19
 * @version     v1.0
 */
@Xjava(id="DBHttpDAO" ,value=XType.XSQL)
public interface IDBHttpDAO
{
    
    /**
     * 查询数据Http请求列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     * 
     * @return
     */
    @Xsql("XSQL_Data_DBHttp_Query")
    public Map<String ,DBHttp> queryList();
    
    
    
    /**
     * 查询数据Http请求列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-24
     * @version     v1.0
     * 
     * @param i_DBHttp  数据Http请求
     * @return
     */
    @Xsql("XSQL_Data_DBHttp_Query")
    public Map<String ,DBHttp> queryList(DBHttp i_DBHttp);
    
    
    
    /**
     * 按ID或XID查询数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     * 
     * @param i_DBHttp  数据Http请求
     * @return
     */
    @Xsql(id="XSQL_Data_DBHttp_Query" ,returnOne=true)
    public DBHttp queryByIDXID(DBHttp i_DBHttp);
    
    
    
    /**
     * 按ID查询数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-16
     * @version     v1.0
     * 
     * @param i_HttpID  数据Http请求ID
     * @return
     */
    @Xsql(id="XSQL_Data_DBHttp_Query" ,returnOne=true)
    public DBHttp queryByID(@Xparam("id") String i_HttpID);
    
    
    
    /**
     * 按XID查询数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-16
     * @version     v1.0
     * 
     * @param i_HttpXID  数据Http请求的XID
     * @return
     */
    @Xsql(id="XSQL_Data_DBHttp_Query" ,returnOne=true)
    public DBHttp queryByXID(@Xparam("xid") String i_HttpXID);
    
    
    
    /**
     * 新增、修改、逻辑删除数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     *
     * @param i_DBHttp        数据Http请求
     * @param i_DBHttpParams  数据Http请求参数的集合
     * @return
     */
    @Xsql("GXSQL_Data_DBHttp_Save")
    public boolean save(@Xparam("Http")       DBHttp            i_DBHttp
                       ,@Xparam("HttpParams") List<DBHttpParam> i_DBHttpParams);
    
}
