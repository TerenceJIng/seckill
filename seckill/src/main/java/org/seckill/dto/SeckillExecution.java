package org.seckill.dto;

import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;

/**
 * ��װ��ɱִ�к�Ľ��
 * @author Terence
 *
 */
public class SeckillExecution {
	
	private long seckillId;
	//��ɱִ�н��״̬
	private int state;
	//״̬��ʾ
	private String stateInfo;
	//��ɱ�ɹ�����
	private  SuccessKilled successKilled;
	
	public SeckillExecution(long seckillId, SeckillStateEnum stateEnum,
			SuccessKilled successKilled) {
		super();
		this.seckillId = seckillId;
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getInfo();
		this.successKilled = successKilled;
	}
	public SeckillExecution(long seckillId, SeckillStateEnum stateEnum) {
		super();
		this.seckillId = seckillId;
		this.state = stateEnum.getState();
		this.stateInfo = stateEnum.getInfo();
	}
	@Override
	public String toString() {
		return "SeckillExecution [seckillId=" + seckillId + ", state=" + state
				+ ", stateInfo=" + stateInfo + ", successKilled="
				+ successKilled + "]";
	}

	
}
