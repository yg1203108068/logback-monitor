package com.javayg.log.monitor.common.service;

import com.javayg.log.monitor.common.repository.BaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * @author 杨港
 * @date 2023/2/12
 * @description
 */
@Service
public abstract class BaseService<M, ID extends Serializable> {

    protected BaseRepository<M, ID> baseRepository;


    public BaseService() {
        super();
    }


    @Autowired(required = false) // required = false ：Spring容器在进行自动注入时取消必须注入，如果有则自动注入，如果没有则跳过
    public void setBaseDao(BaseRepository<M, ID> baseRepository) {
        this.baseRepository = baseRepository;
    }

    public List<M> findIds(Iterable<ID> ids) {
        return baseRepository.findAllById(ids);
    }

    public M save(M m) {
        return baseRepository.save(m);
    }

    public List<M> save(Iterable<M> entities) {
        return baseRepository.saveAll(entities);
    }

    public List<M> findAll() {
        return baseRepository.findAll();
    }


    /**
     * 根据主键删除相应实体
     *
     * @param id 主键
     */
    public void delete(ID id) {
        if (id != null && exists(id)) {
            baseRepository.deleteById(id);
        }
    }

    /**
     * 删除实体
     *
     * @param m 实体
     */
    public void delete(M m) {
        baseRepository.delete(m);
    }

    /**
     * 根据主键删除相应实体
     *
     * @param ids 实体
     */
    public void delete(ID[] ids) {
        for (ID id : ids) {
            delete(id);
        }
    }


    /**
     * 按照主键查询
     *
     * @param id 主键
     * @return 返回id对应的实体
     */
    @Transactional(readOnly = true)
    public Optional<M> findOne(ID id) {
        if (id == null)
            return null;
        return baseRepository.findById(id);
    }

    /**
     * 实体是否存在
     *
     * @param id 主键
     * @return 存在 返回true，否则false
     */
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        if (id == null)
            return false;
        return baseRepository.existsById(id);
    }

    /**
     * 统计实体总数
     *
     * @return 实体总数
     */
    @Transactional(readOnly = true)
    public long count() {
        return baseRepository.count();
    }

    /**
     * 按照顺序查询所有实体
     *
     * @param sort
     * @return
     */
    @Transactional(readOnly = true)
    public List<M> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    /**
     * 分页及排序查询实体
     *
     * @param pageable 分页及排序数据
     * @return
     */
    @Transactional(readOnly = true)
    public Page<M> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

    public Page<M> findByPage(M m, Pageable pageable) {
        return baseRepository.findAll(Example.of(m), pageable);
    }


    @SuppressWarnings("unchecked")
    protected Class<M> getEntityClass() {
        Type t = getClass().getGenericSuperclass();
        Type trueType = ((ParameterizedType)t).getActualTypeArguments()[0];
        Class<M> entityClass = (Class<M>)trueType;
        return entityClass;
    }

    private class JPAQueryFactory {
    }
}
