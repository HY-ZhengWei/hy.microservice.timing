package org.hy.microservice.timing.dataSource;

import java.util.List;
import java.util.Map;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.user.UserSSO;
import org.hy.microservice.common.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;





/**
 * 数据源配置的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2022-08-22
 * @version     v1.0
 */
@Controller
@RequestMapping(value="dataSourceConfig" ,name="数据源配置")
public class DataSourceConfigController extends BaseController
{
    private static final Logger $Logger = new Logger(DataSourceConfigController.class);
    
    @Autowired
    @Qualifier("DataSourceConfigService")
    private IDataSourceConfigService   dscService;
    
    @Autowired
    @Qualifier("UserService")
    private UserService                userService;
    
    @Autowired
    @Qualifier("MS_Timing_IsCheckToken")
    private Param                      isCheckToken;
    
    
    
    /**
     * 查询数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     *
     * @param i_Token
     * @return
     */
    @RequestMapping(name="查询数据源配置" ,value="queryDSConfig" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<DataSourceConfig> queryDSConfig(@RequestParam(value="token" ,required=false) String i_Token
                                                       ,@RequestBody DataSourceConfig i_DSConfig)
    {
        $Logger.info("queryDSConfig Start: " + i_Token);
        
        BaseResponse<DataSourceConfig> v_RetResp = new BaseResponse<DataSourceConfig>();
        int                            v_Count = 0;
        
        if ( i_DSConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            if ( Help.isNull(i_DSConfig.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
            {
                // 验证票据及用户登录状态
                if ( Help.isNull(i_Token) )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
                
                UserSSO v_User = this.userService.getUser(i_Token);
                if ( v_User == null )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
            }
            
            Map<String ,DataSourceConfig> v_Datas    = this.dscService.queryList();
            List<DataSourceConfig>        v_DataList = Help.toList(v_Datas);
            
            v_Count = v_DataList.size();
            
            Help.setValues(v_DataList ,"userName" ,"");
            Help.setValues(v_DataList ,"password" ,"");
            
            return v_RetResp.setData(v_DataList);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("queryDSConfig End: "  + i_Token + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 测试连接数据源配置是否正确
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-04-20
     * @version     v1.0
     *
     * @param i_Token
     * @param i_DSConfig
     * @return
     */
    @RequestMapping(name="测试连接数据源" ,value="testDSConfig" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<DataSourceConfig> testDSConfig(@RequestParam(value="token" ,required=false) String i_Token
                                                      ,@RequestBody DataSourceConfig i_DSConfig)
    {
        BaseResponse<DataSourceConfig> v_RetResp = new BaseResponse<DataSourceConfig>();
        
        if ( i_DSConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("testDSConfig Start: " + i_Token + ":" + i_DSConfig.toString());
            
            if ( Help.isNull(i_DSConfig.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            if ( Help.isNull(i_DSConfig.getXid()) )
            {
                return v_RetResp.setCode("-3").setMessage("逻辑编号为空");
            }
            
            if ( Help.isNull(i_DSConfig.getDataSourceType()) )
            {
                return v_RetResp.setCode("-4").setMessage("数据源类型为空");
            }
            
            if ( Help.isNull(i_DSConfig.getDriverClassName()) )
            {
                return v_RetResp.setCode("-5").setMessage("连接驱动为空");
            }
            
            if ( Help.isNull(i_DSConfig.getHostName()) )
            {
                return v_RetResp.setCode("-6").setMessage("数据源IP地址为空");
            }
            
            if ( Help.isNull(i_DSConfig.getPort()) || i_DSConfig.getPort() <=0 || i_DSConfig.getPort() >= 65535 )
            {
                return v_RetResp.setCode("-7").setMessage("数据源端口为空或非法");
            }
            
            if ( Help.isNull(i_DSConfig.getUserName()) )
            {
                return v_RetResp.setCode("-8").setMessage("用户名称为空");
            }
            
            if ( Help.isNull(i_DSConfig.getPassword()) )
            {
                return v_RetResp.setCode("-9").setMessage("访问密码为空");
            }
            
            if ( Help.isNull(i_DSConfig.getDatabaseName()) )
            {
                return v_RetResp.setCode("-10").setMessage("数据库实例名称为空");
            }
            
            if ( Help.isNull(i_DSConfig.getUserID()) && Help.isNull(i_DSConfig.getCreateUserID()) )
            {
                return v_RetResp.setCode("-101").setMessage("操作用户编号为空");
            }
            
            if ( !Help.isNull(i_DSConfig.getDataSourceType()) )
            {
                if ( !DataSourceType.isValueTypeOK(i_DSConfig.getDataSourceType()) )
                {
                    return v_RetResp.setCode("-102").setMessage("数据源类型非合法值：" + i_DSConfig.getDataSourceType());
                }
            }
            
            if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
            {
                // 验证票据及用户登录状态
                if ( Help.isNull(i_Token) )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
                
                UserSSO v_User = this.userService.getUser(i_Token);
                if ( v_User == null )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
                
                if ( !v_User.getUserId().equals(i_DSConfig.getUserID()) )
                {
                    return v_RetResp.setCode("-902").setMessage("操作用户与登录用户不一致");
                }
            }
            
            String v_TestRet = this.dscService.testConnect(i_DSConfig);
            if ( Help.isNull(v_TestRet) )
            {
                $Logger.info("用户（" + i_DSConfig.getUserID() + "）测试数据库" + i_DSConfig.getHostName() + ":" + i_DSConfig.getPort() + ":" + i_DSConfig.getUserName() + "，成功");
                return v_RetResp.setMessage("连接成功");
            }
            else
            {
                $Logger.error("用户（" + i_DSConfig.getUserID() + "）测试数据库" + i_DSConfig.getHostName() + ":" + i_DSConfig.getPort() + ":" + i_DSConfig.getUserName() + "，异常：" + v_TestRet);
                return v_RetResp.setCode("-998").setMessage(StringHelp.replaceAll(v_TestRet ,new String[] {"'" ,"\""} ,StringHelp.$ReplaceNil));
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("testDSConfig End: " + i_Token + ":" + i_DSConfig.toString());
        }
    }
    
    
    
    /**
     * 保存数据源配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2022-08-22
     * @version     v1.0
     *
     * @param i_Token
     * @param i_DSConfig
     * @return
     */
    @RequestMapping(name="保存数据源配置" ,value="saveDSConfig" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<DataSourceConfig> saveDSConfig(@RequestParam(value="token" ,required=false) String i_Token
                                                      ,@RequestBody DataSourceConfig i_DSConfig)
    {
        BaseResponse<DataSourceConfig> v_RetResp = new BaseResponse<DataSourceConfig>();
        
        if ( i_DSConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("saveDSConfig Start: " + i_Token + ":" + i_DSConfig.toString());
            
            if ( Help.isNull(i_DSConfig.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            // 新创建的验证
            if ( Help.isNull(i_DSConfig.getId()) )
            {
                if ( Help.isNull(i_DSConfig.getXid()) )
                {
                    return v_RetResp.setCode("-3").setMessage("逻辑编号为空");
                }
                
                if ( Help.isNull(i_DSConfig.getDataSourceType()) )
                {
                    return v_RetResp.setCode("-4").setMessage("数据源类型为空");
                }
                
                if ( Help.isNull(i_DSConfig.getDriverClassName()) )
                {
                    return v_RetResp.setCode("-5").setMessage("连接驱动为空");
                }
                
                if ( Help.isNull(i_DSConfig.getHostName()) )
                {
                    return v_RetResp.setCode("-6").setMessage("数据源IP地址为空");
                }
                
                if ( Help.isNull(i_DSConfig.getPort()) || i_DSConfig.getPort() <=0 || i_DSConfig.getPort() >= 65535 )
                {
                    return v_RetResp.setCode("-7").setMessage("数据源端口为空或非法");
                }
                
                if ( Help.isNull(i_DSConfig.getUserName()) )
                {
                    return v_RetResp.setCode("-8").setMessage("用户名称为空");
                }
                
                if ( Help.isNull(i_DSConfig.getPassword()) )
                {
                    return v_RetResp.setCode("-9").setMessage("访问密码为空");
                }
                
                if ( Help.isNull(i_DSConfig.getDatabaseName()) )
                {
                    return v_RetResp.setCode("-10").setMessage("数据库实例名称为空");
                }
            }
            // 更新的验证
            else
            {
                if ( !Help.isNull(i_DSConfig.getPort()) && (i_DSConfig.getPort() <=0 || i_DSConfig.getPort() >= 65535) )
                {
                    return v_RetResp.setCode("-7").setMessage("数据源端口为空或非法");
                }
            }
            
            if ( Help.isNull(i_DSConfig.getUserID()) && Help.isNull(i_DSConfig.getCreateUserID()) )
            {
                return v_RetResp.setCode("-101").setMessage("操作用户编号为空");
            }
            
            if ( !Help.isNull(i_DSConfig.getDataSourceType()) )
            {
                if ( !DataSourceType.isValueTypeOK(i_DSConfig.getDataSourceType()) )
                {
                    return v_RetResp.setCode("-102").setMessage("数据源类型非合法值：" + i_DSConfig.getDataSourceType());
                }
            }
            
            if ( isCheckToken != null && Boolean.parseBoolean(isCheckToken.getValue()) )
            {
                // 验证票据及用户登录状态
                if ( Help.isNull(i_Token) )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
                
                UserSSO v_User = this.userService.getUser(i_Token);
                if ( v_User == null )
                {
                    return v_RetResp.setCode("-901").setMessage("非法访问");
                }
                
                if ( !v_User.getUserId().equals(i_DSConfig.getUserID()) )
                {
                    return v_RetResp.setCode("-902").setMessage("操作用户与登录用户不一致");
                }
            }
            
            // 防止同名
            if ( !Help.isNull(i_DSConfig.getXid()) )
            {
                DataSourceConfig v_SameXID = this.dscService.queryByXID(i_DSConfig);
                
                if ( v_SameXID != null && !v_SameXID.getId().equals(i_DSConfig.getId()) )
                {
                    $Logger.warn("修改XID[" + i_DSConfig.getXid() + "]时，出现相同的XID，所以禁止创建数据源配置");
                    return v_RetResp.setCode("-903").setMessage("逻辑编号已存在，禁止创建");
                }
            }
            
            // 逻辑删除时不验证数据库连接是否有效
            if ( Help.isNull(i_DSConfig.getIsDel()) || i_DSConfig.getIsDel() == 0 )
            {
                String v_TestRet = this.dscService.testConnect(i_DSConfig);
                if ( !Help.isNull(v_TestRet) )
                {
                    $Logger.error("用户（" + i_DSConfig.getUserID() + "）测试数据库" + i_DSConfig.getHostName() + ":" + i_DSConfig.getPort() + ":" + i_DSConfig.getUserName() + "，异常：" + v_TestRet);
                    return v_RetResp.setCode("-904").setMessage(v_TestRet);
                }
            }
            
            DataSourceConfig v_SaveRet = null;
            if ( Help.isNull(i_DSConfig.getId()) )
            {
                v_SaveRet = this.dscService.insert(i_DSConfig);
            }
            else
            {
                v_SaveRet = this.dscService.update(i_DSConfig);
            }
            
            if ( v_SaveRet != null )
            {
                $Logger.info("用户（" + v_SaveRet.getCreateUserID() + "）创建" + v_SaveRet.getXid() + "，成功");
                return v_RetResp.setData(v_SaveRet);
            }
            else
            {
                $Logger.error("用户（" + Help.NVL(i_DSConfig.getCreateUserID() ,i_DSConfig.getUserID()) + "）创建" + i_DSConfig.getXid() + "，异常");
                return v_RetResp.setCode("-998").setMessage("系统异常");
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("saveDSConfig End: " + i_Token + ":" + i_DSConfig.toString());
        }
    }
    
}
