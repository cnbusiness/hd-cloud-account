package com.hd.cloud.service;

import com.hd.cloud.bo.MoneyEntityWallet;
/**
 * 
  * @ClassName: MoneyEntityWalletService
  * @Description:  钱包管理
  * @author ShengHao shenghaohao@hadoop-tech.com
  * @Company hadoop-tech 
  * @date 2018年4月17日 下午5:07:20
  *
 */
public interface MoneyEntityWalletService {

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
	* @Title: findMoneyEntityWalletByUserIdAndPassword 
	* @param: 
	* @Description: 验证原支付密码是否错误
	* @return MoneyEntityWallet
	 */
	public MoneyEntityWallet findMoneyEntityWalletByUserIdAndPassword(long userId,int type,String password);
}
