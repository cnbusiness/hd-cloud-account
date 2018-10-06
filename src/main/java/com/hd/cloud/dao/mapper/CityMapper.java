package com.hd.cloud.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import com.hd.cloud.bo.City;

/**
 * 
 * @ClassName: CityMapper
 * @Description: 城市
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:47:40
 *
 */
@Mapper
public interface CityMapper {

	/**
	 * 
	 * @Title: queryCounTryCode
	 * @Description: 查询该国家码的数量
	 * @Table pub_city_dict_sd 城市字典表
	 */
	@Select("select count(1) from pub_city_dict_sd where city_dict_intl_code=#{countryCode}")
	public int queryCounTryCode(@Param("countryCode") String countryCode);

	/**
	 * 
	 * @Title: queryCounTryCode
	 * @Description: 查询该国家简码
	 * @Table pub_city_dict_sd 城市字典表
	 */
	@Select("select city_dict_country_code from pub_city_dict_sd where city_dict_intl_code=#{countryCode} limit 0,1")
	public String queryCountrySimpleCode(@Param("countryCode") String countryCode);

	/**
	 * 
	 * @Title: getCountryCodeList
	 * @Description: 查询 国家码列表
	 * @Table pub_city_dict_sd 城市字典表
	 */
	@Select("select pub_city_dict_sd_seq,city_dict_intl_code,city_dict_name from pub_city_dict_sd where city_dict_intl_code IS NOT NULL and active_flag='y' ")
	@Results(value = {
			@Result(property = "cityId", column = "pub_city_dict_sd_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "countryCode", column = "city_dict_intl_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "cityName", column = "city_dict_name", javaType = String.class, jdbcType = JdbcType.VARCHAR) })
	List<City> getCountryCodeList();

	/**
	 * 
	 * @Title: getCountryCodeList
	 * @Description: 查询 国家码列表
	 * @Table pub_city_dict_sd 城市字典表
	 */
	@Select("SELECT pub_city_dict_sd_seq,city_dict_name FROM pub_city_dict_sd WHERE city_dict_level<=3 AND city_dict_parent_seq = #{parentId} LIMIT 10000")
	@Results(value = {
			@Result(property = "cityId", column = "pub_city_dict_sd_seq", javaType = int.class, jdbcType = JdbcType.INTEGER),
			@Result(property = "cityName", column = "city_dict_name", javaType = String.class, jdbcType = JdbcType.VARCHAR) })
	List<City> getCountryCodeListByParentId(@Param("parentId") long parentId);

	/**
	 * 
	 * @Title: getCountryNameById
	 * @Description: 查询 城市详情
	 * @Table pub_city_dict_sd 城市字典表
	 */
	@Select("SELECT pub_city_dict_sd_seq, city_dict_name FROM pub_city_dict_sd WHERE pub_city_dict_sd_seq = #{countryCode}")
	@Results(value = {
			@Result(property = "cityId", column = "pub_city_dict_sd_seq", javaType = long.class, jdbcType = JdbcType.BIGINT),
			@Result(property = "countryCode", column = "city_dict_intl_code", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "cityName", column = "city_dict_name", javaType = String.class, jdbcType = JdbcType.VARCHAR) })
	City getCountryNameById(int countryCode);
}
