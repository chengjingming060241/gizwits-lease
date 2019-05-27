package com.gizwits.lease.china.service.impl;

import com.gizwits.lease.china.entity.ChinaArea;
import com.gizwits.lease.china.dao.ChinaAreaDao;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.china.entity.china.dto.AreaDto;
import com.gizwits.lease.china.service.ChinaAreaService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 全国省市行政编码表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
@Service
public class ChinaAreaServiceImpl extends ServiceImpl<ChinaAreaDao, ChinaArea> implements ChinaAreaService {
	
    @Override
    public List<AreaDto> getAreas(Integer code) {
        EntityWrapper<ChinaArea> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("parent_code",code);
        List<AreaDto> areaDtos = new ArrayList<>();
        List<ChinaArea> chinaAreaList =  selectList(entityWrapper);
        for(ChinaArea area:chinaAreaList){
            AreaDto areaDto = new AreaDto();
            String name = area.getName();
            areaDto.setName(name);
            if(name.contains("市") && code == 0){
                Integer code1 = getCodeByCode(area.getCode());
                areaDto.setCode(code1);
            }else {
                areaDto.setCode(area.getCode());
            }
            areaDtos.add(areaDto);
        }
        return areaDtos;
    }

    public Integer getCodeByCode(Integer code){
        EntityWrapper<ChinaArea> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("parent_code",code);
        return selectOne(entityWrapper).getCode();
    }

    public ChinaArea getAreaByName(String name, String parentName){
        EntityWrapper<ChinaArea> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("name",name).eq("parent_name",parentName);
        return selectOne(entityWrapper);
    }

}
