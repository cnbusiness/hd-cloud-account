package com.hd.cloud.dao.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.hd.cloud.bo.MoneyEntityWallet;
import com.hd.cloud.dao.MoneyEntityWalletDao;
import com.hd.cloud.dao.mapper.MoneyEntityWalletMapper;

@Repository
public class MoneyEntityWalletDaoMyBatisImpl implements MoneyEntityWalletDao{

	@Inject
	private MoneyEntityWalletMapper entityWalletMapper;
	
	@Override
	public MoneyEntityWallet getMoneyEntityWalletByWalletIdAndType(long walletId, int type) {
		return entityWalletMapper.getMoneyEntityWalletByWalletIdAndType(walletId, type);
	}

	@Override
	public int update(MoneyEntityWallet moneyEntityWallet) {
		return entityWalletMapper.update(moneyEntityWallet);
	}

	@Override
	public int save(MoneyEntityWallet moneyEntityWallet) {
		return entityWalletMapper.save(moneyEntityWallet);
	}

	@Override
	public MoneyEntityWallet findMoneyEntityWalletByUserIdAndPassword(long userId, String password) {
		return entityWalletMapper.findMoneyEntityWalletByUserIdAndPassword(userId, password);
	}

	@Override
	public MoneyEntityWallet findMoneyEntityWalletByShopIdAndPassword(long shopId, String password) {
		return entityWalletMapper.findMoneyEntityWalletByShopIdAndPassword(shopId, password);
	}

}
