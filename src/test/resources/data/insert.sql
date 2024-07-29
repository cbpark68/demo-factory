use demofactory;

delete from facility;
delete from facility_code;
delete from user_auth;
delete from user;
delete from factory;

insert into factory
(factory_no,factory_name,factory_status,logo_file_name) values
(1,"site_admin","ACTIVE","logo.png"),
(2,"k-factory","ACTIVE","logo.png"),
(3,"미래학습관","ACTIVE","logo.png"),
(4,"site-4","ACTIVE","logo.png");

insert into user
(user_no,user_id,user_pw,user_name,factory_no) values
(1,"admin","1234","관리자",1),
(2,"kfactory","admin","Factory Manager",2),
(3,"sfactory","admin","Factory Manager",3),
(4,"kfactory1","admin","Factory User",2),
(5,"sfactory1","admin","Factory User",3),
(6,"site4","admin","Factory Manager",4);


insert into user_auth
(user_auth_no,user_no,auth) values
(1,1,"ROLE_ADMIN"),
(2,2,"ROLE_FACTORY_MANAGER"),
(3,3,"ROLE_FACTORY_MANAGER"),
(4,4,"ROLE_FACTORY_USER"),
(5,5,"ROLE_FACTORY_USER"),
(6,6,"ROLE_FACTORY_MANAGER");

insert into facility_code
(facility_code_no,facility_code,facility_code_name,factory_no,facility_code_info) values
(1,'server','서버',2,"{\"code\":\"value\",\"x\":\"0\",\"y\":\"1\"}"),
(2,'notebook','노트북',2,"{\"code\":\"value\",\"x\":\"0\",\"y\":\"1\"}"),
(3,'car','자동차',2,"{\"code\":\"value\",\"x\":\"0\",\"y\":\"1\"}"),
(4,'server1','서버',3,"{\"code\":\"value\",\"x\":\"0\",\"y\":\"1\"}"),
(5,'notebook1','노트북',3,"{\"code\":\"value\",\"x\":\"0\",\"y\":\"1\"}"),
(6,'car1','자동차',3,"{\"code\":\"value\",\"x\":\"0\",\"y\":\"1\"}");

insert into facility
(facility_no,facility_code_no,facility_name,factory_no,data_save_yn) values
(1,1,'line1',1,'Y'),
(2,1,'server1',1,'N'),
(3,2,'notebook0001',2,'Y'),
(4,2,'notebook0002',2,'Y'),
(5,3,'car0001',2,'Y'),
(6,3,'car0002',2,'Y'),
(7,1,'line2',1,'Y'),
(8,1,'line1',2,'Y'),
(9,1,'line2',2,'Y'),
(10,2,'notebook0001',3,'Y'),
(11,2,'notebook0002',3,'Y'),
(12,3,'car0001',3,'Y'),
(13,3,'car0002',3,'Y'),
(15,1,'line1',3,'Y'),
(16,1,'line2',3,'Y');

