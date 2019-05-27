package com.gizwits.lease.export.web;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.benefit.dto.ShareBenefitSheetForListDto;
import com.gizwits.lease.benefit.dto.ShareBenefitSheetForQueryDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleListDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleQueryDto;
import com.gizwits.lease.benefit.service.ShareBenefitRuleService;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.UserStatus;
import com.gizwits.lease.constant.WalletEnum;
import com.gizwits.lease.device.entity.dto.DeviceAlarmQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceAlramInfoDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaListDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceShowDto;
import com.gizwits.lease.device.service.DeviceAlarmService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.exceptions.ExcelException;
import com.gizwits.lease.manager.dto.AgentForListDto;
import com.gizwits.lease.manager.dto.AgentForQueryDto;
import com.gizwits.lease.manager.dto.OperatorForListDto;
import com.gizwits.lease.manager.dto.OperatorForQueryDto;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.OperatorService;
import com.gizwits.lease.order.dto.OrderListDto;
import com.gizwits.lease.order.dto.OrderQueryDto;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.product.dto.ProductCategoryForListDto;
import com.gizwits.lease.product.dto.ProductCategoryForQueryDto;
import com.gizwits.lease.product.dto.ProductForListDto;
import com.gizwits.lease.product.dto.ProductQueryDto;
import com.gizwits.lease.product.dto.ProductServiceListQuerytDto;
import com.gizwits.lease.product.dto.ProductServicecModeListDto;
import com.gizwits.lease.product.service.ProductCategoryService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.product.service.ProductServiceModeService;
import com.gizwits.lease.refund.dto.RefundInfoDto;
import com.gizwits.lease.refund.dto.RefundListQueryDto;
import com.gizwits.lease.refund.service.RefundApplyService;
import com.gizwits.lease.user.dto.UserForListDto;
import com.gizwits.lease.user.dto.UserForQueryDto;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.utils.JxlExcelUtils;
import com.gizwits.lease.wallet.dto.DepositListDto;
import com.gizwits.lease.wallet.dto.DepositQueryDto;
import com.gizwits.lease.wallet.dto.UserWalletChargeListDto;
import com.gizwits.lease.wallet.dto.UserWalletChargeOrderQueryDto;
import com.gizwits.lease.wallet.service.UserWalletChargeOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller - 导出
 *
 * @author lilh
 * @date 2017/8/10 10:23
 */
@Api(description = "导出")
@Controller
@RequestMapping
public class ExportController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceAlarmService deviceAlarmService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private ProductServiceModeService productServiceModeService;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private ShareBenefitRuleService shareBenefitRuleService;

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;

    @Autowired
    private ProductCategoryService productCategoryService;

    @Autowired
    private UserWalletChargeOrderService userWalletChargeOrderService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private RefundApplyService refundApplyService;


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "设备列表导出", consumes = "application/json")
    @PostMapping("/device/device/export")
    public void device(@RequestBody @Valid RequestObject<ExportHelper<DeviceQueryDto, String>> requestObject, HttpServletResponse response) {
        ExportHelper<DeviceQueryDto, String> helper = requestObject.getData();
        Pageable<DeviceQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new DeviceQueryDto() : helper.getQuery());
        pageable.getQuery().setIds(helper.getIds());
        pageable.getQuery().setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        pageable.getQuery().setAccessableOwnerIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        pageable.getQuery().setOperatorAccountId(null);
        Page<DeviceShowDto> result = deviceService.listPage(pageable);
        export("设备列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "产品列表导出", consumes = "application/json")
    @PostMapping("/product/product/export")
    public void product(@RequestBody @Valid RequestObject<ExportHelper<ProductQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<ProductQueryDto, Integer> helper = requestObject.getData();
        Pageable<ProductQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new ProductQueryDto() : helper.getQuery());
        //按更新时间降序
        pageable.setOrderByField("utime");
        pageable.setAsc(false);
        pageable.getQuery().setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        SysUser current = sysUserService.getCurrentUser();
        Integer manufacturerAccountId = productService.resolveManufacturerAccount(current);
        if (Objects.nonNull(manufacturerAccountId)) {
            pageable.getQuery().setManufacturerAccountId(manufacturerAccountId);
        } else {
            pageable.getQuery().setAccessableUserIds(sysUserService.resolveAccessableUserIds(current));
        }
        pageable.getQuery().setIds(helper.getIds());
        Page<ProductForListDto> result = productService.page(pageable);
        export("产品列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "运营商列表导出", consumes = "application/json")
    @PostMapping("/operator/export")
    public void operator(@RequestBody @Valid RequestObject<ExportHelper<OperatorForQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<OperatorForQueryDto, Integer> helper = requestObject.getData();
        Pageable<OperatorForQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new OperatorForQueryDto() : helper.getQuery());
        pageable.getQuery().setIds(helper.getIds());
        pageable.getQuery().setUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        Page<OperatorForListDto> result = operatorService.page(pageable);
        export("运营商列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "代理商列表导出", consumes = "application/json")
    @PostMapping("/agent/export")
    public void agent(@RequestBody @Valid RequestObject<ExportHelper<AgentForQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<AgentForQueryDto, Integer> helper = requestObject.getData();
        Pageable<AgentForQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new AgentForQueryDto() : helper.getQuery());
        pageable.getQuery().setIds(helper.getIds());
        pageable.getQuery().setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        Page<AgentForListDto> result = agentService.page(pageable);
        export("代理商列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "用户列表导出", consumes = "application/json")
    @PostMapping("/user/normal/export")
    public void userNormal(@RequestBody @Valid RequestObject<ExportHelper<UserForQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<UserForQueryDto, Integer> helper = requestObject.getData();
        Pageable<UserForQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new UserForQueryDto() : helper.getQuery());
        pageable.getQuery().setStatus(UserStatus.NORMAL.getCode());
        pageable.getQuery().setIds(helper.getIds());
        Page<UserForListDto> result = userService.page(pageable);
        export("用户列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "告警设备列表导出", consumes = "application/json")
    @PostMapping("/device/deviceAlarm/export")
    public void deviceAlarm(@RequestBody @Valid RequestObject<ExportHelper<DeviceAlarmQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<DeviceAlarmQueryDto, Integer> helper = requestObject.getData();
        DeviceAlarmQueryDto query = Objects.isNull(helper.getQuery()) ? new DeviceAlarmQueryDto() : helper.getQuery();
        query.setCurrentPage(1);
        query.setPageSize(getDefaultExportSize());
        query.setIds(helper.getIds());
        List<DeviceAlramInfoDto> result = deviceAlarmService.listPage(query);
        export("告警设备列表", helper, result, response);
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "投放点列表导出", consumes = "application/json")
    @PostMapping("/device/deviceLaunchArea/export")
    public void deviceLaunchArea(@RequestBody @Valid RequestObject<ExportHelper<DeviceLaunchAreaQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<DeviceLaunchAreaQueryDto, Integer> helper = requestObject.getData();
        Pageable<DeviceLaunchAreaQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new DeviceLaunchAreaQueryDto() : helper.getQuery());
        pageable.getQuery().setIds(helper.getIds());
        pageable.getQuery().setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        Page<DeviceLaunchAreaListDto> result = deviceLaunchAreaService.getDeviceLaunchAreaListPage(pageable);
        export("投放点列表", helper, result.getRecords(), response);
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "收费模式列表导出", consumes = "application/json")
    @PostMapping("/product/productServiceMode/export")
    public void productServiceMode(@RequestBody @Valid RequestObject<ExportHelper<ProductServiceListQuerytDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<ProductServiceListQuerytDto, Integer> helper = requestObject.getData();
        Pageable<ProductServiceListQuerytDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new ProductServiceListQuerytDto() : helper.getQuery());
        pageable.getQuery().setIds(helper.getIds());
        pageable.getQuery().setCreatorId(sysUserService.getCurrentUser().getId());
        Page<ProductServicecModeListDto> result = productServiceModeService.getProductServiceModeListPage(pageable);
        export("收费模式列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "订单列表导出", consumes = "application/json")
    @PostMapping("/order/orderBase/export")
    public void orderBase(@RequestBody @Valid RequestObject<ExportHelper<OrderQueryDto, String>> requestObject, HttpServletResponse response) {
        ExportHelper<OrderQueryDto, String> helper = requestObject.getData();
        OrderQueryDto query = Objects.isNull(helper.getQuery()) ? new OrderQueryDto() : helper.getQuery();
        query.setCurrentPage(1);
        query.setPagesize(getDefaultExportSize());
        query.setSysUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        query.setIds(helper.getIds());
        query.setOperatorAccountId(null);
        Page<OrderListDto> result = orderBaseService.getOrderListDtoPage(query);
        export("订单列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分润规则列表导出", consumes = "application/json")
    @PostMapping("/benefit/shareBenefitRule/export")
    public void shareBenefitRule(@RequestBody @Valid RequestObject<ExportHelper<ShareBenefitRuleQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<ShareBenefitRuleQueryDto, Integer> helper = requestObject.getData();
        Pageable<ShareBenefitRuleQueryDto> pageable = getPageable();
        //按更新时间降序
        pageable.setOrderByField("utime");
        pageable.setAsc(false);
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new ShareBenefitRuleQueryDto() : helper.getQuery());
        pageable.getQuery().setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        pageable.getQuery().setCreatorId(sysUserService.getCurrentUser().getId());
        pageable.getQuery().setIds(helper.getIds());
        Page<ShareBenefitRuleListDto> result = shareBenefitRuleService.listPage(pageable);
        export("分润规则列表", helper, result.getRecords(), response);
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分润单列表导出", consumes = "application/json")
    @PostMapping("/benefit/shareBenefitSheet/export")
    public void shareBenefitSheet(@RequestBody @Valid RequestObject<ExportHelper<ShareBenefitSheetForQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<ShareBenefitSheetForQueryDto, Integer> helper = requestObject.getData();
        Pageable<ShareBenefitSheetForQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new ShareBenefitSheetForQueryDto() : helper.getQuery());
        pageable.getQuery().setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        pageable.getQuery().setIds(helper.getIds());
        Page<ShareBenefitSheetForListDto> result = shareBenefitSheetService.page(pageable);
        export("分润单列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "用户黑名单列表导出", consumes = "application/json")
    @PostMapping("/user/black/export")
    public void userBlack(@RequestBody @Valid RequestObject<ExportHelper<UserForQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<UserForQueryDto, Integer> helper = requestObject.getData();
        Pageable<UserForQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new UserForQueryDto() : helper.getQuery());
        pageable.getQuery().setStatus(UserStatus.BLACK.getCode());
        pageable.getQuery().setIds(helper.getIds());
        Page<UserForListDto> result = userService.page(pageable);
        export("用户黑名单列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "品类列表导出", consumes = "application/json")
    @PostMapping("/product/productCategory/export")
    public void productCategory(@RequestBody @Valid RequestObject<ExportHelper<ProductCategoryForQueryDto, Integer>> requestObject, HttpServletResponse response) {
        ExportHelper<ProductCategoryForQueryDto, Integer> helper = requestObject.getData();
        Pageable<ProductCategoryForQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new ProductCategoryForQueryDto() : helper.getQuery());
        pageable.getQuery().setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        pageable.getQuery().setIds(helper.getIds());
        Page<ProductCategoryForListDto> result = productCategoryService.page(pageable);
        export("品类列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "充值记录列表导出", consumes = "application/json")
    @PostMapping("/wallet/userWalletChargeOrder/export")
    public void userWalletChargeOrder(@RequestBody @Valid RequestObject<ExportHelper<DepositQueryDto, String>> requestObject, HttpServletResponse response) {
        ExportHelper<DepositQueryDto, String> helper = requestObject.getData();
        Pageable<DepositQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new DepositQueryDto() : helper.getQuery());
        pageable.getQuery().setIds(helper.getIds());
        pageable.getQuery().setWalletType(WalletEnum.BALENCE.getCode());
        Page<DepositListDto> result = userWalletChargeOrderService.listDeposit(pageable);
        export("充值记录列表", helper, result.getRecords(), response);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "退款列表导出", consumes = "application/json")
    @PostMapping("/refund/export")
    public void refundExport(@RequestBody @Valid RequestObject<ExportHelper<RefundListQueryDto, String>> requestObject, HttpServletResponse response) {
        ExportHelper<RefundListQueryDto, String> helper = requestObject.getData();
        Pageable<RefundListQueryDto> pageable = getPageable();
        pageable.setQuery(Objects.isNull(helper.getQuery()) ? new RefundListQueryDto() : helper.getQuery());
        pageable.getQuery().setSysUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        pageable.getQuery().setRefundNos(helper.getIds());
        Page<RefundInfoDto> result = refundApplyService.list(pageable);
        export("退款列表", helper, result.getRecords(), response);
    }

    private <T> Pageable<T> getPageable() {
        Pageable<T> pageable = new Pageable<>();
        pageable.setSize(getDefaultExportSize());
        return pageable;
    }

    private void export(String filename, ExportHelper<?, ?> helper, List<?> data, HttpServletResponse response) {
        //PoiExcelUtils.exportExcel(filename, null, helper.getTitles(), helper.getProperties(), data, response);
        try {
            JxlExcelUtils.listToExcel(data, helper.getTitles(), helper.getProperties(), filename, response);
        } catch (ExcelException e) {
            logger.error(e.getMessage());
        }
    }


    private int getDefaultExportSize() {
        return SysConfigUtils.get(CommonSystemConfig.class).getDefaultExportSize();
    }
}
