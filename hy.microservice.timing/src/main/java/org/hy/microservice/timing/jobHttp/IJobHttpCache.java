package org.hy.microservice.timing.jobHttp;

/**
 * 定时任务请求的缓存层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-03
 * @version     v1.0
 */
public interface IJobHttpCache
{
    
    /**
     * 刷新XJava对象池
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param i_JobHttp
     * @return
     */
    public boolean refreshXJava(JobHttp i_JobHttp);
    
    
    
    /**
     * 刷新其它数据对象的引用
     * 
     * 原则：不销毁引用我的对象，仅替换新的我就好。
     * 解绑：无新X对象时，表示移除引用关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param i_Old  旧的定时任务请求X对象
     * @param i_New  新的定时任务请求X对象
     * @return
     */
    public boolean refreshRelations(XJobHttp i_Old ,XJobHttp i_New);
    
}
