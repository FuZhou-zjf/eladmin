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
package me.zhengjie.order.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.order.domain.Order;
import me.zhengjie.order.service.OrderService;
import me.zhengjie.order.service.dto.OrderQueryCriteria;
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
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.order.service.dto.OrderDto;

/**
* @website https://eladmin.vip
* @author LaoZhao
* @date 2024-10-21
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "订单管理管理")
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('order:list')")
    public void exportOrder(HttpServletResponse response, OrderQueryCriteria criteria) throws IOException {
        orderService.download(orderService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询订单管理")
    @ApiOperation("查询订单管理")
    @PreAuthorize("@el.check('order:list')")
    public ResponseEntity<PageResult<OrderDto>> queryOrder(
            OrderQueryCriteria criteria, // 查询条件对象
            Pageable pageable,            // 分页参数
            @RequestParam(value = "createTime", required = false) List<String> createTimeStrs) {

        // 如果前端传递了日期范围参数，则解析并设置到查询条件中
        if (createTimeStrs != null && createTimeStrs.size() == 2) {
            List<Timestamp> createTime = DateQuery.parseTimestamps(createTimeStrs);
            criteria.setOrderCreatedAt(createTime);
        }

        // 将封装好的查询条件传递给 Service 层
        return new ResponseEntity<>(orderService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @PostMapping
    @Log("新增订单管理")
    @ApiOperation("新增订单管理")
    @PreAuthorize("@el.check('order:add')")
    public ResponseEntity<Object> createOrder(@Validated @RequestBody Order resources){
        try {
            // 调用 orderService 的 create 方法保存订单数据
            orderService.create(resources);  // 使用 create 而不是 save

            // 返回 201 Created 响应
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            // 打印异常日志，并返回 500 错误
            e.printStackTrace();
            return new ResponseEntity<>("订单创建失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    @Log("修改订单管理")
    @ApiOperation("修改订单管理")
    @PreAuthorize("@el.check('order:edit')")
    public ResponseEntity<Object> updateOrder(@Validated @RequestBody Order resources){
        orderService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除订单管理")
    @ApiOperation("删除订单管理")
    @PreAuthorize("@el.check('order:del')")
    public ResponseEntity<Object> deleteOrder(@RequestBody Long[] ids) {
        orderService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}