package cn.tyson.simplefilemanager.controller;

import cn.tyson.simplefilemanager.model.entity.TextFile;
import cn.tyson.simplefilemanager.model.vo.PagerResult;
import cn.tyson.simplefilemanager.model.vo.TextFileVo;
import cn.tyson.simplefilemanager.service.TextFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/v0.1/files")
public class TextFileController {

    @Autowired
    private TextFileService textFileService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public PagerResult<TextFile> getFiles(@RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
                                          @RequestParam(value = "limit", defaultValue = "10", required = false) int limit) {
        return textFileService.getFiles(offset, limit);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public TextFile getFile(@PathVariable(value = "id") Integer id) {
        return textFileService.getFile(id);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public TextFile saveFile(@Valid @RequestBody TextFileVo textFileVo) {
        return textFileService.saveFile(textFileVo);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public TextFile updateFile(@PathVariable(value = "id") Integer id,
                               @RequestBody TextFileVo textFileVo,
                               @RequestParam(value = "session_id") String sessionId) {
        return textFileService.updateFile(id, textFileVo, sessionId);
    }

    @RequestMapping(value = "/{id}/actions/download", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable(value = "id") Integer id) throws UnsupportedEncodingException {
        return textFileService.download(id);
    }

    @RequestMapping(value = "/{id}/actions/lock", method = RequestMethod.POST)
    public Map<String, Object> lock(@PathVariable(value = "id") Integer id) {
        Map<String, Object> map = new HashMap<>(2);
        String sessionId = UUID.randomUUID().toString();
        Boolean lock = textFileService.lock(id, sessionId);
        map.put("success", lock);
        if (lock) {
            map.put("session_id", sessionId);
        } else {
            map.put("session_id", null);
        }
        return map;
    }

    @RequestMapping(value = "/{id}/actions/unlock", method = RequestMethod.DELETE)
    public Map<String, Object> unlock(@PathVariable(value = "id") Integer id,
                                      @RequestParam(value = "session_id") String sessionId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("success", textFileService.unlock(id, sessionId));
        return map;
    }

    @RequestMapping(value = "/{id}/actions/relock", method = RequestMethod.PUT)
    public Map<String, Object> relock(@PathVariable(value = "id") Integer id,
                                      @RequestParam(value = "session_id") String sessionId) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("success", textFileService.relock(id, sessionId));
        return map;
    }

}
