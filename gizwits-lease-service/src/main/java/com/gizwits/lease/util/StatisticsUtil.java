package com.gizwits.lease.util;

import java.math.BigDecimal;

public class StatisticsUtil {

	public static double calcIncreasePercent(double pre, double next) {
		return pre == 0 ? 0 :
				BigDecimal.valueOf((next - pre) * 100).divide(BigDecimal.valueOf(pre), 2, BigDecimal.ROUND_HALF_UP)
						.doubleValue();
	}
}
