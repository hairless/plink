package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.service.impl.JobServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * job
 *
 * @Author Trevor
 * @Create 2020/1/14 14:19
 */

@RestController
@Slf4j
@RequestMapping("/job")
public class JobController {
    @Autowired
    private JobServiceImpl jobService;

    /**
     * 添加作业
     *
     * @param job
     * @return
     */
    @RequestMapping("/addJob")
    public Result addJob(@RequestBody  Job job) {
        return jobService.addJob(job);
    }

    /**
     * 删除作业
     *
     * @param idList
     * @return
     */
    @RequestMapping(value="/deleteJob",method = RequestMethod.POST)
    public Result deleteJob(@RequestParam @Valid List<Long> idList) {
        return jobService.deleteJob(idList);
    }

    /**
     * 编辑作业
     *
     * @param job
     * @return
     */
    @RequestMapping("/updateJob")
    public Result updateJob(@RequestBody @Valid Job job) {
        return jobService.updateJob(job);
    }

    /**
     * 查询单个作业
     *
     * @param jobId
     * @return
     */
    @RequestMapping("/queryJob/{jobId}")
    public Result queryJob(@PathVariable(value = "jobId")Long jobId) {
        return jobService.queryJob(jobId);
    }

    /**
     * 查询所有作业
     *
     * @return
     */
    @RequestMapping("/queryJobAll")
    public Result queryJobAll() {
        //TODO 分页参数设置
        return jobService.queryJobAll();
    }


    //TODO 模糊查询
}
