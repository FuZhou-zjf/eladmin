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
package me.zhengjie.rest;

import lombok.RequiredArgsConstructor;
import me.zhengjie.annotation.Log;
import me.zhengjie.domain.LocalStorage;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.service.LocalStorageService;
import me.zhengjie.service.dto.LocalStorageDto;
import me.zhengjie.service.dto.LocalStorageQueryCriteria;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.PageResult;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Zheng Jie
 * @date 2019-09-05
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "工具：本地存储管理")
@RequestMapping("/api/localStorage")
public class LocalStorageController {

    private final LocalStorageService localStorageService;
    private static final Logger logger = LoggerFactory.getLogger(LocalStorageController.class);

    @Value("${file.chunk-path}")
    private String chunkPath;

    @Value("${file.chunk-size}")
    private String chunkSize;

    @Value("${file.chunk-expire-hours}")
    private Integer chunkExpireHours;

    @PostConstruct
    public void init() {
        logger.info("=== LocalStorageController 初始化 ===");
        logger.info("配置信息: chunkPath={}, chunkSize={}, expireHours={}",
                chunkPath, chunkSize, chunkExpireHours);

        // 检查并创建分片目录
        File chunkDir = new File(chunkPath);
        if (!chunkDir.exists()) {
            boolean created = chunkDir.mkdirs();
            logger.info("创建分片目录: {} - {}", chunkPath, created ? "成功" : "失败");
        }
    }

    // 添加一个简单的测试接口
    @GetMapping("/test-chunk")
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<String> testChunk() {
        logger.info("测试接口被调用");
        return ResponseEntity.ok("测试成功");
    }

    @GetMapping
    @ApiOperation("查询文件")
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<PageResult<LocalStorageDto>> queryFile(LocalStorageQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(localStorageService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void exportFile(HttpServletResponse response, LocalStorageQueryCriteria criteria) throws IOException {
        localStorageService.download(localStorageService.queryAll(criteria), response);
    }

    @PostMapping
    @ApiOperation("上传文件")
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity<Map<String, Object>> createFile(@RequestParam String name, @RequestParam("file") MultipartFile file) {
        logger.info("分片上传被调用");
        // 如果需要自定义名称，就使用传入的name，否则使用原始文件名
        String finalName = (name != null && !name.trim().isEmpty()) ? name : file.getOriginalFilename();
        LocalStorage localStorage = localStorageService.create(finalName, file);

        Map<String, Object> response = new HashMap<>();
        response.put("status", 201);
        response.put("message", "文件上传成功");

        Map<String, Object> data = new HashMap<>();
        data.put("id", localStorage.getId());
        data.put("realName", localStorage.getRealName());
        data.put("name", localStorage.getName());
        data.put("path", localStorage.getPath());
        data.put("type", localStorage.getType());
        data.put("size", localStorage.getSize());

        response.put("data", data);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ApiOperation("上传图片")
    @PostMapping("/pictures")
    public ResponseEntity<LocalStorage> uploadPicture(@RequestParam MultipartFile file){
        String suffix = FileUtil.getExtensionName(file.getOriginalFilename());
        if(!FileUtil.IMAGE.equals(FileUtil.getFileType(suffix))){
            throw new BadRequestException("只能上传图片");
        }
        LocalStorage localStorage = localStorageService.create(null, file);
        return new ResponseEntity<>(localStorage, HttpStatus.OK);
    }

    @PutMapping
    @Log("修改文件")
    @ApiOperation("修改文件")
    @PreAuthorize("@el.check('storage:edit')")
    public ResponseEntity<Object> updateFile(@Validated @RequestBody LocalStorage resources){
        localStorageService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除文件")
    @DeleteMapping
    @ApiOperation("多选删除")
    public ResponseEntity<Object> deleteFile(@RequestBody Long[] ids) {
        localStorageService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/chunk")
    @ApiOperation("分片上传")
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity<Object> uploadChunk(
            @RequestParam("qqpartindex") Integer partIndex,
            @RequestParam("qqpartbyteoffset") Long partByteOffset,
            @RequestParam("qqchunksize") Long chunkSize,
            @RequestParam("qqtotalparts") Integer totalParts,
            @RequestParam("qqtotalfilesize") Long totalFileSize,
            @RequestParam("qqfilename") String filename,
            @RequestParam("qquuid") String uuid,
            @RequestParam(value = "name", required = false) String name,  // 添加自定义文件名参数
            @RequestParam("qqfile") MultipartFile file
    ) {
        File mergedFile = null;
        try {
            // 基础验证
            if (partIndex >= totalParts) {
                throw new BadRequestException("分片索引超出范围");
            }

            if (file.isEmpty()) {
                throw new BadRequestException("分片文件不能为空");
            }

            if (file.getSize() > chunkSize) {
                throw new BadRequestException("分片大小超出限制");
            }

            // 确保分片目录存在
            String chunkDirPath = chunkPath + File.separator + uuid;
            File chunkDir = new File(chunkDirPath);
            if (!chunkDir.exists()) {
                chunkDir.mkdirs();
            }

            // 保存分片
            String chunkFilePath = chunkDirPath + File.separator + "part_" + partIndex;
            File chunkFile = new File(chunkFilePath);
            file.transferTo(chunkFile);

            // 检查是否所有分片都已上传
            if (isUploadComplete(chunkDir, totalParts)) {
                // 生成最终文件名
                // 使用传入的name或原始文件名
                String finalFileName = name != null ? name : filename;
                // 合并分片
                mergedFile = mergeChunks(chunkDir, finalFileName, totalParts);

                try {
                    // 使用双参数的create方法
                    LocalStorage localStorage = localStorageService.create(finalFileName, convertToMultipartFile(mergedFile));
                    
                    // 如果需要设置不同的realName
                    if (!finalFileName.equals(filename)) {
                        localStorage.setRealName(filename);
                        localStorageService.update(localStorage);
                    }

                    // 清理临时文件
                    FileUtils.deleteDirectory(chunkDir);

                    return ResponseEntity.ok(new HashMap<String, Object>() {{
                        put("success", true);
                        put("data", localStorage);
                    }});
                } finally {
                    // 确保清理临时文件
                    try {
                        FileUtils.deleteDirectory(chunkDir);
                        if (mergedFile != null && mergedFile.exists()) {
                            mergedFile.delete();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("success", true);
                put("message", "分片上传成功");
            }});

        } catch (Exception e) {
            // 发生异常时也要清理临时文件
            if (mergedFile != null && mergedFile.exists()) {
                mergedFile.delete();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HashMap<String, Object>() {{
                        put("success", false);
                        put("message", "上传失败111：" + e.getMessage());
                    }});
        }
    }

    @GetMapping("/chunk/check")
    @ApiOperation("检查分片是否存在")
    public ResponseEntity<Object> checkChunk(
            @RequestParam("qquuid") String uuid,
            @RequestParam("qqpartindex") Integer partIndex
    ) {
        File chunkFile = new File(chunkPath + File.separator + uuid + File.separator + "part_" + partIndex);
        return ResponseEntity.ok(new HashMap<String, Object>() {{
            put("exists", chunkFile.exists());
        }});
    }

    @DeleteMapping("/chunk/{uuid}")
    @ApiOperation("删除上传的分片")
    public ResponseEntity<Object> deleteChunks(@PathVariable String uuid) {
        try {
            File chunkDir = new File(chunkPath + File.separator + uuid);
            if (chunkDir.exists()) {
                FileUtils.deleteDirectory(chunkDir);
            }
            return ResponseEntity.ok(new HashMap<String, Object>() {{
                put("success", true);
            }});
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HashMap<String, Object>() {{
                        put("success", false);
                        put("message", e.getMessage());
                    }});
        }
    }

    private boolean isUploadComplete(File chunkDir, int totalParts) {
        File[] chunks = chunkDir.listFiles((dir, name) -> name.startsWith("part_"));
        if (chunks == null) {
            return false;
        }

        for (int i = 0; i < totalParts; i++) {
            File chunk = new File(chunkDir, "part_" + i);
            if (!chunk.exists() || chunk.length() == 0) {
                return false;
            }
        }
        return true;
    }

    private File mergeChunks(File chunkDir, String filename, int totalParts) throws IOException {
        File mergedFile = new File(System.getProperty("java.io.tmpdir"), filename);
        try (FileOutputStream fos = new FileOutputStream(mergedFile);
             FileChannel outChannel = fos.getChannel()) {

            for (int i = 0; i < totalParts; i++) {
                File chunk = new File(chunkDir, "part_" + i);
                if (!chunk.exists()) {
                    throw new IOException("分片文件丢失：part_" + i);
                }

                try (FileInputStream fis = new FileInputStream(chunk);
                     FileChannel inChannel = fis.getChannel()) {
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                }
            }
        }
        return mergedFile;
    }

    private MultipartFile convertToMultipartFile(final File file) {
        return new MultipartFile() {
            @Override
            public String getName() {
                return file.getName();
            }

            @Override
            public String getOriginalFilename() {
                return file.getName();
            }

            @Override
            public String getContentType() {
                return FileUtil.getFileType(FileUtil.getExtensionName(file.getName()));
            }

            @Override
            public boolean isEmpty() {
                return file.length() == 0;
            }

            @Override
            public long getSize() {
                return file.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return FileUtils.readFileToByteArray(file);
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(file);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                FileUtils.copyFile(file, dest);
            }
        };
    }

}