package org.hy.microservice.timing.http;

import java.util.Map;

import org.hy.common.Help;
import org.hy.common.thread.Job;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.common.ConfigToJavaDefault;
import org.hy.microservice.timing.job.IJobConfigDAO;
import org.hy.microservice.timing.job.JobConfig;





/**
 * 数据请求的缓存层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-12-08
 * @version     v1.0
 */
@Xjava
public class DBHttpCache implements IDBHttpCache
{
    @Xjava(ref="DBHttpToJava_XHttp")
    private ConfigToJavaDefault<DBHttp ,XHttp> toXHttp;
    
    @Xjava
    private IJobConfigDAO                      jobConfigDAO;
    
    
    
    /**
     * 刷新XJava对象池
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     *
     * @param i_DBHttp
     * @return
     */
    @Override
    public boolean refreshXJava(DBHttp i_DBHttp)
    {
        // 删除
        if ( !i_DBHttp.getIsDel().equals(0) )
        {
            if ( !Help.isNull(i_DBHttp.getXidOld()) )
            {
                XHttp v_XHttpOld = (XHttp) XJava.getObject(i_DBHttp.getXidOld() ,false);
                if ( v_XHttpOld != null )
                {
                    refreshRelations(v_XHttpOld ,null);
                    
                    XJava.remove(i_DBHttp.getXidOld());
                }
            }
            
            if ( !Help.isNull(i_DBHttp.getXid()) )
            {
                XHttp v_XHttpOld = (XHttp) XJava.getObject(i_DBHttp.getXid() ,false);
                if ( v_XHttpOld != null )
                {
                    refreshRelations(v_XHttpOld ,null);
                    
                    XJava.remove(i_DBHttp.getXid());
                }
            }
        }
        // 修改 & 创建
        else
        {
            XHttp v_XHttpOld = null;
            // 防止修改Xid，先删除原Xid
            if ( !Help.isNull(i_DBHttp.getXidOld()) )
            {
                v_XHttpOld = (XHttp) XJava.getObject(i_DBHttp.getXidOld() ,false);
                if ( v_XHttpOld != null )
                {
                    XJava.remove(i_DBHttp.getXidOld());
                }
            }
            
            // 必先删除，才能再生成新的
            if ( !Help.isNull(i_DBHttp.getXid()) )
            {
                if ( XJava.getObject(i_DBHttp.getXid() ,false) != null )
                {
                    XJava.remove(i_DBHttp.getXid());
                }
            }
            
            XHttp v_XHttpNew = this.toXHttp.getObject(i_DBHttp);
            if ( v_XHttpOld != null )
            {
                // 替换原来的
                this.refreshRelations(v_XHttpOld ,v_XHttpNew);
            }
        }
        
        return true;
    }
    
    
    
    /**
     * 刷新其它数据对象的引用
     * 
     * 原则：不销毁引用我的对象，仅替换新的我就好。
     * 解绑：无新X对象时，表示移除引用关系
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-12-08
     * @version     v1.0
     *
     * @param i_Old  旧的数据任务X对象
     * @param i_New  新的数据任务X对象
     * @return
     */
    @Override
    public boolean refreshRelations(XHttp i_Old ,XHttp i_New)
    {
        Map<String ,Job> v_XJobs = XJava.getObjects(Job.class);
        if ( !Help.isNull(v_XJobs) )
        {
            for (Job v_XJob : v_XJobs.values())
            {
                if ( i_Old.getXJavaID().equals(v_XJob.getXid()) )
                {
                    v_XJob.setXid(i_New.getXJavaID());
                }
            }
            
            JobConfig v_UpdateJob = new JobConfig();
            v_UpdateJob.setXidOld(i_Old.getXJavaID());
            v_UpdateJob.setXid   (i_New.getXJavaID());
            this.jobConfigDAO.updateXIDByLocal(v_UpdateJob);
        }
        
        return true;
    }
    
}
