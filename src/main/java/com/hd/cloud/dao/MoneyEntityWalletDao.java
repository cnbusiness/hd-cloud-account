package com.hd.cloud.dao;

import com.hd.cloud.bo.MoneyEntityWallet;

public interface MoneyEntityWalletDao {

	/**
	 * 
	 * @Title: getMoneyEntityWalletByWalletIdAndType
	 * @param:
	 * @Description: 根据钱包id和类型查询钱包信息
	 * @return MoneyEntityWallet
	 */
	public MoneyEntityWallet getMoneyEntityWalletByWalletIdAndType(long walletId, int type);
	
	/**
	 * 
	* @Title: update 
	* @param: 
	* @Description: 编辑
	* @return int
	 */
	public int update(MoneyEntityWallet moneyEntityWallet);
	
	/**
	 * 
	* @Title: save 
	* @param: 
	* @Description: 保存
	* @return int
	 */
	public int save(MoneyEntityWallet moneyEntityWallet);
	
	/**
	 * 
	* @Title: findMoneyEntityWalletByUserIdAndPassword 
	* @param: 
	* @Description: 验证原支付密码是否错误
	* @return MoneyEntityWallet
	 */
	public MoneyEntityWallet findMoneyEntityWalletByUserIdAndPassword(long userId,String password);
	
	
	/**
	 * 
	* @Title: findMoneyEntityWalletByShopIdAndPassword 
	* @param: 
	* @Description: 验证店铺原支付密码是否错误
	* @return MoneyEntityWallet
	 */
	public MoneyEntityWallet findMoneyEntityWalletByShopIdAndPassword(long shopId,String password);
}
