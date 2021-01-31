package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.TestBootStrapApp;
import com.github.hairless.plink.model.dto.JobStateInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author chaixiaoxue
 * @version 1.0
 * @date 2021/1/15 15:16
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestBootStrapApp.class})
public class JobStateInfoControllerTest extends  BaseControllerTest{

    @Test
    public void queryJobStateInfosTest() throws Exception{
        getTest("/mng/jobStateInfo/queryJobStateInfos");
    }

    @Test
    public void queryJobStateInfoTest() throws Exception{
        getTest("/mng/jobStateInfo/queryJobStateInfo?jobStateInfoId=1");
    }
    @Test
    public void deleteJobStateInfoTest() throws Exception{
        getTest("/mng/jobStateInfo/deleteJobStateInfo?jobStateInfoId=1");
    }

    @Test
    public void addJobStateInfoTest() throws Exception{
        JobStateInfoDTO jobStateInfoDTO = new JobStateInfoDTO();
        jobStateInfoDTO.setJobId(1L);
        jobStateInfoDTO.setInstanceId(1L);
        jobStateInfoDTO.setExternalPath("file:/Users/liuxiaoshuai/vdb1/flink_cp/39c709ceabea69dd74d84c4916b49e5f/chk-29");
        jobStateInfoDTO.setType(0);
        jobStateInfoDTO.setDuration(10000l);
        jobStateInfoDTO.setSize(1000l);
        jobStateInfoDTO.setReportTimestamp(System.currentTimeMillis()/1000);
        postMvcResult("/mng/jobStateInfo/addJobStateInfo",jobStateInfoDTO);
    }

}
