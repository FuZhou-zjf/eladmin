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
package me.zhengjie.appinfo.service.impl;

import me.zhengjie.appinfo.domain.AppInfo;
import me.zhengjie.finance.service.FinanceRecordsService;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.appinfo.repository.AppInfoRepository;
import me.zhengjie.appinfo.service.AppInfoService;
import me.zhengjie.appinfo.service.dto.AppInfoDto;
import me.zhengjie.appinfo.service.dto.AppInfoQueryCriteria;
import me.zhengjie.appinfo.service.mapstruct.AppInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import me.zhengjie.utils.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author Laozhao
* @date 2024-10-26
**/
@Service
@RequiredArgsConstructor
public class AppInfoServiceImpl implements AppInfoService {

    private final AppInfoRepository appInfoRepository;
    private final AppInfoMapper appInfoMapper;
    private final FinanceRecordsService financeRecordsService;
    private static final Logger log = LoggerFactory.getLogger(AppInfoServiceImpl.class);


    @Override
    public PageResult<AppInfoDto> queryAll(AppInfoQueryCriteria criteria, Pageable pageable){
        Page<AppInfo> page = appInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(appInfoMapper::toDto));
    }

    @Override
    public List<AppInfoDto> queryAll(AppInfoQueryCriteria criteria){
        return appInfoMapper.toDto(appInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public AppInfoDto findById(Long accountId) {
        AppInfo appInfo = appInfoRepository.findById(accountId).orElseGet(AppInfo::new);
        ValidationUtil.isNull(appInfo.getAccountId(),"AppInfo","accountId",accountId);
        return appInfoMapper.toDto(appInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(AppInfo resources) {
        appInfoRepository.save(resources);
        financeRecordsService.createFinanceRecordsForApp(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AppInfo resources) {
        AppInfo appInfo = appInfoRepository.findById(resources.getAccountId()).orElseGet(AppInfo::new);
        ValidationUtil.isNull( appInfo.getAccountId(),"AppInfo","id",resources.getAccountId());
        // 使用日志记录传入的 resources 中的 apiUrl 值
        log.info("传入的 apiUrl 值: {}", resources.getApiUrl());

        // 更新记录
        appInfo.copy(resources);

        // 使用日志记录更新后的 appInfo 对象的 apiUrl 值
        log.info("更新后的 appInfo.apiUrl 值: {}", appInfo.getApiUrl());

        // 保存到数据库
        AppInfo savedAppInfo = appInfoRepository.save(appInfo);

        // 保存后再次打印保存结果
        log.info("保存后的 apiUrl 值: {}", savedAppInfo.getApiUrl());

        financeRecordsService.createFinanceRecordsForApp(resources);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long accountId : ids) {
            appInfoRepository.deleteById(accountId);
        }
    }


    @Override
    public void download(List<AppInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (AppInfoDto appInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("App名称", appInfo.getAppName());
            map.put("账号名", appInfo.getAccountUsername());
            map.put("账号密码", appInfo.getAccountPassword());
            map.put("账号状态", appInfo.getAccountStatus());
            map.put("创建时间", appInfo.getCreatedAt());
            map.put("更新时间", appInfo.getUpdatedAt());
            map.put("全名", appInfo.getFullName());
            map.put("SSN或EIN", appInfo.getSsn());
            map.put("出生日期", appInfo.getBirthDate());
            map.put("地址1", appInfo.getAddressLine1());
            map.put("地址2", appInfo.getAddressLine2());
            map.put("城市", appInfo.getCity());
            map.put("州/省", appInfo.getState());
            map.put("邮编", appInfo.getPostalCode());
            map.put("联系电话", appInfo.getPhoneNumber());
            map.put("电子邮件", appInfo.getEmail());
            map.put("银行账号", appInfo.getBankAccountNumber());
            map.put("路由号", appInfo.getBankRoutingNumber());
            map.put("证件号", appInfo.getGovernmentIdNumber());
            map.put("安全问题", appInfo.getSecurityQuestion());
            map.put("答案", appInfo.getSecurityAnswer());
            map.put("备注", appInfo.getRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}