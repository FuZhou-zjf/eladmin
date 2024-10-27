/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.appinfo.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.appinfo.domain.AppInfo;
import me.zhengjie.appinfo.service.AppInfoService;
import me.zhengjie.appinfo.service.dto.AppInfoQueryCriteria;
import me.zhengjie.utils.DateQuery;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.appinfo.service.dto.AppInfoDto;

/**
* @website https://eladmin.vip
* @author Laozhao
* @date 2024-10-26
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "app账号管理管理")
@RequestMapping("/api/appInfo")
public class AppInfoController {

    private final AppInfoService appInfoService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('appInfo:list')")
    public void exportAppInfo(HttpServletResponse response, AppInfoQueryCriteria criteria) throws IOException {
        appInfoService.download(appInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询App账号管理")
    @ApiOperation("查询App账号管理")
    @PreAuthorize("@el.check('appInfo:list')")
    public ResponseEntity<PageResult<AppInfoDto>> queryAppInfo(
            AppInfoQueryCriteria criteria,
            Pageable pageable,
            @RequestParam(value = "createTime", required = false) List<String> createTimeStrs) {

        // 如果前端传递了日期范围，则解析并设置到查询条件对象中
        if (createTimeStrs != null && createTimeStrs.size() == 2) {
            List<Timestamp> createTime = DateQuery.parseTimestamps(createTimeStrs);
            criteria.setCreatedAt(createTime);
        }

        return new ResponseEntity<>(appInfoService.queryAll(criteria, pageable), HttpStatus.OK);
    }


    @PostMapping
    @Log("新增app账号管理")
    @ApiOperation("新增app账号管理")
    @PreAuthorize("@el.check('appInfo:add')")
    public ResponseEntity<Object> createAppInfo(@Validated @RequestBody AppInfo resources){
        appInfoService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改app账号管理")
    @ApiOperation("修改app账号管理")
    @PreAuthorize("@el.check('appInfo:edit')")
    public ResponseEntity<Object> updateAppInfo(@Validated @RequestBody AppInfo resources){
        appInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除app账号管理")
    @ApiOperation("删除app账号管理")
    @PreAuthorize("@el.check('appInfo:del')")
    public ResponseEntity<Object> deleteAppInfo(@RequestBody Long[] ids) {
        appInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}