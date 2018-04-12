package pers.cjr.chatbot.nb.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import pers.cjr.chatbot.nb.biz.service.IndexService;
import pers.cjr.chatbot.nb.biz.utils.JSONUtil;
import pers.cjr.chatbot.nb.table.mapper.UserMapper;


@Controller
public class IndexController {
    @Autowired
    private Environment env;
    @Autowired
    private IndexService indexService;

    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("env",env);
        model.addAttribute("list", JSONUtil.objectToJsonStr(indexService.getList()));
        return "index";
    }

}
