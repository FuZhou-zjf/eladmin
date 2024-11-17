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
package me.zhengjie.finance.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.finance.domain.FinanceRecords;
import me.zhengjie.finance.service.FinanceRecordsService;
import me.zhengjie.finance.service.dto.FinanceRecordsQueryCriteria;
import me.zhengjie.utils.DateQuery;
import me.zhengjie.utils.SecurityUtils;
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
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.finance.service.dto.FinanceRecordsDto;

/**
* @website https://eladmin.vip
* @author Laozhao
* @date 2024-11-01
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "财务管理管理")
@RequestMapping("/api/financeRecords")
public class FinanceRecordsController {

    private final FinanceRecordsService financeRecordsService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('financeRecords:list')")
    public void exportFinanceRecords(HttpServletResponse response, FinanceRecordsQueryCriteria criteria) throws IOException {
        financeRecordsService.download(financeRecordsService.queryAll(criteria), response);
    }
@GetMapping
@Log("查询财务管理")
@ApiOperation("查询财务管理")
@PreAuthorize("@el.check('financeRecords:list')")
public ResponseEntity<PageResult<FinanceRecordsDto>> queryFinanceRecords(
        FinanceRecordsQueryCriteria criteria, // 查询条件对象
        Pageable pageable,                    // 分页参数
        @RequestParam(value = "date", required = false) List<String> createTimeStrs) {

    // 如果前端传递了日期范围参数，则解析并设置到查询条件中
    if (createTimeStrs != null && createTimeStrs.size() == 2) {
        List<Timestamp> createTime = DateQuery.parseTimestamps(createTimeStrs);
        criteria.setDate(createTime);
    }

    // 将封装好的查询条件传递给 Service 层
    return new ResponseEntity<>(financeRecordsService.queryAll(criteria, pageable), HttpStatus.OK);
}
    @GetMapping("/query")
    @Log("按条件查询财务管理")
    @ApiOperation("按条件查询财务管理")
    @PreAuthorize("@el.check('financeRecords:list')")
    public ResponseEntity<PageResult<FinanceRecordsDto>> queryFinanceRecordsByCriteria(FinanceRecordsQueryCriteria criteria, Pageable pageable){

        return new ResponseEntity<>(financeRecordsService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增财务管理")
    @ApiOperation("新增财务管理")
    @PreAuthorize("@el.check('financeRecords:add')")
    public ResponseEntity<Object> createFinanceRecords(@Validated @RequestBody FinanceRecords resources){
        financeRecordsService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改财务管理")
    @ApiOperation("修改财务管理")
    @PreAuthorize("@el.check('financeRecords:edit')")
    public ResponseEntity<Object> updateFinanceRecords(@Validated @RequestBody FinanceRecords resources){
        financeRecordsService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除财务管理")
    @ApiOperation("删除财务管理")
    @PreAuthorize("@el.check('financeRecords:del')")
    public ResponseEntity<Object> deleteFinanceRecords(@RequestBody Long[] ids) {
        financeRecordsService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/{summaryType}-summary")
    @Log("获取财务汇总")
    @ApiOperation("获取财务汇总")
    @PreAuthorize("@el.check('financeRecords:list')")
    public ResponseEntity<Map<String, Object>> getFinancialSummary(
            @PathVariable String summaryType, FinanceRecordsQueryCriteria criteria) {

        // 默认使用当前登录用户信息
        if (criteria.getAccountId() == null) {
            criteria.setAccountId(SecurityUtils.getCurrentUserId());
        }
        if (criteria.getAccountType() == null) {
            criteria.setAccountType("employee");
        }

        Map<String, Object> result;
        switch (summaryType) {
            case "daily":
                result = financeRecordsService.getDailySummary(criteria);
                break;
            case "weekly":
                result = financeRecordsService.getWeeklySummary(criteria);
                break;
            case "monthly":
                result = financeRecordsService.getMonthlySummary(criteria);
                break;
            default:
                throw new IllegalArgumentException("Invalid summary type: " + summaryType);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}