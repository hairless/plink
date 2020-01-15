package com.github.hairless.plink.service;

import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.resp.Result;

import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/14 20:25
 */

public interface JobService {
    Result addJob(Job job);

    Result deleteJob(List<String> idList);

    Result updateJob(Job job);

    Result queryJob(String id);

    Result selectAll();

}
