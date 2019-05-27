--oeder_base索引
drop index idx_status on order_base;
drop index index_user_id on order_base;
drop index index_uid on order_base;
drop index index_sys_user_id on order_base;

create index index_user_id on order_base(user_id,order_status, ctime);
create index index_sys_user_id on order_base(sys_user_id,order_status,ctime);

--device索引
 drop index idx_launch_area on device;
 drop index idx_operator_id on device;
 drop index idx_status on device;
 drop index idx_product_id on device;
 drop index idx_owner_id_work_status on device;


create index index_owner_id on device(owner_id,status,ctime);
create index index_product_id on device(product_id,status);