package org.hy.microservice.timing.http;

import org.hy.common.xml.XHttp;





/**
 * 数据请求的缓存层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-12-08
 * @version     v1.0
 */
public interface IDBHttpCache
{
    
    /**
     * 刷新XJava对象池
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     *
     * @param i_DBHttp
     * @return
     */
    public boolean refreshXJava(DBHttp i_DBHttp);
    
    
    
    /**
     * 刷新其它数据对象的引用
     * 
     * 原则：不销毁引用我的对象，仅替换新的我就好。
     * 解绑：无新X对象时，表示移除引用关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-12-08
     * @version     v1.0
     *
     * @param i_Old  旧的数据任务X对象
     * @param i_New  新的数据任务X对象
     * @return
     */
    public boolean refreshRelations(XHttp i_Old ,XHttp i_New);
    
}
