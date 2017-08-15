--���ݿ��ʼ���ű�
--Ϊʲô��дDDL
--��¼ÿ�����ߵ�DDL�޸�
--����V1.1
ALTER TABLE seckill
DROP INDEX idx_create_time,
ADD index idx_c_s(start_time,create_time);
--����v1.2
--ddl


--�������ݿ�
CREATE database seckill;
--ʹ�����ݿ�
use seckill;
--������ɱ���� ��ֻ��InnoDB���ݿ�֧������
CREATE TABLE seckill(
seckill_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '��Ʒ���id',
name VARCHAR(120) NOT NULL COMMENT '��Ʒ����',
number int NOT NULL COMMENT '�������',
/*Ĭ��ʱ���Ҫ���Զ����ʱ��ǰ�棬��������*/
create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '����ʱ��',
start_time TIMESTAMP NOT NULL COMMENT '��ɱ����ʱ��',
end_time TIMESTAMP NOT NULL COMMENT '��ɱ����ʱ��',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='��ɱ����';

--��ʼ������
insert into seckill(name,number,start_time,end_time)
values('1000Ԫ��ɱiphone6',100,'2016-07-07 00:00:00','2016-07-08 00:00:00'),
values('500Ԫ��ɱipad2',200,'2016-07-07 00:00:00','2016-07-08 00:00:00'),
values('400Ԫ��ɱС��note4',300,'2016-07-07 00:00:00','2016-07-08 00:00:00'),
values('200Ԫ��ɱ����note',300,'2016-07-07 00:00:00','2016-07-08 00:00:00');

--��ɱ�ɹ���ϸ��
--�û���¼��֤�����Ϣ
create table success_killed(
seckill_id BIGINT not null COMMENT '��ɱ��Ʒid',
user_phone BIGINT not null COMMENT '�û��ֻ���',
state tinyint not null DEFAULT -1 COMMENT '״̬��ʶ��-1����Ч  0���ɹ�  1���Ѹ���',
create_time timestamp not null COMMENT '����ʱ��',
PRIMARY key(seckill_id,user_phone),/*��������*/
key idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='��ɱ�ɹ���ϸ��';



--�������ݿ����̨
mysql -uroot -proot


