package org.seckill.dao;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.*;

/**
 * ����Spring��Junit���ϣ�Junit����ʱ����SPringIOC����
 * spring-test,junit:spring���Ե�����
 * 1:RunWith:Junit������Ҫ������
 * @author Terence
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
// 2:����Junit Spring�������ļ�
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	//3:ע��Daoʵ�������� --���Զ�ȥSpring�����в���seckillDao��ʵ����ע�뵽��Ԫ������
	@Resource
	private SeckillDao seckillDao;
	
    @Test
    public void testQueryById() throws Exception {
    	long id=1000;
    	Seckill seckill=seckillDao.queryById(id);
    	System.out.println(seckill.getName());
    	System.out.println(seckill);
    	/*
    	 * 1000Ԫ��ɱiphone6
    	 * Seckill [seckillId=1000, name=1000Ԫ��ɱiphone6, number=100, createTime=Thu Aug 03 09:20:36 CST 2017, startTime=Thu Jul 07 00:00:00 CST 2016, endTime=Fri Jul 08 00:00:00 CST 2016]
    	 */
    }

    /**
     * java�����е�ʱ����List<Seckill> queryAll(int offset,int limit);�еĲ����������:queryAll(int arg0,int arg1),�������Ǿ�û�а취ȥ���ݶ������
     * 
     * @throws Exception
     */
    @Test
    public void queryAll() throws Exception {
    	List<Seckill> seckills=seckillDao.queryAll(0, 10);
    	for(Seckill s:seckills)
    	{
    		System.out.println(s);
    	}
    }
    
    @Test
    public void reduceNumber() throws Exception {
    	long seckillId=1000;
    	Date killTime=new Date();
    	int updateCount=seckillDao.reduceNumber(seckillId, killTime);
    	System.out.println("updateCount="+updateCount);

    }
}
