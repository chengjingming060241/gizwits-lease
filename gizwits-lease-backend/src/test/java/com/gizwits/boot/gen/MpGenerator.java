package com.gizwits.boot.gen;

import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.gizwits.boot.utils.MpGeneratorUtil;

/**
 * <p>
 * 代码生成器演示
 * </p>
 */
public class MpGenerator {

    /**
     * <p>
     * MySQL 生成演示
     * </p>
     */
    public static void main(String[] args) {
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert(){
            //自定义数据库表字段类型转换【可选】
            @Override
            public DbColumnType processTypeConvert(String fieldType) {
                return super.processTypeConvert(fieldType);
            }
        });
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("charge@2017");
        dsc.setUrl("jdbc:mysql://116.62.4.120:3306/gizwits_lease?characterEncoding=utf8&useSSL=true");


        String modelOutputStr="/Users/yehongwei/gizwits/gizwits-lease/gizwits-lease-model/src/main/java";
        MpGeneratorUtil.createModel(dsc,modelOutputStr,"yinhui","com.gizwits.lease","order",null,
                new String[]{"order_ext_port"},null);

        String daoOutputStr = "/Users/yehongwei/gizwits/gizwits-lease/gizwits-lease-dao/src/main/java";
        MpGeneratorUtil.createDao(dsc,daoOutputStr,"yinhui","com.gizwits.lease","order",null,
                new String[]{"order_ext_port"},null);


        String serviceOutputStr = "/Users/yehongwei/gizwits/gizwits-lease/gizwits-lease-service/src/main/java";
        MpGeneratorUtil.createService(dsc,serviceOutputStr,"yinhui","com.gizwits.lease","order",null,
                new String[]{"order_ext_port"},null);
/*

        String controllerOutputStr = "/Users/yehongwei/gizwits/gizwits-lease/gizwits-lease-backend/src/main/java";
        MpGeneratorUtil.createController(dsc,controllerOutputStr,"yinhui","com.gizwits.lease","message",null,
                new String[]{"operator"},null);
*/
        String mapperDir = "/Users/yehongwei/gizwits/gizwits-lease/gizwits-lease-dao/src/main/resources/mapper/";
        MpGeneratorUtil.createMapperXml(dsc,mapperDir, "yinhui","com.gizwits.lease","order",null,
                new String[]{"order_ext_port"},null);



        String controllerOutputStr = "D:/gizProject/gizwits-lease/gizwits-lease-backend/src/main/java";
        MpGeneratorUtil.createController(dsc,controllerOutputStr,"gagi","com.gizwits.lease","stat",null,
                new String[]{"stat_fault_alert_type"},null);

    }

}