package com.fengtu.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: yls
 * @Date: 2020/5/18 14:15
 * @Description:
 * @Version 1.0
 */
@Configuration
public class FileFolderPOJO {

    @Value("${copy.last.day.sourcefolder}")
    private String sourceFolder;

    @Value("${copy.last.day.targetfolder}")
    private String targetFolder;

    //年
    private String year;

    //yue
    private String month;

    //日
    private String day;

    //分区
    private String partition;

    public String getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public String getTargetFolder() {
        return targetFolder;
    }

    public void setTargetFolder(String targetFolder) {
        this.targetFolder = targetFolder;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    @Override
    public String toString() {
        return "FileFolderPOJO{" +
                "sourceFolder='" + sourceFolder + '\'' +
                ", targetFolder='" + targetFolder + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", partition='" + partition + '\'' +
                '}';
    }
}
