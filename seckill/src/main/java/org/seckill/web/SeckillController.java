package org.seckill.web;


import java.util.Date;
import java.util.List;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seckill") //url��/ģ��/��Դ/{id}/ϸ��/seckill/list
public class SeckillController {
	@Autowired
	private SeckillService seckillService;
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model)
	{
		System.out.println("**********");
		List<Seckill> list=seckillService.getSeckillList();
		model.addAttribute("list",list);
		//list.jsp+model=ModelAndView
		return "list";
	}
	
	@RequestMapping(value="/{seckillId}/detail",method=RequestMethod.GET)
	public String detail(@PathVariable("seckillId")Long seckillId,Model model){
		if(seckillId==null) //���󲻴��ڵ�ʱ��ֱ���ض���ص��б�ҳ
		{
			return "redirect:/seckill/list";
		}
		Seckill seckill=seckillService.getById(seckillId);
		if(seckill==null)
		{
			//���������󲻴���
			return "forward:/seckill/list";
			
		}
		return "detail";		
	}
	
	@RequestMapping(value = "/{seckillId}/exposer", 
			method = RequestMethod.POST, 
			produces = { "application/json;charset=UTF-8" })
	@ResponseBody //��װ��json
	public SeckillResult<Exposer> exposer(Long seckillId) {
		SeckillResult<Exposer> result;
		try {
			//Exposer:����Ƿ���ɱ��״̬��Ϣ��
			Exposer exposer = seckillService.exportSeckillUrl(seckillId); 
			
			result = new SeckillResult<Exposer>(true, exposer);
		} catch (Exception e) {
			e.printStackTrace();
			result = new SeckillResult<Exposer>(false, e.getMessage());
		}

		return result;
	}

	/*
	 * md5����֤�û���������û�б��۸�
	 * Ĭ�ϵ�ajax�����Json��ʽ�����Խ�����������װ��Json��ʽ��
	 */
	@RequestMapping(value = "/{seckillId}/{md5}/execution", 
			method = RequestMethod.POST, 
			produces = { "application/json;charset=UTF-8" })
	@ResponseBody
	public SeckillResult<SeckillExecution> execute(
			@PathVariable("seckillId") Long seckillId,
			@PathVariable("md5") String md5, 
			//required = false��ʾ�绰���벻�Ǳ���ġ�
			@CookieValue(value = "killPhone", required = false) Long phone) {
		//Spring MVC valid
		if (phone == null) {
			return new SeckillResult<SeckillExecution>(false, "δע��");
		}
		
		SeckillResult<SeckillExecution> result;
		try {
			SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);
			return new SeckillResult<SeckillExecution>(true, execution);
		} catch (RepeatKillException e1) {
			SeckillExecution execution = new SeckillExecution(seckillId,
					SeckillStateEnum.REPEAT_KILL);
			return new SeckillResult<SeckillExecution>(false, execution);
		} catch (SeckillCloseException e2) {
			SeckillExecution execution = new SeckillExecution(seckillId,
					SeckillStateEnum.END);
			return new SeckillResult<SeckillExecution>(false, execution);
		} catch (Exception e) {
			SeckillExecution execution = new SeckillExecution(seckillId,
					SeckillStateEnum.INNER_ERROR);
			return new SeckillResult<SeckillExecution>(false, execution);
		}

	}

	// ��ȡϵͳʱ��
	@RequestMapping(value = "/time/now", method = RequestMethod.GET)
	public SeckillResult<Long> time() {
		Date now = new Date();
		return new SeckillResult<Long>(true, now.getTime());
	}

}
