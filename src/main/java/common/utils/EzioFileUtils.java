package common.utils;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.List;

/**
 * Here be dragons
 * Created by haotian on 2018/11/11 7:42 PM
 */
public class EzioFileUtils {

   public static class ExtFilter implements FilenameFilter {

        private String ext;

        public ExtFilter(String ext) {
            this.ext = ext;
        }

        public boolean accept(File dir, String name) {
            return name.endsWith(ext);
        }
    }

    public static List<File> listFile(File dir, FileFilter ff, boolean recursive) {
        List<File> list = Lists.newArrayList();
        File[] files = dir.listFiles(ff);
        if (files != null && files.length > 0) {
            for (File f : files) {
                //如果是文件,添加文件到list中
                if (f.isFile()) {
                    list.add(f);
                }
                //获取子目录中的文件,添加子目录中的经过过滤的所有文件添加到list
                else if (recursive) {
                    list.addAll(listFile(f, ff, true));
                }
            }
        }
        return list;
    }

    public static List<File> listFile(File dir, List<String> extensions, boolean recursive) {
        if (!dir.exists()) {
            throw new IllegalArgumentException("目录：" + dir + "不存在");
        }
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(dir + "不是目录");
        }
        FileFilter ff = null;
        if (extensions == null || extensions.size() == 0) {
            ff = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return true;
                }
            };
        } else {
            ff = new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        return true;
                    }
                    String name = pathname.getName();
                    for (String ext : extensions) {
                        if (name.endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                }
            };
        }
        return listFile(dir, ff, recursive);
    }
}
