package org.hy.microservice.timing.monitor;

import java.util.List;

import org.hy.common.xml.annotation.XType;
import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.annotation.Xparam;
import org.hy.common.xml.annotation.Xsql;





/**
 * 定时任务的责任人的操作DAO层
 *
 * @author      ZhengWei(HY)
 * @createDate  2023-10-07
 * @version     v1.0
 */
@Xjava(id="JobUserDAO" ,value=XType.XSQL)
public interface IJobUserDAO
{
    
    /**
     * 查询所有责任人列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-07
     * @version     v1.0
     * 
     * @return
     */
    @Xsql("XSQL_Timing_JobUser_Query_All")
    public List<JobUser> queryAll();
    
    
    
    /**
     * 查询具体定时任务的责任人列表
     * 
     * @author      ZhengWei(HY)
     * @createDate  2023-10-07
     * @version     v1.0
     * 
     * @return
     */
    @Xsql("XSQL_Timing_JobUser_Query_ByJobID")
    public List<JobUser> queryByJobID(@Xparam("id") String i_JobID);
    
}
