package com.jidd.basic.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.jidd.basic.enums.JiddSymbolEnum;
@SuppressWarnings("rawtypes")
public class JiddValidateUtils {

	/**
	 * 验证空值
	 * 
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String) {
			return StringUtils.isBlank(o.toString());
		}

		if (o instanceof Collection) {
			return ((Collection) o).isEmpty();
		}

		if (o instanceof Map) {
			return ((Map) o).isEmpty();
		}
		if (o instanceof Object[]) {
			return ((Object[]) o).length == 0;
		}
		return false;
	}

	/**
	 * 验证邮箱格式
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (isEmpty(email)) {
			return false;
		}
		return email
				.matches("\\w+[a-zA-Z0-9_\\-\\.]+@\\w+.((com|cn|org|edu|hk)|((com|cn|org|edu|hk).((cn|org|edu|hk))))");
	}

	/**
	 * 判断是否是一个有效的用户名(最长为50位)
	 * 
	 * @param name
	 *            [用户名]
	 * @return 如果是(true),否则(false)
	 */
	public static boolean isUserName(String name) {
		if (JiddStringUtils.isBlank(name)) {
			return false;
		}
		return Pattern.matches("[\u4e00-\u9fa5a-zA-Z0-9]{1,15}", name);
	}

	/**
	 * 验证用户姓名
	 * 
	 * @param realName
	 * @return
	 */
	public static boolean isRealName(String realName) {
		if (isEmpty(realName)) {
			return false;
		}
		return realName.matches("^[a-zA-Z·.\u4e00-\u9fa5]+$");
	}

	/**
	 * 验证身份证号
	 * 
	 * @param idCode
	 * @return
	 */
	public static boolean isIdCode(String idCode) {
		if (isEmpty(idCode)) {
			return false;
		}
		idCode = idCode.toLowerCase();
		if (idCode.length() == 18) {
			int sum = 0;
			int[] wi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };// wi表示第i个位子上的加权因子
			char[] checkCode = { '1', '0', 'x', '9', '8', '7', '6', '5', '4',
					'3', '2' };
			for (int i = 0; i < 17; i++) {
				char ch = idCode.charAt(i);
				int code = ch - '0';
				sum = sum + code * wi[i];
			}
			int y = sum % 11;
			char c = (char) (idCode.charAt(17));
			return checkCode[y] == c;
		} else {
			if (idCode.length() == 15) {
				if (StringUtils.isNumeric(idCode)) {
					String str = "19" + idCode.substring(6, 11);
					return JiddDateUtils.parseDate(JiddDateUtils.yyyyMMdd,
							str) != null;
				}
			}
		}
		return false;
	}

	/**
	 * 验证是否为手机号码
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean isMobile(String mobile) {
		if (isEmpty(mobile)) {
			return false;
		}
		// 2017-01-12 手机验证，放宽条件
		return mobile.matches("^1\\d{10}$");
	}

	/**
	 * 验证卡号，不使用luhn算法，因为有些新卡不支持luhn算法
	 *
	 * @param cardNo
	 * @return
	 */
	public static boolean isCardNo(String cardNo) {
		return !isEmpty(cardNo)
				&& cardNo.length() >= 11 && cardNo.length() <= 19
				&& cardNo.matches("^[0-9]*$");
	}

	/**
	 * 验证卡号
	 * 
	 * @param cardNo
	 * @return
	 */
	public static boolean isBankCard(String cardNo) {
		if (isEmpty(cardNo)) {
			return false;
		}
		if (!cardNo.matches("^[0-9]*$")) {
			return false;
		}
		int sum = 0;
		int digit = 0;
		int addend = 0;
		boolean timesTwo = false;
		for (int i = cardNo.length() - 1; i >= 0; i--) {
			digit = Integer.parseInt(cardNo.substring(i, i + 1));
			if (timesTwo) {
				addend = digit * 2;
				if (addend > 9) {
					addend -= 9;
				}
			} else {
				addend = digit;
			}
			sum += addend;
			timesTwo = !timesTwo;
		}
		int modulus = sum % 10;
		return modulus == 0;
	}

	/**
	 * 判断是否符合金额格式
	 * 
	 * @param money
	 *            [金额对象]
	 * @param maxDecimalLen
	 *            [允许最大的小数点长度]
	 * @return
	 */
	public static boolean isMoney(Object money, int maxDecimalLen) {
		if (isEmpty(money)) {
			return false;
		}
		if (money instanceof Integer || money instanceof Long) {
			return true;
		}
		String ms = money.toString();
		if (money instanceof Double || money instanceof Float
				|| money instanceof String || money instanceof BigDecimal) {
			String regex = "^(([1-9]\\d*)|0)";
			if (maxDecimalLen > 0) {
				regex = regex.concat("(\\.\\d{1," + maxDecimalLen + "})?");
			}
			regex = regex.concat("$");
			/*if (money instanceof BigDecimal) {
				ms = ((BigDecimal) money).toPlainString();
			}*/
			return ms.matches(regex);
		}
		return false;
	}

	/**
	 * 验证ip地址的正确性
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIp(String ip) {
		if (isEmpty(ip)) {
			return false;
		}
		return ip
				.matches("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
	}

	/**
	 * 验证电话号码
	 * 
	 * @param telNo
	 * @return
	 */
	public static boolean isTelNo(String telNo) {
		if (isEmpty(telNo)) {
			return false;
		}
		return telNo.matches("^[0][1-9]{2,3}-[0-9]{5,10}$")
				|| telNo.matches("^[1-9]{1}[0-9]{5,8}$");
	}

	/**
	 * 验证是否是数字
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isNum(String num) {
		if (isEmpty(num)) {
			return false;
		}
		return num.matches("^[0-9]*$");
	}

	/**
	 * 判断是否是字母
	 * 
	 * @param letter
	 * @return
	 */
	public static boolean isLett(String letter) {
		if (isEmpty(letter)) {
			return false;
		}
		return letter.matches("^[a-zA-Z]+$");
	}

	/**
	 * 判断是否是汉字
	 * 
	 * @param sinogram
	 * @return
	 */
	public static boolean isSinogram(String sinogram) {
		if (isEmpty(sinogram)) {
			return false;
		}
		return sinogram.matches("^[\u4e00-\u9fa5]+$");
	}

	/**
	 * 判断是否是 字母、数字的组合(必须都包含)
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isLettAndNum(String source) {
		if (isEmpty(source)) {
			return false;
		}
		return source.matches("^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{2,}$");
	}

	/**
	 * 判断是否是 字母、数字、特殊字符的组合(必须都包含)
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isLettAndNumAndSpecChar(String source) {
		if (isEmpty(source)) {
			return false;
		}
		return source
				.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$_&%~])[0-9a-zA-Z!@#$_&%~]{3,}$");
	}

	/**
	 * 判断是否是字母、数字或者两者组合
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isLettOrNum(String source) {
		if (isEmpty(source)) {
			return false;
		}
		return source.matches("^[0-9a-zA-Z]+$");
	}

	/**
	 * 判断是否是汉字、字母、数字或者三者组合
	 * 
	 * @param source
	 * @return
	 */
	public static boolean isLettOrNumOrSig(String source) {
		if (isEmpty(source)) {
			return false;
		}
		return source.matches("^[0-9a-zA-Z\u4e00-\u9fa5]+$");
	}

	/**
	 * 验证是否为HTTP链接（忽略queryString部分）
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isUrl(String url) {
		if (isEmpty(url)) {
			return false;
		}
		return url
				.matches("^http[s]?://[\\w\\.\\-:]+(?:/|(?:/[\\w\\.\\-]+)*)?(/|(\\?.*))?$");
	}

	private static final JiddSymbolEnum[] PATTERN_SYMBOL = new JiddSymbolEnum[] {
		JiddSymbolEnum.Space, JiddSymbolEnum.Line, JiddSymbolEnum.Slash,
		JiddSymbolEnum.MaoHao };

	/**
	 * 判断时间格式
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static boolean isDateMatchPattern(String date, String pattern) {
		if (JiddStringUtils.isBlank(date) || JiddStringUtils.isBlank(pattern)) {
			return false;
		}
		for (JiddSymbolEnum symbol : PATTERN_SYMBOL) {
			if (!pattern.contains(symbol.symbol())
					&& date.contains(symbol.symbol())) {
				return false;
			}
			if (pattern.contains(symbol.symbol())
					&& !date.contains(symbol.symbol())) {
				return false;
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			sdf.setLenient(false);
			sdf.parse(date);
			// System.out.println(sdf.toPattern());
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断字符串是否是Double类型
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isDoubleType(String s) {
		try {
			Double.parseDouble(s);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Map obj = new HashMap();
		boolean flag = isEmpty(obj);
		System.out.println(flag);
		System.out.println(isRealName("龍"));
	}
}
