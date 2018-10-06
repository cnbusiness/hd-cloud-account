package com.hd.cloud.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import com.hd.cloud.enummap.UserTypeEnum;

/**
 * 
 * @ClassName: SequenceUtil
 * @Description: 生成订单号
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2017年12月18日 下午5:28:44
 *
 */
final public class SequenceUtil {

	private static SequenceUtil sequenceUtil = null;
	private static String dateString="";
	private static String dateOrderString = "", dateMsgString = "", dateRefundString = "";
	private static AtomicInteger orderAi = new AtomicInteger();
	private static AtomicInteger rebateOrderAi = new AtomicInteger();
	private static AtomicInteger msgAi = new AtomicInteger();
	private final static String MSG_PREFIX = "SM";
	private final static String REFUND_PREFIX = "20";
	private final static int fixLen = 6;
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
	
	private final static String PERSONAL_WALLET_PREFIX = "U";
	private final static String SHOP_WALLET_PREFIX = "P";
	private final static String SYSTEM_WALLET_PREFIX="S";

	/**
	 * 构造函数
	 */
	private SequenceUtil() {
	}

	/**
	 * 
	 * getInstance
	 * 
	 * @param
	 * @return SequenceUtil
	 */
	public synchronized static SequenceUtil getInstance() {
		if (null == sequenceUtil) {
			sequenceUtil = new SequenceUtil();
		}
		return sequenceUtil;
	}

	/**
	 * 
	 * @Title: generateOrderId
	 * @param:
	 * @Description: 生成订单号 规则 : 10 + 150515100013(yyMMddHHmmss) + 1(服务器节点编号) +
	 *               1(自增流水号)+ 3151（随机生成数，位数根据自增流水的长度来定）
	 *               注：自增流水号、服务器节点编号和随机生成数的位数之和是固定6位 generateOrderId
	 * @param :
	 *            serverNum
	 *            服务器编号（每台节点服务器在配置文件中定义一个编号,编号是数字字符串,如"1"：服务器节点一，"2":服务器节点二，"3":服务器节点三）
	 *            String
	 * @return String
	 */
	public String generateOrderId(String serverNum) {
		String strOrder = sdf.format(new Date());
		String seq = "";
		if (dateOrderString.equals(strOrder)) {
			seq = String.valueOf(orderAi.incrementAndGet());
			if (seq.length() <= 0 || seq.length() > 4) {
				return strOrder + "0" + SequenceUtil.getFixLenthString(fixLen - 1);
			}
			return strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		} else {
			dateOrderString = strOrder;
			orderAi = new AtomicInteger();
			seq = String.valueOf(orderAi.incrementAndGet());
			return strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		}
	}

	/**
	 * 
	 * @Title: generateMsgId
	 * @param:
	 * @Description: 生成随机id
	 * @return String
	 */
	public String generateMsgId(String serverNum) {
		String strOrder = sdf.format(new Date());
		String seq = "";
		if (dateMsgString.equals(strOrder)) {
			seq = String.valueOf(msgAi.incrementAndGet());
			if (seq.length() <= 0 || seq.length() > 4) {
				return MSG_PREFIX + strOrder + "0" + SequenceUtil.getFixLenthString(fixLen - 1);
			}
			return MSG_PREFIX + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		} else {
			dateMsgString = strOrder;
			msgAi = new AtomicInteger();
			seq = String.valueOf(msgAi.incrementAndGet());
			return MSG_PREFIX + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		}
	}

	/**
	 * 
	 * @Title: generateOrderRefundNo
	 * @param: prefix[根据两位交易类型]+时间戳(精确到秒)+流水号(四位递增),如：IO01201508111000130001
	 * @Description: 退款订单号
	 * @return String
	 */
	public String generateOrderRefundNo(String serverNum) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String strOrder = sdf.format(new Date());
		String prefix = REFUND_PREFIX;
		String seq = "";
		if (dateRefundString.equals(strOrder)) {
			seq = String.valueOf(rebateOrderAi.incrementAndGet());
			if (seq.length() <= 0 || seq.length() > 4) {
				return prefix + strOrder + "0" + SequenceUtil.getFixLenthString(fixLen - 1);
			}
			return prefix + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		} else {
			dateRefundString = strOrder;
			rebateOrderAi = new AtomicInteger();
			seq = String.valueOf(rebateOrderAi.incrementAndGet());
			return prefix + strOrder + serverNum + seq
					+ SequenceUtil.getFixLenthString(fixLen - seq.length() - serverNum.length());
		}
	}

	/**
	 * 钱包ID生成规则：前缀(U:个人用户、P:店铺用户、S:系统用户)+时间戳(精确到秒)+流水号(四位递增)
	 * @param userType
	 * @return
	 */
	public String generateWalletId(int userType) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		String strOrder = sdf.format(new Date());
		String prefix = "";
		String seq = "";
		if(userType == UserTypeEnum.SYSTEM_USER.getIntValue()) {
			prefix = SYSTEM_WALLET_PREFIX;
		} else if(userType == UserTypeEnum.PERSONAL_USER.getIntValue()) {
			prefix = PERSONAL_WALLET_PREFIX;
		} else if(userType == UserTypeEnum.SHOP_USER.getIntValue()) {
			prefix = SHOP_WALLET_PREFIX;
		}
		if(dateString.equals(strOrder)){
			seq = String.valueOf(rebateOrderAi.incrementAndGet());
			if(seq.length()<=0 || seq.length() > 4){
				System.out.println("============================================");
				return prefix + strOrder + "0" + SequenceUtil.getFixLenthString(fixLen-1);
			}
			return prefix + strOrder + seq + SequenceUtil.getFixLenthString(fixLen-seq.length());		
		} else {
			dateString = strOrder;
			rebateOrderAi = new AtomicInteger();
			seq = String.valueOf(rebateOrderAi.incrementAndGet());
			return prefix + strOrder + seq + SequenceUtil.getFixLenthString(fixLen-seq.length());			
		}
	}
	
	
	/**
	 * strLength必须至少要有一位,不能超过5位
	 * 
	 * @param strLength
	 * @return
	 */
	private static String getFixLenthString(int strLength) {
		if (strLength <= 0 || strLength > 5) {
			return "";
		}
		int pross = 0;
		if (strLength == 5) {
			pross = ThreadLocalRandom.current().nextInt(10000, 99999);
		} else if (strLength == 4) {
			pross = ThreadLocalRandom.current().nextInt(1000, 9999);
		} else if (strLength == 3) {
			pross = ThreadLocalRandom.current().nextInt(100, 999);
		} else if (strLength == 2) {
			pross = ThreadLocalRandom.current().nextInt(10, 99);
		} else if (strLength == 1) {
			pross = ThreadLocalRandom.current().nextInt(1, 9);
		} else {
			return "";
		}
		return String.valueOf(pross);
	}

}