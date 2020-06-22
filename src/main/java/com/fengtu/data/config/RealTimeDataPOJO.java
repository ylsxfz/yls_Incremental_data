package com.fengtu.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: yls
 * @Date: 2020/5/19 10:14
 * @Description: hive执行任务的相关数据库、表、参数
 * @Version 1.0
 */
@Configuration
public class RealTimeDataPOJO {
    //源数据库
    @Value("${realtime.data.process.source-database}")
    private String sourceDatabase;

    //源文件上传映射的外部关联表
    @Value("${realtime.data.process.source-table}")
    private String sourceTable;

    //源数据转换为列式存储的数据表
    @Value("${realtime.data.process.source-parquet-table}")
    private String sourceParquetTable;

    //结果集数据库
    @Value("${realtime.data.process.target-database}")
    private String targetDatabase;

    //结果集数据表
    @Value("${realtime.data.process.target-table}")
    private String targetTable;

    //分区字段
    @Value("${realtime.data.process.partition-field}")
    private String partitionField;


    public String getSourceDatabase() {
        return sourceDatabase;
    }

    public void setSourceDatabase(String sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSourceParquetTable() {
        return sourceParquetTable;
    }

    public void setSourceParquetTable(String sourceParquetTable) {
        this.sourceParquetTable = sourceParquetTable;
    }

    public String getTargetDatabase() {
        return targetDatabase;
    }

    public void setTargetDatabase(String targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getPartitionField() {
        return partitionField;
    }

    public void setPartitionField(String partitionField) {
        this.partitionField = partitionField;
    }

    @Override
    public String toString() {
        return "RealTimeDataPOJO{" +
                "sourceDatabase='" + sourceDatabase + '\'' +
                ", sourceTable='" + sourceTable + '\'' +
                ", sourceParquetTable='" + sourceParquetTable + '\'' +
                ", targetDatabase='" + targetDatabase + '\'' +
                ", targetTable='" + targetTable + '\'' +
                ", partitionField='" + partitionField + '\'' +
                '}';
    }
}
