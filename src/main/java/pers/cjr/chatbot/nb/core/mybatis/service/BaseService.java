package pers.cjr.chatbot.nb.core.mybatis.service;


import pers.cjr.chatbot.nb.core.mybatis.mapper.BaseMapper;

/**
 * Created by jiangyong on 2017/4/25.
 */
public abstract class BaseService<M extends BaseMapper<T>, T, I> extends ServiceImpl<M, T, I> {

}
