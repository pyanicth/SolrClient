package statistic;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import net.semanticmetadata.lire.AbstractDocumentBuilder;

import org.apache.lucene.document.Field;

/**
 * Created by zuo on 15/4/9.
 */
public class MySurfDocumentBuilder extends AbstractDocumentBuilder {
	
	
    public static BufferedImage resizeQueryImage(BufferedImage image, int length) {

        if (length == 0) {
            return image;
        }

        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();
        int width;
        int height;

        double ratio = imgWidth > imgHeight ? 1.0 * length / imgWidth : 1.0 * length / imgHeight;
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

	@Override
	public Field[] createDescriptorFields(BufferedImage paramBufferedImage) {
		// TODO Auto-generated method stub
		return null;
	}
}
