package org.hy.microservice.timing.monitor;

import org.hy.common.Help;
import org.hy.common.app.Param;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.user.UserSSO;
import org.hy.microservice.common.user.UserService;
import org.hy.microservice.timing.job.JobConfig;
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
 * 任务责任人的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-10-08
 * @version     v1.0
 */
@Controller
@RequestMapping(value="jobUser" ,name="任务责任人")
public class JobUserController extends BaseController
{
    
    private static final Logger $Logger = new Logger(JobUserController.class);
    
    
    
    @Autowired
    @Qualifier("JobUserService")
    private IJobUserService            jobUserService;
    
    @Autowired
    @Qualifier("UserService")
    private UserService                userService;
    
    @Autowired
    @Qualifier("MS_Timing_IsCheckToken")
    private Param                      isCheckToken;
    
    
    
    /**
     * 查询任务责任人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-08
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobConfig
     * @return
     */
    @RequestMapping(name="查询任务责任人" ,value="queryJobUser" ,method={RequestMethod.POST ,RequestMethod.GET} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<JobUser> queryJobUser(@RequestParam(value="token" ,required=false) String i_Token
                                             ,@RequestBody JobConfig i_JobConfig)
    {
        BaseResponse<JobUser> v_RetResp = new BaseResponse<JobUser>();
        
        if ( i_JobConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("queryJobUser Start: " + i_Token + ":" + i_JobConfig.toString());
            
            if ( Help.isNull(i_JobConfig.getUserID()) )
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
                
                if ( !v_User.getUserId().equals(i_JobConfig.getUserID()) )
                {
                    return v_RetResp.setCode("-902").setMessage("操作用户与登录用户不一致");
                }
            }
            
            if ( Help.isNull(i_JobConfig.getId()) )
            {
                // 查询所有责任人
                v_RetResp.setData(this.jobUserService.queryAll());
            }
            else
            {
                // 查询与定时任务相关的责任人
                v_RetResp.setData(this.jobUserService.queryByJobID(i_JobConfig.getId()));
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
            $Logger.info("queryJobUser End: " + i_Token + ":" + i_JobConfig.toString());
        }
    }
    
    
    
    /**
     * 保存任务责任人
     * 
     * @author      ZhengWei(HY)
     * @createDate  2024-01-16
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobUser
     * @return
     */
    @RequestMapping(name="保存任务责任人" ,value="saveJobUser" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<JobUser> saveJobUser(@RequestParam(value="token" ,required=false) String i_Token
                                            ,@RequestBody JobUser i_JobUser)
    {
        $Logger.info("saveJobUser Start: " + i_Token);
        
        BaseResponse<JobUser> v_RetResp = new BaseResponse<JobUser>();
        
        if ( i_JobUser == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            if ( Help.isNull(i_JobUser.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            if ( Help.isNull(i_JobUser.getId()) )
            {
                if ( Help.isNull(i_JobUser.getUserName()) )
                {
                    return v_RetResp.setCode("-201").setMessage("任务责任人名称为空");
                }
                
                if ( Help.isNull(i_JobUser.getPhone())
                  && Help.isNull(i_JobUser.getEmail())
                  && Help.isNull(i_JobUser.getOpenID()) )
                {
                    return v_RetResp.setCode("-202").setMessage("任务责任人手机、邮箱、微信均为空");
                }
                
                // 禁止创建时就删除
                i_JobUser.setIsDel(null);
            }
            else
            {
                JobUser v_JobUserExists = this.jobUserService.queryByUserID(i_JobUser.getId());
                if ( v_JobUserExists == null )
                {
                    return v_RetResp.setCode("-200").setMessage("任务责任人ID=" + i_JobUser.getId() + "不存");
                }
                else
                {
                    if ( i_JobUser.getIsDel() != null && i_JobUser.getIsDel() == 1 )
                    {
                        // 删除时，不用判定
                    }
                    else
                    {
                        if ( Help.isNull(i_JobUser.getUserName()) )
                        {
                            return v_RetResp.setCode("-201").setMessage("任务责任人名称为空");
                        }
                        
                        if ( Help.isNull(i_JobUser.getPhone())
                          && Help.isNull(i_JobUser.getEmail())
                          && Help.isNull(i_JobUser.getOpenID()) )
                        {
                            return v_RetResp.setCode("-202").setMessage("任务责任人手机、邮箱、微信均为空");
                        }
                        
                        i_JobUser.setPhone( Help.NVL(i_JobUser.getPhone()));
                        i_JobUser.setEmail( Help.NVL(i_JobUser.getEmail()));
                        i_JobUser.setOpenID(Help.NVL(i_JobUser.getOpenID()));
                    }
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
            }
            
            synchronized (this)
            {
                JobUser v_SaveRet = this.jobUserService.save(i_JobUser);
                if ( v_SaveRet != null )
                {
                    $Logger.info("用户（" + i_JobUser.getCreateUserID() + "）创建成功");
                    return v_RetResp.setData(v_SaveRet);
                }
                else
                {
                    $Logger.error("用户（" + Help.NVL(i_JobUser.getCreateUserID() ,i_JobUser.getUserID()) + "）创建" + i_JobUser.getUserName() + "，异常");
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
            $Logger.info("saveJobUser End: "  + i_Token + ": " + v_RetResp.getMessage());
        }
    }
    
}
