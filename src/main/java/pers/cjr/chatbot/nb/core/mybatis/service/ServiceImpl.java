package pers.cjr.chatbot.nb.core.mybatis.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import pers.cjr.chatbot.nb.core.mybatis.mapper.BaseMapper;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * IService 实现类（ 泛型：M 是 mapper 对象， T 是实体 ， I 是主键泛型 ）
 *
 * Created by JiangYong(JiangYong@yahoo.com) on 2016-06-29.
 */
public abstract class ServiceImpl<M extends BaseMapper<T>, T, I> implements IService<T, I>  {

	@Autowired
    protected M baseMapper;

    /**
     * 判断数据库操作是否成功
     *
     * @param result
     *            数据库操作返回影响条数
     * @return boolean
     */
    protected boolean retBool(int result) {
        return (result >= 1);
    }



    @Override
    public boolean insert(T entity) {
        return retBool(baseMapper.insert(entity));
    }

    @Override
    public boolean insertSelective(T entity) {
        return retBool(baseMapper.insertSelective(entity));
    }




    @Override
    public boolean deleteById(I id) {
        return retBool(baseMapper.deleteByPrimaryKey(id));
    }

    @Override
    public int deleteByIds(String ids) {
        return baseMapper.deleteByIds(ids);
    }

    @Override
    public boolean delete(T entity) {
        return retBool(baseMapper.delete(entity));
    }

    @Override
    public boolean deleteByCondition(Condition condition) {
        return retBool(baseMapper.deleteByExample(condition));
    }




    @Override
    public boolean updateById(T entity) {
        return retBool(baseMapper.updateByPrimaryKey(entity));
    }

    @Override
    public boolean updateSelectiveById(T entity) {
        return retBool(baseMapper.updateByPrimaryKeySelective(entity));
    }

    @Override
    public boolean updateByCondition(T entity, Condition condition) {
        return retBool(baseMapper.updateByExample(entity, condition));
    }

    @Override
    public boolean updateSelectiveByCondition(T entity, Condition condition) {
        return retBool(baseMapper.updateByExampleSelective(entity, condition));
    }


    @Override
    public T selectById(I id) {
        return baseMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<T> selectByIds(String ids) {
        return baseMapper.selectByIds(ids);
    }

    @Override
    public T selectOne(T entity) {
        return baseMapper.selectOne(entity);
    }

    @Override
    public List<T> selectAll() {
        return baseMapper.selectAll();
    }

    @Override
    public PageInfo<T> selectAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<T> list = baseMapper.selectAll();
        return new PageInfo<T>(list);
    }

    @Override
    public List<T> selectByCondition(Condition condition) {
        return baseMapper.selectByExample(condition);
    }

    @Override
    public PageInfo<T> selectByCondition(Condition condition, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<T> list = baseMapper.selectByExample(condition);
        return new PageInfo<T>(list) ;
    }

    @Override
    public List<T> selectByConditionAndRowBounds(Condition condition, RowBounds rowBounds) {
        return baseMapper.selectByExampleAndRowBounds(condition, rowBounds);
    }



    @Override
    public int countByCondition(Condition condition) {
        return baseMapper.selectCountByExample(condition);
    }


}
