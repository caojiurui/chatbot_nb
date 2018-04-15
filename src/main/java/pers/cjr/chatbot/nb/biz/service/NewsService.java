package pers.cjr.chatbot.nb.biz.service;

import com.google.common.collect.Lists;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.cjr.chatbot.nb.biz.entity.*;
import pers.cjr.chatbot.nb.biz.utils.JSONUtil;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class NewsService  {

    @Autowired
    private ArticleSearchRepository articleSearchRepository;

    public void save(){
        News news = new News();
        news.setId(1L);
        news.setTitle("明天最流行的脏话是啥");
        news.setContent("你老妹个丝袜大白腿的");
        news.setCreateTime(new Date());
        articleSearchRepository.save(news);
    }

    public void search(){
        String queryString="明天";//搜索关键字
        QueryStringQueryBuilder builder=new QueryStringQueryBuilder(queryString);
        Iterable<News> searchResult = articleSearchRepository.search(builder);
        List<News> myList = Lists.newArrayList(searchResult);
        System.out.println(JSONUtil.objectToJsonStr(myList));
    }
}
