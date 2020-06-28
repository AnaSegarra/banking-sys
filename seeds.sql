-- CREATE SCHEMA banking_sys;
-- CREATE SCHEMA banking_sys_test;

USE banking_sys;

INSERT INTO users (dtype,username, password) VALUES
	("Admin", "admin", "$2a$10$lKzEHNyVvwoQG4EL2eGtK.EaP.DFBDM.YehCQPC.o6M6/MgjZgw2S");
    
INSERT INTO users (`dtype`, `password`,`username`,`birthday`,`mail_city`,`mail_country`,`mail_number`,`mail_street`,`mail_zip_code`,`name`,`city`,`country`,`number`,`street`,`zip_code`) 
	VALUES ('AccountHolder', '$2a$10$Jdq.cL5ngkpxhfvPiDF6peZhyuVrTtuytK2SCewgh7F3oAWfejEmG','ana_s','1994-04-16','Boadilla del Monte','Spain',13,'avda Cantabria','28600','Ana Segarra','Madrid','Spain',8,'avda Madrid','28700'),
		('AccountHolder', '$2a$10$GZ0EFD0iKbHRsXNYAjkDjOIwJyDRR7F1J2N2V.QIczit/jetfR2Dq','gema_s','1991-10-20',NULL,NULL,NULL,NULL,NULL,'Gema Segarra','Madrid','Spain',13,'avda Luna','28200'),
		('AccountHolder', '$2a$10$2/IhQY39dLHfJeHm4VEXZuCBbzFUpvrcVNf3a3xkv4SOGLFYHX9Sq','gabi_c','2017-01-10',NULL,NULL,NULL,NULL,NULL,'Gabi','Madrid','Spain',13,'avda Luna','28200'),
		('ThirdPartyUser', '$2a$10$YhmlFn.yLAP42NLmsrKiqeSG9ICyvwMr0QCWRi2D/dm/ktp6MVh4O','company_1',NULL,NULL,NULL,0,NULL,NULL,NULL,NULL,NULL,0,NULL,NULL);

INSERT INTO roles (role, user_id) VALUES
	("ROLE_ADMIN", 1),
    ("ROLE_ACCOUNTHOLDER", 2),
	("ROLE_ACCOUNTHOLDER", 3),
    ("ROLE_ACCOUNTHOLDER", 4),
	("ROLE_THIRDPARTY", 5);
