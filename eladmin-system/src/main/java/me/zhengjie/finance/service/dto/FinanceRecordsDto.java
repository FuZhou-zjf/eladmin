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
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Laozhao
* @date 2024-11-01
**/
@Data
public class FinanceRecordsDto implements Serializable {

    /** 记录ID */
    private Long id;

    /** 订单ID */
    private Long orderId;

    /** 交易日期 */
    private Timestamp date;

    /** 账户ID */
    private Long accountId;

    /** 账户类型: 员工/卖家/推荐人 */
    private String accountType;

    /** 交易类型: 收入/支出 */
    private String type;

    /** 交易类别: 销售/佣金/推荐费 */
    private String category;

    /** 金额 */
    private BigDecimal amount;

    /** 备注 */
    private String description;

    /** 创建时间 */
    private Timestamp createdAt;

    /** 更新时间 */
    private Timestamp updatedAt;
}