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
package me.zhengjie.seller.service.dto;

import lombok.Data;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.io.Serializable;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;

/**
* @website https://eladmin.vip
* @description /
* @author Laozhao
* @date 2024-10-09
**/
@Data
public class SellerInfoDto implements Serializable {

    /** 卖家ID */
    /** 防止精度丢失 */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long sellerId;

    /** 卖家姓名 */
    private String name;

    /** 卖家昵称 */
    private String nickName;


    /** 联系方式 */
    private String contactInfo;

    /** 电子邮件 */
    private String email;

    /** 电话号码 */
    private String phoneNumber;

    /** 身份证号码 */
    private String identityNumber;

    /** 社会安全号 */
    private String ssn;

    /** 身份证正面照片 */
    private String idFront;

    /** 身份证背面照片 */
    private String idBack;

    /** 手持身份证照片 */
    private String idHandheld;

    /** SSN正面照片 */
    private String ssnFront;

    /** SSN背面照片 */
    private String ssnBack;

    /** 水电账单照片 */
    private String utilityBill;

    /** 银行对账单照片 */
    private String bankStatement;

    /** 卖家视频链接 */
    private String videoUrl;

    /** 总收入 */
    private BigDecimal totalIncome;

    /** 创建时间 */
    private Timestamp createdAt;

    /** 最后更新时间 */
    private Timestamp lastUpdated;

    /** 备注 */
    private String remarks;

    /** 收款方式 */
    private String paymentMethod;
}