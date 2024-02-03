package org.hy.microservice.timing.jobHttp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.timing.common.XObject;





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
    private IJobHttpDAO   JobHttpDAO;
    
    @Xjava
    private IJobHttpCache JobHttpCache;
    
    
    
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
        return this.JobHttpDAO.queryList();
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
        return this.JobHttpDAO.queryList(i_JobHttp);
    }
    
    
    
    /**
     * 按ID查询定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     * 
     * @param i_PushID  定时任务请求ID
     * @return
     */
    @Override
    public JobHttp queryByID(String i_PushID)
    {
        return this.JobHttpDAO.queryByID(i_PushID);
    }
    
    
    
    /**
     * 按XID查询定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     * 
     * @param i_PushXID  定时任务请求的XID
     * @return
     */
    @Override
    public JobHttp queryByXID(String i_PushXID)
    {
        return this.JobHttpDAO.queryByXID(i_PushXID);
    }
    
    
    
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
    @Override
    public List<XObject> queryRelations(String i_PushID)
    {
        return new ArrayList<XObject>();
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
        
        int v_Ret = this.JobHttpDAO.insert(io_JobHttp);
        if ( v_Ret == 1 )
        {
            return this.JobHttpCache.refreshXJava(io_JobHttp);
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
        int v_Ret = this.JobHttpDAO.update(io_JobHttp);
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
            return this.JobHttpCache.refreshXJava(io_JobHttp);
        }
        else
        {
            return false;
        }
    }
    
}
