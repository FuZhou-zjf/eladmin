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
package me.zhengjie.finance.service.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import me.zhengjie.annotation.Query;
import org.springframework.format.annotation.DateTimeFormat;

/**
* @website https://eladmin.vip
* @author Laozhao
* @date 2024-11-01
**/
@Data
public class FinanceRecordsQueryCriteria{

    /** 精确查询账号ID */
    @Query
    private Long accountId;

    /** 精确查询账号ID */
    @Query
    private String orderNumber;

    /** 精确查询账号类型 */
    @Query
    private String accountType;

    /** 交易类别: 销售/佣金/推荐费 */
    @Query
    private String category;

    @Query
    private String type;

    /** 范围查询，起始日期 */
    @Query(type = Query.Type.GREATER_THAN, propName = "date")
    private Timestamp startDate;

    /** 范围查询，结束日期 */
    @Query(type = Query.Type.LESS_THAN, propName = "date")
    private Timestamp endDate;

    /** 日期范围查询 */
    @Query(type = Query.Type.BETWEEN, propName = "date")
    private List<Timestamp> date;





}