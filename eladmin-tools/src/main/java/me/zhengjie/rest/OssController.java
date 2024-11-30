package me.zhengjie.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.domain.OssFile;
import me.zhengjie.domain.vo.OssConfigVO;
import me.zhengjie.service.OssService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.web.PageableDefault;

@RestController
@RequiredArgsConstructor
@Api(tags = "工具OSS管理")
@RequestMapping("/api/oss")
public class OssController {
    
    private final OssService ossService;
    
    @GetMapping
    @ApiOperation("查询文件")
    @PreAuthorize("@el.check('oss:list')")
    public ResponseEntity<Page<OssFile>> query(
            @RequestParam(required = false) String fileName,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return new ResponseEntity<>(ossService.query(fileName, pageable), HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation("上传文件")
    @PreAuthorize("@el.check('oss:add')")
    public ResponseEntity<OssFile> upload(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(ossService.upload(file), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除文件")
    @PreAuthorize("@el.check('oss:del')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        ossService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/config")
    @ApiOperation("获取OSS配置")
    @PreAuthorize("@el.check('oss:list')")
    public ResponseEntity<OssConfigVO> getConfig() {
        return new ResponseEntity<>(ossService.getOssConfig(), HttpStatus.OK);
    }
} 