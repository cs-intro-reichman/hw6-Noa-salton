import java.awt.Color;

/** A library of image processing functions. */
public class Runigram {

	public static void main(String[] args) {
	    
		//// Hide / change / add to the testing code below, as needed.
		
		// Tests the reading and printing of an image:	
		Color[][] tinypic = read("tinypic.ppm");
		print(tinypic);

		// Creates an image which will be the result of various 
		// image processing operations:
		Color[][] image;

		// Tests the horizontal flipping of an image:
		image = scaled(tinypic, 3,5);
		System.out.println();
		print(image);
		
		//// Write here whatever code you need in order to test your work.
		//// You can continue using the image array.
	}

	/** Returns a 2D array of Color values, representing the image data
	 * stored in the given PPM file. */
	public static Color[][] read(String fileName) {
		In in = new In(fileName);
		// Reads the file header, ignoring the first and the third lines.
		in.readString();
		int numCols = in.readInt();
		int numRows = in.readInt();
		in.readInt();
		// Creates the image array
		Color[][] image = new Color[numRows][numCols];
		// Reads the RGB values from the file into the image array. 
		// For each pixel (i,j), reads 3 values from the file,
		// creates from the 3 colors a new Color object, and 
		// makes pixel (i,j) refer to that object.
		//// Replace the following statement with your code.
		for (int row =0; row<numRows; row++){
			for (int col=0; col<numCols; col++) {
				image[row][col] = new Color(in.readInt(),in.readInt(),in.readInt());
			}
		}
		return image;
	}

    // Prints the RGB values of a given color.
	private static void print(Color c) {
	    System.out.print("(");
		System.out.printf("%3s,", c.getRed());   // Prints the red component
		System.out.printf("%3s,", c.getGreen()); // Prints the green component
        System.out.printf("%3s",  c.getBlue());  // Prints the blue component
        System.out.print(")  ");
	}

	// Prints the pixels of the given image.
	// Each pixel is printed as a triplet of (r,g,b) values.
	// This function is used for debugging purposes.
	// For example, to check that some image processing function works correctly,
	// we can apply the function and then use this function to print the resulting image.
	private static void print(Color[][] image) {
		//// Replace this comment with your code
		//// Notice that all you have to so is print every element (i,j) of the array using the print(Color) function.
		for (int rows=0; rows<image.length; rows++){
			for (int cols=0; cols<image[0].length; cols++) {
				print(image[rows][cols]);
			} System.out.println();
		}
	}
	
	/**
	 * Returns an image which is the horizontally flipped version of the given image. 
	 */
	public static Color[][] flippedHorizontally(Color[][] image) {
		Color [][] result = new Color[image.length][image[0].length];
		int index = 0;
		for (int row=0; row<image.length; row++) {
			for (int col=image[row].length -1; col >= 0; col--) {
				result[row][index] = image[row][col];
				index++;
			} index = 0;
		}
		return result;
	}
	
	/**
	 * Returns an image which is the vertically flipped version of the given image. 
	 */
	public static Color[][] flippedVertically(Color[][] image){
		Color [][] result = new Color[image.length][image[0].length];
		int index = 0;
		for (int row=image.length-1; row >= 0; row--) {
			for (int col=0; col < image[row].length; col++) {
				result[index][col] = image[row][col];
			} index ++;
		}
		return result;
	}
	
	// Computes the luminance of the RGB values of the given pixel, using the formula 
	// lum = 0.299 * r + 0.587 * g + 0.114 * b, and returns a Color object consisting
	// the three values r = lum, g = lum, b = lum.
	private static Color luminance(Color pixel) {
		int lum = (int) (pixel.getRed()*0.299 + pixel.getGreen()*0.587 + pixel.getBlue()*0.114);
		return new Color(lum, lum, lum); }
	
	/**
	 * Returns an image which is the grayscaled version of the given image.
	 */
	public static Color[][] grayScaled(Color[][] image) {
		Color[][] result = new Color[image.length][image[0].length];
		for (int row=0; row<image.length; row++) {
			for (int col=0; col<image[row].length; col++){
			result[row][col] = luminance(image[row][col]);}
		}return result;
	}	
	
	/**
	 * Returns an image which is the scaled version of the given image. 
	 * The image is scaled (resized) to have the given width and height.
	 */
	public static Color[][] scaled(Color[][] image, int newWidth, int newHeight) {
		int oldWidth = image.length;
		int oldHeight = image[0].length;
	
		// Initialize a new image array for the scaled image
		Color[][] newImage = new Color[newWidth][newHeight];
	
		// Compute scaling factors
		double scaleX = (double) oldWidth / newWidth;
		double scaleY = (double) oldHeight / newHeight;
	
		// Map pixels from the target image to the source image
		for (int i = 0; i < newWidth; i++) {
			for (int j = 0; j < newHeight; j++) {
				// Calculate the corresponding pixel in the source image
				int srcX = (int)(i * scaleX);
				int srcY = (int)(j * scaleY);
	
				// Make sure the coordinates are within bounds
				srcX = Math.min(srcX, oldWidth - 1);
				srcY = Math.min(srcY, oldHeight - 1);
	
				// Set the pixel color in the new image
				newImage[i][j] = image[srcX][srcY];
			}
		}
	
		return newImage;
	}
	
	
	/**
	 * Computes and returns a blended color which is a linear combination of the two given
	 * colors. Each r, g, b, value v in the returned color is calculated using the formula 
	 * v = alpha * v1 + (1 - alpha) * v2, where v1 and v2 are the corresponding r, g, b
	 * values in the two input color.
	 */
	public static Color blend(Color c1, Color c2, double alpha) {
		int newRed = (int) (c1.getRed()*alpha + (1-alpha)*c2.getRed());
		int newGreen = (int) (c1.getGreen()*alpha+c2.getGreen()*(1-alpha));
		int newBlue = (int) (c1.getBlue()*alpha + c2.getBlue()*(1-alpha));
		return new Color(newRed, newGreen, newBlue);}
	
	/**
	 * Cosntructs and returns an image which is the blending of the two given images.
	 * The blended image is the linear combination of (alpha) part of the first image
	 * and (1 - alpha) part the second image.
	 * The two images must have the same dimensions.
	 */
	public static Color[][] blend(Color[][] image1, Color[][] image2, double alpha) {
		Color[][] blended = new Color[image1.length][image1[0].length];
		for (int row=0; row<image1.length; row++) {
			for (int col=0; col<image1[row].length; col++) {
				blended[row][col] = blend(image1[row][col], image2[row][col], alpha);
			}
		}
		return blended;
	}

	/**
	 * Morphs the source image into the target image, gradually, in n steps.
	 * Animates the morphing process by displaying the morphed image in each step.
	 * Before starting the process, scales the target image to the dimensions
	 * of the source image.
	 */
	public static void morph(Color[][] source, Color[][] target, int n) {
		Color [][] scaledTarget = scaled(target, target[0].length, target.length);
		if ( source.length != target.length || source[0].length != target[0].length) {
			scaledTarget = scaled(target, source[0].length, source.length);
		} 
		display(source);
		for ( int i=0; i<=n; i++){
			display(blend(source, scaledTarget, (n-i)/n));
			StdDraw.pause(500);

		}
	}
	
	/** Creates a canvas for the given image. */
	public static void setCanvas(Color[][] image) {
		StdDraw.setTitle("Runigram 2023");
		int height = image.length;
		int width = image[0].length;
		StdDraw.setCanvasSize(width, height);
		StdDraw.setXscale(0, width);
		StdDraw.setYscale(0, height);
        // Enables drawing graphics in memory and showing it on the screen only when
		// the StdDraw.show function is called.
		StdDraw.enableDoubleBuffering();
	}

	/** Displays the given image on the current canvas. */
	public static void display(Color[][] image) {
		int height = image.length;
		int width = image[0].length;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// Sets the pen color to the pixel color
				StdDraw.setPenColor( image[i][j].getRed(),
					                 image[i][j].getGreen(),
					                 image[i][j].getBlue() );
				// Draws the pixel as a filled square of size 1
				StdDraw.filledSquare(j + 0.5, height - i - 0.5, 0.5);
			}
		}
		StdDraw.show();
	}
}

