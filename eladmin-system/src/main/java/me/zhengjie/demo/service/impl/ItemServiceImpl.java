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
package me.zhengjie.demo.service.impl;

import me.zhengjie.demo.domain.Item;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.demo.repository.ItemRepository;
import me.zhengjie.demo.service.ItemService;
import me.zhengjie.demo.service.dto.ItemDto;
import me.zhengjie.demo.service.dto.ItemQueryCriteria;
import me.zhengjie.demo.service.mapstruct.ItemMapper;
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

/**
* @website https://eladmin.vip
* @description 服务实现
* @author laozhao
* @date 2024-10-05
**/
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public PageResult<ItemDto> queryAll(ItemQueryCriteria criteria, Pageable pageable){
        Page<Item> page = itemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(itemMapper::toDto));
    }

    @Override
    public List<ItemDto> queryAll(ItemQueryCriteria criteria){
        return itemMapper.toDto(itemRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public ItemDto findById(Integer id) {
        Item item = itemRepository.findById(id).orElseGet(Item::new);
        ValidationUtil.isNull(item.getId(),"Item","id",id);
        return itemMapper.toDto(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Item resources) {
        itemRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Item resources) {
        Item item = itemRepository.findById(resources.getId()).orElseGet(Item::new);
        ValidationUtil.isNull( item.getId(),"Item","id",resources.getId());
        item.copy(resources);
        itemRepository.save(item);
    }

    @Override
    public void deleteAll(Integer[] ids) {
        for (Integer id : ids) {
            itemRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<ItemDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (ItemDto item : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" name",  item.getName());
            map.put(" price",  item.getPrice());
            map.put(" quantity",  item.getQuantity());
            map.put(" createdAt",  item.getCreatedAt());
            map.put(" updatedAt",  item.getUpdatedAt());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}