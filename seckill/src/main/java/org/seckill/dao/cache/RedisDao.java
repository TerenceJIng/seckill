package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * ע�⣺
 * ��Redis�зŵĶ���һ��Ҫ���л�֮���ٷ��룬�ο�http://www.cnblogs.com/yaobolove/p/5632891.html
 * ���л���Ŀ���ǽ�һ��ʵ����Serializable�ӿڵĶ���ת����һ���ֽ�����,���ԡ� �Ѹ��ֽ����б�������(����:������һ���ļ���),
 * �Ժ������ʱ�����ֽ����лָ�Ϊԭ���Ķ���
 * ���л��Ķ���ռԭ�пռ��ʮ��֮һ��ѹ���ٶȿ��Դﵽ������������ͬʱ��ʡ��CPU
 * 
 * Redis �������ʱ��Ҫ�������л�������Ϊ���л���ʵ���Ͼ��ǽ��������ֽ���ʽ�洢�����������ܶ�����������ַ��������ͻ���ͼƬ����Ƶ�ȶ��������ͣ�
 * �����Խ��䱣�����ֽ������С��������л������Գ־û���������紫�䡣��Ҫ��ԭ����ʱ��ֻ�轫�ֽ������ٷ����л����ɡ�
 * 
 * ��ΪҪ����Ŀ���õ�������Ҫ���@Service�����������һ������ 
 * ��ΪҪ��ʼ�����ӳ�JedisPool������Ҫimplements InitializingBean������Ĭ�ϵ�
 * afterPropertiesSet()����
 *
 */
@Service
public class RedisDao {
	private final Logger logger=LoggerFactory.getLogger(this.getClass());
	private final JedisPool jedisPool;
	public RedisDao(String ip,int port)
	{
		//һ���򵥵����ã�
		jedisPool=new JedisPool(ip,port);
	}
	
	
	// protostuff���л������õ��ļܹ�
	// ������RuntimeSchema������schema(�ܹ������л�ģʽ)ͨ������������ʱ�����ʹ��
	private RuntimeSchema<Seckill> schema=RuntimeSchema.createFrom(Seckill.class);
	
	public Seckill getSeckill(long seckillId)
	{
		//����Redis�����߼�������Ӧ�÷���Service�£���Ϊ�������ݷ��ʲ���߼�
		try{
			Jedis jedis=jedisPool.getResource();
			try{
				String key="seckill:"+seckillId;
				//��û��ʵ���ڲ����л�����
				//get->byte[]->�����л�-��Object(Seckill)
				//�����Զ������л���ʽ
				//�����Զ�������л�����pom.xml�ļ���������������protostuff��pojo
				byte[] bytes=jedis.get(key.getBytes());
				//���»�ȡ����
				if(bytes!=null)
				{
					Seckill seckill=schema.newMessage();
					//��bytes���մ�Seckill�ഴ����ģʽ�ܹ�schema�����л���ֵ������seckill;
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					//Seckill�������л�
					return seckill;
				}				
			}
			finally{
				jedis.close();
			}
		}
		catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public String putSeckill(Seckill seckill)
	{
		//set Object��Seckill��->���л�-��bytes[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();
				// protostuff����
				// ��seckill�������л����ֽ�����
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				// ����ʱ��+key���+�������л����=���Ž�����jedis����أ����ؽ��result(OK/NO)
				int timeout = 60 * 60; // 1Сʱ
				// ����key��Ӧ���ַ���value������һ������ʱ��
				String result = jedis.setex(key.getBytes(), timeout, bytes);

				return result;

			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	

}
