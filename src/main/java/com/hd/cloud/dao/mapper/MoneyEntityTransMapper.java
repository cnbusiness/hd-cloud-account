package com.hd.cloud.dao.mapper;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.type.JdbcType;

import com.hd.cloud.bo.MoneyEntityTrans;
import com.hd.cloud.dao.sql.MoneyEntityTransProvider;
import com.hd.cloud.vo.MoneyEntityTransVo;

@Mapper
public interface MoneyEntityTransMapper {

	
	/**
	 * 
	 * @Title: getMoneyEntityTransList
	 * @Description: 查询用户交易记录
	 * @Table money_entity_trans_bt(交易记录表)
	 */
	@SelectProvider(type=MoneyEntityTransProvider.class,method="getMoneyEntityTransList")
	@Results(value = {
			@Result(property = "id", column = "money_entity_trans_bt_seq", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "type", column = "entity_trans_do_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "transInternalCode", column = "entity_trans_internal_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "transFromId", column = "entity_trans_from_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "transToId", column = "entity_trans_to_seq", javaType = Long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "amt", column = "entity_trans_amt", javaType = Double.class, jdbcType = JdbcType.DOUBLE),
			@Result(property = "desc", column = "entity_trans_status_itype", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "desc", column = "entity_trans_desc", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "createTime", column = "create_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP)
	})
	public List<MoneyEntityTrans> getMoneyEntityTransList(MoneyEntityTransVo moneyEntityTransVo);
	
	/**
	 * 
	 * @Title: getMoneyEntityTransCount
	 * @Description: 查询用户交易记录总数
	 * @Table money_entity_trans_bt(交易记录表)
	 */
	@SelectProvider(type=MoneyEntityTransProvider.class,method="getMoneyEntityTransCount")
	public Integer getMoneyEntityTransCount(MoneyEntityTransVo moneyEntityTransVo);
	
	
	/**
	 * 
	 * @Title: getMoneyEntityTransList
	 * @Description: 保存用户交易记录
	 * @Table money_entity_trans_bt(交易记录表)
	 */
	@InsertProvider(type=MoneyEntityTransProvider.class,method="save")
	@SelectKey(statement = {
			"SELECT LAST_INSERT_ID() AS id  " }, keyProperty = "id", before = false, resultType = Integer.class)
	public int save(MoneyEntityTrans moneyEntityTrans);
}
