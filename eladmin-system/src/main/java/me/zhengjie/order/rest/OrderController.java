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
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.order.service.dto.OrderDto;

/**
* @website https://eladmin.vip
* @author LaoZhao
* @date 2024-10-09
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
    public ResponseEntity<PageResult<OrderDto>> queryOrder(OrderQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(orderService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增订单管理")
    @ApiOperation("新增订单管理")
    @PreAuthorize("@el.check('order:add')")
    public ResponseEntity<Object> createOrder(@Validated @RequestBody Order resources){
        orderService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
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