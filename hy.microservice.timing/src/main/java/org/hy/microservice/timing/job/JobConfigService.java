package org.hy.microservice.timing.job;

import java.io.Serializable;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;





/**
 * 任务配置信息的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-12
 * @version     v1.0
 */
@Xjava
public class JobConfigService implements IJobConfigService ,Serializable
{
    
    private static final long serialVersionUID = 1688409498515125449L;

    @Xjava
    private IJobConfigDAO jobConfigDAO;
    
    
    
    /**
     * 查询任务配置信息
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-12
     * @version     v1.0
     *
     * @return
     */
    @Override
    public List<JobConfig> queryList()
    {
        return this.jobConfigDAO.queryList();
    }
    
    
    
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
    @Override
    public JobConfig queryByID(String i_ID)
    {
        JobConfig v_JobConfig = new JobConfig();
        v_JobConfig.setId(i_ID);
        
        return this.queryByID(v_JobConfig);
    }
    
    
    
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
    @Override
    public JobConfig queryByID(JobConfig i_DSConfig)
    {
        return this.jobConfigDAO.queryByXID(i_DSConfig);
    }
    
    
    
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
    @Override
    public JobConfig save(JobConfig io_JobConfig)
    {
        if ( io_JobConfig == null )
        {
            return null;
        }
        
        if ( Help.isNull(io_JobConfig.getId()) )
        {
            io_JobConfig.setId(StringHelp.getUUID());
        }
        
        io_JobConfig.setIsDel(Help.NVL(io_JobConfig.getIsDel() ,0));
        
        this.jobConfigDAO.save(io_JobConfig ,io_JobConfig.makeStartTimes());
        return io_JobConfig;
    }
    
}
