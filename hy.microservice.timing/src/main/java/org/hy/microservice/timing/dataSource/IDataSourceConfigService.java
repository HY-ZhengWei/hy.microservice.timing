package org.hy.microservice.timing.dataSource;

import java.util.Map;

import org.hy.common.db.DataSourceGroup;





/**
 * 数据源配置的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2022-08-18
 * @version     v1.0
 */
public interface IDataSourceConfigService
{
    
    /**
     * 测试连接数据库
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-04-20
     * @version     v1.0
     *
     * @param io_DSConfig
     * @return  当返回空时，表示成功。其它为异常信息
     */
    public String testConnect(DataSourceConfig io_DSConfig);
    
    
    
    /**
     * 获取数据源对象
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-09-05
     * @version     v1.0
     *
     * @param i_DataSourceConfig
     * @return
     */
    public DataSourceGroup getDataSourceGroup(DataSourceConfig i_DataSourceConfig);
    
    
    
    /**
     * 查询数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     *
     * @return
     */
    public Map<String ,DataSourceConfig> queryList();
    
    
    
    /**
     * 按XID查询数据源配置
     * 
     * 注意：使用查询缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-29
     * @version     v1.0
     * 
     * @param i_XID  数据源配置XID
     *
     * @return
     */
    public DataSourceConfig queryByXID(String i_XID);
    
    
    
    /**
     * 按XID查询数据源配置
     * 
     * 注意：使用查询缓存
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     * 
     * @param i_DSConfig  数据源配置对象
     *
     * @return
     */
    public DataSourceConfig queryByXID(DataSourceConfig i_DSConfig);
    
    
    
    /**
     * 新增数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     *
     * @param io_DSConfig  数据源配置对象
     * @return
     */
    public DataSourceConfig insert(DataSourceConfig io_DSConfig);
    
    
    
    /**
     * 修改数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     *
     * @param io_DSConfig  数据源配置对象
     * @return
     */
    public DataSourceConfig update(DataSourceConfig io_DSConfig);
    
    
    
    /**
     * 解密
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-18
     * @version     v1.0
     * 
     * @param i_DSConfig  密文
     * @return
     */
    public String decryptUserName(DataSourceConfig i_DSConfig);
    
    
    
    /**
     * 加密
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-18
     * @version     v1.0
     * 
     * @param i_DSConfig  明文
     * @return
     */
    public String encryptUserName(DataSourceConfig i_DSConfig);
    
    
    
    /**
     * 解密
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-18
     * @version     v1.0
     * 
     * @param i_DSConfig  密文
     * @return
     */
    public String decryptPassword(DataSourceConfig i_DSConfig);
    
    
    
    /**
     * 加密
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-18
     * @version     v1.0
     * 
     * @param i_DSConfig  明文
     * @return
     */
    public String encryptPassword(DataSourceConfig i_DSConfig);
    
}
