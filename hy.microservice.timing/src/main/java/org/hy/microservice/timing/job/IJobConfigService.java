package org.hy.microservice.timing.job;

import java.util.List;





/**
 * 任务配置信息的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-12
 * @version     v1.0
 */
public interface IJobConfigService
{
    
    /**
     * 查询任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @return
     */
    public List<JobConfig> queryList();
    
    
    
    /**
     * 按XID查询任务配置信息
     * 
     * 注意：使用查询缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     * 
     * @param i_ID  任务配置信息ID
     *
     * @return
     */
    public JobConfig queryByID(String i_ID);
    
    
    
    /**
     * 按ID查询任务配置信息
     * 
     * 注意：使用查询缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     * 
     * @param i_DSConfig  任务配置信息对象
     *
     * @return
     */
    public JobConfig queryByID(JobConfig i_DSConfig);
    
    
    
    /**
     * 新增任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @param io_JobConfig  任务配置信息对象
     * @return
     */
    public JobConfig save(JobConfig io_JobConfig);
    
}
