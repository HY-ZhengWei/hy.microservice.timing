package org.hy.microservice.timing.http;

import java.util.List;
import java.util.Map;

import org.hy.microservice.timing.common.XObject;





/**
 * 数据Http请求的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-10-19
 * @version     v1.0
 */
public interface IDBHttpService
{
    
    
    /**
     * 查询任务数据Http请求列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     * 
     * @return
     */
    public Map<String ,DBHttp> queryList();
    
    
    
    /**
     * 查询任务数据Http请求列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-24
     * @version     v1.0
     * 
     * @param i_DBHttp  数据Http请求
     * @return
     */
    public Map<String ,DBHttp> queryList(DBHttp i_DBHttp);
    
    
    
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
    public DBHttp queryByID(String i_HttpID);
    
    
    
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
    public DBHttp queryByXID(String i_HttpXID);
    
    
    
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
    public DBHttp queryByIDXID(DBHttp i_DBHttp);
    
    
    
    /**
     * 新增、修改、逻辑删除数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     *
     * @param io_DBHttp  数据Http请求
     * @return
     */
    public DBHttp save(DBHttp io_DBHttp);
    
    
    
    /**
     * 按数据请求ID，排查引其它数据对象是否有引用它
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-27
     * @version     v1.0
     *
     * @param i_HttpID  数据请求ID
     * @return
     */
    public List<XObject> queryRelations(String i_HttpID);
    
}
