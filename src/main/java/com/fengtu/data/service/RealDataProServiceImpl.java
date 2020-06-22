package com.fengtu.data.service;

import com.alibaba.druid.sql.parser.Lexer;
import com.fengtu.data.config.FileFolderPOJO;
import com.fengtu.data.config.RealTimeDataPOJO;
import com.fengtu.data.hdfs.config.HadoopConf;
import com.fengtu.data.hdfs.utils.HdfsClientUtils;
import com.fengtu.data.hive.impl.HiveServiceImpl;
import com.fengtu.data.utils.DateUtils;
import com.fengtu.data.utils.FileScanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

/**
 * @Auther: yls
 * @Date: 2020/5/19 10:01
 * @Description:
 * @Version 1.0
 */
@Component
public class RealDataProServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealDataProServiceImpl.class);
    @Autowired
    private FileFolderPOJO fileFolderPOJO;

    @Autowired
    private HadoopConf hadoopConf;

    @Autowired
    private HiveServiceImpl hiveServiceImpl;

    @Autowired
    private RealTimeDataPOJO realTimeDataPOJO;


    public void mainJobRun() throws Exception {
        String format = "yyyyMMdd";
        Date currentTime = DateUtils.getCurrentTime();
        //获取最小分区
        Date minDate = DateUtils.subtractDate(currentTime, 15);
        String startPartition= DateUtils.toString(minDate, format);
        //获取最大分区
        Date maxDate = DateUtils.addDate(currentTime, 5);
        String endPartition = DateUtils.toString(maxDate, format);
        //分区字段
        String partitionField = realTimeDataPOJO.getPartitionField();

        //1、设置目标文件夹
        setScanFilter();
        //处理的时间分区：前一天的时间分区
        String partition = fileFolderPOJO.getPartition();
        //2、拷贝前一天的文件
        FileScanUtils.copyOneDayFiles(fileFolderPOJO);

        //3、上传前一天的文件
        uploadFileToHdfs(partitionField);

        //4、修复hive的分区
        msckPartitionsSouceTab();

        //5、添加列式存储分区
        addPartitionsParquestTab(partition);

        //6、转换文件格式
        convertDataToParquestTab(partition);

        //7、设置参数
        hiveServiceImpl.uploadDataByHive("set hive.exec.dynamic.partition.mode=nonstrict");

        //8、拆分发件信息
        analysisSendData(partition, startPartition, endPartition, partitionField);

        //9、拆分收件信息
        analysisReceiveData(partition, startPartition, endPartition, partitionField);
    }

    /**
     * 功能描述:
     * 〈拆分收件数据〉
     *
     * @author : yls
     * @date : 2020/5/19 11:01
     * @param partition 处理的分区
     * @param startPartition 最小分区
     * @param endPartition 最大分区
     * @param partitionField 分区字段
     * @return : java.lang.String
     */
    public void analysisReceiveData(String partition, String startPartition, String endPartition, String partitionField) {
        String sql="insert into table\n" +
                realTimeDataPOJO.getTargetDatabase()+"."+realTimeDataPOJO.getTargetTable()+"\n"+
                "partition("+partitionField+")\n" +
                "select * from\n" +
                "(\n" +
//                "select split(orderno,'<=>')[1] as orderno,\n" +
                "select\n" +
                "orderno,\n" +
                "rmobile,\n" +
                "rcity,\n" +
                "companyName,\n" +
                "rname,\n" +
                "radd,\n" +
                "case when signoffTime<\"2017-00-01 00:00:00\"\n" +
                "then concat(date_add(delyTime,3),\" \",split(delyTime,' ')[1])\n" +
                "else signoffTime end as action_time,\n" +
                "\"RECEIVE\" as type,\n" +
                "case when signoffTime<\"2017-00-01 00:00:00\" or  signoffTime=\"null\" or signoffTime=\"\"\n" +
                "then (case when delyTime!='null' and length(regexp_replace(date_add(delyTime,3),'-',''))=8\n" +
                "then regexp_replace(date_add(delyTime,3),'-','')  else \"19700101\"  end) \n" +
                "else regexp_replace(split(signoffTime,\" \")[0],'-','') \n" +
                "end as\n" + partitionField + "\n" +
                "from\n" +
                realTimeDataPOJO.getSourceDatabase()+"."+realTimeDataPOJO.getSourceParquetTable()+"\n"+
                "where\n"+partitionField+"='"+partition+"' \n" +
                ") as temp\n" +
                "where \n"+partitionField+" !='null' and length("+partitionField+")=8 \n" +
                "and\n"+partitionField+">='"+startPartition+"'\nand\n"+partitionField+"<'"+endPartition+"' " +
                "and\n"+partitionField+"\nrlike '^\\\\d+$'\n" +
                "distribute by rand()";
        LOGGER.info("开始拆分收件信息==》"+sql);
        hiveServiceImpl.uploadDataByHive(sql);
        LOGGER.info("收件信息拆分完毕。");
    }

    /**
     * 功能描述:
     * 〈拆分发件信息〉
     *
     * @author : yls
     * @date : 2020/5/19 11:01
     * @param partition 处理的分区
     * @param startPartition 最小分区
     * @param endPartition 最大分区
     * @param partitionField 分区字段
     * @return : void
     */
    public void analysisSendData(String partition, String startPartition, String endPartition, String partitionField) {
        String sql =   "insert into table\n" +
                realTimeDataPOJO.getTargetDatabase()+"."+realTimeDataPOJO.getTargetTable()+"\n"+
                "partition("+partitionField+")\n" +
                "select * from\n" +
                "(\n" +
//                "select split(orderno,'<=>')[1] as orderno,\n" +
                "select\n" +
                "orderno,\n" +
                "smobile,\n" +
                "scity,\n" +
                "companyName,\n" +
                "sname,\n" +
                "sadd,\n" +
                "delyTime,\n" +
                "\"SEND\" as type,\n" +
                "case when delyTime!='null' and length(regexp_replace(split(delyTime,\" \")[0],'-',''))=8\n" +
                "then regexp_replace(split(delyTime,\" \")[0],'-','')\n" +
                "else \"19700101\" end  as\n"+partitionField+" \n" +
                "from\n" +
                realTimeDataPOJO.getSourceDatabase()+"."+realTimeDataPOJO.getSourceParquetTable()+"\n"+
                "where\n"+partitionField+"='"+partition+"' \n" +
                ") as temp\n" +
                "where \n"+partitionField+" !='null' and length("+partitionField+")=8 \n" +
                "and\n"+partitionField+">='"+startPartition+"'\nand\n"+partitionField+"<'"+endPartition+"' " +
                "and\n"+partitionField+"\nrlike '^\\\\d+$'\n" +
                "distribute by rand()";
        LOGGER.info("开始拆分发件信息==》"+sql);
        hiveServiceImpl.uploadDataByHive(sql);
        LOGGER.info("发件信息拆分完毕。");
    }

    /**
     * 功能描述:
     * 〈转换数据格式〉
     *
     * @author : yls
     * @date : 2020/5/19 11:02
     * @param partition
     * @return : void
     */
    public void convertDataToParquestTab(String partition) {
        //如果正怎处理的分区存在，需要先删除存在的分区
        hiveServiceImpl.uploadDataByHive("alter table \n"+
                realTimeDataPOJO.getSourceDatabase()+"."+realTimeDataPOJO.getSourceParquetTable()+"\n" +
                "drop if exists\n" +
                "partition("+ realTimeDataPOJO.getPartitionField() + "="+partition+")");
        //开始转换格式
        String sql = "insert into table\n"+
                realTimeDataPOJO.getSourceDatabase()+"."+realTimeDataPOJO.getSourceParquetTable()+"\n" +
                "partition("+ realTimeDataPOJO.getPartitionField() + "="+partition+")\n" +
                "select \n" +
                "id,orderNo,bizType,transType,payment,isCOD,codCy,codValue,isInsure,insureValue,isSPU,isInspect,\n" +
                "isCustoms,specialBiz,companyCode,companyName,branceCode,branceName,gName,gType,gQty,gWt,gVol,gPkg,gSize,\n" +
                "sCountry,sProvince,sCity,sDistrict,sCSRCode,sTBID,sOrgan,sName,sID,sMobile,sMobileAttr,\n" +
                "sMobileType,sTel,sAdd,sZip,sCourierName,sCourierMobile,sBrCode,sBrName,sBrTel,sBrAdd,\n" +
                "collTime,orderTime,delyTime,eATime,sendTime,signoffTime,rCountry,rProvince,rCity,rDistrict,\n" +
                "rCSRCode,rTBID,rOrgan,rName,rID,rMobile,rMobileAttr,rMobileType,rTel,rAdd,rZip,rCourierName,\n" +
                "rCourierMobile,rBrCode,rBrName,rBrTel,rBrAdd\n" +
                "from \n" + realTimeDataPOJO.getSourceDatabase()+"."+realTimeDataPOJO.getSourceTable()+"\n"+
                "where \n"+ realTimeDataPOJO.getPartitionField() + "="+partition;
        LOGGER.info("开始转换数据格式==》"+sql);
        hiveServiceImpl.uploadDataByHive(sql);
        LOGGER.info("数据格式转换完毕："+sql);
    }

    /**
     * 功能描述:
     * 〈列式存储添加分区〉
     *
     * @author : yls
     * @date : 2020/5/19 10:54
     * @param partition 需要处理的分区
     * @return : void
     */
    public void addPartitionsParquestTab(String partition) {
        String sql = "alter table\n"+
                realTimeDataPOJO.getSourceDatabase()+"."+realTimeDataPOJO.getSourceParquetTable()+"\n"+
                "add if not exists\n" +
                "partition("+ realTimeDataPOJO.getPartitionField() + "="+partition+")";
        hiveServiceImpl.uploadDataByHive(sql);
    }

    /**
     * 功能描述:
     * 〈修复源数据表的分区〉
     *
     * @author : yls
     * @date : 2020/5/19 10:53
     * @param
     * @return : void
     */
    public void msckPartitionsSouceTab() {
        String sql = "msck repair table\n"
                + realTimeDataPOJO.getSourceDatabase()+"."+ realTimeDataPOJO.getSourceTable();
        hiveServiceImpl.uploadDataByHive(sql);
    }

    /**
     * 功能描述:
     * 〈上传文件到hdfs〉
     *
     * @author : yls
     * @date : 2020/5/18 15:45
     * @param
     * @return : void
     */
    public void uploadFileToHdfs(String partitionField) throws Exception {
        //3.1 检查文件夹是否存在
        String uplodaFolder = hadoopConf.getUplodaFolder();
        if (!uplodaFolder.endsWith("/")){
            uplodaFolder = uplodaFolder + "/";
        }
        uplodaFolder = uplodaFolder;
        boolean exist = HdfsClientUtils.exist(uplodaFolder);
        if (!exist){
            HdfsClientUtils.mkdirs(uplodaFolder);
        }


        String temp = uplodaFolder + partitionField +"="+ fileFolderPOJO.getPartition();
        LOGGER.info("删除临时路径："+temp);
        boolean exist1 = HdfsClientUtils.exist(temp);
        if (exist1){
            HdfsClientUtils.delete(temp,true);
        }

        //3.2 开始上传文件
        String sourceFolderPath = FileScanUtils.checkFolderPath(fileFolderPOJO.getTargetFolder());
        sourceFolderPath = sourceFolderPath +partitionField+"="+ fileFolderPOJO.getPartition();

        File source = new File(sourceFolderPath);
        if (!source.exists()){
            source.mkdirs();
        }

        //上传数据
        HdfsClientUtils.copyFromLocalFile(sourceFolderPath,
                uplodaFolder,true);
    }


    /**
     * 功能描述:
     * 〈设置扫描规则,只扫描前一天处理的文件〉
     *
     * @author : yls
     * @date : 2020/5/18 14:38
     * @param
     * @return : void
     */
    public void setScanFilter() {
        Date currentTime = DateUtils.getCurrentTime();
        Date date = DateUtils.subtractDate(currentTime, 1);
        int year = DateUtils.getYear(date);
        int month = DateUtils.getMonth(date);
        int day = DateUtils.getDay(date);

        fileFolderPOJO.setYear(year+"");
        fileFolderPOJO.setMonth(month+"");
        fileFolderPOJO.setDay(day+"");
        fileFolderPOJO.setPartition(year+""+month+""+day);

        String targetFolder = fileFolderPOJO.getTargetFolder();
        targetFolder = FileScanUtils.checkFolderPath(targetFolder);
    }


}
