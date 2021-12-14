package org.hy.microservice.timing.config;

import java.util.List;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.net.common.ClientCluster;
import org.hy.common.thread.Jobs;
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
        StringBuilder       v_Buffer            = new StringBuilder();
        for (ClientCluster v_Client : v_DisasterRecoverys)
        {
            v_Buffer.append(v_Client.getHost()).append(":").append(v_Client.getPort()).append(",");
        }
        
        Param v_ClusterServers = new Param().setName("ClusterServers").setValue(v_Buffer.toString()).setComment("全部集群服务器列表(用逗号分隔)");
        XJava.putObject(v_ClusterServers.getName() ,v_ClusterServers);
    }

}
