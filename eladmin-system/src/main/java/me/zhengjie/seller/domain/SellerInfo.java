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
package me.zhengjie.seller.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Objects;


/**
* @website https://eladmin.vip
* @description /
* @author Laozhao
* @date 2024-10-09
**/
@Entity
@Data
@Table(name="bus_seller_info")
public class SellerInfo implements Serializable {

    @Id
    @Column(name = "`seller_id`")
    @ApiModelProperty(value = "卖家ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @Column(name = "`name`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "卖家姓名")
    private String name;

    @Column(name = "`nickname`")
    @ApiModelProperty(value = "卖家昵称")
    private String nickName;

    @Column(name = "`contact_info`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "联系方式")
    private String contactInfo;

    @Column(name = "`email`")
    @NotBlank
    @ApiModelProperty(value = "电子邮件")
    private String email;

    @Column(name = "`phone_number`")
    @NotBlank
    @ApiModelProperty(value = "电话号码")
    private String phoneNumber;

    @Column(name = "`identity_number`")
    @NotBlank
    @ApiModelProperty(value = "身份证号码")
    private String identityNumber;

    @Column(name = "`ssn`",nullable = false)
    @NotBlank
    @ApiModelProperty(value = "社会安全号")
    private String ssn;

    @Column(name = "`id_front`")
    @ApiModelProperty(value = "身份证正面照片")
    private String idFront;

    @Column(name = "`id_back`")
    @ApiModelProperty(value = "身份证背面照片")
    private String idBack;

    @Column(name = "`id_handheld`")
    @ApiModelProperty(value = "手持身份证照片")
    private String idHandheld;

    @Column(name = "`ssn_front`")
    @ApiModelProperty(value = "SSN正面照片")
    private String ssnFront;

    @Column(name = "`ssn_back`")
    @ApiModelProperty(value = "SSN背面照片")
    private String ssnBack;

    @Column(name = "`utility_bill`")
    @ApiModelProperty(value = "水电账单照片")
    private String utilityBill;

    @Column(name = "`bank_statement`")
    @ApiModelProperty(value = "银行对账单照片")
    private String bankStatement;

    @Column(name = "`video_url`")
    @ApiModelProperty(value = "卖家视频链接")
    private String videoUrl;

    @Column(name = "`total_income`")
    @ApiModelProperty(value = "总收入")
    private BigDecimal totalIncome;


    /** 创建时间 */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    /** 更新时间 */
    @UpdateTimestamp
    @Column(name = "last_updated")
    private Timestamp lastUpdated;

    @Column(name = "`remarks`")
    @ApiModelProperty(value = "备注")
    private String remarks;

    @Column(name = "`payment_method`")
    @ApiModelProperty(value = "收款方式")
    private String paymentMethod;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SellerInfo)) return false;
        SellerInfo that = (SellerInfo) o;
        return Objects.equals(sellerId, that.sellerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sellerId);
    }

    public void copy(SellerInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
