package com.github.hairless.plink.common.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtil {

    public static String readFileToString(String file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(file)), StandardCharsets.UTF_8));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }
}
