package com.hd.cloud.dao;
/**
 * 
  * @ClassName: MoneyEntityTransDao
  * @Description: 交易记录
  * @author ShengHao shenghaohao@hadoop-tech.com
  * @Company hadoop-tech 
  * @date 2018年5月2日 上午10:59:55
  *
 */

import java.util.List;

import com.hd.cloud.bo.MoneyEntityTrans;
import com.hd.cloud.vo.MoneyEntityTransVo;

public interface MoneyEntityTransDao {

	/**
	 * 
	 * @Title: getMoneyEntityTransList
	 * @param:
	 * @Description: 列表
	 * @return List<MoneyEntityTrans>
	 */
	public List<MoneyEntityTrans> getMoneyEntityTransList(MoneyEntityTransVo moneyEntityTransVo);

	/**
	 * 
	 * @Title: getMoneyEntityTransCount
	 * @param:
	 * @Description: 总数
	 * @return int
	 */
	public int getMoneyEntityTransCount(MoneyEntityTransVo moneyEntityTransVo);

	/**
	 * 
	 * @Title: save
	 * @param:
	 * @Description: 保存
	 * @return int
	 */
	public int save(MoneyEntityTrans moneyEntityTrans);
}
