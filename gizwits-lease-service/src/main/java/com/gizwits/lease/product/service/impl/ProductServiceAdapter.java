package com.gizwits.lease.product.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.lease.product.dao.ProductDao;
import com.gizwits.lease.product.entity.Product;

/**
 * @author lilh
 * @date 2017/7/8 14:47
 */
public class ProductServiceAdapter extends ServiceImpl<ProductDao, Product> {

    /*@Autowired
    private DefaultCommonRoleResolverManager defaultCommonRoleResolverManager;

    @Autowired
    private SysUserRoleTypeHelperResolver helperResolver;

    @Override
    public Product selectById(Serializable id) {
        Wrapper<Product> wrapper = new EntityWrapper<>();
        wrap(wrapper, wrapper1 -> wrapper1.eq("id", id));
        return super.selectOne(wrapper);
    }

    @Override
    public List<Product> selectBatchIds(List<? extends Serializable> idList) {
        Wrapper<Product> wrapper = new EntityWrapper<>();
        wrap(wrapper, wrapper1 -> wrapper1.in("id", idList));
        return super.selectList(wrapper);
    }

    @Override
    public Product selectOne(Wrapper<Product> wrapper) {
        wrap(wrapper, wrapper1 -> {
        });
        return super.selectOne(wrapper);
    }

    @Override
    public int selectCount(Wrapper<Product> wrapper) {
        wrap(wrapper, wrapper1 -> {
        });
        return super.selectCount(wrapper);
    }

    @Override
    public List<Product> selectList(Wrapper<Product> wrapper) {
        wrap(wrapper, wrapper1 -> {
        });
        return super.selectList(wrapper);
    }

    @Override
    public Page<Product> selectPage(Page<Product> page) {
        return this.selectPage(page, new EntityWrapper<>());
    }

    @Override
    public Page<Product> selectPage(Page<Product> page, Wrapper<Product> wrapper) {
        wrap(wrapper, wrapper1 -> {
        });
        return super.selectPage(page, wrapper);
    }

    @SuppressWarnings("unchecked")
    private void wrap(Wrapper<Product> wrapper, Worker worker) {
        SysUserRoleTypeHelper helper = helperResolver.resolve();
        CommonRoleResolver resolver = defaultCommonRoleResolverManager.getCommonRoleResolver(helper.getCommonRole());
        resolver.wrapProduct(wrapper, helper);
        wrapper.eq("is_deleted", 0);
        worker.wrap(wrapper);
    }

    @FunctionalInterface
    interface Worker {
        void wrap(Wrapper<Product> wrapper);
    }*/
}
