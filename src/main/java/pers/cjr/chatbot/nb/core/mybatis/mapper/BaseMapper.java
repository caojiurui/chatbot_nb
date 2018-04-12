package pers.cjr.chatbot.nb.core.mybatis.mapper;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 继承自己的MyMapper
 * 特别注意，该接口不能被扫描到，否则会出错
 *
 * @author jiangyong
 * @since 2017/4/20
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T> {

}
