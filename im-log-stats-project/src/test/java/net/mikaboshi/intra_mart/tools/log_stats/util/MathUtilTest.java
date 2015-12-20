/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package net.mikaboshi.intra_mart.tools.log_stats.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * {@link MathUtil}のテスト
 * @version 1.0.15
 * @since 1.0.15
 */
public class MathUtilTest {

	@Test
	public void test_getMedian_null() {

		assertEquals(1L, MathUtil.getMedian(null, 1L));
	}

	@Test
	public void test_getMedian_要素数0() {

		assertEquals(2L, MathUtil.getMedian(new ArrayList<Long>(), 2L));
	}

	@Test
	public void test_getMedian_要素数1() {

		List<Long> list = Arrays.asList(new Long[] { 10L });

		assertEquals(10L, MathUtil.getMedian(list, 0L));
	}

	@Test
	public void test_getMedian_要素数2() {

		List<Long> list = Arrays.asList(new Long[] { 10L, 12L });

		assertEquals(11L, MathUtil.getMedian(list, 0L));
	}

	@Test
	public void test_getMedian_要素数3() {

		List<Long> list = Arrays.asList(new Long[] { 10L, 12L, 14L });

		assertEquals(12L, MathUtil.getMedian(list, 0L));
	}

	@Test
	public void test_getMedian_偶数_割り切れる() {

		List<Long> list = Arrays.asList(new Long[] { 2L, 4L, 6L, 8L });

		assertEquals(5L, MathUtil.getMedian(list, 0L));
	}

	@Test
	public void test_getMedian_偶数_割り切れない() {

		List<Long> list = Arrays.asList(new Long[] { 2L, 5L, 10L, 15L });

		assertEquals(7L, MathUtil.getMedian(list, 0L));
	}

	@Test
	public void test_getMedian_奇数_割り切れない() {

		List<Long> list = Arrays.asList(new Long[] { 2L, 3L, 4L, 5L, 6L });

		assertEquals(4L, MathUtil.getMedian(list, 0L));
	}

	@Test
	public void test_getPercentile_null() {

		assertEquals(1L, MathUtil.getPercentile(null, 0, 1L));
	}

	@Test
	public void test_getPercentile_要素数0() {

		assertEquals(2L, MathUtil.getPercentile(null, 0, 2L));
	}

	@Test
	public void test_getPercentile_Pが不正() {

		List<Long> list = Arrays.asList(new Long[] { 2L, 4L, 6L, 8L, 10L, 12L, 14L, 16L, 18L, 20L });

		assertEquals(3L, MathUtil.getPercentile(list, -1, 3L));
		assertEquals(4L, MathUtil.getPercentile(list, 101, 4L));
	}

	@Test
	public void test_getPercentile_要素数10_割り切れる() {

		List<Long> list = Arrays.asList(new Long[] { 2L, 4L, 6L, 8L, 10L, 12L, 14L, 16L, 18L, 20L });

		assertEquals(19L, MathUtil.getPercentile(list, 90, 0L));
	}

	@Test
	public void test_getPercentile_要素数10_割り切れない() {

		List<Long> list = Arrays.asList(new Long[] { 2L, 4L, 6L, 8L, 10L, 12L, 14L, 16L, 18L, 19L });

		assertEquals(18L, MathUtil.getPercentile(list, 90, 0L));
	}

	@Test
	public void test_getPercentile_要素数1() {

		List<Long> list = Arrays.asList(new Long[] { 2L });

		assertEquals(2L, MathUtil.getPercentile(list, 90, 0L));
	}

	@Test
	public void test_getPercentile_要素数2() {

		List<Long> list = Arrays.asList(new Long[] { 2L, 4L });

		assertEquals(4L, MathUtil.getPercentile(list, 90, 0L));
	}

	@Test
	public void test_getPercentile_要素数3() {

		List<Long> list = Arrays.asList(new Long[] { 2L, 4L, 6L });

		assertEquals(6L, MathUtil.getPercentile(list, 90, 0L));
	}

	@Test
	public void test_getPercentile_要素数11() {

		List<Long> list = Arrays.asList(new Long[] { 2L, 4L, 6L, 8L, 10L, 12L, 14L, 16L, 18L, 20L, 22L });

		assertEquals(20L, MathUtil.getPercentile(list, 90, 0L));
	}

	@Test
	public void test_getPercentile_要素数19() {

		List<Long> list = Arrays.asList(new Long[] {
				2L, 4L, 6L, 8L, 10L, 12L, 14L, 16L, 18L, 20L,
				22L, 24L, 26L, 28L, 30L, 32L, 34L, 36L, 38L });

		assertEquals(36L, MathUtil.getPercentile(list, 90, 0L));
	}

	@Test
	public void test_getPercentile_要素数20() {

		List<Long> list = Arrays.asList(new Long[] {
				2L, 4L, 6L, 8L, 10L, 12L, 14L, 16L, 18L, 20L,
				22L, 24L, 26L, 28L, 30L, 32L, 34L, 36L, 38L, 40L });

		assertEquals(37L, MathUtil.getPercentile(list, 90, 0L));
	}
}
