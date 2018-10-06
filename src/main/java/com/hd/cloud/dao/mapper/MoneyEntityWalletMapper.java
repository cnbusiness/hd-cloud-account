package com.hd.cloud.dao.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.hd.cloud.bo.MoneyEntityWallet;
import com.hd.cloud.dao.sql.MoneyEntityWalletProvider;

@Mapper
public interface MoneyEntityWalletMapper {

	/**
	 * 
	 * @Title: getMoneyEntityWalletBywalletIdAndType
	 * @Description: 查询用户钱包
	 * @Table 2.1.11money_entity_wallet_bd (实体钱包表)
	 */
	@Select("SELECT entity_wallet_account,entity_wallet_seq,entity_wallet_entity_itype,entity_wallet_pay_password,"
			+ "entity_wallet_valid_coin_cnt,entity_wallet_freeze_coin_cnt,entity_wallet_game_point_cnt,entity_wallet_status_itype"
			+ " FROM money_entity_wallet_bd where active_flag='y' AND entity_wallet_seq=#{walletId} AND entity_wallet_entity_itype=#{type} ")
	@Results(value = {
			@Result(property = "account", column = "entity_wallet_account", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "walletId", column = "entity_wallet_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "type", column = "entity_wallet_entity_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "payPassword", column = "entity_wallet_pay_password", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "validCoinCnt", column = "entity_wallet_valid_coin_cnt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "freezeCoinCnt", column = "entity_wallet_freeze_coin_cnt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "gameCnt", column = "entity_wallet_game_point_cnt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "status", column = "entity_wallet_status_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER) })
	public MoneyEntityWallet getMoneyEntityWalletByWalletIdAndType(@Param("walletId") long walletId,
			@Param("type") int type);

	/**
	 * 
	 * @Title: update
	 * @Description: 更新用户钱包
	 * @Table 2.1.11money_entity_wallet_bd (实体钱包表)
	 */
	@UpdateProvider(type = MoneyEntityWalletProvider.class, method = "update")
	int update(MoneyEntityWallet moneyEntityWallet);

	/**
	 * 
	 * @Title: save
	 * @Description: 保存
	 * @Table 2.1.11money_entity_wallet_bd (实体钱包表)
	 */
	@InsertProvider(type = MoneyEntityWalletProvider.class, method = "save")
	int save(MoneyEntityWallet moneyEntityWallet);

	/**
	 * @Title: findMoneyEntityWalletByUserIdAndPassword
	 * @Description: 验证旧密码是否正确
	 * @Table: 1.3money_entity_wallet_bd（实体钱包表）
	 */
	@Select("select entity_wallet_account,entity_wallet_seq,entity_wallet_entity_itype,entity_wallet_pay_password,"
			+ "entity_wallet_valid_coin_cnt,entity_wallet_freeze_coin_cnt,entity_wallet_game_point_cnt,entity_wallet_status_itype"
			+ "from money_entity_wallet_bd  WHERE entity_wallet_entity_itype=1 AND user_user_base_sb_seq = #{userId} AND entity_wallet_pay_password=#{password} and active_flag='y' ")
	@Results(value = {
			@Result(property = "account", column = "entity_wallet_account", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "walletId", column = "entity_wallet_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "type", column = "entity_wallet_entity_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "payPassword", column = "entity_wallet_pay_password", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "validCoinCnt", column = "entity_wallet_valid_coin_cnt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "freezeCoinCnt", column = "entity_wallet_freeze_coin_cnt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "gameCnt", column = "entity_wallet_game_point_cnt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "status", column = "entity_wallet_status_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER) })
	MoneyEntityWallet findMoneyEntityWalletByUserIdAndPassword(@Param("userId") long userId,
			@Param("password") String password);
	
	/**
	 * @Title: findMoneyEntityWalletByShopIdAndPassword
	 * @Description: 验证店铺原支付密码是否错误
	 * @Table: 1.3money_entity_wallet_bd（实体钱包表）
	 */
	@Select("select entity_wallet_account,entity_wallet_seq,entity_wallet_entity_itype,entity_wallet_pay_password,"
			+ "entity_wallet_valid_coin_cnt,entity_wallet_freeze_coin_cnt,entity_wallet_game_point_cnt,entity_wallet_status_itype"
			+ "from money_entity_wallet_bd  WHERE entity_wallet_entity_itype=2 AND user_user_base_sb_seq = #{shopId} AND entity_wallet_pay_password=#{password} and active_flag='y' ")
	@Results(value = {
			@Result(property = "account", column = "entity_wallet_account", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "walletId", column = "entity_wallet_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "type", column = "entity_wallet_entity_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "payPassword", column = "entity_wallet_pay_password", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "validCoinCnt", column = "entity_wallet_valid_coin_cnt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "freezeCoinCnt", column = "entity_wallet_freeze_coin_cnt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "gameCnt", column = "entity_wallet_game_point_cnt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "status", column = "entity_wallet_status_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER) })
	MoneyEntityWallet findMoneyEntityWalletByShopIdAndPassword(@Param("shopId") long shopId,
			@Param("password") String password);
}
