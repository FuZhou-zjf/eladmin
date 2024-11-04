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
package me.zhengjie.finance.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Laozhao
* @date 2024-11-01
**/
@Entity
@Data
@Table(name="bus_finance_records")
public class FinanceRecords implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @ApiModelProperty(value = "记录ID")
    private Long id;

    @Column(name = "`order_id`")
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @Column(name = "`date`")
    @ApiModelProperty(value = "交易日期")
    private Timestamp date;

    @Column(name = "`account_id`")
    @ApiModelProperty(value = "账户ID")
    private Long accountId;

    @Column(name = "`account_type`")
    @ApiModelProperty(value = "账户类型: 员工/卖家/推荐人")
    private String accountType;

    @Column(name = "`type`")
    @ApiModelProperty(value = "交易类型: 收入/支出")
    private String type;

    @Column(name = "`category`")
    @ApiModelProperty(value = "交易类别: 销售/佣金/推荐费")
    private String category;

    @Column(name = "`amount`")
    @ApiModelProperty(value = "金额")
    private BigDecimal amount;

    @Column(name = "`description`")
    @ApiModelProperty(value = "备注")
    private String description;

    @Column(name = "`created_at`")
    @CreationTimestamp
    @ApiModelProperty(value = "创建时间")
    private Timestamp createdAt;

    @Column(name = "`updated_at`")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedAt;

    public void copy(FinanceRecords source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
