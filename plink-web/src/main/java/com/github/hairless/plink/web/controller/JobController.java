package com.github.hairless.plink.web.controller;

import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.service.impl.JobServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Result addJob(@RequestBody @Valid Job job) {
        return jobService.addJob(job);
    }

    /**
     * 删除作业
     *
     * @param idList
     * @return
     */
    @RequestMapping("/deleteJob")
    public Result deleteJob(@RequestParam @Valid List<String> idList) {
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
     * @param id
     * @return
     */
    @RequestMapping("/queryJob")
    public Result queryJob(@RequestParam @Valid String id) {
        return jobService.queryJob(id);
    }

    /**
     * 查询所有作业
     *
     * @return
     */
    @RequestMapping("/selectJobAll")
    public Result selectJobAll() {
        return jobService.selectAll();
    }

}
