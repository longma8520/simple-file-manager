package cn.tyson.simplefilemanager.service.impl;

import cn.tyson.simplefilemanager.model.entity.TextFile;
import cn.tyson.simplefilemanager.model.vo.PagerResult;
import cn.tyson.simplefilemanager.model.vo.TextFileVo;
import cn.tyson.simplefilemanager.repository.TextFileRepository;
import cn.tyson.simplefilemanager.service.TextFileService;
import cn.tyson.simplefilemanager.utils.FileUtils;
import cn.tyson.simplefilemanager.utils.LockUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TextFileServiceImpl implements TextFileService {

    private static final String SUFFIX_TXT = ".txt";

    private static final String PREFIX_LOCK_KEY = "lock:";

    @Autowired
    private TextFileRepository textFileRepository;

    @Autowired
    private LockUtils lockUtils;

    @Override
    public PagerResult<TextFile> getFiles(int offset, int limit) {
        PageRequest pageRequest = PageRequest.of(offset/limit,
                limit, Sort.by(Sort.Order.desc("updateTime")));
        Page<TextFile> page = textFileRepository.findAll(pageRequest);
        return new PagerResult(page.getContent(), page.getTotalElements());
    }

    @Override
    public TextFile getFile(Integer id) {
        Optional<TextFile> optional = textFileRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new IllegalArgumentException("file not exist");

    }

    @Override
    public TextFile saveFile(TextFileVo textFileVo) {
        validFileName(textFileVo.getFileName());
        String path = FileUtils.writeTxt(textFileVo.getText(), textFileVo.getFileName() + SUFFIX_TXT);
        TextFile textFile = new TextFile();
        textFile.setText(textFileVo.getText());
        textFile.setFileName(textFileVo.getFileName());
        textFile.setPath(path);
        return textFileRepository.save(textFile);
    }

    @Override
    public TextFile updateFile(Integer id, TextFileVo textFileVo, String sessionId) {
        try {
            TextFile textFile = getFile(id);
            if (!textFile.getFileName().equals(textFileVo.getFileName())) {
                validFileName(textFileVo.getFileName());
            }
            String path = FileUtils.writeTxt(textFileVo.getText(), textFileVo.getFileName() + SUFFIX_TXT);
            textFile.setFileName(textFileVo.getFileName());
            textFile.setText(textFileVo.getText());
            textFile.setPath(path);
            return textFileRepository.save(textFile);
        } finally {
            String key = PREFIX_LOCK_KEY + id;
            lockUtils.unlock(key, sessionId);
        }

    }

    @Override
    public ResponseEntity<byte[]> download(Integer id) throws UnsupportedEncodingException {
        TextFile textFile = getFile(id);
        if (textFile == null) {
            throw new IllegalArgumentException("file not exist");
        }
        return FileUtils.buildResponseEntity(textFile.getPath());
    }

    /**
     * 校验文件名是否已存在
     *
     * @param fileName 文件名
     */
    private void validFileName(String fileName) {
        if (textFileRepository.countByFileName(fileName) > 0) {
            throw new IllegalArgumentException("file name exist");
        }
    }

    @Override
    public Boolean lock(Integer id, String sessionId) {
        String key = PREFIX_LOCK_KEY + id;
        return lockUtils.tryLock(key, sessionId);
    }

    @Override
    public Boolean unlock(Integer id, String sessionId) {
        String key = PREFIX_LOCK_KEY + id;
        return lockUtils.unlock(key, sessionId);
    }

    @Override
    public Boolean relock(Integer id, String sessionId) {
        String key = PREFIX_LOCK_KEY + id;
        return lockUtils.relock(key, sessionId);
    }
}
