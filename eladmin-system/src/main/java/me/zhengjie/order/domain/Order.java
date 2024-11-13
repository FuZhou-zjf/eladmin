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
package me.zhengjie.order.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import me.zhengjie.seller.domain.SellerInfo;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;


/**
* @website https://eladmin.vip
* @description /
* @author LaoZhao
* @date 2024-10-21
**/
@Entity
@Data
@Table(name="bus_order")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`order_id`")
    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @Column(name = "`order_number`",unique = true)
    @ApiModelProperty(value = "订单编号")
    private String orderNumber;

    @Column(name = "`order_status`")
    @ApiModelProperty(value = "订单状态")
    private String orderStatus;

    @Column(name = "`order_account_username`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "账号名")
    private String orderAccountUsername;

    @Column(name = "`order_account_password`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "密码")
    private String orderAccountPassword;

    @Column(name = "`order_app_id`")
    @ApiModelProperty(value = "App项目编号")
    private Long orderAppId;

    @Column(name = "`order_app_name`")
    @ApiModelProperty(value = "App名称")
    private String orderAppName;


    @Column(name = "`order_seller_name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "卖家名称")
    private String orderSellerName;

    @Column(name = "`order_seller_ssn`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "卖家SSN")
    private String orderSellerSsn;

    @Column(name = "`order_contact_info`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "卖家联系方式")
    private String orderContactInfo;

    @Column(name = "`order_payment_method`")
    @ApiModelProperty(value = "支付方式")
    private String orderPaymentMethod;

    @Column(name = "`order_contact_other`")
    @ApiModelProperty(value = "紧急联系方式")
    private String orderContactOther;


    @Column(name = "`order_referrer_name`")
    @ApiModelProperty(value = "推荐人名称")
    private String orderReferrerName;

    @Column(name = "`order_referrer_ssn`")
    @ApiModelProperty(value = "推荐人SSN")
    private String orderReferrerSsn;

    @Column(name = "`order_referrer_info`")
    @ApiModelProperty(value = "推荐人联系方式")
    private String orderReferrerInfo;

    @Column(name = "`order_referrer_other`")
    @ApiModelProperty(value = "推荐人紧急联系方式")
    private String orderReferrerOther;

    /** 卖家 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_seller_id", referencedColumnName = "seller_id")
    private SellerInfo orderSeller;

    /** 推荐人 */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_referrer_id", referencedColumnName = "seller_id")
    private SellerInfo orderReferrer;

    @Column(name = "`order_referrer_method`")
    @ApiModelProperty(value = "推荐人支付方式")
    private String orderReferrerMethod;

    @Column(name = "`order_referral_fee`")
    @ApiModelProperty(value = "推荐费")
    private BigDecimal orderReferralFee;

    @Column(name = "`order_amount`")
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderAmount;

    @Column(name = "`order_employee_id`")
    @ApiModelProperty(value = "下单员工ID")
    private Long orderEmployeeId;

    @Column(name = "`order_commission`")
    @ApiModelProperty(value = "佣金")
    private BigDecimal orderCommission;

    @CreationTimestamp
    @Column(name = "`order_created_at`",updatable = false)
    @ApiModelProperty(value = "创建时间")
    private Timestamp orderCreatedAt;

    @UpdateTimestamp
    @Column(name = "`order_updated_at`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp orderUpdatedAt;

    @Column(name = "`order_remark`")
    @ApiModelProperty(value = "备注")
    private String orderRemark;

    @Column(name = "`order_seller_nickname`")
    @ApiModelProperty(value = "卖家昵称")
    private  String orderSellerNickname;


    public void copy(Order source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
