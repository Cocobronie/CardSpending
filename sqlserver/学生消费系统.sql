select * from StuInfo
drop table StuInfo
create table StuInfo
(
	Id varchar(40),
	password varchar(40),
	surplus varchar(40),
	electricity varchar(20),
	water varchar(40),
	isHelp varchar(20)
)

insert into StuInfo values('8202201417','1234','23.5','56.4','324.3','0')

create table History
(
	Id varchar(40),
	date varchar(40),
	type varchar(40),
	money varchar(20),
)
drop table History
--yyyy-MM-dd HH:mm:ss
insert into History values('8202201417','2022-1-3 16:18:23','消费记录','16.4')
insert into History values('8202201417','2022-1-3 16:18:23','消费记录','4.4')
insert into History values('8202201417','2022-1-3 16:18:23','消费记录','56.4')
insert into History values('8202201417','2022-1-3 16:18:23','充值记录','5.22')
select * from History where Id='8202201417'
select * from History