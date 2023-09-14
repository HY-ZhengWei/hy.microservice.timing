package org.hy.microservice.timing.dataSource;

import org.hy.common.db.DataSourceGroup;
import org.hy.common.xcql.DataSourceCQL;





/**
 * 数据源类型
 *
 * @author      ZhengWei(HY)
 * @createDate  2022-09-28
 * @version     v1.0
 */
public class DataSourceType
{
    
    public static final String $DBType_Oracle     = DataSourceGroup.$DBType_Oracle;
                                                  
    public static final String $DBType_MySQL      = DataSourceGroup.$DBType_MySQL;
                                                  
    public static final String $DBType_SQLServer  = DataSourceGroup.$DBType_SQLServer;
                                                  
    public static final String $DBType_PostgreSQL = DataSourceGroup.$DBType_PostgreSQL;
    
    public static final String $Message_XNet      = "XNET";
                                                  
    public static final String $Message_RocketMQ  = "ROCKETMQ";
    
    public static final String $DBType_Neo4j      = DataSourceCQL.$DBType_Neo4j;
    
    
    
    /**
     * 数值类型是否为合法
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-09-28
     * @version     v1.0
     *
     * @param i_ValueType  数值的类型
     * @return
     */
    public static boolean isValueTypeOK(String i_ValueType)
    {
        if ( $DBType_Oracle.equalsIgnoreCase(i_ValueType) )
        {
            return true;
        }
        else if ( $DBType_MySQL.equalsIgnoreCase(i_ValueType) )
        {
            return true;
        }
        else if ( $DBType_SQLServer.equalsIgnoreCase(i_ValueType) )
        {
            return true;
        }
        else if ( $DBType_PostgreSQL.equalsIgnoreCase(i_ValueType) )
        {
            return true;
        }
        else if ( $Message_XNet.equalsIgnoreCase(i_ValueType) )
        {
            return true;
        }
        else if ( $Message_RocketMQ.equalsIgnoreCase(i_ValueType) )
        {
            return true;
        }
        else if ( $DBType_Neo4j.equalsIgnoreCase(i_ValueType) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    
    
    private DataSourceType()
    {
        
    }
    
}
