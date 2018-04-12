package pers.cjr.chatbot.nb.biz.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.cjr.chatbot.nb.table.mapper.UserMapper;
import pers.cjr.chatbot.nb.table.model.User;

import java.util.List;

@Service
public class IndexService {
    @Autowired
    UserMapper userMapper;

    public List<User> getList (){
        return userMapper.selectAll();
    }
}
