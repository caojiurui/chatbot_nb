package pers.cjr.chatbot.nb.biz.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class FileUtil {
    private static Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 采用jdk自带的ZipInputStream解压zip文件，不支持中文文件名条目（utf8编码可能可以，没测试）
     * 结果 toDir/压缩包内文件 (不包含zip文件名)
     *
     * @param zipFile
     * @param toDir
     */
    public static void unzip(File zipFile, String toDir) throws Exception {
        if (toDir == null) {
            toDir = ".";
        }
        ZipFile zf = null;
        ZipInputStream zis = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            zf = new ZipFile(zipFile);
            zis = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                File outFile = new File(toDir + File.separator + entry.getName());
                File outDir = outFile.getParentFile();
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }
                if (entry.isDirectory()) {
                    outFile.mkdir();
                    continue;
                }
                bis = new BufferedInputStream(zf.getInputStream(entry));
                bos = new BufferedOutputStream(new FileOutputStream(outFile));
                byte[] b = new byte[1024 * 4];
                int len = -1;
                while ((len = bis.read(b)) != -1) {
                    bos.write(b, 0, len);
                }
                bos.flush();
            }
            log.debug("解压文件{}到{}", zipFile.getAbsolutePath(), toDir);
        } catch (Exception e) {
            log.error("解压文件{}到{}出错:{}", zipFile.getAbsolutePath(), toDir, e.getMessage());
            throw e;
        } finally {
            close(bos, bis, zis, zf);
        }
    }

    /**
     * 采用jdk自带的ZipOutputStream打包zip文件，不支持中文文件名条目（utf8编码可能可以，没测试），不包含srcdir
     *
     * @param srcdir
     * @param destdir
     * @param zipName 如果为null，用srcdir的文件夹名称作为zip名字
     * @return
     * @throws Exception
     */
    public static File zip(String srcdir, String destdir, String zipName) throws Exception {
        return zip(srcdir, destdir, zipName, false);
    }

    /**
     * 采用jdk自带的ZipOutputStream打包zip文件，不支持中文文件名条目（utf8编码可能可以，没测试）
     *
     * @param srcdir
     * @param destdir
     * @param zipName
     * @param containSrcDir true从srcdir开始打包 false从srcdir内部文件开始打包
     * @return
     * @throws Exception
     */
    public static File zip(String srcdir, String destdir, String zipName, boolean containSrcDir) throws Exception {
        File dest = new File(destdir);
        if (!dest.exists()) {
            dest.mkdirs();
        }
        File src = new File(srcdir);
        File zip = new File(dest.getPath() + File.separator + (zipName == null ? src.getName() + ".zip" : zipName));
        if (zip.exists()) {
            zip.delete();
        }
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip)));
            pack(src, zos, containSrcDir ? "" : null);
            log.debug("打包文件{}到{}", srcdir, zip.getAbsolutePath());
        } catch (Exception e) {
            log.error("打包文件{}失败", zip.getAbsolutePath());
            throw e;
        } finally {
            close(zos);
        }
        return zip;
    }

    private static void pack(File file, ZipOutputStream zos, String pdir) throws Exception {
        if (file.isDirectory()) {
            if (pdir != null) {
//				zos.putNextEntry(new ZipEntry(pdir + file.getName() + "/"));
//				try {zos.closeEntry();} catch (IOException e) {}
                addZipDir(zos, pdir + file.getName() + "/");
            }
            File[] files = file.listFiles();
            for (File f : files) {
                pack(f, zos, pdir == null ? "" : pdir + file.getName() + "/");
            }
        } else {
//			byte[] b = new byte[1024 * 4];
//			zos.putNextEntry(new ZipEntry(pdir + file.getName()));
//			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
//			int len;
//			while ((len = bis.read(b)) != -1) {
//				zos.write(b, 0, len);
//			}
//			zos.flush();
//			try {zos.closeEntry();} catch (IOException e) {}
//			close(bis);
            addZipFile(zos, pdir + file.getName(), file);
        }
    }

    /**
     * @param zos
     * @param dir "/"结尾
     * @throws Exception
     */
    public static void addZipDir(ZipOutputStream zos, String dir) throws Exception {
        zos.putNextEntry(new ZipEntry(dir));
        try {
            zos.closeEntry();
        } catch (IOException e) {
        }
    }

    public static void addZipFile(ZipOutputStream zos, String zipEntryPath, File srcFile) throws Exception {
        byte[] b = new byte[1024 * 4];
        zos.putNextEntry(new ZipEntry(zipEntryPath));
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
        int len;
        while ((len = bis.read(b)) != -1) {
            zos.write(b, 0, len);
        }
        zos.flush();
        try {
            zos.closeEntry();
        } catch (IOException e) {
        }
        close(bis);
    }

    public static void addZipFile(ZipOutputStream zos, String zipEntryPath, String srcFile) throws Exception {
        File file = new File(srcFile);
        addZipFile(zos, zipEntryPath, file);
    }

    /**
     * 删除文件夹
     *
     * @param dir
     */
    public static void rmdir(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    if (file.delete()) {
                        log.debug("删除文件{}", file.getAbsolutePath());
                    } else {
                        log.debug("删除文件{}失败", file.getAbsolutePath());
                    }
                } else {
                    rmdir(file);
                }
            }
            if (dir.delete()) {
                log.debug("删除文件夹{}", dir.getAbsolutePath());
            } else {
                log.debug("删除文件夹{}失败", dir.getAbsolutePath());
            }
        }
    }

    /**
     * 用系统命令拷贝文件
     *
     * @param src
     * @param dest
     * @throws Exception
     */
    public static void copyByOs(String src, String dest) throws Exception {
        File sfile = new File(src);
        copyByOs(sfile, dest);
    }

    public static void copyByOs(File sfile, String dest) throws Exception {
        File dfile = new File(dest);
        copyByOs(sfile, dfile);
    }

    public static void copyByOs(File sfile, File dfile) throws Exception {
        File dpdir = dfile.getParentFile();
        if (!dpdir.exists()) {
            dpdir.mkdirs();
        }
        String src = sfile.getAbsolutePath();
        String dest = dfile.getAbsolutePath();

        Runtime rt = Runtime.getRuntime();
        String cmd[] = null;
        BufferedReader br = null;
        try {
            if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
                cmd = new String[]{"cmd.exe", "/c", "copy " + src + " " + dest};
            } else {
                cmd = new String[]{"/bin/sh", "-c", "cp -f " + src + " " + dest};
            }
            Process p = rt.exec(cmd);
//			if (p.waitFor() == 0) {
//				log.debug("拷贝文件{}到{}", src, dest);
//			} else {
            if (false && log.isDebugEnabled()) {
                log.debug(cmd[2]);
                br = new BufferedReader(new InputStreamReader(p.getInputStream(), System.getProperty("sun.jnu.encoding")));
                String line = null;
                while ((line = br.readLine()) != null) {
                    log.debug(line);
                }
            }
//			}
        } catch (Exception e) {
            log.error("拷贝文件{}到{}失败：{}", cmd[2], e.getMessage());
            throw e;
        } finally {
            close(br);
        }
    }

    public static void copyByJava(String src, String dest) throws Exception {

    }

    /**
     * file copy , use FileChannel;
     *
     * @param originalFile
     * @param targetFile
     * @throws IOException
     */
    public static void copyByChannel(File originalFile, File targetFile) throws IOException {
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inStream = new FileInputStream(originalFile);
            outStream = new FileOutputStream(targetFile);
            inChannel = inStream.getChannel();
            outChannel = outStream.getChannel();
            inChannel.transferTo(inChannel.position(), inChannel.size(), outChannel);
            log.debug("拷贝文件{}到{}", originalFile.getAbsolutePath(), targetFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("拷贝文件出错:{}", e);
            throw e;
        } finally {
            if (null != inChannel) {
                inChannel.close();
            }
            if (null != inStream) {
                inStream.close();
            }
            if (null != outChannel) {
                outChannel.close();
            }
            if (null != outStream) {
                outStream.close();
            }
        }
    }

    /**
     * file copy , use FileChannel;
     * if originalFile not exists throw FileNotFoundException;
     * if targetFile not exists  createNewFile;
     *
     * @param originalFile
     * @param targetFilePath
     * @throws IOException
     */
    public static void copyByChannel(File originalFile, String targetFilePath) throws IOException {
        if (!originalFile.exists()) {
            throw new FileNotFoundException(originalFile.getPath() + " not exists");
        }
        File targetFile = new File(targetFilePath);
        boolean targetCreate = false;
        if (!targetFile.exists()) {
            File parentFile = targetFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            targetFile.createNewFile();
            targetCreate = true;
        }
        try {
            copyByChannel(originalFile, targetFile);
        } catch (IOException e) {
            if (targetCreate) {
                targetFile.delete();
            }
            throw e;
        }
    }

    /**
     * file copy , use FileChannel;
     * if originalFile not exists throw FileNotFoundException;
     * if targetFile not exists  createNewFile;
     *
     * @param originalFilePath
     * @param targetFilePath
     * @throws IOException
     */
    public static void copyByChannel(String originalFilePath, String targetFilePath) throws IOException {
        File originalFile = new File(originalFilePath);
        copyByChannel(originalFile, targetFilePath);
    }

    /**
     * 检查目标目录是否存在，如果不存在建立
     *
     * @param dirPath 目录路径
     * @return 存在/成功建立:true, 建立失败:false
     */
    public static boolean checkDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        }
        return true;
    }

    public static void close(Object... objs) {
        for (Object obj : objs) {
            if (obj != null) {
                try {
                    Method close = obj.getClass().getMethod("close");
                    if (close != null) {
                        close.invoke(obj);
                    }
                } catch (Exception e) {

                }
            }
        }
    }


    /**
     * 读取文件获取数据——String
     *
     * @param path
     * @return
     */
    public static String ReadFile(String path) {
        File file = new File(path);
        try {
            return FileUtils.readFileToString(file);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 写入String到文件
     *
     * @param path
     * @param data
     */
    public static void writeFile(String path, String data) {
        File file = new File(path);
        try {
            if (checkDir(file.getParent())) {
                FileUtils.writeStringToFile(file, data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(file);
        }
        System.out.println("writeFile >>>>>> " + path);
    }

    /**
     * 检测文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 判断链接是否有效,请求2次
     * @param url
     * @return
     */
    public static boolean checkFielUrl(String url) {
        int counts = 0;
        boolean flag = true;
        while (counts < 2) {
            try {
                URLConnection urlConnection = new URL(url).openConnection();
                urlConnection.setConnectTimeout(2000);
                int state = ((HttpURLConnection) urlConnection).getResponseCode();
                if (state != 200) {
                    throw new Exception("!");
                }
                break;
            } catch (Exception ex) {
                counts++;
                flag = false;
                continue;
            }
        }
        return flag;
    }

    public static void main(String[] args) throws Exception {
//		checkFielUrl("http://172.16.81.43:4301/iflydisk/xls/4/16/54/8633effa9f228142a0340c11f9595cbd.xls");
//		checkFielUrl("http://test.download.cycore.cn/test/2016/11/12/18/40/47/cf06905d-496d-41f0-b76c-67cb2107c050.xls");
        FileUtil.zip("d:/111", "d:/zip", "111.zip");
    }

}
