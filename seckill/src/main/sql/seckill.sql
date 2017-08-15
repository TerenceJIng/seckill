--ʹ�ô洢����ִ����ɱ
DELIMITER $$ -- console;ת��Ϊ$$�����廻�з�����ʾ

-- ����洢����
-- ������in ���������out �������
-- row_count():������һ���޸�����sql��delete,insert,update����Ӱ��������
-- row_count():0:δ�޸����ݣ�>0����ʾ�޸����ݵ�������<0:sql����/δִ���޸�sql��
CREATE PROCEDURE execute_seckill(in v_seckill_id bigint,in v_phone bigint,
												in v_kill_time timestamp,out r_result int) 
BEGIN
    DECLARE insert_count INT DEFAULT 0;
    
    START TRANSACTION ;
    
    INSERT ignore success_killed(seckill_id,user_phone,create_time) 
    VALUES(v_seckill_id,v_phone,v_kill_time); -- �Ȳ��빺����ϸ
    
    SELECT ROW_COUNT() INTO insert_count;
    IF(insert_count = 0) THEN
      ROLLBACK ;
      SET r_result = -1;   -- �ظ���ɱ
    ELSEIF(insert_count < 0) THEN
      ROLLBACK ;
      SET r_result = -2;   -- �ڲ�����
    ELSE   -- �Ѿ����빺����ϸ��������Ҫ���ٿ��
      update seckill 
      set number = number -1 
      WHERE seckill_id = v_seckill_id 
      		AND start_time < v_kill_time 
      		AND end_time > v_kill_time 
      		AND number > 0;
      
      select ROW_COUNT() INTO insert_count;
      IF (insert_count = 0)  THEN
        ROLLBACK ;
        SET r_result = 0;   -- ���û���ˣ�������ɱ�Ѿ��ر�
      ELSEIF (insert_count < 0) THEN
        ROLLBACK ;
        SET r_result = -2;   -- �ڲ�����
      ELSE
        COMMIT ;    -- ��ɱ�ɹ��������ύ
        SET  r_result = 1;   -- ��ɱ�ɹ�����ֵΪ1
      END IF;
    END IF;
  END
$$

-- ����
DELIMITER ;-- ��DELIMITER���¶��廹ԭ�ɷֺţ�

SET @r_result = -3;
-- ִ�д洢����
CALL execute_seckill(1003,18864598658,now(),@r_result);
-- ��ȡ���
select @r_result;


drop procedure execute_seckill; -- ɾ���洢����

--�洢����
--1���洢�����Ż��������м������е�ʱ��
--2����Ҫ���������洢����
--3���򵥵��߼�����Ӧ�ô洢����
--4��QPSһ����ɱ�����Խӽ�6000/qps
