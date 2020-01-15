package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author Trevor
 * @Create 2020/1/14 20:26
 */
@Slf4j
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;


    @Override
    public int addJob(Job job) {
        return jobMapper.insert(job);
    }

    @Override
    public int deleteJob(List<String> idList) {
        idList.forEach(id -> {
            jobMapper.deleteByPrimaryKey(id);
        });

        return 0;
    }

    @Override
    public int updateJob(Job job) {
        return jobMapper.updateByPrimaryKey(job);
    }

    @Override
    public Job queryJob(String id) {
        return jobMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Job> selectAll() {
        return jobMapper.selectAll();
    }
}
