package com.fengtu.data.init;

import com.fengtu.data.hdfs.HdfsClient;
import com.fengtu.data.hdfs.config.HadoopConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @Auther: yls
 * @Date: 2020/5/18 14:48
 * @Description:
 * @Version 1.0
 */
@Component
public class ClusterInit implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterInit.class);

    @Autowired
    private HadoopConf hadoopConf;

    @Autowired
    private HdfsClient hdfsClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //1、初始化hdfs客户端
       try {
           hdfsClient.init(hadoopConf.getHdfsAddress());
           LOGGER.info("hdfs初始化成功："+hadoopConf);
       }catch (Exception e){
           LOGGER.error("hdfs初始化失败：");
           LOGGER.error(e.getMessage());
       }

    }
}
