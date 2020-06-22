package com.fengtu.data.quartz;

import com.fengtu.data.config.FileFolderPOJO;
import com.fengtu.data.hdfs.config.HadoopConf;
import com.fengtu.data.hdfs.utils.HdfsClientUtils;
import com.fengtu.data.hive.impl.HiveServiceImpl;
import com.fengtu.data.service.RealDataProServiceImpl;
import com.fengtu.data.utils.DateUtils;
import com.fengtu.data.utils.FileScanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author yls
 * @Date 2020/3/30 11:50
 * @Description 定时器：注解的方式自动注入
 * @Version 1.0
 **/
@Component
@Configuration
@EnableScheduling
public class AppTaskQuartz {
    //日志
    private static final Logger LOGGER = LoggerFactory.getLogger(AppTaskQuartz.class);

    private static  boolean isRun = false;

    @Autowired
    private RealDataProServiceImpl realDataProService;

    //@Scheduled(cron = "0 0 12  * * * ")
    @Scheduled(cron = "0/20 * * * * * ")
    public void  AppTaskRun(){
       LOGGER.info("=》AppTaskQuartz定时器-----------run；");
       try {
           if (!isRun){
               realDataProService.mainJobRun();
               isRun = true;
           }
           LOGGER.info("测试完成。");
       }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("定时器执行出现异常："+e.getMessage());
       }


    }

}
