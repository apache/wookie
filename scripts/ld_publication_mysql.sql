drop database if exists ld_publication;
create database if not exists ld_publication DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
use ld_publication;

create table scodata(scokey varchar(250) NOT NULL,
	xmldoc longtext,
	PRIMARY KEY  (`scokey`)
)
;

create table event (
	pk INTEGER auto_increment not null,
	uolid int not null ,
	triggerid varchar (256) null,
	type varchar (32) not null,
	classname varchar (32) not null,
	componentid varchar (256) not null,
	PRIMARY KEY  (`pk`)
)
;



create table propertydefinition (
	pk INTEGER auto_increment not null ,
	datatype varchar (32)  not null ,
	scope int not null ,
	href varchar (512)  null ,
	defaultvalue text  null ,
	definedby int not null ,
	PRIMARY KEY  (`pk`)
)
;


create table propertylookup (
	uolid INTEGER not null ,
	propid varchar (250)  not null ,
	propdefforeignpk INTEGER not null ,
	PRIMARY KEY  (`uolid`,`propid`),
 CONSTRAINT `FKC5E6A8DB4215462E` FOREIGN KEY (`propdefforeignpk`) REFERENCES `propertydefinition` (`pk`)
)
;


create table propertyvalue (
	pk INTEGER auto_increment not null ,
	propdefforeignpk int not null ,
	userid varchar (64)  null ,
	runid int null ,
	propvalue text  null ,
	PRIMARY KEY  (`pk`)
)
;


create table roleinstance (
	roleinstanceid INTEGER auto_increment not null ,
	roleid varchar (256)  not null ,
	runid int not null ,
	PRIMARY KEY  (`roleinstanceid`)
)
;


create table roleparticipation (
	userid varchar (64)  not null ,
	roleinstanceid int not null ,
	PRIMARY KEY  (`userid`,`roleinstanceid`)
) 
;


create table run (
	runid INTEGER auto_increment not null ,
	uolid int not null ,
	name varchar (1024)  null ,
	starttime timestamp not null ,
	PRIMARY KEY  (`runid`)
) 
;

create table runparticipation (
	userid varchar (64)  not null ,
	runid int not null ,
	activerole int null ,
	PRIMARY KEY  (`userid`,`runid`)
) 
;

create table unitoflearning (
	id INTEGER auto_increment not null ,
	uri varchar (512)  not null ,
	title varchar (1024)  null ,
	rolesid varchar (256)  null ,
	contenturi varchar (256)  null ,
	PRIMARY KEY  (`id`)
) 
;

create table lduser (
	userid varchar (64)  not null ,
	PRIMARY KEY  (`userid`)
) 
;


create  index ix_event_uolid on event(uolid)
;
 
create  index ix_event_triggerid on event(triggerid)
;
 
create  index ix_event_type on event(type)
;
 
create  index ix_propertydefinition_href on propertydefinition(href)
;
 
create  index ix_propertylookup_propid on propertylookup(propid)
;
 
create  index ix_propertylookup_propdefforeignpk on propertylookup(propdefforeignpk)
;
 
create  index ix_propertylookup_uolid on propertylookup(uolid)
;
 
create  index ix_propertyvalue_userid on propertyvalue(userid)
;
 
create  index ix_propertyvalue_runid on propertyvalue(runid)
;
 
create  index ix_propertyvalue_propdefforeignpk on propertyvalue(propdefforeignpk)
;
 
create  index ix_roleinstance_roleid_runid on roleinstance(roleid, runid)
;
 
create  index ix_roleinstance_runid on roleinstance(runid)
;
 
create  index ix_roleparticipation_userid on roleparticipation(userid)
;
 
create  index ix_roleparticipation_roleinstanceid on roleparticipation(roleinstanceid)
;
 
create  index ix_run_uolid on run(uolid)
;
 
create  index ix_runparticipation_runid on runparticipation(runid)
;
 
create  index ix_runparticipation_activerole on runparticipation(activerole)
;

create  index ix_runparticipation_userid on runparticipation(userid)
;


alter table propertylookup add
	constraint fk__propertylookup__propertydefinition foreign key
	(
		propdefforeignpk
	) references propertydefinition (
		pk
	) on delete cascade
;

alter table propertyvalue add
	constraint fk__propertyvalue__propertydefinition foreign key
	(
		propdefforeignpk
	) references propertydefinition (
		pk
	) on delete cascade 
;

alter table roleinstance add
	constraint fk_roleinstance_run foreign key
	(
		runid
	) references run (
		runid
	) on delete cascade
;

alter table roleparticipation add
	constraint fk_roleparticipation_roleinstance foreign key
	(
		roleinstanceid
	) references roleinstance (
		roleinstanceid
	) on delete cascade
;
alter table roleparticipation add
	constraint fk_roleparticipation_user foreign key
	(
		userid
	) references lduser (
		userid
	) on delete cascade 
;

alter table run add
	constraint fk_run_unitoflearning foreign key 
	(
		uolid
	) references unitoflearning (
		id
	) on delete cascade
;

alter table runparticipation add
	constraint fk_runparticipation_run foreign key 
	(
		runid
	) references run (
		runid
	) on delete cascade
;
alter table runparticipation add
	constraint fk_runparticipation_user foreign key
	(
		userid
	) references lduser (
		userid
	)
;

/****** object:  view lookupeventview    script date: 14-07-2004 20:00:11 ******/

create view lookupeventview
as
select     event.pk, propertylookup.propdefforeignpk, event.componentid, event.classname, event.type, event.uolid, 
                      event.triggerid
from         event inner join
                      propertylookup on event.uolid = propertylookup.uolid and event.triggerid = propertylookup.propid
;


/****** object:  view lookuppropertydefinitionview    script date: 27-11-2003 12:46:12 ******/

create view lookuppropertydefinitionview
as
select     propertylookup.uolid, propertylookup.propid, propertylookup.propdefforeignpk, propertydefinition.defaultvalue, 
                      propertydefinition.datatype, propertydefinition.scope, propertydefinition.href
from         propertylookup inner join
                      propertydefinition on propertylookup.propdefforeignpk = propertydefinition.pk
;



/****** object:  view lookuppropertyvalueview    script date: 27-11-2003 12:46:12 ******/

create view lookuppropertyvalueview
as
select     propertyvalue.pk, propertylookup.uolid, propertylookup.propid, propertyvalue.userid, propertyvalue.runid, 
                      propertydefinition.datatype, propertyvalue.propvalue
from         propertydefinition inner join
                      propertylookup on propertydefinition.pk = propertylookup.propdefforeignpk inner join
                      propertyvalue on propertydefinition.pk = propertyvalue.propdefforeignpk
;



/****** object:  view propertylookupview    script date: 14-07-2004 20:00:11 ******/

create view propertylookupview
as
select     propertylookup.*, propertydefinition.datatype as datatype, propertydefinition.scope as scope
from         propertylookup inner join
                      propertydefinition on propertylookup.propdefforeignpk = propertydefinition.pk

;



/****** object:  view propertyvalueview    script date: 27-11-2003 12:46:12 ******/

create view propertyvalueview
as
select     propertyvalue.pk, propertyvalue.propvalue, propertydefinition.datatype, propertylookup.propid, propertylookup.propdefforeignpk, propertydefinition.scope
from         propertyvalue inner join
                      propertydefinition on propertyvalue.propdefforeignpk = propertydefinition.pk inner join
                      propertylookup on propertydefinition.pk = propertylookup.propdefforeignpk
;




/****** object:  view runparticipationview    script date: 27-11-2003 12:46:13 ******/
create view runparticipationview
as
select     run.uolid, runparticipation.activerole, runparticipation.userid, roleinstance.roleid, runparticipation.runid
from         runparticipation inner join
                      run on runparticipation.runid = run.runid left outer join
                      roleinstance on runparticipation.activerole = roleinstance.roleinstanceid

;



/****** object:  view roleparticipationview    script date: 27-11-2003 12:46:13 ******/
create view roleparticipationview
as
select     roleinstance.runid, roleinstance.roleid, roleparticipation.roleinstanceid, roleparticipation.userid
from         roleinstance inner join
                      roleparticipation on roleinstance.roleinstanceid = roleparticipation.roleinstanceid

;