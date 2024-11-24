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
package me.zhengjie.appinfo.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Laozhao
* @date 2024-10-26
**/
@Entity
@Data
@Table(name="bus_app_info")
public class AppInfo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`account_id`")
    @ApiModelProperty(value = "账号ID")
    private Long accountId;

    @Column(name = "`app_name`")
    @ApiModelProperty(value = "App名称")
    private String appName;

    @Column(name = "`order_number`")
    @ApiModelProperty(value = "订单编号")
    private String orderNumber;


    @Column(name = "`account_username`")
    @ApiModelProperty(value = "账号名")
    private String accountUsername;

    @Column(name = "`account_password`")
    @ApiModelProperty(value = "账号密码")
    private String accountPassword;

    @Column(name = "`account_status`")
    @ApiModelProperty(value = "账号状态")
    private String accountStatus;

    @Column(name = "`sale_fee`")
    @ApiModelProperty(value = "收款金额，用来计算账号出售费用")
    private BigDecimal saleFee;

    @CreationTimestamp
    @Column(name = "`created_at`")
    @ApiModelProperty(value = "创建时间")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "`updated_at`")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updatedAt;

    @Column(name = "`full_name`")
    @ApiModelProperty(value = "全名")
    private String fullName;

    @Column(name = "`ssn`")
    @ApiModelProperty(value = "SSN或EIN")
    private String ssn;

    @Column(name = "`birth_date`")
    @ApiModelProperty(value = "出生日期")
    private Timestamp birthDate;

    @Column(name = "`address_line1`")
    @ApiModelProperty(value = "地址1")
    private String addressLine1;

    @Column(name = "`address_line2`")
    @ApiModelProperty(value = "地址2")
    private String addressLine2;

    @Column(name = "`city`")
    @ApiModelProperty(value = "城市")
    private String city;

    @Column(name = "`state`")
    @ApiModelProperty(value = "州/省")
    private String state;

    @Column(name = "`postal_code`")
    @ApiModelProperty(value = "邮编")
    private String postalCode;

    @Column(name = "`phone_number`")
    @ApiModelProperty(value = "联系电话")
    private String phoneNumber;

    @Column(name = "`api_url`")
    @ApiModelProperty(value = "API接口地址")
    private String apiUrl;

    @Column(name = "`email`")
    @ApiModelProperty(value = "电子邮件")
    private String email;

    @Column(name = "`bank_account_number`")
    @ApiModelProperty(value = "银行账号")
    private String bankAccountNumber;

    @Column(name = "`bank_routing_number`")
    @ApiModelProperty(value = "路由号")
    private String bankRoutingNumber;

    @Column(name = "`government_id_number`")
    @ApiModelProperty(value = "证件号")
    private String governmentIdNumber;

    @Column(name = "`security_question`")
    @ApiModelProperty(value = "安全问题")
    private String securityQuestion;

    @Column(name = "`security_answer`")
    @ApiModelProperty(value = "答案")
    private String securityAnswer;

    @Column(name = "buyer_name")
    @ApiModelProperty(value = "已售客户")
    private String buyerName;

    @Column(name = "`remark`")
    @ApiModelProperty(value = "备注")
    private String remark;

    public void copy(AppInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
