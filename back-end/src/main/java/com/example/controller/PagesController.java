package com.example.controller;

import com.example.common.Result;
import com.example.entity.Pages;
import com.example.service.PagesService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 问卷表前端操作接口
 **/
@RestController
@RequestMapping("/pages")
public class PagesController {

    @Resource
    private PagesService pagesService;


    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Pages pages) {
        pagesService.add(pages);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        pagesService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        pagesService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result updateById(@RequestBody Pages pages) {
        pagesService.updateById(pages);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Pages pages = pagesService.selectById(id);
        return Result.success(pages);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Pages pages) {
        List<Pages> list = pagesService.selectAll(pages);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Pages pages,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Pages> page = pagesService.selectPage(pages, pageNum, pageSize);
        return Result.success(page);
    }

}