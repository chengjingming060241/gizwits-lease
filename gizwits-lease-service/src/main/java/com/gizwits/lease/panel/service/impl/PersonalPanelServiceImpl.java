package com.gizwits.lease.panel.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.enums.PanelModuleItemType;
import com.gizwits.lease.enums.PanelModuleType;
import com.gizwits.lease.enums.PanelType;
import com.gizwits.lease.enums.ShowType;
import com.gizwits.lease.panel.dao.PersonalPanelDao;
import com.gizwits.lease.panel.dto.ItemDto;
import com.gizwits.lease.panel.dto.ItemForUpdateDto;
import com.gizwits.lease.panel.dto.MainPageShowPanelDto;
import com.gizwits.lease.panel.dto.ModuleItemDto;
import com.gizwits.lease.panel.entity.Panel;
import com.gizwits.lease.panel.entity.PersonalPanel;
import com.gizwits.lease.panel.service.PanelService;
import com.gizwits.lease.panel.service.PersonalPanelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
@Service
public class PersonalPanelServiceImpl extends ServiceImpl<PersonalPanelDao, PersonalPanel> implements PersonalPanelService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalPanelServiceImpl.class);

    @Autowired
    private PanelService panelService;


    @Override
    public boolean updateItemValue(Integer sysUserId, Integer moduleItem, String itemValue, String itemOdd, Integer panelType) {

        //该条记录不在personalPanel表里
        if (exist(sysUserId) && !exist(sysUserId, panelType, moduleItem)) {
            //插入该条记录
            addPanelModuleItem(sysUserId, panelType, moduleItem);
        } else if (!exist(sysUserId)) {
            //用户面板表中没有数据，则初始化
            init(sysUserId);
        }

        return updateItem(sysUserId, panelType, moduleItem, itemValue, itemOdd);
    }

    @Override
    public boolean updateDataItemValue(Integer sysUserId, Integer moduleItem, String itemValue, String itemOdd) {
        return updateItemValue(sysUserId, moduleItem, itemValue, itemOdd, PanelType.DATA.getCode());
    }

    @Override
    public boolean updateChartItemValue(Integer sysUserId, Integer moduleItem, String itemValue) {
        return updateItemValue(sysUserId, moduleItem, itemValue, null, PanelType.CHART.getCode());
    }

    @Override
    public MainPageShowPanelDto getCurrentShowPanelData(SysUser current) {
        initIfNecessary(current);
        List<PersonalPanel> personalPanels = selectList(new EntityWrapper<PersonalPanel>().eq("user_id", current.getId()).eq("is_show", ShowType.DISPLAY.getCode()));
        List<Panel> panels = panelService.selectBatchIds(personalPanels.stream().map(PersonalPanel::getPanelId).collect(Collectors.toList()));
        Map<Integer, Panel> panelMap = panels.stream().collect(Collectors.toMap(Panel::getId, item -> item));
        personalPanels = personalPanels.stream().sorted(Comparator.comparingInt(PersonalPanel::getSort)).collect(Collectors.toList());
        MainPageShowPanelDto result = new MainPageShowPanelDto();
        result.setItems(new ArrayList<>(personalPanels.size()));
        personalPanels.forEach(personalPanel -> {
            ModuleItemDto item = getModuleItemDto(panelMap.get(personalPanel.getPanelId()), personalPanel);
            result.getItems().add(item);
        });
        result.setSysUserId(current.getId());
        return result;
    }

    @Override
    public Map<String, Map<String, List<ItemDto>>> getEditPanelData(SysUser current) {
        initIfNecessary(current);
        List<PersonalPanel> personalPanels = selectList(new EntityWrapper<PersonalPanel>().eq("user_id", current.getId()));
        List<Panel> panels = panelService.selectBatchIds(personalPanels.stream().map(PersonalPanel::getPanelId).collect(Collectors.toList()));
        Map<Integer, Panel> panelMap = panels.stream().collect(Collectors.toMap(Panel::getId, item -> item));
        personalPanels = personalPanels.stream().sorted(Comparator.comparingInt(PersonalPanel::getSort)).collect(Collectors.toList());
        List<ItemDto> items = new ArrayList<>(personalPanels.size());
        personalPanels.forEach(personalPanel -> {
            ItemDto item = getItemDto(panelMap.get(personalPanel.getPanelId()), personalPanel);
            items.add(item);
        });

        //先对PanelType进行分组
        Map<String, List<ItemDto>> panelGroup = items.stream().collect(Collectors.groupingBy(ItemDto::getPanelTypeName));
        Map<String, Map<String, List<ItemDto>>> result = new TreeMap<>(Comparator.comparingInt(PanelType::getCode));
        panelGroup.forEach((s, itemDtos) -> {
            //再对PanelModuleType进行分组
            Map<String, List<ItemDto>> map = new TreeMap<>(Comparator.comparingInt(PanelModuleType::getCode));
            map.putAll(itemDtos.stream().collect(Collectors.groupingBy(ItemDto::getModuleName)));
            result.put(s, map);
        });

        return result;
    }

    @Override
    public boolean update(ItemForUpdateDto dto) {
        List<PersonalPanel> fromDb = selectBatchIds(dto.getItems().stream().map(ItemForUpdateDto.UpdateDto::getId).collect(Collectors.toList()));
        Map<Integer, Integer> idToShow = dto.getItems().stream().collect(Collectors.toMap(ItemForUpdateDto.UpdateDto::getId, ItemForUpdateDto.UpdateDto::getIsShow));
        List<PersonalPanel> needToUpdate = fromDb.stream().filter(item -> !Objects.equals(item.getIsShow(), idToShow.get(item.getId()))).collect(Collectors.toList());
        needToUpdate.forEach(item -> item.setIsShow(idToShow.get(item.getId())));
        return updateBatchById(needToUpdate);
    }

    private void initIfNecessary(SysUser current) {
        if (!exist(current.getId())) {
            init(current.getId());
        }

        //在panel表新增的记录
        if (hasNewAddItem(current.getId())) {
            List<Panel> panels = panelService.selectList(null);
            List<PersonalPanel> personalPanels = selectList(new EntityWrapper<PersonalPanel>().eq("user_id", current.getId()));
            Set<Integer> alreadyHasPanel = personalPanels.stream().map(PersonalPanel::getPanelId).collect(Collectors.toSet());
            List<Panel> needToAdd = panels.stream().filter(item -> !alreadyHasPanel.contains(item.getId())).collect(Collectors.toList());
            List<PersonalPanel> toAdd = new ArrayList<>(needToAdd.size());
            needToAdd.forEach(item -> toAdd.add(panelToPersonalPanel(current.getId(), item)));
            insertBatch(toAdd);
        }
    }

    private boolean hasNewAddItem(Integer userId) {
        return panelService.selectCount(new EntityWrapper<>()) != selectCount(new EntityWrapper<PersonalPanel>().eq("user_id", userId));
    }

    private ItemDto getItemDto(Panel panel, PersonalPanel personalPanel) {
        ItemDto item = new ItemDto();
        item.setId(personalPanel.getId());
        item.setIsShow(personalPanel.getIsShow());
        item.setModuleItemName(PanelModuleItemType.getDesc(panel.getModuleItem()));
        item.setModuleName(PanelModuleType.getDesc(panel.getModule()));
        item.setPanelTypeName(PanelType.getDesc(panel.getType()));
        item.setSort(panel.getSort());
        item.setItemId(panel.getModuleItem());
        return item;
    }

    private ModuleItemDto getModuleItemDto(Panel panel, PersonalPanel personalPanel) {
        ModuleItemDto item = new ModuleItemDto();
        item.setId(personalPanel.getId());
        item.setItemId(panel.getModuleItem());
        item.setItemName(PanelModuleItemType.getDesc(panel.getModuleItem()));
        item.setItemValue(personalPanel.getItemValue());
        item.setUri(panel.getUri());
        item.setSort(personalPanel.getSort());
        return item;
    }

    private boolean updateItem(Integer sysUserId, Integer panelType, Integer moduleItem, String itemValue, String itemOdd) {
        Panel panel = resolvePanel(panelType, moduleItem);
        if (Objects.nonNull(panel)) {
            PersonalPanel personalPanel = resolvePersonalPanel(sysUserId, panel.getId());
            if (Objects.nonNull(personalPanel)) {
                personalPanel.setItemValue(itemValue);
                personalPanel.setItemOdd(itemOdd);
                personalPanel.setUtime(new Date());
                return updateById(personalPanel);
            }
        }
        return false;
    }

    private void init(Integer sysUserId) {
        if (exist(sysUserId)) {
            return;
        }

        List<Panel> panels = panelService.selectList(null);
        List<PersonalPanel> personalPanels = new LinkedList<>();
        panels.forEach(panel -> {
            PersonalPanel personalPanel = panelToPersonalPanel(sysUserId, panel);
            personalPanels.add(personalPanel);
        });
        insertBatch(personalPanels);
    }

    private void addPanelModuleItem(Integer sysUserId, Integer panelType, Integer moduleItem) {
        Panel panel = resolvePanel(panelType, moduleItem);
        if (Objects.nonNull(panel)) {
            PersonalPanel personalPanel = panelToPersonalPanel(sysUserId, panel);
            insert(personalPanel);
        }
    }

    private PersonalPanel panelToPersonalPanel(Integer sysUserId, Panel panel) {
        List<Integer> defaultDisplayPanelItemIds = Arrays.asList(SysConfigUtils.get(CommonSystemConfig.class).getDefaultDisplayPanelItem());
        PersonalPanel personalPanel = new PersonalPanel();
        personalPanel.setCtime(new Date());
        personalPanel.setPanelId(panel.getId());
        personalPanel.setUserId(sysUserId);
        personalPanel.setSort(panel.getSort());
        //1.显示，0.不显示
        personalPanel.setIsShow(defaultDisplayPanelItemIds.contains(panel.getId()) ? ShowType.DISPLAY.getCode() : ShowType.HIDDEN.getCode());
        return personalPanel;
    }

    private boolean exist(Integer sysUserId) {
        return selectCount(new EntityWrapper<PersonalPanel>().eq("user_id", sysUserId)) > 0;
    }

    private boolean exist(Integer sysUserId, Integer panelType, Integer moduleItem) {
        Panel panel = resolvePanel(panelType, moduleItem);
        return Objects.nonNull(panel) && Objects.nonNull(resolvePersonalPanel(sysUserId, panel.getId()));
    }

    private Panel resolvePanel(Integer panelType, Integer moduleItem) {
        Panel panel = panelService.selectOne(new EntityWrapper<Panel>().eq("type", panelType).eq("module_item", moduleItem));
        if (Objects.isNull(panel)) {
            LOGGER.error("type={},module_item={}的记录不存在", panelType, moduleItem);
        }
        return panel;
    }

    private PersonalPanel resolvePersonalPanel(Integer sysUserId, Integer panelId) {
        return selectOne(new EntityWrapper<PersonalPanel>().eq("user_id", sysUserId).eq("panel_id", panelId));
    }
}
