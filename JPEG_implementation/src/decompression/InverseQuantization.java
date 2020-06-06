package decompression;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import compression.Quantization;

/**
 * Implementation of the dequantization.
 * 
 * @author Merlante Simonluca
 * @author Niederstätter Ulrike
 * @author Unterrainer Stephan
 *
 */
public class InverseQuantization {

	// The matrix used for the quantization and dequantization.
		private static double[] quant_mat = { 
			16, 11, 10, 16,  24,  40,  51,  61,
			12, 12, 14, 19,  26,  58,  60,  55, 
			14, 13, 16, 24,  40,  57,  69,  56, 
			14, 17, 22, 29,  51,  87,  80,  62, 
			18, 22, 37, 56,  68, 109, 103,  77, 
			24, 35, 55, 64,  81, 104, 113,  92, 
			49, 64, 78, 87, 103, 121, 120, 101, 
			72, 92, 95, 98, 112, 100, 103,  99 
		};

	private static Mat quantisMat = fillMat();	// Quantization matrix multiplied by the quality factor.

	/**
	 * Multiplies the quantization matrix with the quality factor and stores it
	 * in a new matrix. This matrix will then be returned.
	 * @return the quantization matrix multiplied by the quality factor.
	 */
	public static Mat fillMat() {
		double quality = 101 - Quantization.getQualityFactor();
		for (int i = 0; i < quant_mat.length; i++) {
			quant_mat[i] = quant_mat[i] * quality;
		}
		Mat quantmat = new Mat(8, 8, CvType.CV_64FC1);
		quantmat.put(0, 0, quant_mat);
		return quantmat;
	}

	/**
	 * Performs the dequantization process on each block.
	 * @param mat List of blocks.
	 * @return List of blocks after dequantization process.
	 */
	public static List<Mat> inverseQuantise(List<Mat> mat) {
		List<Mat> result = new ArrayList<Mat>();

		for (int i = 0; i < mat.size(); i++) {
			Mat submat = mat.get(i);
			Mat multiplication = punctualMultiplication(submat);
			result.add(multiplication);
		}

		return result;
	}

	/**
	 * Applies the punctual multiplication on each element of a matrix
	 * @param mat The matrix.
	 * @return The matrix after the punctual multiplication.
	 */
	public static Mat punctualMultiplication(Mat mat) {
		Mat result = new Mat(8, 8, CvType.CV_64FC1);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				double[] data1 = mat.get(i, j);
				double[] data2 = quantisMat.get(i, j);
				double data3 = Math.round(data1[0] * data2[0]);
				result.put(i, j, data3);

			}
		}
		return result;
	}
}
