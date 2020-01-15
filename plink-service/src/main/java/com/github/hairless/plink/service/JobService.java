package com.github.hairless.plink.service;

import com.github.hairless.plink.model.pojo.Job;

import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/14 20:25
 */

public interface JobService {
    int addJob(Job job);

    int deleteJob(List<String> idList);

    int updateJob(Job job);

    Job queryJob(String id);

    List<Job> selectAll();

}
