package org.seckill.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Terence
 *
 */

public interface SeckillDao {
	/**
	 * �����
	 * @param seckillId
	 * @param killTime
	 * @return ���Ӱ������>1,��ʾ��������
	 */
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	
	/**
	 * ����id��ѯ��ɱ���
	 * @param seckillId
	 * @return ���������
	 */
	Seckill queryById(long seckillId);
	
	/**
	 * ����ƫ������ѯ��ɱ�б�
	 * @param offset
	 * @param limit
	 * @return
	 * Ψһ�β��Զ���ֵ
	 * ���ж��������ʱ��Ҫָ��ʵ�ʵ��β����Ƹ�ֵ����Ȼ�Ҳ�����Ӧֵ����ΪJava��û�б����βεļ�¼
	 * java�����е�ʱ����List<Seckill> queryAll(int offset,int limit);�еĲ����������:queryAll(int arg0,int arg1),�������Ǿ�û�а취ȥ���ݶ������
	 */
	List<Seckill> queryAll(@Param("offset")int offset,@Param("limit")int limit);

	
	/**
	 * ��ɱ�����Ż���
	 *  ʹ�ô洢����ִ����ɱ
	 * @param paramMap
	 */
	void killByProcedure(Map<String,Object> paramMap);
}
