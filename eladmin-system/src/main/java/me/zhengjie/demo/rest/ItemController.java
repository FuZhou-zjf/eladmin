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
package me.zhengjie.demo.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.demo.domain.Item;
import me.zhengjie.demo.service.ItemService;
import me.zhengjie.demo.service.dto.ItemQueryCriteria;
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
import me.zhengjie.demo.service.dto.ItemDto;

/**
* @website https://eladmin.vip
* @author laozhao
* @date 2024-10-08
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "测试商品管理")
@RequestMapping("/api/item")
public class ItemController {

    private final ItemService itemService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('item:list')")
    public void exportItem(HttpServletResponse response, ItemQueryCriteria criteria) throws IOException {
        itemService.download(itemService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询测试商品")
    @ApiOperation("查询测试商品")
    @PreAuthorize("@el.check('item:list')")
    public ResponseEntity<PageResult<ItemDto>> queryItem(ItemQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(itemService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增测试商品")
    @ApiOperation("新增测试商品")
    @PreAuthorize("@el.check('item:add')")
    public ResponseEntity<Object> createItem(@Validated @RequestBody Item resources){
        itemService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改测试商品")
    @ApiOperation("修改测试商品")
    @PreAuthorize("@el.check('item:edit')")
    public ResponseEntity<Object> updateItem(@Validated @RequestBody Item resources){
        itemService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除测试商品")
    @ApiOperation("删除测试商品")
    @PreAuthorize("@el.check('item:del')")
    public ResponseEntity<Object> deleteItem(@RequestBody Integer[] ids) {
        itemService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}