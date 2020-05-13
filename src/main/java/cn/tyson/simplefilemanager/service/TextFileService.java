package cn.tyson.simplefilemanager.service;

import cn.tyson.simplefilemanager.model.entity.TextFile;
import cn.tyson.simplefilemanager.model.vo.PagerResult;
import cn.tyson.simplefilemanager.model.vo.TextFileVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public interface TextFileService {

    /**
     * 获取文件列表
     *
     * @param offset 偏移量
     * @param limit  页大小
     * @return
     */
    PagerResult<TextFile> getFiles(int offset, int limit);

    /**
     * 根据id获取文件
     *
     * @param id
     * @return
     */
    TextFile getFile(Integer id);

    /**
     * 保存文件
     *
     * @param textFileVo
     */
    TextFile saveFile(TextFileVo textFileVo);

    /**
     * 修改文件
     *
     * @param id
     * @param textFileVo
     */
    TextFile updateFile(Integer id, TextFileVo textFileVo, String sessionId);

    /**
     * 下载文件
     *
     * @param id 文件id
     * @return
     */
    ResponseEntity<byte[]> download(Integer id) throws UnsupportedEncodingException;

    /**
     * 加锁
     *
     * @param id        文件id
     * @return
     */
    Boolean lock(Integer id, String sessionId);

    /**
     * 解锁
     *
     * @param id 文件id
     */
    Boolean unlock(Integer id, String sessionId);

    /**
     * 续锁
     *
     * @param id 文件id
     */
    Boolean relock(Integer id, String sessionId);
}
