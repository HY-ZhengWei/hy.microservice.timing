package org.hy.microservice.timing.jobHttp;

import java.util.List;
import java.util.Map;

import org.hy.microservice.timing.common.XObject;





/**
 * 定时任务请求的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-03
 * @version     v1.0
 */
public interface IJobHttpService
{
    
    /**
     * 查询定时任务请求列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     * 
     * @return
     */
    public Map<String ,JobHttp> queryList();
    
    
    
    /**
     * 按条件查询定时任务请求列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     * 
     * @param i_JobHttp  定时任务请求
     * @return
     */
    public Map<String ,JobHttp> queryList(JobHttp i_JobHttp);
    
    
    
    /**
     * 按ID查询定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-18
     * @version     v1.0
     * 
     * @param i_ID  定时任务请求ID
     * @return
     */
    public JobHttp queryByID(String i_ID);
    
    
    
    /**
     * 按XID查询定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     * 
     * @param i_XID  定时任务请求的XID
     * @return
     */
    public JobHttp queryByXID(String i_XID);
    
    
    
    /**
     * 按定时任务请求ID，排查引其它数据对象是否有引用它
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-27
     * @version     v1.0
     *
     * @param i_PushID  定时任务请求ID
     * @return
     */
    public List<XObject> queryRelations(String i_PushID);
    
    
    
    /**
     * 新增定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param io_JobHttp  定时任务请求
     * @return
     */
    public boolean insert(JobHttp io_JobHttp);
    
    
    
    /**
     * 修改定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param i_JobHttp  定时任务请求
     * @return
     */
    public boolean update(JobHttp i_JobHttp);
    
}
