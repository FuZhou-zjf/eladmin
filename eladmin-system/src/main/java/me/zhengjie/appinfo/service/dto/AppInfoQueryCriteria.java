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
import java.sql.Timestamp;
import java.util.List;
import me.zhengjie.annotation.Query;

/**
* @website https://eladmin.vip
* @author Laozhao
* @date 2024-10-26
**/
@Data
public class AppInfoQueryCriteria{

    /** 精确 */
    @Query
    private String appName;

    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String accountUsername;

    /** 精确 */
    @Query
    private String accountStatus;


    /** 模糊 */
    @Query(type = Query.Type.INNER_LIKE)
    private String fullName;

    /** 精确 */
    @Query
    private String ssn;

    /** 日期范围查询 */
    @Query(type = Query.Type.BETWEEN, propName = "createdAt")
    private List<Timestamp> createdAt;


    /** 范围查询，起始日期 */
    @Query(type = Query.Type.GREATER_THAN, propName = "createdAt")
    private Timestamp startDate;

    /** 范围查询，结束日期 */
    @Query(type = Query.Type.LESS_THAN, propName = "createdAt")
    private Timestamp endDate;


}