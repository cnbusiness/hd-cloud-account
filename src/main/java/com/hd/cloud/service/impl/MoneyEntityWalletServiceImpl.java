package com.hd.cloud.service.impl;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.hd.cloud.bo.MoneyEntityWallet;
import com.hd.cloud.dao.MoneyEntityWalletDao;
import com.hd.cloud.service.MoneyEntityWalletService;
@Service
public class MoneyEntityWalletServiceImpl implements MoneyEntityWalletService{

	@Inject
	private MoneyEntityWalletDao moneyEntityWalletDao;
	
	@Override
	public MoneyEntityWallet getMoneyEntityWalletByWalletIdAndType(long walletId, int type) {
		return moneyEntityWalletDao.getMoneyEntityWalletByWalletIdAndType(walletId, type);
	}

	@Override
	public int update(MoneyEntityWallet moneyEntityWallet) {
		return moneyEntityWalletDao.update(moneyEntityWallet);
	}

	@Override
	public MoneyEntityWallet findMoneyEntityWalletByUserIdAndPassword(long userId, int type, String password) {
		if(type==1) {
			return moneyEntityWalletDao.findMoneyEntityWalletByUserIdAndPassword(userId, password);
		}else {
			return moneyEntityWalletDao.findMoneyEntityWalletByShopIdAndPassword(userId, password);
		}
	}

}
