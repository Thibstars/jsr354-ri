/*
 * Copyright (c) 2012, 2013, Credit Suisse (Anatole Tresch), Werner Keil.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Contributors: Anatole Tresch - initial implementation Wernner Keil -
 * extensions and adaptions.
 */
package org.javamoney.moneta;

import java.math.BigDecimal;

import javax.money.CurrencyUnit;
import javax.money.MonetaryCurrencies;
import javax.money.MonetaryRoundings;

import org.junit.Test;

/**
 * @author Anatole
 * 
 */
public class PerformanceTest {

	private static final BigDecimal TEN = new BigDecimal(10.0d);
	protected static final CurrencyUnit EURO = MonetaryCurrencies
			.getCurrency("EUR");
	protected static final CurrencyUnit DOLLAR = MonetaryCurrencies
			.getCurrency("USD");

	@Test
	public void comparePerformance() {
		StringBuilder b = new StringBuilder();
		b.append("PerformanceTest - Looping code Money,BD:\n");
		b.append("========================================\n");
		b.append("Money money1 = money1.add(Money.of(EURO, 1234567.3444));\n");
		b.append("money1 = money1.subtract(Money.of(EURO, 232323));\n");
		b.append("money1 = money1.multiply(3.4);\n");
		b.append("money1 = money1.divide(5.456);\n");
		b.append("money1 = money1.with(MonetaryRoundings.getRounding());\n");
		System.out.println(b);
		b.setLength(0);
		Money money1 = Money.of(EURO, BigDecimal.ONE);
		long start = System.currentTimeMillis();
		final int NUM = 100000;
		for (int i = 0; i < NUM; i++) {
			money1 = money1.add(Money.of(EURO, 1234567.3444));
			money1 = money1.subtract(Money.of(EURO, 232323));
			money1 = money1.multiply(3.4);
			money1 = money1.divide(5.456);
			money1 = money1.with(MonetaryRoundings.getRounding());
		}
		long end = System.currentTimeMillis();
		long duration = end - start;
		System.out.println("Duration for " + NUM + " operations (Money,BD): "
				+ duration + " ms (" + ((duration * 1000) / NUM)
				+ " ns per loop) -> "
				+ money1);
		System.out.println();
		b.append("PerformanceTest - Looping code FastMoney,long:\n");
		b.append("==============================================\n");
		b.append("FastMoney money1 = money1.add(FastMoney.of(EURO, 1234567.3444));\n");
		b.append("money1 = money1.subtract(FastMoney.of(EURO, 232323));\n");
		b.append("money1 = money1.multiply(3.4);\n");
		b.append("money1 = money1.divide(5.456);\n");
		b.append("money1 = money1.with(MonetaryRoundings.getRounding());\n");
		System.out.println(b);
		b.setLength(0);
		FastMoney money2 = FastMoney.of(EURO, BigDecimal.ONE);
		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			money2 = money2.add(FastMoney.of(EURO, 1234567.3444));
			money2 = money2.subtract(FastMoney.of(EURO, 232323));
			money2 = money2.multiply(3.4);
			money2 = money2.divide(5.456);
			money2 = money2.with(MonetaryRoundings.getRounding());
		}
		end = System.currentTimeMillis();
		duration = end - start;
		System.out.println("Duration for " + NUM
				+ " operations (FastMoney,long): "
				+ duration + " ms (" + ((duration * 1000) / NUM)
				+ " ns per loop) -> "
				+ money2);
		System.out.println();
		b.append("PerformanceTest - Looping code Mixed 1, long/BD:\n");
		b.append("================================================\n");
		b.append("FastMoney money1 = money1.add(Money.of(EURO, 1234567.3444));\n");
		b.append("money1 = money1.subtract(Money.of(EURO, 232323));\n");
		b.append("money1 = money1.multiply(3.4);\n");
		b.append("money1 = money1.divide(5.456);\n");
		b.append("money1 = money1.with(MonetaryRoundings.getRounding());\n");
		System.out.println(b);
		b.setLength(0);
		FastMoney money3 = FastMoney.of(EURO, BigDecimal.ONE);
		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			money3 = money3.add(Money.of(EURO, 1234567.3444));
			money3 = money3.subtract(Money.of(EURO, 232323));
			money3 = money3.multiply(3.4);
			money3 = money3.divide(5.456);
			money3 = money3.with(MonetaryRoundings.getRounding());
		}
		end = System.currentTimeMillis();
		duration = end - start;
		System.out.println("Duration for " + NUM
				+ " operations (FastMoney/Money mixed 2): "
				+ duration + " ms (" + ((duration * 1000) / NUM)
				+ " ns per loop) -> "
				+ money3);
		System.out.println();
		b.append("PerformanceTest - Looping code Mixed 2, BD/long:\n");
		b.append("================================================\n");
		b.append("Money money1 = money1.add(FastMoney.of(EURO, 1234567.3444));\n");
		b.append("money1 = money1.subtract(FastMoney.of(EURO, 232323));\n");
		b.append("money1 = money1.multiply(3.4);\n");
		b.append("money1 = money1.divide(5.456);\n");
		b.append("money1 = money1.with(MonetaryRoundings.getRounding());\n");
		System.out.println(b);
		b.setLength(0);
		Money money4 = Money.of(EURO, BigDecimal.ONE);
		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			money4 = money4.add(FastMoney.of(EURO, 1234567.3444));
			money4 = money4.subtract(Money.of(EURO, 232323));
			money4 = money4.multiply(3.4);
			money4 = money4.divide(5.456);
			money4 = money4.with(MonetaryRoundings.getRounding());
		}
		end = System.currentTimeMillis();
		duration = end - start;
		System.out.println("Duration for " + NUM
				+ " operations (Money/FastMoney mixed 2): "
				+ duration + " ms (" + ((duration * 1000) / NUM)
				+ " ns per loop) -> "
				+ money4);
		System.out.println();
		System.out.println();
	}

	@Test
	public void comparePerformanceWithFastMoneyCaching() {
		StringBuilder b = new StringBuilder();
		b.append("PerformanceTest - Test Caching benefit FastMoney:\n");
		b.append("=======================================---------=\n");
		b.append("FastMoney money1 = FastMoney.of(EURO, c));\n");
		b.append("vs\n");
		b.append("FastMoney money1 = FastMoney.of(EURO, c++));\n");
		System.out.println(b);
		b.setLength(0);
		final int NUM = 1000000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			FastMoney.of(EURO, 1);
		}
		long end = System.currentTimeMillis();
		long duration = end - start;
		System.out.println("Duration for " + NUM
				+ " creations of FastMoney 'EUR c=1': "
				+ duration + " ms (" + ((duration * 1000) / NUM)
				+ " ns per loop).");
		System.out.println();
		System.out.println();
		start = System.currentTimeMillis();
		for (int i = 0; i < NUM; i++) {
			Money.of(EURO, i);
		}
		end = System.currentTimeMillis();
		duration = end - start;
		System.out.println("Duration for " + NUM
				+ " creations of FastMoney 'EUR c=1,c++': "
				+ duration + " ms (" + ((duration * 1000) / NUM)
				+ " ns per loop).");
		System.out.println();
		System.out.println();
	}
}