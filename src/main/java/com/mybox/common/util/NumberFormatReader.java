package com.mybox.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class NumberFormatReader {

	public static boolean isLongFormat(String number) {
		if (!hasNumber(number)) return false;
		return getLongFormatNumber(number) > 0;
	}

	public static Long convertLongValue(Long number) {
		return number == null ? 0 : number;
	}

	private static boolean hasNumber(String number) {
		return StringUtils.hasText(number) && !"null".equalsIgnoreCase(number);
	}

	private static long getLongFormatNumber(String number) {
		long longFormatNumber = 0;
		try {
			longFormatNumber = Long.parseLong(number);
		} catch (NumberFormatException e) {
			log.error("Long Type 이 아닙니다. : {}", number);
		}
		return longFormatNumber;
	}

}
