package statistic;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Point;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;

import com.company.FileSaver;
import com.google.gson.Gson;

public class ImageFeactureSizeStatistic {

	public static void main(String[] args) throws IOException {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		generateImageString("/Users/ruishan/out");
		generateImageFeature("/Users/ruishan/out");
		performStatistic();
	}

	public static void generateImageString(String fileName) throws IOException {
		System.out.println("creating image base 64 string start...");

		File file = new File(fileName);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File currentFile = files[i];
			// File currentFile = new
			// File("/Users/ruishan/book_covers/Reference/020.jpg");
			BufferedImage image = ImageIO.read(currentFile);
			if (null != image) {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				String suffix = suffix(currentFile);
				BufferedImage resizedImage = MySurfDocumentBuilder.resizeQueryImage(image, 1000);
				ImageIO.write(resizedImage, suffix, outStream);

				FileSaver.save("/Users/ruishan/fileStatistic/imageStringFile/" + i + ".txt", Base64.encodeBase64String(outStream.toByteArray()));
			}
		}
		System.out.println("creating image base 64 string finished...");
	}

	public static void generateImageFeature(String fileName) throws IOException {
		System.out.println("creating image feature string start...");
		File file = new File(fileName);
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			File currentFile = files[i];
			// File currentFile = new
			// File("/Users/ruishan/book_covers/Reference/020.jpg");
			BufferedImage bufferedImage = ImageIO.read(currentFile);
			if (null != bufferedImage) {
				DescriptorExtractor orbExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
				MatOfKeyPoint points = new MatOfKeyPoint();

				FeatureDetector orbDetector = FeatureDetector.create(FeatureDetector.ORB);

				BufferedImage resizedImage = MySurfDocumentBuilder.resizeQueryImage(bufferedImage, 1000);
				// BufferedImage resizedImage = bufferedImage;

				Mat mat = toMat(resizedImage);
				orbDetector.detect(mat, points);
				Mat features = new Mat();
				orbExtractor.compute(mat, points, features);
				int rows = features.rows();

				KeyPoint[] keyPoints = points.toArray();

				List<String> featureList = new ArrayList<String>();
				int[][] points2DArray = new int[rows][2];
				for (int j = 0; j < rows; j++) {
					Mat feature = features.row(j);
					OrbFeature orbFeature = new OrbFeature(feature);

					Point point = keyPoints[j].pt;
					points2DArray[j][0] = (int) point.x;
					points2DArray[j][1] = (int) point.y;

					featureList.add(Base64.encodeBase64String(orbFeature.getByteArrayRepresentation()));
				}

				int[] pointsArray = towDArrayToOneDArray(points2DArray);
				FeatureAndPoints obj = new FeatureAndPoints(featureList, pointsArray);
				Gson gson = new Gson();

				try {
					// String compressedString =
					// compressString(gson.toJson(obj));
					FileSaver.save("/Users/ruishan/fileStatistic/FeatureFile/" + i + ".txt", gson.toJson(obj));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("creating image feature string finished...");
	}

	public static int[] towDArrayToOneDArray(int[][] points2dArray) {
		if (null == points2dArray) {
			return null;
		}
		int length = points2dArray.length;
		int[] out = new int[length * points2dArray[0].length];
		int pos = 0;
		for (int i = 0; i < length; i++) {
			int[] temp = points2dArray[i];
			for (int j = 0; j < temp.length; j++) {
				out[pos++] = temp[j];
			}
		}
		return out;
	}

	private static void performStatistic() {
		System.out.println("average image string file size is " + calculateAllFilesSize("/Users/ruishan/fileStatistic/imageStringFile") + " KB");
		System.out.println("average image feature file size is " + calculateAllFilesSize("/Users/ruishan/fileStatistic/FeatureFile") + " KB");

	}

	private static int calculateAllFilesSize(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		int totalSize = 0;
		for (int i = 0; i < files.length; i++) {
			File currentFile = files[i];
			totalSize += currentFile.length();
		}
		return (totalSize / files.length) / 1024;
	}

	private static String compressString(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("ISO-8859-1");
	}

	private static String suffix(File currentFile) {
		if (null == currentFile)
			return "";

		String fileName = currentFile.getName();
		return fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
	}

	private static Mat toMat(BufferedImage image) {
		if (null == image) {
			return null;
		}
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		mat.put(0, 0, data);
		return mat;
	}

}
