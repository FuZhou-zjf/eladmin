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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
* @website https://eladmin.vip
* @author Laozhao
* @date 2024-10-09
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "卖家管理")
@RequestMapping("/api/sellerInfo")
public class SellerInfoController {

    private final SellerInfoService sellerInfoService;
    private static final Logger log = LoggerFactory.getLogger(SellerInfoController.class);

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
    @GetMapping("/checkSeller")
    @ApiOperation("检查卖家信息")
    public ResponseEntity<Map<String, Object>> checkSeller(
            @RequestParam String name,
            @RequestParam(required = false) String ssn,
            @RequestParam(required = false) String contactInfo) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 根据名称和SSN检查卖家是否存在
            boolean exists = (ssn != null && !ssn.isEmpty()) 
                ? sellerInfoService.checkSellerExists(name, ssn)
                : sellerInfoService.checkNameExists(name);

            response.put("exists", exists);
            
            if (exists) {
                // 使用查询条件获取卖家信息
                SellerInfoQueryCriteria criteria = new SellerInfoQueryCriteria();
                criteria.setName(name);
                if (ssn != null && !ssn.isEmpty()) {
                    criteria.setSsn(ssn);
                }
                
                List<SellerInfoDto> sellers = sellerInfoService.queryAll(criteria);
                if (!sellers.isEmpty()) {
                    SellerInfoDto seller = sellers.get(0);
                    response.put("contactInfo", seller.getContactInfo());
                    response.put("paymentMethod", seller.getPaymentMethod());
                    response.put("email", seller.getEmail());
                    response.put("phoneNumber", seller.getPhoneNumber());
                    response.put("remarks", seller.getRemarks());
                }
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("检查卖家信息时发生错误", e);
            response.put("error", "检查卖家信息时发生错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/checkReferrer")
    @ApiOperation("检查推荐人信息")
    public ResponseEntity<Map<String, Object>> checkReferrer(
            @RequestParam String name,
            @RequestParam(required = false) String contactInfo) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查推荐人是否存在
            boolean exists = (contactInfo != null && !contactInfo.isEmpty())
                ? sellerInfoService.existsByNameAndContactInfo(name, contactInfo)
                : sellerInfoService.existsByName(name);

            response.put("exists", exists);
            
            if (exists) {
                // 使用查询条件获取推荐人信息
                SellerInfoQueryCriteria criteria = new SellerInfoQueryCriteria();
                criteria.setName(name);
                if (contactInfo != null && !contactInfo.isEmpty()) {
                    criteria.setContactInfo(contactInfo);
                }
                
                List<SellerInfoDto> referrers = sellerInfoService.queryAll(criteria);
                if (!referrers.isEmpty()) {
                    SellerInfoDto referrer = referrers.get(0);
                    response.put("contactInfo", referrer.getContactInfo());
                    response.put("paymentMethod", referrer.getPaymentMethod());
                    response.put("email", referrer.getEmail());
                    response.put("phoneNumber", referrer.getPhoneNumber());
                    response.put("remarks", referrer.getRemarks());
                }
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("检查推荐人信息时发生错误", e);
            response.put("error", "检查推荐人信息时发生错误");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}