package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.Point;

public class OneDArrayToPointList {

	@Test
	public void test() {
		int[] originalArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		List<Point> expectedResult = expectedPointList();
		
		List<Point> actual = convertArrayToPoints(originalArray);
		Assert.assertEquals(expectedResult, actual);
	}
	
	private List<Point> convertArrayToPoints(int[] positionArray) {
		List<Point> points = new ArrayList<Point>();
		if (null == positionArray) {
			return points;
		}

		for (int i = 0; i < positionArray.length; i++) {
			Point point = new Point(positionArray[i], positionArray[i + 1]);
			i++;
			points.add(point);
			point = null;
		}
		return points;
	}
	
	private List<Point> expectedPointList(){
		List<Point> expectedResult = new ArrayList<Point>();
		expectedResult.add(new Point(1, 2));
		expectedResult.add(new Point(3, 4));
		expectedResult.add(new Point(5, 6));
		expectedResult.add(new Point(7, 8));
		expectedResult.add(new Point(9, 10));
		return expectedResult;
	}

}
