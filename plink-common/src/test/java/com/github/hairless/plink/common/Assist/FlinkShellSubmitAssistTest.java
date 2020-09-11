package com.github.hairless.plink.common.Assist;

import com.github.hairless.plink.common.util.SystemUtil;
import org.junit.Test;

import java.io.IOException;

/**
 * @author: silence
 * @date: 2020/9/4
 */
public class FlinkShellSubmitAssistTest {

    @Test
    public void asyncExecShellCommand() throws IOException, InterruptedException {
        if (SystemUtil.isUnix()) {
            int exitCode = FlinkShellSubmitAssist.syncExecShellCommand("echo hello plink");
            assert exitCode == 0;
        }
    }
}