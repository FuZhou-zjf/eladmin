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
package me.zhengjie.order.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author LaoZhao
* @date 2024-10-09
**/
@Data
public class OrderDto implements Serializable {

    /** 订单ID */
    private Long orderId;

    /** 订单编号 */
    private String orderNumber;

    /** 订单状态 */
    private String orderStatus;

    /** 账号名 */
    private String orderAccountUsername;

    /** 密码 */
    private String orderAccountPassword;

    /** App项目编号 */
    private Long orderAppId;

    /** App名称 */
    private String orderAppName;

    /** 卖家ID */
    private Long orderSellerId;

    /** 卖家名称 */
    private String orderSellerName;

    /** 卖家SSN */
    private String orderSellerSsn;

    /** 卖家联系方式 */
    private String orderContactInfo;

    /** 支付方式 */
    private String orderPaymentMethod;

    /** 紧急联系方式 */
    private String orderContactOther;

    /** 推荐人ID */
    private Long orderReferrerId;

    /** 推荐人名称 */
    private String orderReferrerName;

    /** 推荐人SSN */
    private String orderReferrerSsn;

    /** 推荐人联系方式 */
    private String orderReferrerInfo;

    /** 推荐人紧急联系方式 */
    private String orderReferrerOther;

    /** 推荐人支付方式 */
    private String orderReferrerMethod;

    /** 推荐费 */
    private BigDecimal orderReferralFee;

    /** 订单金额 */
    private BigDecimal orderAmount;

    /** 下单员工ID */
    private Long orderEmployeeId;

    /** 佣金 */
    private BigDecimal orderCommission;

    /** 创建时间 */
    private Timestamp orderCreatedAt;

    /** 更新时间 */
    private Timestamp orderUpdatedAt;

    /** 备注 */
    private String orderRemark;
}