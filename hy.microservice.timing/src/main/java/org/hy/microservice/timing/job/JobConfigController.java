package org.hy.microservice.timing.job;

import java.util.ArrayList;
import java.util.List;

import org.hy.common.Help;
import org.hy.common.StringHelp;
import org.hy.common.app.Param;
import org.hy.common.net.common.ClientCluster;
import org.hy.common.thread.Job;
import org.hy.common.thread.Jobs;
import org.hy.common.xml.XJava;
import org.hy.common.xml.log.Logger;
import org.hy.microservice.common.BaseController;
import org.hy.microservice.common.BaseResponse;
import org.hy.microservice.common.user.UserSSO;
import org.hy.microservice.common.user.UserService;
import org.hy.microservice.timing.cluster.TimingClusterInfo;
import org.hy.microservice.timing.monitor.IJobUserService;
import org.hy.microservice.timing.monitor.JobUser;
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
 * 任务配置信息的控制层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-09-13
 * @version     v1.0
 */
@Controller
@RequestMapping(value="job" ,name="任务配置")
public class JobConfigController extends BaseController
{
    
    private static final Logger $Logger = new Logger(JobConfigController.class);
    
    
    
    @Autowired
    @Qualifier("JobConfigService")
    private IJobConfigService          jobConfigService;
    
    @Autowired
    @Qualifier("JobUserService")
    private IJobUserService            jobUserService;
    
    @Autowired
    @Qualifier("MS_Timing_JobIntervalTypes")
    private List<Param>                jobIntervalTypes;
    
    @Autowired
    @Qualifier("UserService")
    private UserService                userService;
    
    @Autowired
    @Qualifier("MS_Timing_IsCheckToken")
    private Param                      isCheckToken;
    
    
    
    /**
     * 保存任务配置
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobConfig
     * @return
     */
    @RequestMapping(name="保存任务配置" ,value="saveJob" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<JobConfig> saveJobConfig(@RequestParam(value="token" ,required=false) String i_Token
                                                ,@RequestBody JobConfig i_JobConfig)
    {
        BaseResponse<JobConfig> v_RetResp = new BaseResponse<JobConfig>();
        
        if ( i_JobConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("saveJobConfig Start: " + i_Token + ":" + i_JobConfig.toString());
            
            if ( Help.isNull(i_JobConfig.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            synchronized ( this )
            {
                // 新创建的验证
                if ( Help.isNull(i_JobConfig.getId()) )
                {
                    if ( Help.isNull(i_JobConfig.getCode()) )
                    {
                        return v_RetResp.setCode("-10").setMessage("任务编号为空");
                    }
                    
                    if ( Help.isNull(i_JobConfig.getIntervalType()) )
                    {
                        return v_RetResp.setCode("-11").setMessage("间隔类型为空");
                    }
                    
                    if ( i_JobConfig.getIntervalLen() <= 0 )
                    {
                        return v_RetResp.setCode("-12").setMessage("间隔长度为空");
                    }
                    
                    if ( Help.isNull(i_JobConfig.getXid()) )
                    {
                        return v_RetResp.setCode("-13").setMessage("被执行任务对象的XJava标识为空");
                    }
                    
                    if ( Help.isNull(i_JobConfig.getMethodName()) )
                    {
                        return v_RetResp.setCode("-14").setMessage("被执行任务对象的方法名为空");
                    }
                    
                    if ( Help.isNull(i_JobConfig.getMethodName()) )
                    {
                        return v_RetResp.setCode("-14").setMessage("被执行任务对象的方法名为空");
                    }
                    
                    if ( Help.isNull(i_JobConfig.getName()) && Help.isNull(i_JobConfig.getComment()) )
                    {
                        return v_RetResp.setCode("-15").setMessage("任务名称和备注至少填写一个");
                    }
                    
                    if ( !Help.isNull(i_JobConfig.getStartTimes()) )
                    {
                        if ( i_JobConfig.getStartTimes().size() >= 2 )
                        {
                            if ( Job.$IntervalType_Minute == i_JobConfig.getIntervalType()
                              || Job.$IntervalType_Second == i_JobConfig.getIntervalType() )
                            {
                                return v_RetResp.setCode("-16").setMessage("多组开始时间，不支持间隔类型为：分钟或秒");
                            }
                        }
                    }
                    
                    if ( !this.checkIntervalType(i_JobConfig.getIntervalType()) )
                    {
                        return v_RetResp.setCode("-17").setMessage("间隔类型非法传参");
                    }
                    
                    // 防止重复
                    JobConfig v_CheckJobConfig = this.jobConfigService.queryByCode(i_JobConfig.getCode());
                    if ( v_CheckJobConfig != null )
                    {
                        return v_RetResp.setCode("-18").setMessage("任务编码已存在");
                    }
                    
                    i_JobConfig.setCreateUserID(i_JobConfig.getUserID());
                    i_JobConfig.setUpdateUserID(i_JobConfig.getUserID());
                }
                // 更新的验证
                else
                {
                    JobConfig v_OldJobConfig = this.jobConfigService.queryByID(i_JobConfig.getId());
                    if ( v_OldJobConfig == null )
                    {
                        return v_RetResp.setCode("-30").setMessage("修改任务不存在");
                    }
                    
                    if ( Help.isNull(i_JobConfig.getIntervalType()) )
                    {
                        return v_RetResp.setCode("-11").setMessage("间隔类型为空");
                    }
                    
                    if ( i_JobConfig.getIntervalLen() <= 0 )
                    {
                        return v_RetResp.setCode("-12").setMessage("间隔长度为空");
                    }
                    
                    if ( i_JobConfig.getIsEnabled() == null )
                    {
                        // 当未修改启用状态时，取原始启用状态
                        i_JobConfig.setIsEnabled(Help.NVL(v_OldJobConfig.getIsEnabled() ,1));
                    }
                    
                    if ( !Help.isNull(i_JobConfig.getStartTimes()) )
                    {
                        if ( i_JobConfig.getStartTimes().size() >= 2 )
                        {
                            if ( Job.$IntervalType_Minute == i_JobConfig.getIntervalType()
                              || Job.$IntervalType_Second == i_JobConfig.getIntervalType() )
                            {
                                return v_RetResp.setCode("-16").setMessage("多组开始时间，不支持间隔类型为：分钟或秒");
                            }
                        }
                    }
                    else
                    {
                        // 当未修改时间组时，取原始时间组
                        i_JobConfig.setStartTimes(v_OldJobConfig.toStartTimes());
                    }
                    
                    if ( !this.checkIntervalType(i_JobConfig.getIntervalType()) )
                    {
                        return v_RetResp.setCode("-17").setMessage("间隔类型非法传参");
                    }
                    
                    if ( !Help.isNull(i_JobConfig.getCode()) )
                    {
                        if ( !v_OldJobConfig.getCode().equals(i_JobConfig.getCode()) )
                        {
                            // 防止重复
                            JobConfig v_CheckJobConfig = this.jobConfigService.queryByCode(i_JobConfig.getCode());
                            if ( v_CheckJobConfig != null )
                            {
                                return v_RetResp.setCode("-18").setMessage("任务编码已存在");
                            }
                            
                            // 修改定时任务编码时，向后传递原始编码
                            i_JobConfig.setCodeOld(v_OldJobConfig.getCode());
                        }
                    }
                    else
                    {
                        // 当未定时任务编码时，取原始编码
                        i_JobConfig.setCode(v_OldJobConfig.getCode());
                    }
                    
                    i_JobConfig.setUpdateUserID(i_JobConfig.getUserID());
                }
                
                // 验证任务责任人
                if ( !Help.isNull(i_JobConfig.getJobUsers()) )
                {
                    for (JobUser v_JobUser : i_JobConfig.getJobUsers())
                    {
                        if ( Help.isNull(v_JobUser.getId()) )
                        {
                            if ( Help.isNull(v_JobUser.getUserName()) )
                            {
                                return v_RetResp.setCode("-201").setMessage("任务责任人名称为空");
                            }
                            
                            if ( Help.isNull(v_JobUser.getPhone())
                              && Help.isNull(v_JobUser.getEmail())
                              && Help.isNull(v_JobUser.getOpenID()) )
                            {
                                return v_RetResp.setCode("-202").setMessage("任务责任人手机、邮箱、微信均为空");
                            }
                            
                            v_JobUser.setId(StringHelp.getUUID());
                        }
                        else
                        {
                            JobUser v_JobUserExists = this.jobUserService.queryByUserID(v_JobUser.getId());
                            if ( v_JobUserExists == null )
                            {
                                return v_RetResp.setCode("-202").setMessage("任务责任人ID=" + v_JobUser.getId() + "不存");
                            }
                            else
                            {
                                // 判定责任人是否有改变
                                if ( !Help.isNull(v_JobUser.getUserName()) && !v_JobUser.getUserName().equals(v_JobUserExists.getUserName()) )
                                {
                                    v_JobUser.setAllowUpdate(1);
                                }
                                else if ( !Help.isNull(v_JobUser.getPhone()) && !v_JobUser.getPhone().equals(v_JobUserExists.getPhone()) )
                                {
                                    v_JobUser.setAllowUpdate(1);
                                }
                                else if ( !Help.isNull(v_JobUser.getEmail()) && !v_JobUser.getEmail().equals(v_JobUserExists.getEmail()) )
                                {
                                    v_JobUser.setAllowUpdate(1);
                                }
                                else if ( !Help.isNull(v_JobUser.getOpenID()) && !v_JobUser.getOpenID().equals(v_JobUserExists.getOpenID()) )
                                {
                                    v_JobUser.setAllowUpdate(1);
                                }
                                
                                if ( v_JobUser.getAllowUpdate().equals(1) )
                                {
                                    if ( Help.isNull(v_JobUser.getUserName()) )
                                    {
                                        return v_RetResp.setCode("-201").setMessage("任务责任人名称为空");
                                    }
                                    
                                    if ( Help.isNull(v_JobUser.getPhone())
                                      && Help.isNull(v_JobUser.getEmail())
                                      && Help.isNull(v_JobUser.getOpenID()) )
                                    {
                                        return v_RetResp.setCode("-202").setMessage("任务责任人手机、邮箱、微信均为空");
                                    }
                                    
                                    v_JobUser.setPhone( Help.NVL(v_JobUser.getPhone()));
                                    v_JobUser.setEmail( Help.NVL(v_JobUser.getEmail()));
                                    v_JobUser.setOpenID(Help.NVL(v_JobUser.getOpenID()));
                                }
                            }
                        }
                        
                        v_JobUser.setCreateUserID(i_JobConfig.getUserID());
                        v_JobUser.setUpdateUserID(i_JobConfig.getUserID());
                    }
                }
                else
                {
                    i_JobConfig.setJobUsers(new ArrayList<JobUser>());
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
                
                JobConfig v_SaveRet = this.jobConfigService.save(i_JobConfig);
                if ( v_SaveRet != null )
                {
                    $Logger.info("用户（" + v_SaveRet.getCreateUserID() + "）创建" + v_SaveRet.toString() + "，成功");
                    return v_RetResp.setData(v_SaveRet);
                }
                else
                {
                    $Logger.error("用户（" + Help.NVL(i_JobConfig.getCreateUserID() ,i_JobConfig.getUserID()) + "）创建" + i_JobConfig.toString() + "，异常");
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
            $Logger.info("saveJobConfig End: " + i_Token + ":" + i_JobConfig.toString());
        }
    }
    
    
    
    /**
     * 查询定时任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-14
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobConfig
     * @return
     */
    @RequestMapping(name="查询定时任务" ,value="queryJob" ,method={RequestMethod.POST ,RequestMethod.GET} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<JobConfigReport> queryJob(@RequestParam(value="token" ,required=false) String i_Token
                                                 ,@RequestBody JobConfig i_JobConfig)
    {
        BaseResponse<JobConfigReport> v_RetResp = new BaseResponse<JobConfigReport>();
        
        if ( i_JobConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("queryJobConfig Start: " + i_Token + ":" + i_JobConfig.toString());
            
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
            
            return v_RetResp.setData(this.jobConfigService.queryList());
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("queryJobConfig End: " + i_Token + ":" + i_JobConfig.toString());
        }
    }
    
    
    
    /**
     * 手工执行定时任务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-25
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobConfig
     * @return
     */
    @RequestMapping(name="执行定时任务" ,value="execute" ,method={RequestMethod.POST} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<JobConfigReport> execute(@RequestParam(value="token" ,required=false) String i_Token
                                                ,@RequestBody JobConfig i_JobConfig)
    {
        BaseResponse<JobConfigReport> v_RetResp = new BaseResponse<JobConfigReport>();
        
        if ( i_JobConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("execute Start: " + i_Token + ":" + i_JobConfig.toString());
            
            if ( Help.isNull(i_JobConfig.getUserID()) )
            {
                return v_RetResp.setCode("-2").setMessage("用户编号为空");
            }
            
            if ( Help.isNull(i_JobConfig.getCode()) )
            {
                return v_RetResp.setCode("-3").setMessage("定时任务编号为空");
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
            
            JobConfig v_JobDB = this.jobConfigService.queryByCode(i_JobConfig.getCode());
            Job       v_JobMM = (Job) XJava.getObject(i_JobConfig.getCode());
            if ( v_JobMM == null )
            {
                return v_RetResp.setCode("-910").setMessage("定时任务编号不存");
            }
            
            boolean v_ForceRun = v_JobMM.isForceRun();
            v_JobMM.setForceRun(true);
            v_JobMM.execute();
            v_JobMM.setForceRun(v_ForceRun);
            
            return v_RetResp.setData(this.jobConfigService.toJobConfigReport(v_JobMM ,v_JobDB));
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("execute End: " + i_Token + ":" + i_JobConfig.toString());
        }
    }
    
    
    
    /**
     * 我是Master节点?
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-26
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobConfig
     * @return
     */
    @RequestMapping(name="我是主节点吗" ,value="isMaster" ,method={RequestMethod.POST ,RequestMethod.GET} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<TimingClusterInfo> isMaster(@RequestParam(value="token" ,required=false) String i_Token
                                                   ,@RequestBody JobConfig i_JobConfig)
    {
        BaseResponse<TimingClusterInfo> v_RetResp = new BaseResponse<TimingClusterInfo>();
        
        if ( i_JobConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("isMaster Start: " + i_Token + ":" + i_JobConfig.toString());
            
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
            
            Jobs              v_Jobs    = (Jobs) XJava.getObject("JOBS_MS_Common");
            TimingClusterInfo v_Cluster = new TimingClusterInfo();
            
            if ( v_Jobs.isMaster() )
            {
                if ( v_Jobs.isDisasterRecovery() )
                {
                    String              v_IPs      = Help.getIPs();
                    List<ClientCluster> v_Clusters = v_Jobs.getDisasterRecoverys();
                    
                    for (ClientCluster v_Client : v_Clusters)
                    {
                        if ( StringHelp.isContains(v_IPs ,v_Client.getHost()) )
                        {
                            v_Cluster.setIp(v_Client.getHost());
                            break;
                        }
                    }
                    
                }
                
                v_Cluster.setType("master");
            }
            else
            {
                v_Cluster.setType("slave");
            }
            
            return v_RetResp.setData(v_Cluster);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("isMaster End: " + i_Token + ":" + i_JobConfig.toString());
        }
    }
    
    
    
    /**
     * 查询间隔类型
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobConfig
     * @return
     */
    @RequestMapping(name="查询间隔类型" ,value="intervalType" ,method={RequestMethod.POST ,RequestMethod.GET} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<Param> intervalType(@RequestParam(value="token" ,required=false) String i_Token
                                           ,@RequestBody JobConfig i_JobConfig)
    {
        BaseResponse<Param> v_RetResp = new BaseResponse<Param>();
        
        if ( i_JobConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("intervalType Start: " + i_Token + ":" + i_JobConfig.toString());
            
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

            return v_RetResp.setData(this.jobIntervalTypes);
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("intervalType End: " + i_Token + ":" + i_JobConfig.toString());
        }
    }
    
    
    
    /**
     * 查询云服务
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     *
     * @param i_Token
     * @param i_JobConfig
     * @return
     */
    @RequestMapping(name="查询云服务" ,value="cloudServer" ,method={RequestMethod.POST ,RequestMethod.GET} ,produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BaseResponse<Param> cloudServer(@RequestParam(value="token" ,required=false) String i_Token
                                          ,@RequestBody JobConfig i_JobConfig)
    {
        BaseResponse<Param> v_RetResp = new BaseResponse<Param>();
        
        if ( i_JobConfig == null )
        {
            return v_RetResp.setCode("-1").setMessage("未收到任何参数");
        }
        
        try
        {
            $Logger.info("cloudServer Start: " + i_Token + ":" + i_JobConfig.toString());
            
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

            return v_RetResp.setData(this.jobConfigService.queryCloudServers());
        }
        catch (Exception exce)
        {
            $Logger.error(exce);
            return v_RetResp.setCode("-999").setMessage("系统异常，请联系管理员");
        }
        finally
        {
            $Logger.info("cloudServer End: " + i_Token + ":" + i_JobConfig.toString());
        }
    }
    
    
    
    /**
     * 检查间隔类型是否为有效值
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-09-13
     * @version     v1.0
     *
     * @param i_IntervalType  间隔类型
     * @return
     */
    private boolean checkIntervalType(int i_IntervalType)
    {
        String v_IntervalType = i_IntervalType + "";
                
        for (Param v_Item : this.jobIntervalTypes)
        {
            if ( v_IntervalType.equals(v_Item.getValue()) )
            {
                return true;
            }
        }
        
        return false;
    }
    
}
