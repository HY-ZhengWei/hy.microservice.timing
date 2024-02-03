package org.hy.microservice.timing.jobHttp;

import java.util.Map;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 定时任务请求的操作DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-03
 * @version     v1.0
 */
@Xjava(id="JobHttpDAO" ,value=XType.XSQL)
public interface IJobHttpDAO
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
    @Xsql("XSQL_Timing_JobHttp_Query")
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
    @Xsql("XSQL_Timing_JobHttp_Query")
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
    @Xsql(id="XSQL_Timing_JobHttp_Query" ,returnOne=true)
    public JobHttp queryByID(@Xparam("id") String i_ID);
    
    
    
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
    @Xsql(id="XSQL_Timing_JobHttp_Query" ,returnOne=true)
    public JobHttp queryByXID(@Xparam("xid") String i_XID);
    
    
    
    /**
     * 按数据请求ID查询定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     * 
     * @param i_TaskHttpID  数据请求ID
     * @return
     */
    @Xsql(id="XSQL_Timing_JobHttp_Query")
    public Map<String ,JobHttp> queryByTaskHttpID(@Xparam("taskHttpID") String i_TaskHttpID);
    
    
    
    /**
     * 按Token请求ID查询定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     * 
     * @param i_TokenHttpID  Token请求ID
     * @return
     */
    @Xsql(id="XSQL_Timing_JobHttp_Query")
    public Map<String ,JobHttp> queryByTokenHttpID(@Xparam("tokenHttpID") String i_TokenHttpID);
    
    
    
    /**
     * 新增定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param i_JobHttp  定时任务请求
     * @return
     */
    @Xsql("XSQL_Timing_JobHttp_Insert")
    public int insert(JobHttp i_JobHttp);
    
    
    
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
    @Xsql("XSQL_Timing_JobHttp_Update")
    public int update(JobHttp i_JobHttp);
    
}
