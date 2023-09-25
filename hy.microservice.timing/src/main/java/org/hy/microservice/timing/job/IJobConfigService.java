package org.hy.microservice.timing.job;

import java.util.List;

import org.hy.common.app.Param;
import org.hy.common.thread.Job;
import org.hy.common.thread.Jobs;





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
     * 查询云主机的通讯IP和端口
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     * 
     * @return
     */
    public List<Param> queryCloudServers();
    
    
    
    /**
     * 查询任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @return
     */
    public List<JobConfigReport> queryList();
    
    
    
    /**
     * 将XJava对象池中的与数据库中定时任务对象合并成JobConfigReport对外安全的暴露
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-25
     * @version     v1.0
     *
     * @param i_JobMM  XJava对象池中的定时任务对象
     * @param i_JobDB  数据库中的定时任务对象
     * @return
     */
    public JobConfigReport toJobConfigReport(Job i_JobMM ,JobConfig i_JobDB);
    
    
    
    /**
     * 按ID查询任务配置信息
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
     * 按Code查询任务配置信息
     * 
     * 注意：使用查询缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     * 
     * @param i_Code  任务配置编号
     *
     * @return
     */
    public JobConfig queryByCode(String i_Code);
    
    
    
    /**
     * 按ID或Code查询任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     * 
     * @param i_JobConfig  任务配置信息
     * @return
     */
    public JobConfig queryByIDCode(JobConfig i_JobConfig);
    
    
    
    /**
     * 新增、修改、逻辑删除任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @param io_JobConfig  任务配置信息对象
     * @return
     */
    public JobConfig save(JobConfig io_JobConfig);
    
    
    
    /**
     * 删除定时任务池中的任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     *
     * @param i_Jobs   定时任务池
     * @param i_Code   要删除任务编号
     */
    public void delJobByJobs(Jobs i_Jobs ,String i_Code);
    
}
