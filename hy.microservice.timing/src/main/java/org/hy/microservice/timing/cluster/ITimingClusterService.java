package org.hy.microservice.timing.cluster;





/**
 * 任务服务的集群相关的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-25
 * @version     v1.0
 */
public interface ITimingClusterService
{
    
    /**
     * 集群同步定时任务的信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-25
     * @version     v1.0
     *
     * @param i_JobID       任务ID
     * @param i_JobOldCode  旧的任务编号
     * @return              执行是否成功
     */
    public boolean syncCluster(String i_JobID ,String i_JobOldCode);
    
}
