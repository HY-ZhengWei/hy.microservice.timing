package org.hy.microservice.timing.jobHttp;

import java.util.Map;

import org.hy.common.Help;
import org.hy.common.thread.Job;
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
public class JobHttpCache implements IJobHttpCache
{
    @Xjava(ref="JobHttpToJava_XJobHttp")
    private ConfigToJavaDefault<JobHttp ,XJobHttp> toXJobHttp;
    
    @Xjava
    private IJobConfigDAO                          jobConfigDAO;
    
    
    
    /**
     * 刷新XJava对象池
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-19
     * @version     v1.0
     *
     * @param i_JobHttp
     * @return
     */
    @Override
    public boolean refreshXJava(JobHttp i_JobHttp)
    {
        // 删除
        if ( !i_JobHttp.getIsDel().equals(0) )
        {
            if ( !Help.isNull(i_JobHttp.getXidOld()) )
            {
                XJobHttp v_XJobHttpOld = (XJobHttp) XJava.getObject(i_JobHttp.getXidOld() ,false);
                if ( v_XJobHttpOld != null )
                {
                    refreshRelations(v_XJobHttpOld ,null);
                    
                    XJava.remove(i_JobHttp.getXidOld());
                }
            }
            
            if ( !Help.isNull(i_JobHttp.getXid()) )
            {
                XJobHttp v_XJobHttpOld = (XJobHttp) XJava.getObject(i_JobHttp.getXid() ,false);
                if ( v_XJobHttpOld != null )
                {
                    refreshRelations(v_XJobHttpOld ,null);
                    
                    XJava.remove(i_JobHttp.getXid());
                }
            }
        }
        // 修改 & 创建
        else
        {
            XJobHttp v_XJobHttpOld = null;
            // 防止修改Xid，先删除原Xid
            if ( !Help.isNull(i_JobHttp.getXidOld()) )
            {
                v_XJobHttpOld = (XJobHttp) XJava.getObject(i_JobHttp.getXidOld() ,false);
                if ( v_XJobHttpOld != null )
                {
                    XJava.remove(i_JobHttp.getXidOld());
                }
            }
            
            // 必先删除，才能再生成新的
            if ( !Help.isNull(i_JobHttp.getXid()) )
            {
                if ( XJava.getObject(i_JobHttp.getXid() ,false) != null )
                {
                    XJava.remove(i_JobHttp.getXid());
                }
            }
            
            XJobHttp v_XJobHttpNew = this.toXJobHttp.getObject(i_JobHttp);
            if ( v_XJobHttpOld != null )
            {
                // 替换原来的
                this.refreshRelations(v_XJobHttpOld ,v_XJobHttpNew);
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
     * @param i_Old  旧的定时任务请求X对象
     * @param i_New  新的定时任务请求X对象
     * @return
     */
    @Override
    public boolean refreshRelations(XJobHttp i_Old ,XJobHttp i_New)
    {
        Map<String ,Job> v_XJobs = XJava.getObjects(Job.class);
        if ( !Help.isNull(v_XJobs) )
        {
            for (Job v_XJob : v_XJobs.values())
            {
                if ( i_Old.getXJavaID().equals(v_XJob.getXid()) )
                {
                    
                    v_XJob.setXid(i_New == null ? "" : i_New.getXJavaID());
                }
            }
            
            JobConfig v_UpdateJob = new JobConfig();
            v_UpdateJob.setXidOld(i_Old.getXJavaID());
            v_UpdateJob.setXid   (i_New == null ? "" : i_New.getXJavaID());
            this.jobConfigDAO.updateXIDByLocal(v_UpdateJob);
        }
        
        return true;
    }
    
}
