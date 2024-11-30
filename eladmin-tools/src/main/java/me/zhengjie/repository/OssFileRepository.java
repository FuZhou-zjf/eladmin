package me.zhengjie.repository;

import me.zhengjie.domain.OssFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OssFileRepository extends JpaRepository<OssFile, Long>, JpaSpecificationExecutor<OssFile> {
} 