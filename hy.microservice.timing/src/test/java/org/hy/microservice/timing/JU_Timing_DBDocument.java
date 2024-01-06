package org.hy.microservice.timing;

import org.hy.microservice.common.config.XJavaInit;
import org.hy.microservice.common.openapi.DBDocument;
import org.junit.Test;





/**
 * 数据库设计文档的生成
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-01-06
 * @version     v1.0
 */
public class JU_Timing_DBDocument
{
    
    private static boolean $IsInit = false;
    
    
    
    public JU_Timing_DBDocument()
    {
        synchronized ( this )
        {
            if ( !$IsInit )
            {
                $IsInit = true;
                
                new XJavaInit(false ,"C:\\Software\\apache-tomcat-9.0.44\\webapps\\hy.microservice.timing\\WEB-INF\\classes\\");
            }
        }
    }
    
    
    
    @Test
    public void makeDoc()
    {
        DBDocument.makeDatabaseDoc("DS_MS_Timing" ,"V1.4");
    }
    
}
