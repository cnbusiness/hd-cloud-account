package com.hd.cloud.service;

import java.util.List;

import com.hd.cloud.bo.City;

/**
 * 
 * @ClassName: CityService
 * @Description: 城市信息
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:50:05
 *
 */
public interface CityService {

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
	 * @Description: 获取国家名
	 * @return String
	 */
	public City getCountryNameById(int countryId);

}
