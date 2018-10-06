package com.hd.cloud.dao.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Repository;

import com.hd.cloud.bo.City;
import com.hd.cloud.dao.CityDao;
import com.hd.cloud.dao.mapper.CityMapper;

/**
 * 
 * @ClassName: CityDaoMyBatisImpl
 * @Description: 城市dao
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:47:58
 *
 */
@Repository
public class CityDaoMyBatisImpl implements CityDao {

	@Inject
	private CityMapper cityMapper;

	@Override
	public int checkIsCountryCode(String countryCode) {
		return cityMapper.queryCounTryCode(countryCode);
	}

	@Override
	public String queryCountrySimpleCode(String countryCode) {
		return cityMapper.queryCountrySimpleCode(countryCode);
	}

	@Override
	public List<City> getCountryCodeList() {
		return cityMapper.getCountryCodeList();
	}

	@Override
	public List<City> getCountryCodeListByParentId(long parentId) {
		return cityMapper.getCountryCodeListByParentId(parentId);
	}

	@Override
	public City getCountryNameById(int countryId) {
		return cityMapper.getCountryNameById(countryId);
	}
}
