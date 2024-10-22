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
package me.zhengjie.seller.rest;

import me.zhengjie.annotation.Log;
import me.zhengjie.seller.domain.SellerInfo;
import me.zhengjie.seller.service.SellerInfoService;
import me.zhengjie.seller.service.dto.SellerInfoQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import me.zhengjie.seller.service.dto.SellerInfoDto;

/**
* @website https://eladmin.vip
* @author Laozhao
* @date 2024-10-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "卖家测试管理")
    @RequestMapping("/api/sellerInfo")
public class SellerInfoController {

    private final SellerInfoService sellerInfoService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('sellerInfo:list')")
    public void exportSellerInfo(HttpServletResponse response, SellerInfoQueryCriteria criteria) throws IOException {
        sellerInfoService.download(sellerInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询卖家测试")
    @ApiOperation("查询卖家测试")
    @PreAuthorize("@el.check('sellerInfo:list')")
    public ResponseEntity<PageResult<SellerInfoDto>> querySellerInfo(SellerInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(sellerInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增卖家测试")
    @ApiOperation("新增卖家测试")
    @PreAuthorize("@el.check('sellerInfo:add')")
    public ResponseEntity<Object> createSellerInfo(@Validated @RequestBody SellerInfo resources){
        sellerInfoService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改卖家测试")
    @ApiOperation("修改卖家测试")
    @PreAuthorize("@el.check('sellerInfo:edit')")
    public ResponseEntity<Object> updateSellerInfo(@Validated @RequestBody SellerInfo resources){
        sellerInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    @Log("删除卖家测试")
    @ApiOperation("删除卖家测试")
    @PreAuthorize("@el.check('sellerInfo:del')")
    public ResponseEntity<Object> deleteSellerInfo(@RequestBody Long[] ids) {
        sellerInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/checkSellerExists")
    @ApiOperation("检查卖家是否存在")
    public ResponseEntity<Map<String, Object>> checkSellerExists(
            @RequestParam String name,
            @RequestParam String ssn) {

        boolean nameExists = sellerInfoService.checkNameExists(name);
        boolean ssnExists = sellerInfoService.checkSsnExists(ssn);
        boolean sellerExists = sellerInfoService.checkSellerExists(name, ssn);

        Map<String, Object> response = new HashMap<>();
        response.put("nameExists", nameExists);
        response.put("ssnExists", ssnExists);
        response.put("sellerExists", sellerExists);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/checkRecommenderExists")
    public ResponseEntity<Map<String, Object>> checkRecommenderExists(
            @RequestParam String name,
            @RequestParam String contactInfo) {

        boolean nameExists = sellerInfoService.existsByName(name);
        boolean contactExists = sellerInfoService.existsByContactInfo(contactInfo);
        boolean recommenderExists = sellerInfoService.existsByNameAndContactInfo(name, contactInfo);

        Map<String, Object> response = new HashMap<>();
        response.put("nameExists", nameExists);
        response.put("contactExists", contactExists);
        response.put("recommenderExists", recommenderExists);

        return ResponseEntity.ok(response);
    }

}