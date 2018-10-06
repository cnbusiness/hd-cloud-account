package com.hd.cloud.dao;

import java.util.List;

import com.hd.cloud.bo.City;

/**
 * 
 * @ClassName: CityDao
 * @Description: 城市dao
 * @author Sheng sheng.haohao@autoflysoft.com
 * @Company dctp
 * @date 2017年8月7日 下午9:12:19
 *
 */
public interface CityDao {

	/**
	 * 
	 * @Title: checkIsCountryCode
	 * @param: countryCode
	 *             国家码
	 * @Description: 校验国家码是否正确
	 * @return int
	 */
	public int checkIsCountryCode(String countryCode);

	/**
	 * 
	 * @Title: queryCountrySimpleCode
	 * @param:
	 * @Description: 通过区号国家码查询国家简码
	 * @return String
	 */
	public String queryCountrySimpleCode(String countryCode);

	/**
	 * 
	 * @Title: getCountryCodeList
	 * @param:
	 * @Description: 获取国家码列表
	 * @return List<City>
	 */
	public List<City> getCountryCodeList();

	/**
	 * 
	 * @Title: getCountryCodeListByParentId
	 * @param:
	 * @Description: 根据父节点查询子节点信息
	 * @return List<City>
	 */
	public List<City> getCountryCodeListByParentId(long parentId);

	/**
	 * 
	 * @Title: getCountryNameById
	 * @param:
	 * @Description: 通过国家id查询国家名
	 * @return String
	 */
	public City getCountryNameById(int countryId);
}
