package com.yitong.ChuangKeYuan.utils;

/**
 * Created by Say GoBay on 2016/8/9.
 * <p/>
 * 魅族手机获取Sd卡所有文件工具类
 */

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /**
     * 遍历指定文件夹下的资源文件
     *
     * @param folder 文件
     */
    static List<String> list;
    static int tag = 0;

    public static void setList(List<String> list) {
        Utils.list = list;
    }

    public static List<String> simpleScanning(File folder, int isDirectory, int isFile) {

        //指定正则表达式
        Pattern mPattern = Pattern.compile("([^\\.]*)\\.([^\\.]*)");
        // 当前目录下的所有文件
        final String[] filenames = folder.list();
        // 当前目录的名称
        //final String folderName = folder.getName();
        // 当前目录的绝对路径
        //final String folderPath = folder.getAbsolutePath();
        if (filenames != null) {
            // 遍历当前目录下的所有文件
            for (String name : filenames) {
                File file = new File(folder, name);
                // 如果是文件夹则继续递归当前方法
                if (isDirectory == 1) {
//                    System.out.println("这是个文件夹");
//                    System.out.println(""+tag++);
                    Matcher matcher = mPattern.matcher(name);
                    if (matcher.matches()) {
                        // 文件名称
                        String fileName = matcher.group(1);
                        // 文件后缀
                        String fileExtension = matcher.group(2);
                        // 文件路径
                        String filePath = file.getAbsolutePath();
//                        if (Utils.isMusic(fileExtension)) {
//                            // 初始化音乐文件......................
//                            System.out.println("This file is Music File,fileName="+fileName+"."
//                                    +fileExtension+",filePath="+filePath);
//                        }
//                        if (Utils.isPhoto(fileExtension)) {
//                            // 初始化图片文件......................
//                            System.out.println("This file is Photo File,fileName="+fileName+"."
//                                    +fileExtension+",filePath="+filePath);
//                        }
                        if (isFile == 0) {
                            if (Utils.isVideo(fileExtension)) {
                                // 初始化视频文件......................
//                                System.out.println("这是所有的视频文件=" + filePath);
//                            System.out.println("This file is Video File,fileName="+fileName+"."
//                                    +fileExtension+",filePath="+filePath);
                                list.add(filePath);
                            }
                        } else {
                            if (Utils.isFile(fileExtension)) {
                                // 初始化文件......................
//                                System.out.println("这是所有的文件=" + filePath);
//                            System.out.println("This file is Video File,fileName="+fileName+"."
//                                    +fileExtension+",filePath="+filePath);
                                list.add(filePath);
                            }
                        }
                    }
                } else {
                    if (file.isDirectory()) {
                        simpleScanning(file, 0, isFile);
                    }
                    // 如果是文件则对文件进行相关操作
                    else {
                        Matcher matcher = mPattern.matcher(name);
                        if (matcher.matches()) {
                            // 文件名称
                            String fileName = matcher.group(1);
                            // 文件后缀
                            String fileExtension = matcher.group(2);
                            // 文件路径
                            String filePath = file.getAbsolutePath();
//                        if (Utils.isMusic(fileExtension)) {
//                            // 初始化音乐文件......................
//                            System.out.println("This file is Music File,fileName="+fileName+"."
//                                    +fileExtension+",filePath="+filePath);
//                        }
//                        if (Utils.isPhoto(fileExtension)) {
//                            // 初始化图片文件......................
//                            System.out.println("This file is Photo File,fileName="+fileName+"."
//                                    +fileExtension+",filePath="+filePath);
//                        }
                            if (isFile == 0) {
                                if (Utils.isVideo(fileExtension)) {
                                    // 初始化视频文件......................
                                    System.out.println("这是所有的视频文件=" + filePath);
                                    list.add(filePath);
                                }
                            } else {
                                if (Utils.isFile(fileExtension)) {
                                    // 初始化文件......................
                                    System.out.println("这是所有的文件=" + filePath);
                                    list.add(filePath);
                                }
                            }
                        }
                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }

    /**
     * 判断是否是音乐文件
     *
     * @param extension 后缀名
     * @return
     */
    public static boolean isMusic(String extension) {
        if (extension == null)
            return false;

        final String ext = extension.toLowerCase();
        if (ext.equals("mp3") || ext.equals("m4a") || ext.equals("wav") || ext.equals("amr") || ext.equals("awb") ||
                ext.equals("aac") || ext.equals("flac") || ext.equals("mid") || ext.equals("midi") ||
                ext.equals("xmf") || ext.equals("rtttl") || ext.equals("rtx") || ext.equals("ota") ||
                ext.equals("wma") || ext.equals("ra") || ext.equals("mka") || ext.equals("m3u") || ext.equals("pls")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是图像文件
     *
     * @param extension 后缀名
     * @return
     */
    public static boolean isPhoto(String extension) {
        if (extension == null)
            return false;

        final String ext = extension.toLowerCase();
        if (ext.endsWith("jpg") || ext.endsWith("jpeg") || ext.endsWith("gif") || ext.endsWith("png") ||
                ext.endsWith("bmp") || ext.endsWith("wbmp")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是视频文件
     *
     * @param extension 后缀名
     * @return
     */
    public static boolean isVideo(String extension) {
        if (extension == null)
            return false;

        final String ext = extension.toLowerCase();
        if (ext.endsWith("mpeg") || ext.endsWith("mp4") || ext.endsWith("mov") || ext.endsWith("m4v") ||
                ext.endsWith("3gp") || ext.endsWith("3gpp") || ext.endsWith("3g2") ||
                ext.endsWith("3gpp2") || ext.endsWith("avi") || ext.endsWith("divx") ||
                ext.endsWith("wmv") || ext.endsWith("asf") || ext.endsWith("flv") ||
                ext.endsWith("mkv") || ext.endsWith("mpg") || ext.endsWith("rmvb") ||
                ext.endsWith("rm") || ext.endsWith("vob") || ext.endsWith("f4v") ||
                ext.endsWith("MPEG") || ext.endsWith("MP4") || ext.endsWith("MOV") || ext.endsWith("M4V") ||
                ext.endsWith("3GP") || ext.endsWith("3GPP") || ext.endsWith("3G2") ||
                ext.endsWith("3GPP2") || ext.endsWith("AVI") || ext.endsWith("DIVX") ||
                ext.endsWith("WMV") || ext.endsWith("ASF") || ext.endsWith("FLV") ||
                ext.endsWith("MKV") || ext.endsWith("MPG") || ext.endsWith("RMVB") ||
                ext.endsWith("RM") || ext.endsWith("VOB") || ext.endsWith("F4V")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是文件
     *
     * @param extension 后缀名
     * @return
     */
    public static boolean isFile(String extension) {
        if (extension == null)
            return false;
        final String ext = extension.toLowerCase();
        if (ext.endsWith("txt") || ext.endsWith("doc") || ext.endsWith("docx") ||
                ext.endsWith("wps") || ext.endsWith("wpt") ||
                ext.endsWith("ppt") || ext.endsWith("pptx") ||
                ext.endsWith("xls") || ext.endsWith("xlsx") ||
                ext.endsWith("TXT") || ext.endsWith("DOC") || ext.endsWith("DOCX") ||
                ext.endsWith("WPS") || ext.endsWith("WPT") ||
                ext.endsWith("PPT") || ext.endsWith("PPTX") ||
                ext.endsWith("XLS") || ext.endsWith("XLSX") || ext.endsWith("pdf") || ext.endsWith("PDF")) {
            return true;
        }
        return false;
    }


}
