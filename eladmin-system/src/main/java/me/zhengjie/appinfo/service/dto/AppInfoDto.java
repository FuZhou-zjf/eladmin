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
package me.zhengjie.appinfo.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://eladmin.vip
* @description /
* @author Laozhao
* @date 2024-10-26
**/
@Data
public class AppInfoDto implements Serializable {

    /** 账号ID */
    private Long accountId;

    private String orderNumber;

    /** App名称 */
    private String appName;


    /** 账号名 */
    private String accountUsername;

    /** 账号密码 */
    private String accountPassword;

    /** 账号状态 */
    private String accountStatus;

    private BigDecimal saleFee;

    /** 创建时间 */
    private Timestamp createdAt;

    /** 更新时间 */
    private Timestamp updatedAt;

    /** 全名 */
    private String fullName;

    /** SSN或EIN */
    private String ssn;

    /** 出生日期 */
    private Timestamp birthDate;

    /** 地址1 */
    private String addressLine1;

    /** 地址2 */
    private String addressLine2;

    /** 城市 */
    private String city;

    /** 州/省 */
    private String state;

    /** 邮编 */
    private String postalCode;

    /** 联系电话 */
    private String phoneNumber;

    private String apiUrl;

    /** 电子邮件 */
    private String email;

    /** 银行账号 */
    private String bankAccountNumber;

    /** 路由号 */
    private String bankRoutingNumber;

    /** 证件号 */
    private String governmentIdNumber;

    /** 安全问题 */
    private String securityQuestion;

    /** 答案 */
    private String securityAnswer;

    /** 已售客户 */
    private String buyerName;

    /** 备注 */
    private String remark;
}