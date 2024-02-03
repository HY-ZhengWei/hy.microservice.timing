package org.hy.microservice.timing.http;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.timing.common.XObject;
import org.hy.microservice.timing.job.IJobConfigDAO;
import org.hy.microservice.timing.job.JobConfig;





/**
 * 数据Http请求的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-10-19
 * @version     v1.0
 */
@Xjava
public class DBHttpService implements IDBHttpService ,Serializable
{
    
    private static final long serialVersionUID = -5843664346278931443L;

    
    
    @Xjava
    private IDBHttpDAO    dbHttpDAO;
    
    @Xjava
    private IJobConfigDAO jobConfigDAO;
    
    @Xjava
    private IDBHttpCache  dbHttpCache;
    


    /**
     * 查询任务数据Http请求列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     * 
     * @return
     */
    @Override
    public Map<String ,DBHttp> queryList()
    {
        return this.dbHttpDAO.queryList();
    }
    
    
    
    /**
     * 查询任务数据Http请求列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-24
     * @version     v1.0
     * 
     * @param i_DBHttp  数据Http请求
     * @return
     */
    @Override
    public Map<String ,DBHttp> queryList(DBHttp i_DBHttp)
    {
        return this.dbHttpDAO.queryList(i_DBHttp);
    }
    
    
    
    /**
     * 按ID或XID查询数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     * 
     * @param i_DBHttp  数据Http请求
     * @return
     */
    @Override
    public DBHttp queryByIDXID(DBHttp i_DBHttp)
    {
        return this.dbHttpDAO.queryByIDXID(i_DBHttp);
    }
    
    
    
    /**
     * 按ID查询数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-16
     * @version     v1.0
     * 
     * @param i_HttpID  数据Http请求ID
     * @return
     */
    @Override
    public DBHttp queryByID(String i_HttpID)
    {
        return this.dbHttpDAO.queryByID(i_HttpID);
    }
    
    
    
    /**
     * 按XID查询数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-16
     * @version     v1.0
     * 
     * @param i_HttpXID  数据Http请求的XID
     * @return
     */
    @Override
    public DBHttp queryByXID(String i_HttpXID)
    {
        return this.dbHttpDAO.queryByXID(i_HttpXID);
    }
    
    
    
    /**
     * 新增、修改、逻辑删除数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     *
     * @param io_DBHttp  数据Http请求
     * @return
     */
    @Override
    public DBHttp save(DBHttp io_DBHttp)
    {
        if ( io_DBHttp == null )
        {
            return null;
        }
        
        io_DBHttp.setCreateUserID(Help.NVL(io_DBHttp.getCreateUserID() ,io_DBHttp.getUserID()));
        io_DBHttp.setUpdateUserID(Help.NVL(io_DBHttp.getUpdateUserID() ,io_DBHttp.getUserID()));
        io_DBHttp.setIsDel(       Help.NVL(io_DBHttp.getIsDel()        ,0));
        
        DBHttp v_Old = null;
        if ( Help.isNull(io_DBHttp.getId()) )
        {
            io_DBHttp.setId(XObject.$Type_Http + StringHelp.getUUID());
            
            io_DBHttp.setProtocol(      Help.NVL(io_DBHttp.getProtocol()       ,"http"));
            io_DBHttp.setPort(          Help.NVL(io_DBHttp.getPort()           ,"https".equalsIgnoreCase(io_DBHttp.getProtocol()) ? 443 : 80));
            io_DBHttp.setRequestType(   Help.NVL(io_DBHttp.getRequestType()    ,XHttp.$Request_Type_Get));
            io_DBHttp.setContentType(   Help.NVL(io_DBHttp.getContentType()    ,"application/json"));
            io_DBHttp.setCharset(       Help.NVL(io_DBHttp.getCharset()        ,"UTF-8"));
            io_DBHttp.setConnectTimeout(Help.NVL(io_DBHttp.getConnectTimeout() ,30  * 1000));
            io_DBHttp.setReadTimeout(   Help.NVL(io_DBHttp.getReadTimeout()    ,300 * 1000));
            io_DBHttp.setEncodeType(    Help.NVL(io_DBHttp.getEncodeType()     ,0));
        }
        else
        {
            v_Old = this.queryByID(io_DBHttp.getId());
            io_DBHttp.setXid(           Help.NVL(io_DBHttp.getXid()            ,v_Old.getXid()));
            io_DBHttp.setProtocol(      Help.NVL(io_DBHttp.getProtocol()       ,v_Old.getProtocol()));
            io_DBHttp.setPort(          Help.NVL(io_DBHttp.getPort()           ,v_Old.getPort()));
            io_DBHttp.setRequestType(   Help.NVL(io_DBHttp.getRequestType()    ,v_Old.getRequestType()));
            io_DBHttp.setContentType(   Help.NVL(io_DBHttp.getContentType()    ,v_Old.getContentType()));
            io_DBHttp.setCharset(       Help.NVL(io_DBHttp.getCharset()        ,v_Old.getCharset()));
            io_DBHttp.setConnectTimeout(Help.NVL(io_DBHttp.getConnectTimeout() ,v_Old.getConnectTimeout()));
            io_DBHttp.setReadTimeout(   Help.NVL(io_DBHttp.getReadTimeout()    ,v_Old.getReadTimeout()));
            io_DBHttp.setEncodeType(    Help.NVL(io_DBHttp.getEncodeType()     ,v_Old.getEncodeType()));
            io_DBHttp.setHttpParams(    Help.NVL(io_DBHttp.getHttpParams()     ,v_Old.getHttpParams()));
        }
        
        if ( !Help.isNull(io_DBHttp.getHttpParams()) )
        {
            // 所有目标的数据Http请求ID指向数据Http请求本体
            for (DBHttpParam v_Param : io_DBHttp.getHttpParams())
            {
                v_Param.setHttpID(io_DBHttp.getId());
                v_Param.setCreateUserID(io_DBHttp.getCreateUserID());
            }
        }
        else if ( io_DBHttp.getHttpParams() == null )
        {
            io_DBHttp.setHttpParams(new ArrayList<DBHttpParam>());
        }
        
        boolean v_Ret = this.dbHttpDAO.save(io_DBHttp ,io_DBHttp.getHttpParams());
        if ( v_Ret )
        {
            if ( v_Old != null )
            {
                io_DBHttp.setXidOld(v_Old.getXid());
            }
            this.dbHttpCache.refreshXJava(io_DBHttp);
        }
        
        return v_Ret ? io_DBHttp : null;
    }
    
    
    
    /**
     * 按数据请求ID，排查引其它数据对象是否有引用它
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-27
     * @version     v1.0
     *
     * @param i_HttpID  数据请求ID
     * @return
     */
    @Override
    public List<XObject> queryRelations(String i_HttpID)
    {
        List<XObject> v_XObjects = new ArrayList<XObject>();
        if ( Help.isNull(i_HttpID) )
        {
            return v_XObjects;
        }
        
        DBHttp v_Http = this.dbHttpDAO.queryByID(i_HttpID);
        
        JobConfig v_JobParam = new JobConfig();
        v_JobParam.setXid(v_Http.getXid());
        
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
    
}
