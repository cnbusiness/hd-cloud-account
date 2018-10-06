package com.hd.cloud.dao.sql;

import org.apache.ibatis.jdbc.SQL;

import com.hd.cloud.bo.MoneyEntityWallet;

public class MoneyEntityWalletProvider {

	/**
	 * 
	 * @Title: update
	 * @param:
	 * @Description: 编辑
	 * @return String
	 */
	public String update(MoneyEntityWallet moneyEntityWallet) {
		return new SQL() {
			{
				UPDATE("money_entity_wallet_bd");

				if (moneyEntityWallet.getValidCoinCnt() > 0) {
					SET("entity_wallet_valid_coin_cnt = entity_wallet_valid_coin_cnt+#{validCoinCnt}");
				}

				if (moneyEntityWallet.getFreezeCoinCnt() > 0) {
					SET("entity_wallet_freeze_coin_cnt = entity_wallet_freeze_coin_cnt+#{freezeCoinCnt}");
				}

				if (moneyEntityWallet.getFreezeCoinCnt() > 0) {
					SET("entity_wallet_game_point_cnt = entity_wallet_game_point_cnt+#{freezeCoinCnt}");
				}

				if (moneyEntityWallet.getStatus() > 0) {
					SET("entity_wallet_status_itype = #{status}");
				}

				SET("update_by = #{updateBy}");
				SET("update_time = now() ");
				WHERE("entity_wallet_seq = #{walletId} AND entity_wallet_entity_itype=#{type} AND active_flag='y' ");
			}
		}.toString();

	}
	
	/**
	 * 
	* @Title: save 
	* @param: 
	* @Description: 保存
	* @return String
	 */
	public String save(MoneyEntityWallet moneyEntityWallet) {
		String sql = new SQL() {
			{
				INSERT_INTO("money_entity_wallet_bd");
				VALUES("entity_wallet_account", "#{account}");
				VALUES("entity_wallet_seq", "#{walletId}");
				VALUES("entity_wallet_entity_itype", "#{type}");
				VALUES("entity_wallet_status_itype", "#{status}");

				VALUES("create_time", "now()");
				VALUES("create_by", "#{createBy}");
				VALUES("active_flag", "#{activeFlag}");
			}
		}.toString();
		return sql;
	}
}
