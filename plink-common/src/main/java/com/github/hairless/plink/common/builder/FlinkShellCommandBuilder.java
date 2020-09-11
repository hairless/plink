package com.github.hairless.plink.common.builder;

import com.github.hairless.plink.model.common.FlinkSubmitOptions;
import com.github.hairless.plink.model.exception.PlinkException;

public interface FlinkShellCommandBuilder {

    String buildRunCommand(FlinkSubmitOptions flinkSubmitOptions) throws PlinkException;

}
