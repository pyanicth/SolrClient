package com.company;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.imageanalysis.SurfFeature;
import net.semanticmetadata.lire.impl.SurfDocumentBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;

import com.google.gson.Gson;
import com.stromberglabs.jopensurf.SURFInterestPoint;
import com.stromberglabs.jopensurf.Surf;

public class Main {

	public static void main(String[] args) throws IOException {
		// write your code here
		// searchImageByFeature("photo");
		// searchImage("book_covers/Droid");
		/**
		 * for(int i=0;i<50;i++){ new Thread(new Runnable() {
		 * 
		 * @Override public void run() { // TODO Auto-generated method stub try
		 *           {
		 *           searchImageByByteArray("/Users/ruishan/book_covers/Reference"
		 *           ); } catch (IOException e) { // TODO Auto-generaed catch
		 *           block e.printStackTrace(); } } }).run();; }
		 */
		searchImageByByteArray("/Users/ruishan/book_covers/Droid");
//		 orbImageSearch("/Users/ruishan/book_covers/iPhone");
	}

	public static void orbFeatureSearch(String fileName) {

	}

	public static void orbImageSearch(String fileName) throws IOException {
		File file = new File(fileName);
		File[] files = file.listFiles();
		Gson gson = new Gson();
		int count = 0;
		int sumCount = files.length;
		UrlEncodedFormEntity entity;
		for (int i = 0; i < files.length; i++) {
			File currentFile = files[i];
			// File currentFile = new
			// File("/Users/ruishan/book_covers/Reference/020.jpg");
			BufferedImage image = ImageIO.read(currentFile);
			if (null != image) {
				HttpPost post = new HttpPost("http://127.0.0.1:8983/solr/collection1/lireq");
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				String suffix = suffix(currentFile);
				ImageIO.write(image, suffix, outStream);

				List<NameValuePair> list = new ArrayList<NameValuePair>();

				HttpClient httpClient = new DefaultHttpClient();

				NameValuePair pair2 = new BasicNameValuePair("image", Base64.encodeBase64String(outStream.toByteArray()));
				NameValuePair pair3 = new BasicNameValuePair("field", "orb_ha");
				// NameValuePair pair4 = new BasicNameValuePair("mode", "vw");
				// NameValuePair pair5 = new BasicNameValuePair("rows", "30");
				list.add(pair2);
				list.add(pair3);
				// list.add(pair4);
				// list.add(pair5);

				entity = new UrlEncodedFormEntity(list, "utf-8");
				post.setEntity(entity);

				HttpResponse httpResponse = httpClient.execute(post);
				System.out.println(httpResponse.getStatusLine().getStatusCode());
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					InputStream in = httpResponse.getEntity().getContent();
					String contentJson = getFileByte(in, (int) httpResponse.getEntity().getContentLength());
					contentJson = contentJson.trim();
					in.close();
					SimResponse simResponse = gson.fromJson(contentJson, SimResponse.class);
					List<Doc> resultDocs = simResponse.getDocs();
					if (null != resultDocs && resultDocs.size() > 0) {
						String responseId = simResponse.getDocs().get(0).getId();
						String requestId = currentFile.getAbsolutePath();
						if (isSimBook(responseId.substring(responseId.lastIndexOf('/') + 1, responseId.length()), requestId.substring(requestId.lastIndexOf('/') + 1, requestId.length()))) {
							System.out.println("request img:" + requestId + "; response img:" + responseId);
							count++;
							// } else if
							// (isSimBook(simResponse.getDocs().get(1).getId(),
							// requestId)) {
							// System.out.println("request img:" + requestId +
							// "; response img:" + responseId);
							// count++;
							// } else if
							// (isSimBook(simResponse.getDocs().get(2).getId(),
							// requestId)) {
							// System.out.println("request img:" + requestId +
							// "; response img:" + responseId);
							// count++;
							// }
							// for (int i1 = 0; i1 < 30; i1++) {
							// if (null != simResponse.getDocs()) {
							// if
							// (isSimBook(simResponse.getDocs().get(i1).getId(),
							// requestId)) {
							// System.out.println("request img:" + requestId +
							// "; response img:" + responseId);
							// count++;
							// break;
							// }
							// }
						} else {
							System.out.println("request img:" + requestId);
						}
					}
					System.out.println("i:" + i + " count:" + count);
				}
			}
		}
		System.out.println("similar count:" + count);
		System.out.println("sumCount" + sumCount);

	}

	public static void searchImageByByteArray(String fileName) throws IOException {
		File file = new File(fileName);
		File[] files = file.listFiles();
		Gson gson = new Gson();
		int count = 0;
		int sumCount = files.length;
		for (int i = 0; i < files.length; i++) {
			File currentFile = files[i];
			// File currentFile = new
			// File("/Users/ruishan/book_covers/Droid/020.jpg");
			BufferedImage image = ImageIO.read(currentFile);
			HttpPost post = new HttpPost("http://192.168.100.2:8988/solr/collection1/lireq");
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			String suffix = suffix(currentFile);
			ImageIO.write(image, suffix, outStream);

			List<NameValuePair> list = new ArrayList<NameValuePair>();

			HttpClient httpClient = new DefaultHttpClient();

			NameValuePair pair2 = new BasicNameValuePair("image", Base64.encodeBase64String(outStream.toByteArray()));
			NameValuePair pair3 = new BasicNameValuePair("field", "su_ha");
			NameValuePair pair4 = new BasicNameValuePair("mode", "vw");
			list.add(pair2);
			list.add(pair3);
			list.add(pair4);

			UrlEncodedFormEntity entity;
			entity = new UrlEncodedFormEntity(list, "utf-8");
			post.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(post);
			System.out.println(httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				InputStream in = httpResponse.getEntity().getContent();
				String contentJson = getFileByte(in, (int) httpResponse.getEntity().getContentLength());
				contentJson = contentJson.trim();
				SimResponse simResponse = gson.fromJson(contentJson, SimResponse.class);
				if (isSimBook(simResponse.getDocs().get(0).getId(), currentFile.getAbsolutePath())) {
					System.out.println("request: " + currentFile.getAbsolutePath() + "; response: " + simResponse.getDocs().get(0).getId());
					count++;
				}
				System.out.println("i:" + i + " count:" + count);
			}
		}
		System.out.println("similar count:" + count);
		System.out.println("sumCount" + sumCount);

	}

	private static String suffix(File currentFile) {
		if (null == currentFile)
			return "";

		String fileName = currentFile.getName();
		return fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length());
	}

	public static void searchImage(String fileName) throws IOException {
		File file = new File(fileName);
		File[] files = file.listFiles();
		Gson gson = new Gson();
		int count = 0;
		int sumCount = files.length;
		for (int i = 0; i < files.length; i++) {
			URL url = files[i].toURI().toURL();
			HttpClient httpClient = new DefaultHttpClient();
			String strUrl = "http://127.0.0.1:8983/solr/collection1/lireq?url=" + url.toString() + "&field=su_ha&rows=1";
			HttpGet get = new HttpGet(strUrl);
			HttpResponse httpResponse = httpClient.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				InputStream in = httpResponse.getEntity().getContent();
				String contentJson = getFileByte(in, (int) httpResponse.getEntity().getContentLength());
				contentJson = contentJson.trim();
				SimResponse simResponse = gson.fromJson(contentJson, SimResponse.class);
				if (isSimBook(simResponse.getDocs().get(0).getId(), files[i].getAbsolutePath())) {
					count++;
				}
				System.out.println("i:" + i + " count:" + count);
			}
		}
		System.out.println("count:" + count);
		System.out.println("sumCount" + sumCount);
	}

	public static void searchImageByFeature(String fileName) throws IOException {
		File file = new File(fileName);
		File[] files = file.listFiles();
		Gson gson = new Gson();
		int count = 0;
		int sumCount = files.length;
		for (int i = 0; i < files.length; i++) {

			BufferedImage img = ImageIO.read(files[i]);
			if (img == null) {
				continue;
			}
			SurfDocumentBuilder surfDocumentBuilder = new SurfDocumentBuilder();

			Document document = surfDocumentBuilder.createDocument(resizeQueryImage(img, 400), fileName);
			IndexableField[] fields = document.getFields(DocumentBuilder.FIELD_NAME_SURF);
			Surf surf = new Surf(resizeQueryImage(img, 400));
			List<SURFInterestPoint> surfInterestPoints = surf.getFreeOrientedInterestPoints();
			Iterator sipi = surfInterestPoints.iterator();
			List<String> fieldStrs = new ArrayList<String>();
			List<Point> points = new ArrayList<Point>();
			while (sipi.hasNext()) {
				SURFInterestPoint sip = (SURFInterestPoint) sipi.next();

				SurfFeature sf = new SurfFeature(sip);
				fieldStrs.add(Base64.encodeBase64String(sf.getByteArrayRepresentation()));
				Point point = new Point();
				point.setX(sip.getX());
				point.setY(sip.getY());
				points.add(point);
			}
			String feature = gson.toJson(fieldStrs);
			String location = gson.toJson(points);
			HttpClient httpClient = new DefaultHttpClient();

			List<NameValuePair> list = new ArrayList<NameValuePair>();
			NameValuePair pair1 = new BasicNameValuePair("feature", feature);
			NameValuePair pair2 = new BasicNameValuePair("hashes", "");
			NameValuePair pair3 = new BasicNameValuePair("field", "su_ha");
			NameValuePair pair4 = new BasicNameValuePair("rows", 2 + "");
			NameValuePair pair5 = new BasicNameValuePair("location", location);
			list.add(pair1);
			list.add(pair2);
			list.add(pair3);
			list.add(pair4);
			list.add(pair5);
			UrlEncodedFormEntity entity;
			entity = new UrlEncodedFormEntity(list, "utf-8");
			HttpPost post = new HttpPost("http://127.0.0.1:8983/solr/collection1/lireq");
			post.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				InputStream in = httpResponse.getEntity().getContent();
				String contentJson = getFileByte(in, (int) httpResponse.getEntity().getContentLength());
				contentJson = contentJson.trim();
				SimResponse simResponse = gson.fromJson(contentJson, SimResponse.class);
				if (isSimBook(simResponse.getDocs().get(0).getId(), files[i].getAbsolutePath())) {
					count++;
				}
			}
			System.out.println("i:" + i + " count:" + count);
		}
		System.out.println("count:" + count);
		System.out.println("sumCount" + sumCount);
	}

	public static boolean isSimBook(String filePath1, String filePath2) {
		if (getFileName(filePath1).equals(getFileName(filePath2))) {
			return true;
		} else {
			return false;
		}
	}

	public static String getFileName(String filePath) {
		int pos = filePath.lastIndexOf("/");
		if (pos != -1) {
			String fileName = filePath.substring(pos + 1);
			return fileName;
		} else {
			return filePath;
		}
	}

	public static String getFileByte(InputStream inputStream, int length) throws IOException {
		byte[] temp = new byte[9999999];
		byte[] tempByte = new byte[1024];
		int length1 = 0;
		int position = 0;
		while ((length1 = inputStream.read(tempByte)) != -1) {
			int end = position + length1 - 1;
			for (int i = position, j = 0; j < length1; i++, j++) {
				temp[i] = tempByte[j];
			}
			position = end + 1;
		}
		return new String(temp, "utf-8");
	}

	public static BufferedImage resizeQueryImage(BufferedImage image, int length) {

		if (length == 0) {
			return image;
		}

		int imgWidth = image.getWidth();
		int imgHeight = image.getHeight();
		int width;
		int height;

		double ratio = imgWidth < imgHeight ? 1.0 * length / imgWidth : 1.0 * length / imgHeight;
		width = (int) (imgWidth * ratio);
		height = (int) (imgHeight * ratio);

		BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, width, height, null);
		g.dispose();
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return resizedImage;
	}

}