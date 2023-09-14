package org.hy.microservice.timing.dataSource;

import java.io.Serializable;

import org.hy.common.xcql.DataSourceCQL;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.common.ConfigToJavaDefault;





/**
 * 图数据库的XJava对象实例
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-06-26
 * @version     v1.0
 */
@Xjava
public class DataSourceConfigToJava_DSCQL extends ConfigToJavaDefault<DataSourceConfig ,DataSourceCQL> implements Serializable
{
    
    private static final long serialVersionUID = 1439470197063054846L;
    
    
    
    @Override
    protected DataSourceCQL newObject(DataSourceConfig i_Config)
    {
        DataSourceCQL v_DSCQL = new DataSourceCQL();
        
        // neo4j://127.0.0.1:7687
        v_DSCQL.setUrl(i_Config.getDriverClassName() + "://" + i_Config.getHostName() + ":" + i_Config.getPort());
        v_DSCQL.setUsername(i_Config.getUserName());
        v_DSCQL.setPassword(i_Config.getPassword());
        v_DSCQL.setDatabase(i_Config.getDatabaseName());
        
        return v_DSCQL;
    }
    
}
