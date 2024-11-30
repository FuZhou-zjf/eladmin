package me.zhengjie.service;

import me.zhengjie.domain.OssFile;
import me.zhengjie.domain.vo.OssConfigVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    /**
     * 获取OSS配置信息
     */
    OssConfigVO getOssConfig();

    /**
     * 上传文件
     */
    OssFile upload(MultipartFile file);

    /**
     * 分页查询
     */
    Page<OssFile> query(String fileName, Pageable pageable);

    /**
     * 删除文件
     */
    void delete(Long id);
}
