package com.github.hairless.plink.common.util;

import com.github.hairless.plink.model.exception.PlinkException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtil {

    public static boolean exists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static String resolvePath(String parent, String child) throws PlinkException {
        File file = new File(parent, child);
        if (!file.exists()) {
            throw new PlinkException(file.getAbsolutePath() + " is not exist!");
        }
        return file.getAbsolutePath();
    }

    public static String getPathFromEnv(String env) throws PlinkException {
        String path = System.getenv(env);
        if (StringUtils.isBlank(path)) {
            throw new PlinkException(env + " is not set!");
        }
        File file = new File(path);
        if (!file.exists()) {
            throw new PlinkException(env + " is not exist!");
        }
        return file.getAbsolutePath();
    }

    public static String readFileToString(String file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), StandardCharsets.UTF_8));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public static List<String> listFileNames(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                return Arrays.stream(files).map(File::getAbsolutePath).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    public static List<URL> listFileURLs(String dirPath) throws MalformedURLException {
        List<URL> urls = new ArrayList<>();
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    urls.add(file.toURI().toURL());
                }
            }
        }
        return urls;
    }
}
