package com.hd.cloud.enummap;

/**
 * 
 * @ClassName: UserTypeEnum
 * @Description: 用户类型
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年4月18日 上午9:19:11
 *
 */
public enum UserTypeEnum {

	/**
	 * ASCS
	 */
	SYSTEM_USER {
		public String getName() {
			return "ASCS";
		}

		public int getIntValue() {
			return 1;
		}

		@Override
		public String getValue() {
			return "1";
		}
	},
	/**
	 * 用户
	 */
	PERSONAL_USER {
		public String getName() {
			return "用户";
		}

		public int getIntValue() {
			return 2;
		}

		@Override
		public String getValue() {
			return "2";
		}
	},
	/**
	 * 店铺
	 */
	SHOP_USER {
		public String getName() {
			return "店铺";
		}

		public int getIntValue() {
			return 3;
		}

		@Override
		public String getValue() {
			return "3";
		}
	};

	public abstract String getName();

	public abstract int getIntValue();

	public abstract String getValue();

	public static String getCodeByIntValue(int intValue) {
		for (UserTypeEnum type : values()) {
			if (type.getIntValue() == intValue)
				return type.getValue();
		}
		return null;
	}
}
