package org.hy.microservice.timing.jobHttp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.timing.common.XObject;
import org.hy.microservice.timing.job.IJobConfigDAO;
import org.hy.microservice.timing.job.JobConfig;





/**
 * 定时任务请求的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-03
 * @version     v1.0
 */
@Xjava
public class JobHttpService implements IJobHttpService ,Serializable
{
    
    private static final long serialVersionUID = -6264215852718130464L;
    
    

    @Xjava
    private IJobHttpDAO   jobHttpDAO;
    
    @Xjava
    private IJobHttpCache jobHttpCache;
    
    @Xjava
    private IJobConfigDAO jobConfigDAO;
    
    
    
    /**
     * 查询定时任务请求列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     * 
     * @return
     */
    @Override
    public Map<String ,JobHttp> queryList()
    {
        return this.jobHttpDAO.queryList();
    }
    
    
    
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
    @Override
    public Map<String ,JobHttp> queryList(JobHttp i_JobHttp)
    {
        return this.jobHttpDAO.queryList(i_JobHttp);
    }
    
    
    
    /**
     * 按ID查询定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     * 
     * @param i_ID  定时任务请求ID
     * @return
     */
    @Override
    public JobHttp queryByID(String i_ID)
    {
        return this.jobHttpDAO.queryByID(i_ID);
    }
    
    
    
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
    @Override
    public JobHttp queryByXID(String i_XID)
    {
        return this.jobHttpDAO.queryByXID(i_XID);
    }
    
    
    
    /**
     * 按定时任务请求ID，排查引其它数据对象是否有引用它
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-27
     * @version     v1.0
     *
     * @param i_JobHttpID  定时任务请求ID
     * @return
     */
    @Override
    public List<XObject> queryRelations(String i_JobHttpID)
    {
        List<XObject> v_XObjects = new ArrayList<XObject>();
        if ( Help.isNull(i_JobHttpID) )
        {
            return v_XObjects;
        }
        
        JobHttp v_JobHttp = this.jobHttpDAO.queryByID(i_JobHttpID);
        
        JobConfig v_JobParam = new JobConfig();
        v_JobParam.setXid(v_JobHttp.getXid());
        
        List<JobConfig> v_JobLocals = this.jobConfigDAO.queryListByLocal(v_JobParam);
        if ( !Help.isNull(v_JobLocals) )
        {
            for (JobConfig v_Job : v_JobLocals)
            {
                v_XObjects.add(new XObject(v_Job));
            }
        }
        
        return v_XObjects;
    }
    
    
    
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
    @Override
    public boolean insert(JobHttp io_JobHttp)
    {
        io_JobHttp.setId(XObject.$Type_JobHttp + StringHelp.getUUID());
        io_JobHttp.setCreateUserID(Help.NVL(io_JobHttp.getCreateUserID() ,io_JobHttp.getUserID()));
        io_JobHttp.setUpdateUserID(io_JobHttp.getCreateUserID());
        io_JobHttp.setIsDel(       Help.NVL(io_JobHttp.getIsDel() ,0));
        
        int v_Ret = this.jobHttpDAO.insert(io_JobHttp);
        if ( v_Ret == 1 )
        {
            return this.jobHttpCache.refreshXJava(io_JobHttp);
        }
        else
        {
            return false;
        }
    }
    
    
    
    /**
     * 修改定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param io_JobHttp  定时任务请求
     * @return
     */
    @Override
    public boolean update(JobHttp io_JobHttp)
    {
        io_JobHttp.setUpdateUserID(Help.NVL(io_JobHttp.getUpdateUserID() ,io_JobHttp.getUserID()));
        io_JobHttp.setIsDel(       Help.NVL(io_JobHttp.getIsDel() ,0));
        
        JobHttp v_Old = this.queryByID(io_JobHttp.getId());
        int v_Ret = this.jobHttpDAO.update(io_JobHttp);
        if ( v_Ret == 1 )
        {
            if ( Help.isNull(io_JobHttp.getTokenHttpID()) && !"".equals(io_JobHttp.getTokenHttpID()) )
            {
                io_JobHttp.setTokenHttpID(v_Old.getTokenHttpID());
            }
            
            if ( Help.isNull(io_JobHttp.getTaskHttpID()) )
            {
                io_JobHttp.setTaskHttpID(v_Old.getTaskHttpID());
            }
            
            if ( Help.isNull(io_JobHttp.getComment()) )
            {
                io_JobHttp.setComment(v_Old.getComment());
            }
            
            io_JobHttp.setXid(Help.NVL(io_JobHttp.getXid() ,v_Old.getXid()));
            io_JobHttp.setXidOld(v_Old.getXid());
            return this.jobHttpCache.refreshXJava(io_JobHttp);
        }
        else
        {
            return false;
        }
    }
    
}
