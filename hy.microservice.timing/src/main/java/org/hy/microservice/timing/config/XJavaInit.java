package org.hy.microservice.timing.config;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.net.common.ClientCluster;
import org.hy.common.thread.Jobs;
import org.hy.common.thread.ThreadPool;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.common.xml.plugins.AppInitConfig;





/**
 * Web初始化信息
 * 
 * @author      ZhengWei(HY)、马龙
 * @createDate  2020-11-19
 * @version     v1.0
 */
public class XJavaInit extends AppInitConfig
{
    private static Logger  $Logger = Logger.getLogger(XJavaInit.class);
    
    private static boolean $Init   = false;
    
    private String xmlRoot;
    
    
    
    public XJavaInit()
    {
        this(true);
    }
    
    
    
    public XJavaInit(boolean i_IsStartJobs)
    {
        super(i_IsStartJobs);
        this.xmlRoot = Help.getClassHomePath();
        init(i_IsStartJobs);

    }

    public XJavaInit(boolean i_IsLog ,String i_EnCode)
    {
        super(i_IsLog,i_EnCode);
    }
    
    
    
    @SuppressWarnings("unchecked")
    private synchronized void init(boolean i_IsStartJobs)
    {
        if ( !$Init )
        {
            $Init = true;
            
            try
            {
                this.init("config/timing/ms.timing.sys.Config.xml"     ,this.xmlRoot);
                this.init("config/timing/ms.timing.startup.Config.xml" ,this.xmlRoot);
                this.init((List<Param>)XJava.getObject("StartupConfig_MS_Timing") ,this.xmlRoot);
                this.init(((Param)XJava.getObject("MS_Timing_RootPackageName")).getValue());
                init_TPool();
                
                if ( i_IsStartJobs )
                {
                    this.init("config/timing/ms.timing.job.Config.xml" ,this.xmlRoot);
                    
                    // 注册：定时任务服务的灾备机制的心跳任务
                    Jobs v_Jobs = (Jobs)XJava.getObject("MS_Timing_JOBS");
                    XJava.putObject(Jobs.$JOB_DisasterRecoverys_Check ,v_Jobs.createDisasterRecoveryJob());
                    
                    this.init_ClusterServers();
                }
            }
            catch (Exception exce)
            {
                $Logger.error(exce);
            }
        }
    }
    
    
    
    /**
     * 通过灾备机制的服务列表，自动生成集群监控列表。不再麻烦用户配置两次
     * 
     * @author      ZhengWei(HY)
     * @createDate  2021-12-14
     * @version     v1.0
     */
    @SuppressWarnings("unchecked")
    private void init_ClusterServers()
    {
        List<ClientCluster> v_DisasterRecoverys = (List<ClientCluster>)XJava.getObject("DisasterRecoverys");
        XJava.putObject("ClusterServers" ,v_DisasterRecoverys);
    }
    
    
    
    private void init_TPool()
    {
        ThreadPool.setMaxThread(    this.getIntConfig("MS_Timing_TPool_MaxThread"));
        ThreadPool.setMinThread(    this.getIntConfig("MS_Timing_TPool_MinThread"));
        ThreadPool.setMinIdleThread(this.getIntConfig("MS_Timing_TPool_MinIdleThread"));
        ThreadPool.setIntervalTime( this.getIntConfig("MS_Timing_TPool_IntervalTime"));
        ThreadPool.setIdleTimeKill( this.getIntConfig("MS_Timing_TPool_IdleTimeKill"));
        ThreadPool.setWaitResource( this.getIntConfig("MS_Timing_TPool_WaitResource"));
    }
    
    
    
    private int getIntConfig(String i_XJavaID)
    {
        return Integer.parseInt(XJava.getParam(i_XJavaID).getValue());
    }

}
