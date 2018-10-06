package com.hd.cloud.dao.sql;

import org.apache.ibatis.jdbc.SQL;

import com.hd.cloud.bo.MoneyEntityTrans;
import com.hd.cloud.vo.MoneyEntityTransVo;

public class MoneyEntityTransProvider {

	/**
	 * 
	 * @Title: getMoneyEntityTransList
	 * @param:
	 * @Description: 列表
	 * @return String
	 */
	public String getMoneyEntityTransList(MoneyEntityTransVo moneyEntityTransVo) {
		return new SQL() {
			{
				SELECT("money_entity_trans_bt_seq,entity_trans_do_itype,entity_trans_internal_code,entity_trans_from_seq,entity_trans_to_seq,entity_trans_amt,entity_trans_status_itype,entity_trans_desc,create_time");
				FROM("money_entity_trans_bt");
				WHERE(" entity_trans_from_seq=#{userId} or  entity_trans_to_seq=#{userId} ");
				ORDER_BY(" money_entity_trans_bt_seq desc limit #{offset},#{pageSize}");
			}
		}.toString();
	}

	/**
	 * 
	 * @Title: getMoneyEntityTransCount
	 * @param:
	 * @Description: 总数
	 * @return String
	 */
	public String getMoneyEntityTransCount(MoneyEntityTransVo moneyEntityTransVo) {
		return new SQL() {
			{
				SELECT("COUNT(1)");
				FROM("money_entity_trans_bt");
				WHERE(" entity_trans_from_seq=#{userId} or  entity_trans_to_seq=#{userId} ");
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
	public String save(MoneyEntityTrans moneyEntityTrans) {
		String sql = new SQL() {
			{
				INSERT_INTO("money_entity_trans_bt");
				VALUES("entity_trans_do_itype", "#{type}");
				VALUES("entity_trans_internal_code", "#{transInternalCode}");
				VALUES("entity_trans_from_seq", "#{transFromId}");
				VALUES("entity_trans_to_seq", "#{transToId}");
				VALUES("entity_trans_amt", "#{amt}");
				VALUES("entity_trans_status_itype", "#{status}");
				VALUES("entity_trans_desc", "#{desc}");

				VALUES("create_time", "now()");
				VALUES("create_by", "#{createBy}");
				VALUES("active_flag", "#{activeFlag}");
			}
		}.toString();
		return sql;
	}
}
