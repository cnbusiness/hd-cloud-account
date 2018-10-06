package com.hd.cloud.dao.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.hd.cloud.bo.MoneyEntityTrans;
import com.hd.cloud.dao.MoneyEntityTransDao;
import com.hd.cloud.dao.mapper.MoneyEntityTransMapper;
import com.hd.cloud.vo.MoneyEntityTransVo;

@Repository
public class MoneyEntityTransDaoMyBatisImpl implements MoneyEntityTransDao{

	@Inject
	private MoneyEntityTransMapper moneyEntityTransMapper;
	
	@Override
	public List<MoneyEntityTrans> getMoneyEntityTransList(MoneyEntityTransVo moneyEntityTransVo) {
		return moneyEntityTransMapper.getMoneyEntityTransList(moneyEntityTransVo);
	}

	@Override
	public int getMoneyEntityTransCount(MoneyEntityTransVo moneyEntityTransVo) {
		Integer count=moneyEntityTransMapper.getMoneyEntityTransCount(moneyEntityTransVo);
		return count==null ? 0 : count;
	}

	@Override
	public int save(MoneyEntityTrans moneyEntityTrans) {
		return moneyEntityTransMapper.save(moneyEntityTrans);
	}

}
