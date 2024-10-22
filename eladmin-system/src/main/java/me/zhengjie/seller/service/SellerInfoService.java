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
package me.zhengjie.seller.service;

import me.zhengjie.seller.domain.SellerInfo;
import me.zhengjie.seller.service.dto.SellerInfoDto;
import me.zhengjie.seller.service.dto.SellerInfoQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;

import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author Laozhao
* @date 2024-10-09
**/
public interface SellerInfoService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<SellerInfoDto> queryAll(SellerInfoQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<SellerInfoDto>
    */
    List<SellerInfoDto> queryAll(SellerInfoQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param sellerId ID
     * @return SellerInfoDto
     */
    SellerInfoDto findById(Long sellerId);

    /**
    * 创建
    * @param resources /
    */
    void create(SellerInfo resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(SellerInfo resources);

    /**
    * 多选删除
    * @param ids /
    */
    void deleteAll(Long[] ids);

    /**
    * 导出数据
    * @param all 待导出的数据
    * @param response /
    * @throws IOException /
    */
    void download(List<SellerInfoDto> all, HttpServletResponse response) throws IOException;

    boolean checkSellerExists(String name, String ssn);

    boolean checkSsnExists(String ssn);

    boolean checkNameExists(String name);

    /**
     * 根据姓名和联系方式获取推荐人信息，如果不存在则创建新的推荐人
     */
    SellerInfo getOrCreateRecommender(String name, String contactInfo);

    boolean existsByName(String name);

    boolean existsByContactInfo(String contactInfo);

    boolean existsByNameAndContactInfo(String name, String contactInfo);

    SellerInfo save(SellerInfo seller);

    SellerInfo getOrCreateSellerWithInfo(@NotBlank String orderSellerName, @NotBlank String orderSellerSsn, @NotBlank String orderContactInfo, String orderPaymentMethod);
}