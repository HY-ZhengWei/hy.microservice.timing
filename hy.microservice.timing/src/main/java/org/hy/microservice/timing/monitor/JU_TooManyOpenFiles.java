package org.hy.microservice.timing.monitor;

import java.nio.file.FileStore;
import java.nio.file.FileSystems;

import org.hy.common.xml.annotation.Xjava;
import org.hy.common.xml.plugins.analyse.data.ClusterReport;





@Xjava
public class JU_TooManyOpenFiles
{
    
    public void test(int i_Max)
    {
        ClusterReport v_ClusterReport = new ClusterReport();
        
        for (int x=1; x<=i_Max; x++)
        {
            System.out.println("x=" + x);
            v_ClusterReport.calcLinuxDiskUseRate();
        }
    }
    
    
    
    public void test02()
    {
        for (FileStore v_Item : FileSystems.getDefault().getFileStores())
        {
            System.out.println(v_Item);
        }
    }
    
}
