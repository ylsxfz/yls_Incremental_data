package com.fengtu.data.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: yls
 * @Date: 2020/8/20 9:37
 * @Description: hvie导出配置类
 * @Version 1.0
 */
@Configuration
public class DownByHivePOJO {
    /**
     * hive导出的目标目录
     */
    @Value("${hive.down.targetfolder}")
    private String sourceFolder;

    /**
     * hive导出时的查询sql
     */
    @Value(("${hive.down.sql.search}"))
    private String searchSql;



    /**
     * hive导出时的生成文件sql
     */
    @Value(("${hive.down.sql.download}"))
    private String downloadSql;

    public String getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public String getSearchSql() {
        return searchSql;
    }

    public void setSearchSql(String searchSql) {
        this.searchSql = searchSql;
    }

    public String getDownloadSql() {
        return downloadSql;
    }

    public void setDownloadSql(String downloadSql) {
        this.downloadSql = downloadSql;
    }

    @Override
    public String toString() {
        return "DownByHivePOJO{" +
                "sourceFolder='" + sourceFolder + '\'' +
                ", searchSql='" + searchSql + '\'' +
                ", downloadSql='" + downloadSql + '\'' +
                '}';
    }
}
