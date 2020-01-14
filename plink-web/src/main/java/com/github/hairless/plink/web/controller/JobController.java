package com.github.hairless.plink.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author Trevor
 * @Create 2020/1/14 14:19
 */

@Controller
public class JobController {

    @RequestMapping(value = {"/", "/page/**"})
    public void  addJob(){

    }


    @RequestMapping(value = {"/", "/page/**"})
    public void  deleteJob(){

    }

    @RequestMapping(value = {"/", "/page/**"})
    public void  updateJob(){

    }

    @RequestMapping(value = {"/", "/page/**"})
    public void  queryJob(){

    }
}
