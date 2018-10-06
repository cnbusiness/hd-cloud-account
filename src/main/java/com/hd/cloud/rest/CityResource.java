package com.hd.cloud.rest;

import java.util.List;

import javax.inject.Inject;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.bo.City;
import com.hd.cloud.service.CityService;
import com.hlb.cloud.bo.BoUtil;
import com.hlb.cloud.util.CommonConstantUtil;

/**
 * 
 * @ClassName: CityController
 * @Description: 城市
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年11月21日 上午9:44:58
 *
 */
@RefreshScope
@RestController
@RequestMapping("/nofilter")
public class CityResource {

	@Inject
	private CityService cityService;

	/**
	 * 
	 * @Title: getCountryCodeList
	 * @param:
	 * @Description: 获取国家码
	 * @return BoUtil
	 */
	@ResponseBody
	@RequestMapping(value = "/city", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public BoUtil getCountryCodeList() {
		BoUtil bo = BoUtil.getDefaultTrueBo();
		List<City> cities = cityService.getCountryCodeList();
		bo.setData(cities);
		bo.setCode(CommonConstantUtil.SUCCESS);
		return bo;
	}

	/**
	 * 
	 * @Title: getCityList
	 * @param:
	 * @Description: 获取城市信息
	 * @return BoUtil
	 */
	@ResponseBody
	@RequestMapping(value = "/city/{parentId}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
	public BoUtil getCityList(@PathVariable("parentId") Long parentId) {
		BoUtil bo = BoUtil.getDefaultTrueBo();
		if (parentId == null) {
			parentId = 0l;
		}
		List<City> cities = cityService.getCountryCodeListByParentId(parentId);
		bo.setData(cities);
		return bo;
	}
}
