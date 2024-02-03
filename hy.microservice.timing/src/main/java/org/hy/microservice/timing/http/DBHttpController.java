package org.hy.microservice.timing.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.app.Param;
import org.hy.common.xml.XHttp;
import org.hy.common.xml.XJSON;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.user.UserSSO;
import org.hy.microservice.common.user.UserService;
import org.hy.microservice.timing.common.XObject;
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
 * 数据Http请求的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-10-20
 * @version     v1.0
 */
@Controller
@RequestMapping(value="http" ,name="数据请求")
public class DBHttpController extends BaseController
{
    private static final Logger $Logger = new Logger(DBHttpController.class);
    
    @Autowired
    @Qualifier("DBHttpService")
    private IDBHttpService             dbHttpService;
    
    @Autowired
    @Qualifier("UserService")
    private UserService                userService;
    
    @Autowired
    @Qualifier("MS_Data_IsCheckToken")
    private Param                      isCheckToken;
    
    
    
    /**
     * 数据Http请求的执行
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-11-15
     * @version     v1.0
     *
     * @param i_Token
     * @param i_DBHttp
     * @return
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(name="执行请求" ,value="executeHttp" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<Object> executeHttp(@RequestParam(value="token" ,required=false) String i_Token
                                           ,@RequestBody DBHttp i_DBHttp)
    {
        $Logger.info("executeHttp Start: " + i_Token);
        
        BaseResponse<Object> v_RetResp = new BaseResponse<Object>();
        long                 v_Count   = 0L;
        
        if ( i_DBHttp == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            if ( Help.isNull(i_DBHttp.getUserID()) )
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
            
            DBHttp v_Http = null;
            // 用数据推送的逻辑XID找主键ID
            if ( !Help.isNull(i_DBHttp.getXid()) && i_DBHttp.getId() == null )
            {
                v_Http = this.dbHttpService.queryByXID(i_DBHttp.getXid());
                if ( v_Http == null )
                {
                    return v_RetResp.setCode("-11").setMessage("数据请求XID无效");
                }
            }
            else if ( !Help.isNull(i_DBHttp.getId()) )
            {
                v_Http = this.dbHttpService.queryByID(i_DBHttp.getId());
                if ( v_Http == null )
                {
                    return v_RetResp.setCode("-12").setMessage("数据请求ID无效");
                }
            }
            else
            {
                return v_RetResp.setCode("-13").setMessage("数据请求ID或XID为空");
            }
            
            XHttp v_XHttp = (XHttp) XJava.getObject(v_Http.getXid());
            if ( v_XHttp == null )
            {
                return v_RetResp.setCode("-21").setMessage("数据请求构建异常");
            }
            
            Map<String ,Object> v_ExecParams = null;
            if ( !Help.isNull(i_DBHttp.getExecuteParams()) )
            {
                try
                {
                    XJSON v_Json = new XJSON();
                    v_ExecParams = (Map<String ,Object>) v_Json.toJava(i_DBHttp.getExecuteParams() ,Map.class);
                }
                catch (Exception exce)
                {
                    $Logger.error(exce);
                    return v_RetResp.setCode("-31").setMessage("执行参数Json格式非法");
                }
            }
            
            long      v_STime   = Date.getNowTime().getTime();
            Return<?> v_HttpRet = null;
            
            if ( Help.isNull(v_ExecParams) )
            {
                v_HttpRet = v_XHttp.request();
            }
            else
            {
                v_HttpRet = v_XHttp.request(v_ExecParams);
            }
            
            v_RetResp.setRespTimeLen(Date.getNowTime().getTime() - v_STime);
            return v_RetResp.setData(v_HttpRet);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("executeHttp End: "  + i_Token + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 查询数据请求的被引用情况
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-27
     * @version     v1.0
     *
     * @param i_Token
     * @param i_DBHttp
     * @return
     */
    @RequestMapping(name="查询引用" ,value="queryRelation" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<XObject> queryRelation(@RequestParam(value="token" ,required=false) String i_Token
                                              ,@RequestBody DBHttp i_DBHttp)
    {
        $Logger.info("queryRelation Start: " + i_Token);
        
        BaseResponse<XObject> v_RetResp = new BaseResponse<XObject>();
        int                            v_Count = 0;
        
        if ( i_DBHttp == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            if ( Help.isNull(i_DBHttp.getUserID()) )
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
            
            // 用数据请求的逻辑XID找主键ID
            if ( !Help.isNull(i_DBHttp.getXid()) && i_DBHttp.getId() == null )
            {
                DBHttp v_Http = this.dbHttpService.queryByXID(i_DBHttp.getXid());
                if ( v_Http != null )
                {
                    i_DBHttp.setId(v_Http.getId());
                }
                else
                {
                    return v_RetResp.setCode("-11").setMessage("数据请求XID无效");
                }
            }
            
            if ( Help.isNull(i_DBHttp.getId()) )
            {
                return v_RetResp.setCode("-12").setMessage("数据请求ID为空");
            }
            
            List<XObject> v_DataList = this.dbHttpService.queryRelations(i_DBHttp.getId());
            v_Count = v_DataList.size();
            return v_RetResp.setData(v_DataList);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("queryRelation End: "  + i_Token + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 查询数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-16
     * @version     v1.0
     *
     * @param i_Token
     * @param i_DBHttp
     * @return
     */
    @RequestMapping(name="查询数据请求" ,value="queryHttp" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<DBHttp> queryHttp(@RequestParam(value="token" ,required=false) String i_Token
                                         ,@RequestBody DBHttp i_DBHttp)
    {
        $Logger.info("queryHttp Start: " + i_Token);
        
        BaseResponse<DBHttp> v_RetResp = new BaseResponse<DBHttp>();
        int                     v_Count   = 0;
        
        if ( i_DBHttp == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            if ( Help.isNull(i_DBHttp.getUserID()) )
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
            
            List<DBHttp> v_DataList = null;
            if ( !Help.isNull(i_DBHttp.getId()) || !Help.isNull(i_DBHttp.getXid()) )
            {
                v_DataList = new ArrayList<DBHttp>();
                DBHttp v_Http = this.dbHttpService.queryByIDXID(i_DBHttp);
                if ( v_Http != null )
                {
                    v_DataList.add(v_Http);
                }
            }
            else
            {
                Map<String ,DBHttp> v_Datas = this.dbHttpService.queryList(i_DBHttp);
                v_DataList = Help.toList(v_Datas);
            }
            
            v_Count = v_DataList.size();
            return v_RetResp.setData(v_DataList);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("queryHttp End: "  + i_Token + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 保存数据Http请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-16
     * @version     v1.0
     *
     * @param i_Token
     * @param i_DBHttp
     * @return
     */
    @RequestMapping(name="保存数据请求" ,value="saveHttp" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<DBHttp> saveHttp(@RequestParam(value="token" ,required=false) String i_Token
                                        ,@RequestBody DBHttp i_DBHttp)
    {
        BaseResponse<DBHttp> v_RetResp = new BaseResponse<DBHttp>();
        
        if ( i_DBHttp == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("saveHttp Start: " + i_Token + ":" + i_DBHttp.toString());
            
            if ( Help.isNull(i_DBHttp.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            DBHttp v_OldDBHttp = null;
            // 新创建的验证
            if ( Help.isNull(i_DBHttp.getId()) )
            {
                if ( Help.isNull(i_DBHttp.getXid()) )
                {
                    return v_RetResp.setCode("-10").setMessage("逻辑ID为空");
                }
                
                if ( XJava.getObject(i_DBHttp.getXid()) != null )
                {
                    return v_RetResp.setCode("-11").setMessage("逻辑编号" + i_DBHttp.getXid() + "已存在，禁止重复创建");
                }
                
                if ( Help.isNull(i_DBHttp.getIp()) )
                {
                    return v_RetResp.setCode("-12").setMessage("域名为空");
                }
                
                if ( Help.isNull(i_DBHttp.getUrl()) )
                {
                    return v_RetResp.setCode("-12").setMessage("资源地址为空");
                }
                
                // 禁止创建时就删除
                i_DBHttp.setIsDel(null);
                i_DBHttp.setXid(i_DBHttp.getXid().trim());
            }
            else
            {
                v_OldDBHttp = this.dbHttpService.queryByID(i_DBHttp.getId());
                if ( v_OldDBHttp == null )
                {
                    return v_RetResp.setCode("-20").setMessage("ID不存在");
                }
                
                if ( !Help.isNull(i_DBHttp.getXid()) )
                {
                    i_DBHttp.setXid(i_DBHttp.getXid().trim());
                    if ( !v_OldDBHttp.getXid().equals(i_DBHttp.getXid()) )
                    {
                        if ( XJava.getObject(i_DBHttp.getXid()) != null )
                        {
                            return v_RetResp.setCode("-11").setMessage("逻辑编号" + i_DBHttp.getXid() + "已存在，禁止重复创建");
                        }
                        
                        i_DBHttp.setXidOld(v_OldDBHttp.getXid());
                    }
                }
                
                // 未修改请求参数时，填充原来的
                if ( Help.isNull(i_DBHttp.getHttpParams()) )
                {
                    i_DBHttp.setHttpParams(v_OldDBHttp.getHttpParams());
                }
            }
            
            if ( i_DBHttp.getRequestType() != null )
            {
                if ( i_DBHttp.getRequestType() != XHttp.$Request_Type_Get
                  && i_DBHttp.getRequestType() != XHttp.$Request_Type_Post )
                {
                    return v_RetResp.setCode("-13").setMessage("非法的访问方式");
                }
            }
            
            if ( i_DBHttp.getEncodeType() != null )
            {
                if ( i_DBHttp.getEncodeType() != 0
                  && i_DBHttp.getEncodeType() != 1
                  && i_DBHttp.getEncodeType() != 2 )
                {
                    return v_RetResp.setCode("-14").setMessage("非法的参数转义类型");
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
                
                if ( !v_User.getUserId().equals(i_DBHttp.getUserID()) )
                {
                    return v_RetResp.setCode("-902").setMessage("操作用户与登录用户不一致");
                }
            }
            
            if ( !Help.isNull(i_DBHttp.getHttpParams()) )
            {
                for (DBHttpParam v_Param : i_DBHttp.getHttpParams())
                {
                    if ( Help.isNull(v_Param.getParamName()) )
                    {
                        return v_RetResp.setCode("-20").setMessage("参数名称为空");
                    }
                }
            }
            
            if ( !Help.isNull(i_DBHttp.getIsDel()) && i_DBHttp.getIsDel() != 0 )
            {
                List<XObject> v_Objects = this.dbHttpService.queryRelations(i_DBHttp.getId());
                if ( !Help.isNull(v_Objects) )
                {
                    return v_RetResp.setCode("-314").setMessage("数据请求被" + v_Objects.size() + "处引用，禁止删除");
                }
            }
            
            synchronized (this)
            {
                // 防止同名
                if ( !Help.isNull(i_DBHttp.getXid()) )
                {
                    DBHttp v_SameXID = this.dbHttpService.queryByXID(i_DBHttp.getXid());
                    
                    if ( v_SameXID != null && !v_SameXID.getId().equals(i_DBHttp.getId()) )
                    {
                        $Logger.warn("修改XID[" + i_DBHttp.getXid() + "]时，出现相同的XID，禁止重复创建");
                        return v_RetResp.setCode("-903").setMessage("逻辑编号" + i_DBHttp.getXid() + "已存在，禁止重复创建");
                    }
                }
                
                DBHttp v_SaveRet = this.dbHttpService.save(i_DBHttp);
                if ( v_SaveRet != null )
                {
                    $Logger.info("用户（" + i_DBHttp.getCreateUserID() + "）创建" + i_DBHttp.getXid() + "，成功");
                    return v_RetResp.setData(v_SaveRet);
                }
                else
                {
                    $Logger.error("用户（" + Help.NVL(i_DBHttp.getCreateUserID() ,i_DBHttp.getUserID()) + "）创建" + i_DBHttp.getXid() + "，异常");
                    return v_RetResp.setCode("-998").setMessage("系统异常");
                }
            }
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("saveHttp End: " + i_Token + ":" + i_DBHttp.toString());
        }
    }
    
}
