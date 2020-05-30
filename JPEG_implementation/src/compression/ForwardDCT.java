package compression;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import utils.Utils;

public class ForwardDCT {
	 
	private static int matCols = 0;
	private static int matRows = 0;

	/**
	 * Reads an image from the given path and converts it to a matrix.
	 * Splits the obtained matrix into blocks of 8x8.
	 * Performs the forward DCT operation on each block.
	 * Returns a list of blocks of 8x8 on which have been executed the forward DCT operation.
	 * @param path The path of the image.
	 * @return The image as list of 8x8 matrices modified by forward DCT or null if the image could'nt be read.
	 */
	public static ArrayList<Mat> divideBlocksDCT(String path) {
		Mat mat = Utils.imgToMat(path);
		matCols = mat.cols();
		matRows = mat.rows();
		if (mat != null) {

			ArrayList<Mat> blocks = new ArrayList<Mat>();
			for (int rowStart = 0, rowEnd = 8; rowEnd <= mat.width(); rowEnd += 8, rowStart += 8) {
				for (int colStart = 0, colEnd = 8; colEnd <= mat.height(); colEnd += 8, colStart += 8) {
					// Creates a 8x8 matrix/block.
					Mat sub = mat.submat(rowStart, rowEnd, colStart, colEnd);
					// Performs the forward DCT on a matrix/block of 8x8.
					Mat dct_subMat = forwardDCT(sub);
					// Adds the matrix/block to the list of blocks after the forward DCT.
					blocks.add(dct_subMat);
				}
			}
			return blocks; 
		}
		return null;
	}

	public static int getMatCols() {
		return matCols;
	}

	public static void setMatCols(int matCols) {
		ForwardDCT.matCols = matCols;
	}

	public static int getMatRows() {
		return matRows;
	}

	public static void setMatRows(int matRows) {
		ForwardDCT.matRows = matRows;
	}

	/**
	 * Executes the forward DCT operation on a matrix.
	 * @param mat The matrix on which the forward DCT will be performed.
	 * @return The matrix after the forward DCT.
	 */
	public static Mat forwardDCT(Mat mat) {

		// Convert the values of the input matrix into floating points and store them into a new matrix.
		Mat m = new Mat();
		mat.convertTo(m, CvType.CV_64FC1); 
		
		// Create an output matrix with same size and same type of the converted input matrix.
		Mat dct_mat = new Mat(m.rows(), m.cols(), m.type());

		// Convert the values of dct_mat into floating points and store them into the matrix dct_convert.
		Mat dct_convert = new Mat();
		dct_mat.convertTo(dct_convert, CvType.CV_64FC1);// convert also to floating points

		// Perform the forward dct on matrix m an store it in the matrix dct_converted.
		Core.dct(m, dct_convert);

		return dct_convert;
	}
	
	// forwardDCT looks still a bit strange to me due to all those conversions.
}
