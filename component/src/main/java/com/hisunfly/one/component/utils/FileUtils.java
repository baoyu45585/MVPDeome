//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.hisunfly.one.component.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.hisunfly.one.component.app.AppModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

public class FileUtils {
    private static final String LINE_SEP = System.getProperty("line.separator");
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public FileUtils() {
    }

    public static File getFileByPath(String filePath) {
        return isSpace(filePath) ? null : new File(filePath);
    }

    public static boolean isFileExist(String filePath) {
        return isFileExist(getFileByPath(filePath));
    }

    public static boolean isFileExist(File file) {
        return file != null && file.exists();
    }

    public static boolean rename(String filePath, String newName) {
        return rename(getFileByPath(filePath), newName);
    }

    public static boolean rename(File file, String newName) {
        if (file == null) {
            return false;
        } else if (!file.exists()) {
            return false;
        } else if (isSpace(newName)) {
            return false;
        } else if (newName.equals(file.getName())) {
            return true;
        } else {
            File newFile = new File(file.getParent() + File.separator + newName);
            return !newFile.exists() && file.renameTo(newFile);
        }
    }

    public static boolean isDir(String dirPath) {
        return isDir(getFileByPath(dirPath));
    }

    public static boolean isDir(File file) {
        return file != null && file.exists() && file.isDirectory();
    }

    public static boolean isFile(String filePath) {
        return isFile(getFileByPath(filePath));
    }

    public static boolean isFile(File file) {
        return file != null && file.exists() && file.isFile();
    }

    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    public static boolean createOrExistsDir(File file) {
        boolean var10000;
        label25: {
            if (file != null) {
                if (file.exists()) {
                    if (file.isDirectory()) {
                        break label25;
                    }
                } else if (file.mkdirs()) {
                    break label25;
                }
            }

            var10000 = false;
            return var10000;
        }

        var10000 = true;
        return var10000;
    }

    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    public static boolean createOrExistsFile(File file) {
        if (file == null) {
            return false;
        } else if (file.exists()) {
            return file.isFile();
        } else if (!createOrExistsDir(file.getParentFile())) {
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException var2) {
                var2.printStackTrace();
                return false;
            }
        }
    }

    public static boolean createFileByDeleteOldFile(String filePath) {
        return createFileByDeleteOldFile(getFileByPath(filePath));
    }

    public static boolean createFileByDeleteOldFile(File file) {
        if (file == null) {
            return false;
        } else if (file.exists() && !file.delete()) {
            return false;
        } else if (!createOrExistsDir(file.getParentFile())) {
            return false;
        } else {
            try {
                return file.createNewFile();
            } catch (IOException var2) {
                var2.printStackTrace();
                return false;
            }
        }
    }

    public static boolean delete(String filePath) {
        return delete(getFileByPath(filePath));
    }

    public static boolean delete(File file) {
        if (file == null) {
            return false;
        } else {
            return file.isDirectory() ? deleteDir(file) : deleteFile(file);
        }
    }

    public static boolean deleteDir(String dirPath) {
        return deleteDir(getFileByPath(dirPath));
    }

    public static boolean deleteDir(File dir) {
        if (dir == null) {
            return false;
        } else if (!dir.exists()) {
            return true;
        } else if (!dir.isDirectory()) {
            return false;
        } else {
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                File[] var2 = files;
                int var3 = files.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File file = var2[var4];
                    if (file.isFile()) {
                        if (!file.delete()) {
                            return false;
                        }
                    } else if (file.isDirectory() && !deleteDir(file)) {
                        return false;
                    }
                }
            }

            return dir.delete();
        }
    }

    public static boolean deleteFile(String srcFilePath) {
        return deleteFile(getFileByPath(srcFilePath));
    }

    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    public static void deleteFileOrDir(String filePath) throws Exception {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] childs = file.listFiles();
                File[] var3 = childs;
                int var4 = childs.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    File item = var3[var5];
                    if (item.isFile()) {
                        item.delete();
                    } else if (item.isDirectory()) {
                        deleteFileOrDir(item.getAbsolutePath());
                    }
                }

                file.delete();
            } else if (file.isFile()) {
                file.delete();
            }
        }

    }

    public static void deleteFileOrDir(File file) throws Exception {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                File[] childs = file.listFiles();
                File[] var2 = childs;
                int var3 = childs.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    File item = var2[var4];
                    if (item.isFile()) {
                        item.delete();
                    } else if (item.isDirectory()) {
                        deleteFileOrDir(item);
                    }
                }

                file.delete();
            } else if (file.isFile()) {
                file.delete();
            }
        }

    }

    public static void deleteChildFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] childs = file.listFiles();
                File[] var3 = childs;
                int var4 = childs.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    File item = var3[var5];
                    if (item.isFile()) {
                        item.delete();
                    }
                }
            } else if (file.isFile()) {
                file.delete();
            }
        }

    }

    public static boolean deleteAllInDir(String dirPath) {
        return deleteAllInDir(getFileByPath(dirPath));
    }

    public static boolean deleteAllInDir(File dir) {
        return deleteFilesInDirWithFilter(dir, new FileFilter() {
            public boolean accept(File pathname) {
                return true;
            }
        });
    }

    public static boolean deleteFilesInDir(String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    public static boolean deleteFilesInDir(File dir) {
        return deleteFilesInDirWithFilter(dir, new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });
    }

    public static boolean deleteFilesInDirWithFilter(String dirPath, FileFilter filter) {
        return deleteFilesInDirWithFilter(getFileByPath(dirPath), filter);
    }

    public static boolean deleteFilesInDirWithFilter(File dir, FileFilter filter) {
        if (dir == null) {
            return false;
        } else if (!dir.exists()) {
            return true;
        } else if (!dir.isDirectory()) {
            return false;
        } else {
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                File[] var3 = files;
                int var4 = files.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    File file = var3[var5];
                    if (filter.accept(file)) {
                        if (file.isFile()) {
                            if (!file.delete()) {
                                return false;
                            }
                        } else if (file.isDirectory() && !deleteDir(file)) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    public static List<File> listFilesInDir(String dirPath) {
        return listFilesInDir(dirPath, false);
    }

    public static List<File> listFilesInDir(File dir) {
        return listFilesInDir(dir, false);
    }

    public static List<File> listFilesInDir(String dirPath, boolean isRecursive) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive);
    }

    public static List<File> listFilesInDir(File dir, boolean isRecursive) {
        return listFilesInDirWithFilter(dir, new FileFilter() {
            public boolean accept(File pathname) {
                return true;
            }
        }, isRecursive);
    }

    public static List<File> listFilesInDirWithFilter(String dirPath, FileFilter filter) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, false);
    }

    public static List<File> listFilesInDirWithFilter(File dir, FileFilter filter) {
        return listFilesInDirWithFilter(dir, filter, false);
    }

    public static List<File> listFilesInDirWithFilter(String dirPath, FileFilter filter, boolean isRecursive) {
        return listFilesInDirWithFilter(getFileByPath(dirPath), filter, isRecursive);
    }

    public static List<File> listFilesInDirWithFilter(File dir, FileFilter filter, boolean isRecursive) {
        if (!isDir(dir)) {
            return null;
        } else {
            List<File> list = new ArrayList();
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                File[] var5 = files;
                int var6 = files.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    File file = var5[var7];
                    if (filter.accept(file)) {
                        list.add(file);
                    }

                    if (isRecursive && file.isDirectory()) {
                        list.addAll(listFilesInDirWithFilter(file, filter, true));
                    }
                }
            }

            return list;
        }
    }

    public static long getFileLastModified(String filePath) {
        return getFileLastModified(getFileByPath(filePath));
    }

    public static long getFileLastModified(File file) {
        return file == null ? -1L : file.lastModified();
    }

    public static String getFileCharsetSimple(String filePath) {
        return getFileCharsetSimple(getFileByPath(filePath));
    }

    public static String getFileCharsetSimple(File file) {
        int p = 0;
        BufferedInputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(file));
            p = (is.read() << 8) + is.read();
        } catch (IOException var12) {
            var12.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException var11) {
                var11.printStackTrace();
            }

        }

        switch(p) {
        case 61371:
            return "UTF-8";
        case 65279:
            return "UTF-16BE";
        case 65534:
            return "Unicode";
        default:
            return "GBK";
        }
    }

    public static int getFileLines(String filePath) {
        return getFileLines(getFileByPath(filePath));
    }

    public static int getFileLines(File file) {
        int count = 1;
        BufferedInputStream is = null;

        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[1024];
            int readChars;
            int i;
            if (LINE_SEP.endsWith("\n")) {
                while((readChars = is.read(buffer, 0, 1024)) != -1) {
                    for(i = 0; i < readChars; ++i) {
                        if (buffer[i] == 10) {
                            ++count;
                        }
                    }
                }
            } else {
                while((readChars = is.read(buffer, 0, 1024)) != -1) {
                    for(i = 0; i < readChars; ++i) {
                        if (buffer[i] == 13) {
                            ++count;
                        }
                    }
                }
            }
        } catch (IOException var14) {
            var14.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException var13) {
                var13.printStackTrace();
            }

        }

        return count;
    }

    public static long getDirSize(String dirPath) {
        return getDirSize(getFileByPath(dirPath));
    }

    public static long getDirSize(File dir) {
        return getDirLength(dir);
    }

    public static long getFileSize(String filePath) {
        return getFileLength(filePath);
    }

    public static long getFileSize(File file) {
        return getFileLength(file);
    }

    public static long getFileOrDirSizeLong(String filePath) {
        File file = new File(filePath);
        long blockSize = 0L;

        try {
            if (file.isDirectory()) {
                blockSize = getDirSize(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }

        return blockSize;
    }

    public static long getDirLength(String dirPath) {
        return getDirLength(getFileByPath(dirPath));
    }

    public static long getDirLength(File dir) {
        if (!isDir(dir)) {
            return -1L;
        } else {
            long len = 0L;
            File[] files = dir.listFiles();
            if (files != null && files.length != 0) {
                File[] var4 = files;
                int var5 = files.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    if (file.isDirectory()) {
                        len += getDirLength(file);
                    } else {
                        len += file.length();
                    }
                }
            }

            return len;
        }
    }

    public static long getFileLength(String filePath) {
        boolean isURL = filePath.matches("[a-zA-z]+://[^\\s]*");
        if (isURL) {
            try {
                HttpsURLConnection conn = (HttpsURLConnection)(new URL(filePath)).openConnection();
                conn.setRequestProperty("Accept-Encoding", "identity");
                conn.connect();
                if (conn.getResponseCode() == 200) {
                    return (long)conn.getContentLength();
                }

                return -1L;
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        }

        return getFileLength(getFileByPath(filePath));
    }

    public static long getFileLength(File file) {
        return !isFile(file) ? -1L : file.length();
    }

    public static String getFileMD5ToString(String filePath) {
        File file = isSpace(filePath) ? null : new File(filePath);
        return getFileMD5ToString(file);
    }

    public static String getFileMD5ToString(File file) {
        return bytes2HexString(getFileMD5(file));
    }

    public static byte[] getFileMD5(String filePath) {
        return getFileMD5(getFileByPath(filePath));
    }

    public static byte[] getFileMD5(File file) {
        if (file == null) {
            return null;
        } else {
            DigestInputStream dis = null;

            try {
                FileInputStream fis = new FileInputStream(file);
                MessageDigest md = MessageDigest.getInstance("MD5");
                dis = new DigestInputStream(fis, md);
                byte[] buffer = new byte[262144];

                while(dis.read(buffer) > 0) {
                    ;
                }

                md = dis.getMessageDigest();
                byte[] var5 = md.digest();
                return var5;
            } catch (IOException | NoSuchAlgorithmException var15) {
                var15.printStackTrace();
            } finally {
                try {
                    if (dis != null) {
                        dis.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

            }

            return null;
        }
    }

    public static String getDirName(File file) {
        return file == null ? "" : getDirName(file.getAbsolutePath());
    }

    public static String getDirName(String filePath) {
        if (isSpace(filePath)) {
            return "";
        } else {
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1 ? "" : filePath.substring(0, lastSep + 1);
        }
    }

    public static String getFileName(File file) {
        return file == null ? "" : getFileName(file.getAbsolutePath());
    }

    public static String getFileName(String filePath) {
        if (isSpace(filePath)) {
            return "";
        } else {
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastSep == -1 ? filePath : filePath.substring(lastSep + 1);
        }
    }

    public static String getFileNameNoExtension(File file) {
        return file == null ? "" : getFileNameNoExtension(file.getPath());
    }

    public static String getFileNameNoExtension(String filePath) {
        if (isSpace(filePath)) {
            return "";
        } else {
            int lastPoi = filePath.lastIndexOf(46);
            int lastSep = filePath.lastIndexOf(File.separator);
            if (lastSep == -1) {
                return lastPoi == -1 ? filePath : filePath.substring(0, lastPoi);
            } else {
                return lastPoi != -1 && lastSep <= lastPoi ? filePath.substring(lastSep + 1, lastPoi) : filePath.substring(lastSep + 1);
            }
        }
    }

    public static String getFileExtension(File file) {
        return file == null ? "" : getFileExtension(file.getPath());
    }

    public static String getFileExtension(String filePath) {
        if (isSpace(filePath)) {
            return "";
        } else {
            int lastPoi = filePath.lastIndexOf(46);
            int lastSep = filePath.lastIndexOf(File.separator);
            return lastPoi != -1 && lastSep < lastPoi ? filePath.substring(lastPoi + 1) : "";
        }
    }

    private static String bytes2HexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        } else {
            int len = bytes.length;
            if (len <= 0) {
                return "";
            } else {
                char[] ret = new char[len << 1];
                int i = 0;

                for(int var4 = 0; i < len; ++i) {
                    ret[var4++] = HEX_DIGITS[bytes[i] >> 4 & 15];
                    ret[var4++] = HEX_DIGITS[bytes[i] & 15];
                }

                return new String(ret);
            }
        }
    }

    private static boolean isSpace(String s) {
        if (s == null) {
            return true;
        } else {
            int i = 0;

            for(int len = s.length(); i < len; ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    private static boolean writeFileFromIS(File file, InputStream is) {
        BufferedOutputStream os = null;

        boolean var4;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            byte[] data = new byte[8192];

            int len;
            while((len = is.read(data, 0, 8192)) != -1) {
                os.write(data, 0, len);
            }

            boolean var5 = true;
            return var5;
        } catch (IOException var19) {
            var19.printStackTrace();
            var4 = false;
        } finally {
            try {
                is.close();
            } catch (IOException var18) {
                var18.printStackTrace();
            }

            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException var17) {
                var17.printStackTrace();
            }

        }

        return var4;
    }

    public static boolean writeToFile(InputStream is, File file) {
        if (is != null && file != null) {
            if (file.exists()) {
                file.delete();
            }

            BufferedInputStream bis = null;
            FileOutputStream fos = null;

            try {
                bis = new BufferedInputStream(is, 8192);
                fos = new FileOutputStream(file, false);
                byte[] buff = new byte[8192];

                int readLen;
                while((readLen = bis.read(buff)) != -1) {
                    fos.write(buff, 0, readLen);
                }
            } catch (IOException var18) {
                var18.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException var17) {
                        var17.printStackTrace();
                    }
                }

                if (fos != null) {
                    try {
                        fos.flush();
                        fos.close();
                    } catch (IOException var16) {
                        var16.printStackTrace();
                    }
                }

            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean createFile(String path, String fileName) {
        try {
            createDir(path);
            File jf = new File(path + File.separator + fileName);
            if (jf.exists()) {
                return true;
            }

            jf.createNewFile();
            return false;
        } catch (IOException var3) {
            var3.printStackTrace();
        } catch (NullPointerException var4) {
            var4.printStackTrace();
        }

        return false;
    }

    public static void createDir(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    public static String readFileContent(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        if (!TextUtils.isEmpty(filePath) && isFileExist(filePath)) {
            try {
                File jsonFile = new File(filePath);
                BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile)));

                String line;
                while((line = bf.readLine()) != null) {
                    stringBuilder.append(line);
                }

                bf.close();
            } catch (IOException var5) {
                var5.printStackTrace();
                return "";
            } catch (NullPointerException var6) {
                var6.printStackTrace();
            }

            return stringBuilder.toString();
        } else {
            return "";
        }
    }

    public static void writeFileContent(String filePath, String content) {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            writer.close();
        } catch (IOException var3) {
            var3.printStackTrace();
        } catch (NullPointerException var4) {
            var4.printStackTrace();
        }

    }

    public static boolean copyFile(File src, File dst) {
        try {
            FileInputStream inStream = new FileInputStream(src);
            FileOutputStream outStream = new FileOutputStream(dst);
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            inChannel.transferTo(0L, inChannel.size(), outChannel);
            inStream.close();
            outStream.close();
            inChannel.close();
            outChannel.close();
            return true;
        } catch (IOException var6) {
            var6.printStackTrace();
        } catch (NullPointerException var7) {
            var7.printStackTrace();
        }

        return false;
    }

    public static String getExternalMaterialFilesPath(Context context) {
        String pathdir = null;
        File dataDir = AppModel.getApplicationContext().getExternalFilesDir((String)null);
        if (dataDir != null) {
            pathdir = dataDir.getAbsolutePath();
        }

        return pathdir;
    }

    public interface OnReplaceListener {
        boolean onReplace();
    }
}
