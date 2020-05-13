package cn.tyson.simplefilemanager.controller.web;

import cn.tyson.simplefilemanager.service.TextFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "")
public class WebController {

    @Autowired
    private TextFileService textFileService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index(@RequestParam(value = "offset", defaultValue = "0", required = false) int offset,
                              @RequestParam(value = "limit", defaultValue = "50", required = false) int limit) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("data", textFileService.getFiles(offset, limit));
        return modelAndView;
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public ModelAndView newPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("new");
        return modelAndView;
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable(value = "id") Integer id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("edit");
        String sessionId = UUID.randomUUID().toString();
        modelAndView.addObject("lock", textFileService.lock(id, sessionId));
        modelAndView.addObject("sessionId", sessionId);
        modelAndView.addObject("data", textFileService.getFile(id));
        return modelAndView;
    }
}
