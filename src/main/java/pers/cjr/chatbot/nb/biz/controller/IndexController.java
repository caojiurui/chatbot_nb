package pers.cjr.chatbot.nb.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pers.cjr.chatbot.nb.biz.service.NewsService;


@Controller
public class IndexController {
    @Autowired
    private Environment env;
    @Autowired
    private NewsService indexService;


    @RequestMapping("/index")
    public String index(Model model) {
        model.addAttribute("env",env);
//        model.addAttribute("list", JSONUtil.objectToJsonStr(indexService.getList()));
        model.addAttribute("list", "11111111");
        return "index";
    }

}
