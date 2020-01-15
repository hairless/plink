package com.github.hairless.plink.service.impl;

import com.github.hairless.plink.dao.mapper.JobMapper;
import com.github.hairless.plink.model.pojo.Job;
import com.github.hairless.plink.model.resp.Result;
import com.github.hairless.plink.model.resp.ResultCode;
import com.github.hairless.plink.service.JobService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * job service
 *
 * @Author Trevor
 * @Create 2020/1/14 20:26
 */
@Slf4j
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobMapper jobMapper;

    @Override
    public Result addJob(Job job) {
        try {
            jobMapper.insert(job);
            log.info(job.getId() + ":插入成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result(ResultCode.EXCEPTION, e.getMessage());
        }
        return new Result(ResultCode.SUCCESS);
    }

    @Override
    public Result deleteJob(List<String> idList) {
        try {
            idList.forEach(id -> {
                jobMapper.deleteByPrimaryKey(id);
                log.info(id + ":删除成功");
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result(ResultCode.EXCEPTION, e.getMessage());
        }

        return new Result(ResultCode.SUCCESS);
    }

    @Override
    public Result updateJob(Job job) {
        try {
            jobMapper.updateByPrimaryKey(job);
            log.info(job.getId() + ":删除成功");
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result(ResultCode.EXCEPTION, e.getMessage());
        }
        return new Result(ResultCode.SUCCESS);
    }

    @Override
    public Result queryJob(String id) {
        Job job;
        try {
            job = jobMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result(ResultCode.EXCEPTION, e.getMessage());
        }
        return new Result(ResultCode.SUCCESS, job);
    }

    @Override
    public Result selectAll() {
        PageHelper.startPage(1, 1);
        PageInfo<Job> jobPageInfo;
        try {
            jobPageInfo = new PageInfo<>(jobMapper.selectAll());
        } catch (Exception e) {
            log.error(e.getMessage());
            return new Result(ResultCode.EXCEPTION, e.getMessage());
        }
        return new Result(ResultCode.SUCCESS, jobPageInfo);
    }
}
