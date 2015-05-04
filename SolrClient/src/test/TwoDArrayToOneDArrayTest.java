package test;

import org.junit.Assert;
import org.junit.Test;

import statistic.ImageFeactureSizeStatistic;

public class TwoDArrayToOneDArrayTest {

	@Test
	public void test() {
		int[][] toTest2DArray = { { 1, 2 }, { 3, 4 }, { 5, 6 }, { 7, 8 }, { 9, 10 } };
		int[] expected1DArray = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
		Assert.assertArrayEquals(expected1DArray, ImageFeactureSizeStatistic.towDArrayToOneDArray(toTest2DArray));
	}

}
