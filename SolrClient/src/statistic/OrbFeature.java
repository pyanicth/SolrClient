package statistic;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.utils.MetricsUtils;

import org.opencv.core.Mat;

public class OrbFeature implements LireFeature {
	Mat points;
	byte[] bytes;
	double[] descriptor;

	public OrbFeature(Mat points) {
		this.points = points;
		bytes = pointsToByteArray();
		this.descriptor = toDoubleArray(bytes);
	}

	public OrbFeature() {
		this.points = null;
	}

	@Override
	public void extract(BufferedImage arg0) {
	}

	@Override
	public byte[] getByteArrayRepresentation() {
		return bytes;
	}

	private byte[] pointsToByteArray(){
		byte[] data = null;
		data = new byte[points.cols()];
		points.get(0,0,data);
		return data;
		
	}

	@Override
	public float getDistance(LireFeature lireFeature) {
		return !(lireFeature instanceof OrbFeature) ? -1.0F : (float) MetricsUtils.distL2(this.descriptor, ((OrbFeature) lireFeature).descriptor);
	}

	@Override
	public double[] getDoubleHistogram() {
		return this.descriptor;
	}

	@Override
	public String getFeatureName() {
		return "ORB";
	}

	@Override
	public String getFieldName() {
		return "featureORB";
	}

	@Override
	public String getStringRepresentation() {
		throw new UnsupportedOperationException("No implemented!");
	}

	private double[] toDoubleArray(byte[] data) {
		return toDoubleArray(data, 0, data.length);
	}

	private double[] toDoubleArray(byte[] data, int offset, int length) {
		double[] result = new double[length];
		for (int i = offset; i < result.length; i++) {
			result[i] = data[i];
		}
		return result;
	}

	public void setByteArrayRepresentation(byte[] in) {
		setByteArrayRepresentation(in, 0, in.length);
	}

	public void setByteArrayRepresentation(byte[] in, int offset, int length) {
		this.descriptor = toDoubleArray(in,offset,length);
		bytes = in;
	}

	@Override
	public void setStringRepresentation(String arg0) {
		double[] result = null;
		LinkedList<Float> temp = new LinkedList<Float>();
		StringTokenizer st = new StringTokenizer(arg0);
		st.nextToken();
		st.nextToken();
		st.nextToken();
		while (st.hasMoreTokens()) {
			temp.add(Float.parseFloat(st.nextToken()));
			result = new double[temp.size()];
			int i = 0;
			for (Iterator<Float> iterator = temp.iterator(); iterator.hasNext();) {
				Float next = (Float) iterator.next();
				result[i] = next.floatValue();
				i++;
			}
			this.descriptor = result;

		}
	}

}
