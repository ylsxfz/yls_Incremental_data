package com.fengtu.data.service;

import com.fengtu.data.config.DownByHivePOJO;
import com.fengtu.data.hive.impl.HiveServiceImpl;
import com.fengtu.data.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Auther: yls
 * @Date: 2020/8/20 9:32
 * @Description: 每天执行hive
 * @Version 1.0
 */
@Service
public class ExecuteHiveByDayImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteHiveByDayImpl.class);

    @Autowired
    private DownByHivePOJO downByHivePOJO;

    @Autowired
    private HiveServiceImpl hiveServiceImpl;


    public void mainJob() {
        /**
         * 1、获取前一天的时间分区
         */
        String format = "yyyyMMdd";
        Date currentTime = DateUtils.getCurrentTime();
        //获取前一天的分区
        Date minDate = DateUtils.subtractDate(currentTime, 1);
        String startPartition = DateUtils.toString(minDate, format);
        LOGGER.info("本次处理的分区为：" + startPartition);

        /**
         * 2、拼接hive的sql，并执行sql
         */
        String searchSql = downByHivePOJO.getSearchSql();
        searchSql = searchSql.replaceAll("19700101",startPartition);
        String searchTaskName = "yunnan_search_" + startPartition;
        hiveServiceImpl.executeHiveSqlByDay(searchTaskName, searchSql);
        LOGGER.info("本次查询统计执行的sql为：" + searchSql);

        /**
         * 3、导出文件
         */
        String downloadSql = downByHivePOJO.getDownloadSql();
        downloadSql = downloadSql.replaceAll("19700101",startPartition);
        String downTaskName = "yunnan_down_" + startPartition;
        hiveServiceImpl.executeHiveSqlByDay(downTaskName, downloadSql);
        LOGGER.info("本次导出文件执行的sql为：" + downloadSql);


    }

}
