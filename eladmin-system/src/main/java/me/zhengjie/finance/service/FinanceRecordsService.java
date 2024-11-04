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
package me.zhengjie.finance.service;

import me.zhengjie.finance.domain.FinanceRecords;
import me.zhengjie.finance.service.dto.FinanceRecordsDto;
import me.zhengjie.finance.service.dto.FinanceRecordsQueryCriteria;
import me.zhengjie.order.domain.Order;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务接口
* @author Laozhao
* @date 2024-11-01
**/
public interface FinanceRecordsService {

    /**
    * 查询数据分页
    * @param criteria 条件
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    PageResult<FinanceRecordsDto> queryAll(FinanceRecordsQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<FinanceRecordsDto>
    */
    List<FinanceRecordsDto> queryAll(FinanceRecordsQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return FinanceRecordsDto
     */
    FinanceRecordsDto findById(Long id);

    /**
    * 创建
    * @param resources /
    */
    void create(FinanceRecords resources);

    /**
    * 编辑
    * @param resources /
    */
    void update(FinanceRecords resources);

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
    void download(List<FinanceRecordsDto> all, HttpServletResponse response) throws IOException;

    /**
     * 根据订单创建财务记录
     * @param order 订单对象
     */
    void createFinanceRecordsForOrder(Order order);  // 新增方法，用于根据订单生成财务记录

    /**
     * 根据订单更新财务记录
     * @param resources 订单对象
     */

    void updateFinanceRecordsForOrder(Order resources);
}