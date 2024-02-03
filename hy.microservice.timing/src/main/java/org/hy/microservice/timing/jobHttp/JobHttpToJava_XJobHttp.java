package org.hy.microservice.timing.jobHttp;

import java.io.Serializable;

import org.hy.common.Help;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XJava;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.ConfigToJavaDefault;
import org.hy.microservice.timing.http.DBHttp;
import org.hy.microservice.timing.http.IDBHttpService;





/**
 * 定时任务请求的XJava对象实例：XJobHttp类型
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-03
 * @version     v1.0
 */
@Xjava
public class JobHttpToJava_XJobHttp extends ConfigToJavaDefault<JobHttp ,XJobHttp> implements Serializable
{

    private static final long serialVersionUID = 1223444309275483976L;

    private static Logger $Logger = new Logger(JobHttpToJava_XJobHttp.class);

    
    
    @Xjava
    private IDBHttpService    dbHttpService;

    
    
    @Override
    protected XJobHttp newObject(JobHttp i_Config)
    {
        DBHttp v_TaskHttp = this.dbHttpService.queryByID(i_Config.getTaskHttpID());
        if ( v_TaskHttp == null )
        {
            $Logger.error(i_Config.getXJavaID() + "未能匹配到数据请求对象");
            return null;
        }
        
        XHttp v_XTaskHttp = (XHttp) XJava.getObject(v_TaskHttp.getXid());
        if ( v_XTaskHttp == null )
        {
            $Logger.error(i_Config.getXJavaID() + "未能在XJava对象池中匹配到数据请求对象");
            return null;
        }
        
        XHttp v_XTokenHttp = null;
        if ( !Help.isNull(i_Config.getTokenHttpID()) )
        {
            DBHttp v_TokenHttp = this.dbHttpService.queryByID(i_Config.getTokenHttpID());
            if ( v_TokenHttp == null )
            {
                $Logger.error(i_Config.getXJavaID() + "未能匹配到票据请求对象");
                return null;
            }
            
            v_XTokenHttp = (XHttp) XJava.getObject(v_TokenHttp.getXid());
            if ( v_XTokenHttp == null )
            {
                $Logger.error(i_Config.getXJavaID() + "未能在XJava对象池中匹配到票据请求对象");
                return null;
            }
        }
        
        XJobHttp v_XJobHttp = new XJobHttp(i_Config);
        v_XJobHttp.setTaskHttp( v_XTaskHttp);
        v_XJobHttp.setTokenHttp(v_XTokenHttp);
        
        return v_XJobHttp;
    }

}
