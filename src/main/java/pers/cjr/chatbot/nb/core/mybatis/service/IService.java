package pers.cjr.chatbot.nb.core.mybatis.service;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.RowBounds;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 *
 * 顶级 Service
 *
 * Created by JiangYong(JiangYong@yahoo.com) on 2016-06-29.
 */
public interface IService<T, I> {

    /**
     * <p>
     * 插入一条记录
     * </p>
     * @param entity
     * 				实体对象
     * @return boolean
     */
    boolean insert(T entity);

    /**
     * <p>
     * 插入一条记录（选择字段， null 字段不插入）
     * </p>
     * @param entity
     * 				实体对象
     * @return boolean
     */
    boolean insertSelective(T entity);


    /**
     * <p>
     * 根据 ID 删除
     * </p>
     * @param id
     * 			主键ID
     * @return boolean
     */
    boolean deleteById(I id);

    /**
     * <p>
     *     根据主键字符串进行删除，类中只有存在一个带有@Id注解的字段
     * </p>
     *
     * @param ids 如 "1,2,3,4"
     * @return int
     */
    int deleteByIds(String ids);

    /**
     * <p>
     * 根据 entity 条件，删除记录
     * </p>
     * @param entity
     * 				实体对象
     * @return boolean
     */
    boolean delete(T entity);


    /**
     * <p>
     * 根据 查询 条件，删除记录
     * </p>
     * @param condition
     * 				查询条件
     * @return boolean
     */
    boolean deleteByCondition(Condition condition);

    /**
     * <p>
     * 根据 ID 修改
     * </p>
     * @param entity
     * 				实体对象
     * @return boolean
     */
    boolean updateById(T entity);


    /**
     * <p>
     * 根据 ID 选择修改（选择字段， null 字段不更新）
     * </p>
     * @param entity
     * 				实体对象
     * @return boolean
     */
    boolean updateSelectiveById(T entity);


    /**
     * <p>
     * 根据 查询 条件，更新记录
     * </p>
     * @param entity
     * 				实体对象
     * @param condition
     * 				查询条件
     * @return boolean
     */
    boolean updateByCondition(T entity, Condition condition);

    /**
     * <p>
     * 根据 查询 条件，更新记录（选择字段， null 字段不更新）
     * </p>
     * @param entity
     * 				实体对象
     * @param condition
     * 				查询条件
     * @return boolean
     */
    boolean updateSelectiveByCondition(T entity, Condition condition);

    /**
     * <p>
     * 根据 ID 查询
     * </p>
     * @param id
     * 			主键ID
     * @return T
     */
    T selectById(I id);


    /**
     * <p>
     * 根据主键字符串进行查询，类中只有存在一个带有@Id注解的字段
     * </p>
     *
     * @param ids 如 "1,2,3,4"
     * @return List<T>
     */
    List<T> selectByIds(String ids);

    /**
     * <p>
     * 根据 实体 查询
     * </p>
     * @param entity
     * 				实体对象
     * @return T
     */
    T selectOne(T entity);

    /**
     * <p>
     * 查询所有记录
     * </p>
     * @return List<T>
     */
    List<T> selectAll();


	/**
	 * <p>
	 * 根据 条件 查询分页
	 * </p>
	 * @param pageNum  页码
	 * @param pageSize 每页显示数量
	 * @return Page<T>
	 */
	PageInfo<T> selectAll(int pageNum, int pageSize);

	/**
     * <p>
     * 根据 条件 查询
     * </p>
     * @param condition
     * 				查询条件
     * @return List<T>
     */
    List<T> selectByCondition(Condition condition);

	/**
	 * <p>
	 * 根据 条件 查询分页
	 * </p>
	 * @param condition
	 * 				查询条件
	 * @return List<T>
	 */
	PageInfo<T> selectByCondition(Condition condition, int pageNum, int pageSize);


	/**
     * <p>
     * 根据 条件 查询
     * </p>
     * @param condition
     * 				查询条件
     * @param rowBounds
     * 				分页条件
     * @return List<T>
     */
    List<T> selectByConditionAndRowBounds(Condition condition, RowBounds rowBounds);


	/**
     * <p>
     * 根据 查询 统计
     * </p>
     * @param condition
     * 				查询条件
     * @return int
     */
    int countByCondition(Condition condition);
}
