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
package me.zhengjie.order.service;

import me.zhengjie.order.domain.Order;
import me.zhengjie.order.service.dto.OrderDto;
import me.zhengjie.order.service.dto.OrderQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author LaoZhao
* @date 2024-10-09
**/
public interface OrderService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<OrderDto> queryAll(OrderQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<OrderDto>
    */
    List<OrderDto> queryAll(OrderQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param orderId ID
     * @return OrderDto
     */
    OrderDto findById(Long orderId);

    /**
    * 创建
    * @param resources /
    */
    void create(Order resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(Order resources);

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
    void download(List<OrderDto> all, HttpServletResponse response) throws IOException;
}