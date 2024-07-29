use digitaltwin;

delete from dt_facility_data;
delete from dt_facility;
delete from dt_facility_code;
delete from dt_dashboard_item;
delete from dt_dashboard;
delete from dt_dashboard_code;
delete from dt_user_auth;
delete from dt_user;
delete from dt_site;

insert into dt_site (site_no,site_name,site_status,logo_file_name) value (1,"site_admin","ACTIVE","logo.png");
insert into dt_site (site_no,site_name,site_status,logo_file_name) value (2,"k-factory","ACTIVE","logo.png");
insert into dt_site (site_no,site_name,site_status,logo_file_name) value (3,"미래학습관","ACTIVE","logo.png");
insert into dt_site (site_no,site_name,site_status,logo_file_name) value (4,"site-4","ACTIVE","logo.png");

insert into dt_user (user_no,user_id,user_pw,user_name,site_no) values (1,"admin","1234","관리자",1);
insert into dt_user (user_no,user_id,user_pw,user_name,site_no) values (2,"kfactory","admin","Site Manager",2);
insert into dt_user (user_no,user_id,user_pw,user_name,site_no) values (3,"sfactory","admin","Site Manager",3);
insert into dt_user (user_no,user_id,user_pw,user_name,site_no) values (4,"kfactory1","admin","Site User",2);
insert into dt_user (user_no,user_id,user_pw,user_name,site_no) values (5,"sfactory1","admin","Site User",3);
insert into dt_user (user_no,user_id,user_pw,user_name,site_no) values (6,"site4","admin","Site Manager",4);


insert into dt_user_auth (user_auth_no,user_no,auth) values (1,1,"ROLE_ADMIN");
insert into dt_user_auth (user_auth_no,user_no,auth) values (2,2,"ROLE_SITE_MANAGER");
insert into dt_user_auth (user_auth_no,user_no,auth) values (3,3,"ROLE_SITE_MANAGER");
insert into dt_user_auth (user_auth_no,user_no,auth) values (4,4,"ROLE_SITE_USER");
insert into dt_user_auth (user_auth_no,user_no,auth) values (5,5,"ROLE_SITE_USER");
insert into dt_user_auth (user_auth_no,user_no,auth) values (6,6,"ROLE_SITE_MANAGER");

