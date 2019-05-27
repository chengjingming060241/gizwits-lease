/**
添加支付密钥
 */
 INSERT INTO sys_config(ctime,utime,config_key,config_value,remark,status) VALUES ('2017-08-28 11:48:02','2017-08-28 11:48:02','wxPartnerSecret','Ifresh99Ifresh99Ifresh99Ifresh99','微信公众号PartnerSecret','1');

 INSERT INTO sys_config(ctime,utime,config_key,config_value,remark,status) VALUES ('2017-08-28 11:48:02','2017-08-28 11:48:02','wxCertDirectoryPath','路径填写','微信公众号证书','1');

 UPDATE product set  wx_product_id = '39904' WHERE gizwits_product_key = '31a514e63c444aa199d75ebdac00abe3';

 UPDATE sys_config set config_value = 'http://share.ifreshkj.com/app/wx/init?deviceId=' where config_key = 'wxAccessUrl';

