package org.hy.microservice.timing.jobHttp;

import java.util.List;
import java.util.Map;

import org.hy.common.Date;
import org.hy.common.Help;
import org.hy.common.Return;
import org.hy.common.app.Param;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.user.UserSSO;
import org.hy.microservice.common.user.UserService;
import org.hy.microservice.timing.common.XObject;
import org.hy.microservice.timing.http.DBHttp;
import org.hy.microservice.timing.http.IDBHttpService;
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
 * 定时任务请求的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2024-02-03
 * @version     v1.0
 */
@Controller
@RequestMapping(value="jobHttp" ,name="定时任务请求")
public class JobHttpController extends BaseController
{
    private static final Logger $Logger = new Logger(JobHttpController.class);
    
    @Autowired
    @Qualifier("JobHttpService")
    private IJobHttpService            jobHttpService;
    
    @Autowired
    @Qualifier("DBHttpService")
    private IDBHttpService             dbHttpService;
    
    @Autowired
    @Qualifier("UserService")
    private UserService                userService;
    
    @Autowired
    @Qualifier("MS_Timing_IsCheckToken")
    private Param                      isCheckToken;
    
    
    
    /**
     * 定时任务请求的执行
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobHttp
     * @return
     */
    @RequestMapping(name="执行定时任务请求" ,value="executeJobHttp" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<Return<Object>> executeJobHttp(@RequestParam(value="token" ,required=false) String i_Token
                                                      ,@RequestBody JobHttp i_JobHttp)
    {
        $Logger.info("executeJobHttp Start: " + i_Token);
        
        BaseResponse<Return<Object>> v_RetResp = new BaseResponse<Return<Object>>();
        int                          v_Count   = 0;
        
        if ( i_JobHttp == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            if ( Help.isNull(i_JobHttp.getUserID()) )
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
            
            XJobHttp v_XJobHttp = null;
            // 用定时任务请求的逻辑XID找主键ID
            if ( !Help.isNull(i_JobHttp.getXid()) && i_JobHttp.getId() == null )
            {
                v_XJobHttp = (XJobHttp) XJava.getObject(i_JobHttp.getXid());
                if ( v_XJobHttp == null )
                {
                    return v_RetResp.setCode("-11").setMessage("定时任务请求XID无效");
                }
            }
            else if ( !Help.isNull(i_JobHttp.getId()) )
            {
                JobHttp v_JobHttp = this.jobHttpService.queryByID(i_JobHttp.getId());
                if ( v_JobHttp == null )
                {
                    return v_RetResp.setCode("-12").setMessage("定时任务请求ID无效");
                }
                v_XJobHttp = (XJobHttp) XJava.getObject(v_JobHttp.getXid());
            }
            else
            {
                return v_RetResp.setCode("-13").setMessage("定时任务请求ID或XID为空");
            }
            
            if ( v_XJobHttp == null )
            {
                return v_RetResp.setCode("-21").setMessage("定时任务请求构建异常");
            }
            
            long           v_STime   = Date.getNowTime().getTime();
            Return<Object> v_ExecRet = v_XJobHttp.execute();
            
            v_RetResp.setRespTimeLen(Date.getNowTime().getTime() - v_STime);
            v_RetResp.setData(v_ExecRet);
            v_RetResp.setCode(v_ExecRet.getParamInt() + "");
            if ( Help.isNull(v_ExecRet.getParamStr()) )
            {
                if ( v_ExecRet.getException() != null )
                {
                    v_RetResp.setMessage(v_ExecRet.getException().getMessage());
                }
            }
            else
            {
                v_RetResp.setMessage(v_ExecRet.getParamStr());
            }
            
            return v_RetResp;
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("executeJobHttp End: "  + i_Token + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 查询定时任务请求的被引用情况
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobHttp
     * @return
     */
    @RequestMapping(name="查询引用" ,value="queryRelation" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<XObject> queryRelation(@RequestParam(value="token" ,required=false) String i_Token
                                              ,@RequestBody JobHttp i_JobHttp)
    {
        $Logger.info("queryRelation Start: " + i_Token);
        
        BaseResponse<XObject> v_RetResp = new BaseResponse<XObject>();
        int                            v_Count = 0;
        
        if ( i_JobHttp == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            if ( Help.isNull(i_JobHttp.getUserID()) )
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
            
            // 用定时任务请求的逻辑XID找主键ID
            if ( !Help.isNull(i_JobHttp.getXid()) && i_JobHttp.getId() == null )
            {
                JobHttp v_Push = this.jobHttpService.queryByXID(i_JobHttp.getXid());
                if ( v_Push != null )
                {
                    i_JobHttp.setId(v_Push.getId());
                }
                else
                {
                    return v_RetResp.setCode("-11").setMessage("定时任务请求XID无效");
                }
            }
            
            if ( Help.isNull(i_JobHttp.getId()) )
            {
                return v_RetResp.setCode("-12").setMessage("定时任务请求ID为空");
            }
            
            List<XObject> v_DataList = this.jobHttpService.queryRelations(i_JobHttp.getId());
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
     * 查询定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobHttp
     * @return
     */
    @RequestMapping(name="查询定时任务请求" ,value="queryJobHttp" ,method={RequestMethod.GET ,RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<JobHttp> queryJobHttp(@RequestParam(value="token" ,required=false) String i_Token
                                             ,@RequestBody JobHttp i_JobHttp)
    {
        $Logger.info("queryJobHttp Start: " + i_Token);
        
        BaseResponse<JobHttp> v_RetResp = new BaseResponse<JobHttp>();
        int                   v_Count   = 0;
        
        if ( i_JobHttp == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            if ( Help.isNull(i_JobHttp.getUserID()) )
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
            
            Map<String ,JobHttp> v_Datas    = this.jobHttpService.queryList(i_JobHttp);
            List<JobHttp>        v_DataList = Help.toList(v_Datas);
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
            $Logger.info("queryJobHttp End: "  + i_Token + " 返回: " + v_Count);
        }
    }
    
    
    
    /**
     * 保存定时任务请求
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-02-03
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobHttp
     * @return
     */
    @RequestMapping(name="保存定时任务请求" ,value="saveJobHttp" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<JobHttp> saveJobHttp(@RequestParam(value="token" ,required=false) String i_Token
                                            ,@RequestBody JobHttp i_JobHttp)
    {
        BaseResponse<JobHttp> v_RetResp = new BaseResponse<JobHttp>();
        
        if ( i_JobHttp == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("saveJobHttp Start: " + i_Token + ":" + i_JobHttp.toString());
            
            if ( Help.isNull(i_JobHttp.getUserID()) )
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
                
                if ( !v_User.getUserId().equals(i_JobHttp.getUserID()) )
                {
                    return v_RetResp.setCode("-902").setMessage("操作用户与登录用户不一致");
                }
            }
            
            // 用请求的逻辑ID找主键ID
            if ( !Help.isNull(i_JobHttp.getTaskHttpXID()) && i_JobHttp.getTaskHttpID() == null )
            {
                DBHttp v_Http = this.dbHttpService.queryByXID(i_JobHttp.getTaskHttpXID());
                if ( v_Http != null )
                {
                    i_JobHttp.setTaskHttpID(v_Http.getId());
                }
            }
            
            // 用票据的逻辑ID找主键ID
            if ( !Help.isNull(i_JobHttp.getTokenHttpXID()) && i_JobHttp.getTokenHttpID() == null )
            {
                DBHttp v_Token = this.dbHttpService.queryByXID(i_JobHttp.getTokenHttpXID());
                if ( v_Token != null )
                {
                    i_JobHttp.setTokenHttpID(v_Token.getId());
                }
            }
            
            JobHttp v_OldJobHttp = null;
            // 新创建的验证
            if ( Help.isNull(i_JobHttp.getId()) )
            {
                if ( Help.isNull(i_JobHttp.getXid()) )
                {
                    return v_RetResp.setCode("-10").setMessage("逻辑ID为空");
                }
                
                if ( Help.isNull(i_JobHttp.getTaskHttpID()) )
                {
                    return v_RetResp.setCode("-12").setMessage("数据请求ID为空");
                }
                
                if ( XJava.getObject(i_JobHttp.getXid()) != null )
                {
                    return v_RetResp.setCode("-13").setMessage("逻辑编号" + i_JobHttp.getXid() + "已存在，禁止重复创建");
                }
                
                if ( Help.isNull(i_JobHttp.getRequestTemplate()) )
                {
                    return v_RetResp.setCode("-14").setMessage("数据请求模板为空");
                }
                
                // 禁止创建时就删除
                i_JobHttp.setIsDel(null);
                i_JobHttp.setXid(i_JobHttp.getXid().trim());
            }
            else
            {
                v_OldJobHttp = this.jobHttpService.queryByID(i_JobHttp.getId());
                if ( v_OldJobHttp == null )
                {
                    return v_RetResp.setCode("-20").setMessage("ID不存在");
                }
                
                // 删除时不判定、修改时必判定
                if ( i_JobHttp.getIsDel() == null || i_JobHttp.getIsDel() == 0 )
                {
                    if ( Help.isNull(i_JobHttp.getTaskHttpID()) )
                    {
                        return v_RetResp.setCode("-12").setMessage("数据请求ID为空");
                    }
                    
                    if ( !Help.isNull(i_JobHttp.getXid()) )
                    {
                        i_JobHttp.setXid(i_JobHttp.getXid().trim());
                        if ( !v_OldJobHttp.getXid().equals(i_JobHttp.getXid()) )
                        {
                            if ( XJava.getObject(i_JobHttp.getXid()) != null )
                            {
                                return v_RetResp.setCode("-13").setMessage("逻辑编号已存在，禁止重复创建");
                            }
                            
                            i_JobHttp.setXidOld(v_OldJobHttp.getXid());
                        }
                    }
                    
                    if ( Help.isNull(i_JobHttp.getRequestTemplate()) )
                    {
                        return v_RetResp.setCode("-14").setMessage("数据请求模板为空");
                    }
                    
                    // 未修改票据ID时，保持原值
                    if ( i_JobHttp.getTokenHttpID() == null )
                    {
                        i_JobHttp.setTokenHttpID(Help.isNull(v_OldJobHttp.getTokenHttpID()) ? null : v_OldJobHttp.getTokenHttpID());
                    }
                    // 解绑票据ID关系时
                    else if ( "".equals(i_JobHttp.getTokenHttpID()) )
                    {
                        i_JobHttp.setTokenHttpID(null);
                    }
                }
                else
                {
                    i_JobHttp.setTaskHttpID(     v_OldJobHttp.getTaskHttpID());
                    i_JobHttp.setRequestTemplate(v_OldJobHttp.getRequestTemplate());
                    i_JobHttp.setTokenHttpID(Help.isNull(v_OldJobHttp.getTokenHttpID()) ? null : v_OldJobHttp.getTokenHttpID());
                }
            }
            
            if ( !Help.isNull(i_JobHttp.getIsDel()) && i_JobHttp.getIsDel() != 0 )
            {
                List<XObject> v_Objects = this.jobHttpService.queryRelations(i_JobHttp.getId());
                if ( !Help.isNull(v_Objects) )
                {
                    return v_RetResp.setCode("-314").setMessage("定时任务请求被" + v_Objects.size() + "处引用，禁止删除");
                }
            }
            
            DBHttp v_TaskHttp = this.dbHttpService.queryByID(i_JobHttp.getTaskHttpID());
            if ( v_TaskHttp == null )
            {
                return v_RetResp.setCode("-25").setMessage("关联的数据请求ID=" + i_JobHttp.getTaskHttpID() + "不存在");
            }
            else if ( XJava.getObject(v_TaskHttp.getXid() ,false) == null )
            {
                return v_RetResp.setCode("-25").setMessage("关联的数据请求ID=" + i_JobHttp.getTaskHttpID() + "未被构建");
            }
            
            if ( !Help.isNull(i_JobHttp.getTokenHttpID()) )
            {
                DBHttp v_TokenHttp = this.dbHttpService.queryByID(i_JobHttp.getTokenHttpID());
                if ( v_TokenHttp == null )
                {
                    return v_RetResp.setCode("-26").setMessage("关联的数据票据ID=" + i_JobHttp.getTokenHttpID() + "不存在");
                }
                else if ( XJava.getObject(v_TokenHttp.getXid() ,false) == null )
                {
                    return v_RetResp.setCode("-26").setMessage("关联的数据票据ID=" + i_JobHttp.getTokenHttpID() + "未被构建");
                }
                else if ( i_JobHttp.getRequestTemplate().indexOf(JobHttp.$Template_Token) < 0 )
                {
                    return v_RetResp.setCode("-26").setMessage("数据请求模板未配置票据占位符" + JobHttp.$Template_Token);
                }
            }
            
            synchronized ( this )
            {
                // 防止同名
                if ( !Help.isNull(i_JobHttp.getXid()) )
                {
                    JobHttp v_SameXID = this.jobHttpService.queryByXID(i_JobHttp.getXid());
                    
                    if ( v_SameXID != null && !v_SameXID.getId().equals(i_JobHttp.getId()) )
                    {
                        $Logger.warn("修改XID[" + i_JobHttp.getXid() + "]时，出现相同的XID，禁止重复创建");
                        return v_RetResp.setCode("-907").setMessage("逻辑编号" + i_JobHttp.getXid() + "已存在，禁止重复创建");
                    }
                }
                
                boolean v_SaveRet = false;
                if ( Help.isNull(i_JobHttp.getId()) )
                {
                    v_SaveRet = this.jobHttpService.insert(i_JobHttp);
                }
                else
                {
                    v_SaveRet = this.jobHttpService.update(i_JobHttp);
                }
                
                if ( v_SaveRet )
                {
                    $Logger.info("用户（" + i_JobHttp.getCreateUserID() + "）创建" + i_JobHttp.getXid() + "，成功");
                    return v_RetResp.setData(i_JobHttp);
                }
                else
                {
                    $Logger.error("用户（" + Help.NVL(i_JobHttp.getCreateUserID() ,i_JobHttp.getUserID()) + "）创建" + i_JobHttp.getXid() + "，异常");
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
            $Logger.info("saveJobHttp End: " + i_Token + ":" + i_JobHttp.toString());
        }
    }
    
}
