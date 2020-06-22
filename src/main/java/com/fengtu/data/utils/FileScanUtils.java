package com.fengtu.data.utils;

import com.fengtu.data.config.FileFolderPOJO;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: yls
 * @Date: 2020/5/18 12:54
 * @Description:
 * @Version 1.0
 */
public class FileScanUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileScanUtils.class);

    public static void main(String[] args) {
        String sourceFolder = "E:\\uploaFileTemp\\fengtu";
        String backupFolder = "E:\\uploaFileTemp\\fengtu_bakup\\";
        //copyOneDayFiles(sourceFolder, backupFolder);

    }

    /**
     * 功能描述:
     * 〈拷贝前一天的数据〉
     *
     * @author : yls
     * @date : 2020/5/18 14:12
     * @return : void
     */
    public static void copyOneDayFiles(FileFolderPOJO fileFolderPOJO) {
        File sourceFile = new File(fileFolderPOJO.getSourceFolder());
        File backUpFile = new File(fileFolderPOJO.getTargetFolder());
        if (!backUpFile.exists() || backUpFile.isFile()){
            backUpFile.mkdirs();
        }
        List<File> oneLevelFolder = getOneLevelFolder(sourceFile);
        oneLevelFolder.forEach(folder -> {
            LOGGER.info("开始处理："+folder.toString());
            System.out.println("开始处理："+folder.toString());
            File oneDayFolder= scanOneDayFiles(folder,fileFolderPOJO);
            boolean folderNull = isFolderNull(oneDayFolder);
            if (folderNull){
                return;
            }else {
                try {
                    copyFile(oneDayFolder,
                            fileFolderPOJO.getTargetFolder()
                                    +"action_date="
                                    +fileFolderPOJO.getYear()
                                    +fileFolderPOJO.getMonth()
                                    +fileFolderPOJO.getDay());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }


    /**
     * 功能描述:
     * 〈复制文件〉
     *
     * @author : yls
     * @date : 2020/5/18 13:53
     * @param sourceFolder 源文件目录
     * @param targetFolder 拷贝的目标目录
     * @return : void
     */
    public static void copyFile(File sourceFolder,String targetFolder) throws Exception {
        targetFolder = checkFolderPath(targetFolder);
        File[] listFiles = sourceFolder.listFiles();
        for (File file:
             listFiles) {
            System.out.println(file.getName());
            File targetFile = new File(targetFolder+file.getName());
            FileUtils.copyFile(file,targetFile);
        }
    }

    /**
     * 功能描述:
     * 〈复制文件到指定文件夹〉
     *
     * @author : yls
     * @date : 2020/5/18 13:30
     * @param folder 扫描的文件夹
     * @return : void
     */
    public static File scanOneDayFiles(File folder,FileFolderPOJO fileFolderPOJO){
        //1、获取当天时间
        String year = fileFolderPOJO.getYear();
        String month = fileFolderPOJO.getMonth();
        String day = fileFolderPOJO.getDay();
        File yearFolder = getOneLevelFolderByFilter(folder, year);
        boolean folderNull = isFolderNull(yearFolder);
        if (folderNull){
            return null;
        }
        File monthFolder = getOneLevelFolderByFilter(yearFolder, month);
        folderNull = isFolderNull(monthFolder);
        if (folderNull){
            return null;
        }
        File dayFolder = getOneLevelFolderByFilter(monthFolder, day);
        return dayFolder;
    }



    /**
     * 功能描述:
     * 〈如果文件夹为空，记录日志〉
     *
     * @author : yls
     * @date : 2020/5/18 13:48
     * @param file 需要判断的文件夹
     * @return : boolean
     */
    public static boolean isFolderNull(File file){
        if (file==null){
            LOGGER.info("当前文件夹未找到符合要求的数据。");
            return true;
        }
        return false;
    }

    /**
     * 功能描述:
     * 〈判断文件路径是否合法〉
     *
     * @author : yls
     * @date : 2020/5/18 14:56
     * @param folder
     * @return : java.lang.String
     */
    public static String checkFolderPath(String folder){
        if (!folder.endsWith("\\")){
            return folder+"\\";
        }
        return folder;
    }

    /**
     * 功能描述:
     * 〈扫描一级目录〉
     *
     * @author : yls
     * @date : 2020/5/18 12:55
     * @param sourceFile 扫描的源文件路径
     * @return : java.util.List<java.io.File>
     */
    public static List<File> getOneLevelFolder(File sourceFile){
        List<File> oneLevelFolders = new ArrayList<>();
        File[] listFiles = sourceFile.listFiles();
        for (File file:
             listFiles) {
            if (file.isDirectory()) {
                oneLevelFolders.add(file);
            }
        }
        return oneLevelFolders;
    }

    /**
     * 功能描述:
     * 〈扫描一级目录，带过滤器，模糊匹配（包含）〉
     *
     * @author : yls
     * @date : 2020/5/18 13:24
     * @param sourceFile
     * @param filter
     * @return : java.io.File
     */
    public static File getOneLevelFolderByFilter(File sourceFile,String filter){
        File[] listFiles = sourceFile.listFiles();
        for (File file:
                listFiles) {
            if (file.isDirectory() && file.getName().contains(filter)) {
                return file;
            }
        }
        return null;
    }

}
