package com.hd.cloud.service.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.hd.cloud.bo.City;
import com.hd.cloud.dao.CityDao;
import com.hd.cloud.service.CityService;

/**
 * 
 * @ClassName: CityServiceImpl
 * @Description: 城市
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月20日 下午5:52:18
 *
 */
@Service
public class CityServiceImpl implements CityService {

	@Inject
	private CityDao cityDao;

	@Override
	public List<City> getCountryCodeList() {
		return cityDao.getCountryCodeList();
	}

	@Override
	public List<City> getCountryCodeListByParentId(long parentId) {
		return cityDao.getCountryCodeListByParentId(parentId);
	}

	@Override
	public City getCountryNameById(int countryId) {
		return cityDao.getCountryNameById(countryId);
	}

}
