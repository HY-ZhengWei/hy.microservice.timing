package org.hy.microservice.timing.dataSource;

import java.io.Serializable;

import org.hy.common.db.DataSourceGroup;
import org.hy.common.db.DatabaseInfo;
import org.hy.common.xml.annotation.Xjava;
import org.hy.microservice.common.ConfigToJavaDefault;

import com.alibaba.druid.pool.DruidDataSource;





/**
 * 数据源的XJava对象实例
 *
 * @author      ZhengWei(HY)
 * @createDate  2022-08-24
 * @version     v1.0
 */
@Xjava
public class DataSourceConfigToJava_DBG extends ConfigToJavaDefault<DataSourceConfig ,DataSourceGroup> implements Serializable
{
    
    private static final long serialVersionUID = -5310134696785465114L;
    
    
    
    protected DatabaseInfo newDB(DataSourceConfig i_Config)
    {
        DatabaseInfo v_DB = new DatabaseInfo();
        
        v_DB.setDriver(  i_Config.getDriverClassName());
        v_DB.setUser(    i_Config.getUserName());
        v_DB.setPassword(i_Config.getPassword());
        
        if ( DataSourceType.$DBType_SQLServer.equals(i_Config.getDataSourceType().toUpperCase()) )
        {
            v_DB.setUrl("jdbc:jtds:sqlserver://" + i_Config.getHostName() + ":" + i_Config.getPort() + ";DatabaseName=" + i_Config.getDatabaseName());
        }
        else if ( DataSourceType.$DBType_MySQL.equals(i_Config.getDataSourceType().toUpperCase()) )
        {
            v_DB.setUrl("jdbc:mysql://" + i_Config.getHostName()
                      + ":" + i_Config.getPort()
                      + "/" + i_Config.getDatabaseName()
                      + "?useUnicode=true&characterEncoding=utf8");
        }
        else if ( DataSourceType.$DBType_PostgreSQL.equals(i_Config.getDataSourceType().toUpperCase()) )
        {
            v_DB.setUrl("jdbc:postgresq1://" + i_Config.getHostName() + ":" + i_Config.getPort() + "/" + i_Config.getDatabaseName());
        }
        else if ( DataSourceType.$DBType_Oracle.equals(i_Config.getDataSourceType().toUpperCase()) )
        {
            v_DB.setUrl("jdbc:oracle:thin:@" + i_Config.getHostName() + ":" + i_Config.getPort() + ":" + i_Config.getDatabaseName());
        }
        
        return v_DB;
    }
    
    
    
    protected DruidDataSource newDS(DataSourceConfig i_Config)
    {
        DruidDataSource v_DS = new DruidDataSource();
        
        v_DS.setDriverClassName(i_Config.getDriverClassName());
        v_DS.setUsername(       i_Config.getUserName());
        v_DS.setPassword(       i_Config.getPassword());
        
        if ( i_Config.getInitialSize() != null )
        {
            v_DS.setInitialSize(i_Config.getInitialSize());
        }
        if ( i_Config.getMinIdle() != null )
        {
            v_DS.setMinIdle(i_Config.getMinIdle());
        }
        if ( i_Config.getMaxActive() != null )
        {
            v_DS.setMaxActive(i_Config.getMaxActive());
        }
        if ( i_Config.getMaxWait() != null )
        {
            v_DS.setMaxWait(i_Config.getMaxWait());
        }
        if ( i_Config.getLoginTimeout() != null )
        {
            v_DS.setLoginTimeout(i_Config.getLoginTimeout());
        }
        if ( i_Config.getFailFast() != null )
        {
            v_DS.setFailFast(i_Config.getFailFast());
        }
        
        v_DS.setTimeBetweenEvictionRunsMillis(60000);
        v_DS.setMinEvictableIdleTimeMillis(300000);
        v_DS.setTestWhileIdle(true);
        v_DS.setTestOnBorrow(false);
        v_DS.setTestOnReturn(false);
        v_DS.setPoolPreparedStatements(false);
        v_DS.setMaxPoolPreparedStatementPerConnectionSize(20);
        
        if ( DataSourceType.$DBType_SQLServer.equals(i_Config.getDataSourceType().toUpperCase()) )
        {
            v_DS.setValidationQuery("SELECT 1");
            v_DS.setUrl("jdbc:jtds:sqlserver://" + i_Config.getHostName() + ":" + i_Config.getPort() + ";DatabaseName=" + i_Config.getDatabaseName());
        }
        else if ( DataSourceType.$DBType_MySQL.equals(i_Config.getDataSourceType().toUpperCase()) )
        {
            v_DS.setValidationQuery("SELECT 1");
            v_DS.setUrl("jdbc:mysql://" + i_Config.getHostName()
                      + ":" + i_Config.getPort()
                      + "/" + i_Config.getDatabaseName()
                      + "?useUnicode=true&characterEncoding=utf8");
        }
        else if ( DataSourceType.$DBType_PostgreSQL.equals(i_Config.getDataSourceType().toUpperCase()) )
        {
            v_DS.setValidationQuery("SELECT 1 ");
            v_DS.setUrl("jdbc:postgresq1://" + i_Config.getHostName() + ":" + i_Config.getPort() + "/" + i_Config.getDatabaseName());
        }
        else if ( DataSourceType.$DBType_Oracle.equals(i_Config.getDataSourceType().toUpperCase()) )
        {
            v_DS.setValidationQuery("SELECT 1 FROM Dual");
            v_DS.setUrl("jdbc:oracle:thin:@" + i_Config.getHostName() + ":" + i_Config.getPort() + ":" + i_Config.getDatabaseName());
        }
        
        return v_DS;
    }
    
    

    @Override
    protected DataSourceGroup newObject(DataSourceConfig i_Config)
    {
        DataSourceGroup v_DSG = new DataSourceGroup();
        v_DSG.setXJavaID(i_Config.getXJavaID());
        v_DSG.add(this.newDS(i_Config));
        
        return v_DSG;
    }
    
}
