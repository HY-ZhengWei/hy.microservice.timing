package org.hy.microservice.timing.dataSource;

import java.util.Map;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xsql;





/**
 * 数据源配置的操作DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2022-08-18
 * @version     v1.0
 */
@Xjava(id="DataSourceConfigDAO" ,value=XType.XSQL)
public interface IDataSourceConfigDAO
{
    
    /**
     * 查询数据源配置列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-18
     * @version     v1.0
     * 
     * @return
     */
    @Xsql("XSQL_Timing_DataSourceConfig_Query")
    public Map<String ,DataSourceConfig> queryList();
    
    
    
    /**
     * 按XID查询数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     * 
     * @param i_DSConfig  数据源配置
     * @return
     */
    @Xsql(id="XSQL_Timing_DataSourceConfig_Query_ByXID" ,returnOne=true)
    public DataSourceConfig queryByXID(DataSourceConfig i_DSConfig);
    
    
    
    /**
     * 新增数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     *
     * @param i_DSConfig  数据源配置
     * @return
     */
    @Xsql("XSQL_Timing_DataSourceConfig_Insert")
    public int insert(DataSourceConfig i_DSConfig);
    
    
    
    /**
     * 修改数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     *
     * @param i_DSConfig  数据源配置
     * @return
     */
    @Xsql("XSQL_Timing_DataSourceConfig_Update")
    public int update(DataSourceConfig i_DSConfig);
    
}
