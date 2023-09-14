package org.hy.microservice.timing.dataSource;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.db.DBOperation;
import org.hy.common.db.DataSourceGroup;
import org.hy.common.db.DatabaseInfo;
import org.hy.common.license.AES;
import org.hy.common.net.common.ClientCluster;
import org.hy.common.xcql.DataSourceCQL;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.ConfigToJava;
import org.hy.microservice.common.ConfigToJavaDefault;





/**
 * 数据源配置的业务层
 *
 * @author      ZhengWei(HY)
 * @createDate  2022-08-18
 * @version     v1.0
 */
@Xjava
public class DataSourceConfigService implements IDataSourceConfigService ,Serializable
{
    
    private static final long serialVersionUID = -6466058080437356293L;
    
    private static final Logger                             $Logger        = new Logger(DataSourceConfigService.class);

    private static final String                             $Encrypt       = "encrypt";
    
    /** 数据源配置的缓存。Map.key为XID。注意：此缓存中的对象禁止放到XJava对象池中，因为XID会冲突覆盖 */
    private static final Map<String ,DataSourceConfig>      $DSConfigCache = new HashMap<String ,DataSourceConfig>();
    
    @Xjava
    private IDataSourceConfigDAO                            dsConfigDAO;
    
    @Xjava(ref="DataSourceConfigToJava_DBG")
    private ConfigToJava<DataSourceConfig ,DataSourceGroup> dscToJavaDBG;
    
    @Xjava(ref="DataSourceConfigToJava_DSCQL")
    private ConfigToJava<DataSourceConfig ,DataSourceCQL>   dscToJavaDSCQL;
    
    @Xjava(ref="DataSourceConfigToJava_XNet")
    private ConfigToJava<DataSourceConfig ,ClientCluster>   dscToJavaXNet;
    
    
    
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
    @Override
    public synchronized String testConnect(DataSourceConfig io_DSConfig)
    {
        String v_IsOK = null;
        
        if ( DataSourceType.$DBType_Neo4j.equals(io_DSConfig.getDataSourceType()) )
        {
            try
            {
                DataSourceCQL v_DSCQL = ((DataSourceConfigToJava_DSCQL) this.dscToJavaDSCQL).newObject(io_DSConfig);
                v_DSCQL.getConnection().close();
            }
            catch (Exception exce)
            {
                v_IsOK = exce.getMessage();
                $Logger.warn(exce);
                
                if ( Help.isNull(v_IsOK) )
                {
                    v_IsOK = exce.toString();
                }
            }
        }
        else if ( DataSourceType.$Message_RocketMQ.equals(io_DSConfig.getDataSourceType()) )
        {
            return "暂不支持";
        }
        else if ( DataSourceType.$Message_XNet.equals(io_DSConfig.getDataSourceType()) )
        {
            return "暂不支持";
        }
        else
        {
            int v_LoginTimeout = 0;
            try
            {
                DatabaseInfo v_DB = ((DataSourceConfigToJava_DBG) this.dscToJavaDBG).newDB(io_DSConfig);
                DBOperation.DATABASEINFO = v_DB;
                
                v_LoginTimeout = DriverManager.getLoginTimeout();
                DriverManager.setLoginTimeout(10);
                Connection v_Conn = DBOperation.getInstance().getConnection();
                v_Conn.close();
            }
            catch (Exception exce)
            {
                v_IsOK = exce.getMessage();
                $Logger.warn(exce);
                
                if ( Help.isNull(v_IsOK) )
                {
                    v_IsOK = exce.toString();
                }
            }
            finally
            {
                DriverManager.setLoginTimeout(v_LoginTimeout);
            }
        }
        
        return v_IsOK;
    }
    
    
    
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
    @Override
    public DataSourceGroup getDataSourceGroup(DataSourceConfig i_DataSourceConfig)
    {
        return this.dscToJavaDBG.getObject(i_DataSourceConfig);
    }
    
    
    
    /**
     * 获取数据源对象：图数据库
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-06-26
     * @version     v1.0
     *
     * @param i_DataSourceConfig
     * @return
     */
    @Override
    public DataSourceCQL getDataSourceCQL(DataSourceConfig i_DataSourceConfig)
    {
        return this.dscToJavaDSCQL.getObject(i_DataSourceConfig);
    }
    
    
    
    /**
     * 查询数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     *
     * @return
     */
    @Override
    public Map<String ,DataSourceConfig> queryList()
    {
        Map<String ,DataSourceConfig> v_Ret = this.dsConfigDAO.queryList();
        return v_Ret;
    }
    
    
    
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
    @Override
    public synchronized DataSourceConfig queryByXID(String i_XID)
    {
        DataSourceConfig v_Ret = $DSConfigCache.get(i_XID);
        
        if ( v_Ret == null )
        {
            DataSourceConfig v_DSConfig = new DataSourceConfig();
            v_DSConfig.setXid(i_XID);
            
            v_Ret = this.dsConfigDAO.queryByXID(v_DSConfig);
            if ( v_Ret != null )
            {
                $DSConfigCache.put(v_Ret.getXid() ,v_Ret);
            }
        }
        
        return v_Ret;
    }
    
    
    
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
    @Override
    public synchronized DataSourceConfig queryByXID(DataSourceConfig i_DSConfig)
    {
        DataSourceConfig v_Ret = $DSConfigCache.get(i_DSConfig.getXid());
        
        if ( v_Ret == null )
        {
            v_Ret = this.dsConfigDAO.queryByXID(i_DSConfig);
            if ( v_Ret != null )
            {
                $DSConfigCache.put(v_Ret.getXid() ,v_Ret);
            }
        }
        
        return v_Ret;
    }
    
    
    
    /**
     * 新增数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     *
     * @param io_DSConfig  数据源配置对象
     * @return            返回 null 表示异常
     */
    @Override
    public DataSourceConfig insert(DataSourceConfig io_DSConfig)
    {
        io_DSConfig.setId(StringHelp.getUUID());
        io_DSConfig.setDataSourceType(io_DSConfig.getDataSourceType().toUpperCase());
        io_DSConfig.setCreateUserID(Help.NVL(io_DSConfig.getCreateUserID() ,io_DSConfig.getUserID()));
        io_DSConfig.setIsDel(       Help.NVL(io_DSConfig.getIsDel() ,0));
        int v_Ret = this.dsConfigDAO.insert(io_DSConfig);
        return v_Ret == 1 ? io_DSConfig : null;
    }
    
    
    
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
    @Override
    public DataSourceConfig update(DataSourceConfig io_DSConfig)
    {
        if ( !Help.isNull(io_DSConfig.getDataSourceType()) )
        {
            io_DSConfig.setDataSourceType(io_DSConfig.getDataSourceType().toUpperCase());
        }
        int v_Ret = this.dsConfigDAO.update(io_DSConfig);
        
        if ( v_Ret >= 1 )
        {
            DataSourceConfig v_Old = $DSConfigCache.get(io_DSConfig.getXid());
            if ( v_Old != null )
            {
                $DSConfigCache.remove(v_Old.getXid());
                this.dscToJavaDBG.removeObject(v_Old);
            }
            
            ConfigToJavaDefault.removeAllConfigToJava();   // 所有配置化对象，删除后重新构建
        }
        
        return v_Ret == 1 ? io_DSConfig : null;
    }
    
    
    
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
    @Override
    public String decryptUserName(DataSourceConfig i_DSConfig)
    {
        try
        {
            AES v_Aes = new AES(2 ,i_DSConfig.getDatabaseName() + "-userName-org.hy.common.Help");
            return v_Aes.decrypt(i_DSConfig.getUserName().substring($Encrypt.length() + 1));
        }
        catch (Exception exce)
        {
            $Logger.error("id=" + i_DSConfig.getId() + "  ,xid=" + i_DSConfig.getXid() + "  " + i_DSConfig.getHostName() + ":" + i_DSConfig.getPort() ,exce);
        }
        
        return "";
    }
    
    
    
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
    @Override
    public String encryptUserName(DataSourceConfig i_DSConfig)
    {
        try
        {
            AES v_Aes = new AES(2 ,i_DSConfig.getDatabaseName() + "-userName-org.hy.common.Help");
            return $Encrypt + ":" + v_Aes.encrypt(i_DSConfig.getUserName());
        }
        catch (Exception exce)
        {
            $Logger.error("id=" + i_DSConfig.getId() + "  ,xid=" + i_DSConfig.getXid() + "  " + i_DSConfig.getHostName() + ":" + i_DSConfig.getPort() ,exce);
        }
        
        return "";
    }
    
    
    
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
    @Override
    public String decryptPassword(DataSourceConfig i_DSConfig)
    {
        try
        {
            AES v_Aes = new AES(2 ,i_DSConfig.getDatabaseName() + "-password-org.hy.common.Help");
            return v_Aes.decrypt(i_DSConfig.getPassword().substring($Encrypt.length() + 1));
        }
        catch (Exception exce)
        {
            $Logger.error("id=" + i_DSConfig.getId() + "  ,xid=" + i_DSConfig.getXid() + "  " + i_DSConfig.getHostName() + ":" + i_DSConfig.getPort() ,exce);
        }
        
        return "";
    }
    
    
    
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
    @Override
    public String encryptPassword(DataSourceConfig i_DSConfig)
    {
        try
        {
            AES v_Aes = new AES(2 ,i_DSConfig.getDatabaseName() + "-password-org.hy.common.Help");
            return $Encrypt + ":" + v_Aes.encrypt(i_DSConfig.getPassword());
        }
        catch (Exception exce)
        {
            $Logger.error("id=" + i_DSConfig.getId() + "  ,xid=" + i_DSConfig.getXid() + "  " + i_DSConfig.getHostName() + ":" + i_DSConfig.getPort() ,exce);
        }
        
        return "";
    }
    
}
