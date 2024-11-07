package com.example.controller;

import com.example.common.Result;
import com.example.entity.Logs;
import com.example.service.LogsService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 日志表前端操作接口
 **/
@RestController
@RequestMapping("/logs")
public class LogsController {

    @Resource
    private LogsService logsService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Logs logs) {
        logsService.add(logs);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        logsService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        logsService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Logs logs) {
        logsService.updateById(logs);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Logs logs = logsService.selectById(id);
        return Result.success(logs);
    }
}
