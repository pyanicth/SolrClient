package statistic;

import java.util.List;

import org.opencv.core.Point;

public class FeatureAndPoints {

	private List<String> features;
	private List<Point> pointsList;
	private int[][] pointsArray;

	public FeatureAndPoints(List<String> features, List<Point> points) {
		this.features = features;
		this.pointsList = points;
	}

	public FeatureAndPoints(List<String> features, int[][] pointsArray) {
		this.features = features;
		this.setPointsArray(pointsArray);
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

	public List<Point> getPoints() {
		return pointsList;
	}

	public void setPoints(List<Point> points) {
		this.pointsList = points;
	}

	public int[][] getPointsArray() {
		return pointsArray;
	}

	public void setPointsArray(int[][] pointsArray) {
		this.pointsArray = pointsArray;
	}

}
