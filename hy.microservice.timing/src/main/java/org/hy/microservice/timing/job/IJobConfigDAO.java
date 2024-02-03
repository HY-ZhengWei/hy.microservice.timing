package org.hy.microservice.timing.job;

import java.util.List;

import org.hy.common.app.Param;
import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;
import org.hy.microservice.timing.monitor.JobUser;





/**
 * 任务配置信息的操作DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-12
 * @version     v1.0
 */
@Xjava(id="JobConfigDAO" ,value=XType.XSQL)
public interface IJobConfigDAO
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
    @Xsql("XSQL_Timing_CloudServer_Query")
    public List<Param> queryCloudServers();
    
    
    
    /**
     * 查询本地任务配置信息列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-02
     * @version     v1.0
     * 
     * @param i_JobConfig  任务配置信息
     * @return
     */
    @Xsql("XSQL_Timing_JobConfig_Query_ByLocal")
    public List<JobConfig> queryListByLocal(JobConfig i_JobConfig);
    
    
    
    /**
     * 查询任务配置信息列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     * 

     * @return
     */
    @Xsql("XSQL_Timing_JobConfig_Query")
    public List<JobConfig> queryList();
    
    
    
    /**
     * 查询任务配置信息列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-23
     * @version     v1.0
     * 
     * @param i_JobConfig  任务配置信息
     * @return
     */
    @Xsql("XSQL_Timing_JobConfig_Query")
    public List<JobConfig> queryList(JobConfig i_JobConfig);
    
    
    
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
    @Xsql(id="XSQL_Timing_JobConfig_Query" ,returnOne=true)
    public JobConfig queryByIDCode(JobConfig i_JobConfig);
    
    
    
    /**
     * 更新本机定时任务的XID
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-02
     * @version     v1.0
     *
     * @param i_JobConfig
     * @return
     */
    @Xsql("XSQL_Timing_JobConfig_Update_ByLocal")
    public int updateXIDByLocal(JobConfig i_JobConfig);
    
    
    
    /**
     * 新增、修改、逻辑删除任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @param i_JobConfig      任务配置信息
     * @param i_JobStartTimes  任务开始时间组
     * @param i_JobUsers       任务责任人
     * @return
     */
    @Xsql("GXSQL_Timing_JobConfig_Save")
    public boolean save(@Xparam("Job")           JobConfig          i_JobConfig
                       ,@Xparam("JobStartTimes") List<JobStartTime> i_JobStartTimes
                       ,@Xparam("JobUsers")      List<JobUser>      i_JobUsers);
    
}
