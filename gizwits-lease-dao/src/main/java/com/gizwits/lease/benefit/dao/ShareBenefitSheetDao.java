package com.gizwits.lease.benefit.dao;

import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
  * 分润账单表 Mapper 接口
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
public interface ShareBenefitSheetDao extends BaseMapper<ShareBenefitSheet> {

    Double countTotal(@Param("ids")List<Integer> ids, @Param("begin")Date begin,@Param("end")Date end);

}