import java.awt.Color;
public class SeamCarver {
	private Color[][] color = null;
	//private double[][] energy = null;
	private int width = 0;
	private int height = 0;
	private boolean[][] marked;
	private Stack<Integer> reversePost;
	
	private double[][] minEnergy;
	private int[][] minMem;
	private int[] minLevelV;
	
	public SeamCarver(Picture picture) {
		Picture pic = new Picture(picture);
		width = pic.width();
		height = pic.height();
		color = new Color[width][height];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				color[i][j] = pic.get(i, j);
		pic = null;
	}
	
	public int width() {
		return this.width;
	}
	
	public int height() {
		return this.height;
	}
	public Picture picture() {
		Picture picTemp = new Picture(width, height);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				picTemp.set(i, j, color[i][j]);
		return picTemp;	
	}
	public double energy(int x, int y) {
		if (x < 0 || x >= width)
			throw new IndexOutOfBoundsException();
		if (y < 0 || y >= height)
			throw new IndexOutOfBoundsException();
		if (x == 0 || x == width - 1)
			return 195075.0;
		if (y == 0 || y == height - 1)
			return 195075.0;
		return gradientX(x, y) + gradientY(x, y);
		
	}
	
	private double gradientX(int x, int y) {
		int rL = color[x - 1][y].getRed();
        int gL = color[x - 1][y].getGreen();
        int bL = color[x - 1][y].getBlue();
		int rR = color[x + 1][y].getRed();
        int gR = color[x + 1][y].getGreen();
        int bR = color[x + 1][y].getBlue();
        return (rL - rR) * (rL - rR) + (gL - gR) * (gL - gR) + (bL - bR) * (bL - bR);
	}
	
	private double gradientY(int x, int y) {
		int rU = color[x][y - 1].getRed();
        int gU = color[x][y - 1].getGreen();
        int bU = color[x][y - 1].getBlue();
		int rD = color[x][y + 1].getRed();
        int gD = color[x][y + 1].getGreen();
        int bD = color[x][y + 1].getBlue();
        return (rU - rD) * (rU - rD) + (gU - gD) * (gU - gD) + (bU - bD) * (bU - bD);
	}
	
	public int[] findVerticalSeam() {
		minEnergy = new double[width][height];
		minMem = new int[width][height];
		minLevelV = new int[height];
		int[] minLevelTemp;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (j == 0)
					minEnergy[i][j] = 195075;
				else
					minEnergy[i][j] = Double.POSITIVE_INFINITY;
			}
		}
		getTopologicalSort();	
		for (int key : this.reversePost)
			relax(key);
		reversePost = null;
		double min = Double.POSITIVE_INFINITY;
		for (int i = 0; i < width; i++) {
			if (min > minEnergy[i][height - 1]) {
				min = minEnergy[i][height - 1];
				minLevelV[height - 1] = i;
			}
		}
		
		for (int i = height - 1; i > 0; i--) {
			int key = 0;
			int[] result;
			int row = 0;
			key = minMem[minLevelV[i]][i];	
			result = getRowCol(key);
			row = result[0];
			minLevelV[i - 1] = row;
		}
		minLevelTemp = minLevelV;
		minEnergy = null;
		minMem = null;
		minLevelV = null;
		return minLevelTemp;
	}
	
	public int[] findHorizontalSeam() {
		transpose();
		int[] minLevelH = findVerticalSeam();
		transpose();
		return minLevelH;
	}
	
	public void removeVerticalSeam(int[] seam) {
		if (seam == null)
			throw new NullPointerException();
		if (seam.length != height)
			throw new IllegalArgumentException();
		if (width <= 1 || height <= 1)
			throw new IllegalArgumentException();
		for (int i = 1; i < seam.length; i++)
			if (Math.abs(seam[i] - seam[i - 1]) > 1)
				throw new IllegalArgumentException();
		
		Color[][] colorTemp = new Color[width - 1][height];
		for (int i = 0; i < height; i++)
			for (int j = seam[i]; j < width - 1; j++)
				color[j][i] = color[j + 1][i];
		for (int i = 0; i < width - 1; i++)
			for (int j = 0; j < height; j++)
				colorTemp[i][j] = color[i][j];
		width = width - 1;
		color = colorTemp;
	}
	
	public void removeHorizontalSeam(int[] seam) {
		if (seam == null)
			throw new NullPointerException();
		if (seam.length != width)
			throw new IllegalArgumentException();
		if (width <= 1 || height <= 1)
			throw new IllegalArgumentException();
		for (int i = 1; i < seam.length; i++)
			if (Math.abs(seam[i] - seam[i - 1]) > 1)
				throw new IllegalArgumentException();
		transpose();
		removeVerticalSeam(seam);
		transpose();
	}
	 
	 
	private void transpose() {
		Color[][] colorT = new Color[height][width];
		for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
            	colorT[j][i] = color[i][j];
		color = colorT;
		int temp = width;
		width = height;
		height = temp; 
	}
	
	private void relax(int key) {
		if (key < 0 || key >= width * height)
			throw new IndexOutOfBoundsException();
		int row = 0;
		int col = 0;
		int[] result;
		result = getRowCol(key);
		row = result[0];
		col = result[1];
		if (row - 1 >= 0 && col + 1 < height) {
			if (minEnergy[row - 1][col + 1] > minEnergy[row][col] + this.energy(row - 1, col + 1)) {
				minEnergy[row - 1][col + 1] = minEnergy[row][col] + this.energy(row - 1, col + 1);
				minMem[row - 1][col + 1] = key;
			}
		}
		if (row >= 0 && col + 1 < height) {
			if (minEnergy[row][col + 1] > minEnergy[row][col] + this.energy(row, col + 1)) {
				minEnergy[row][col + 1] = minEnergy[row][col] + this.energy(row, col + 1);
				minMem[row][col + 1] = key;
			}
		}
		if (row + 1 < width && col + 1 < height) {
			if (minEnergy[row + 1][col + 1] > minEnergy[row][col] + this.energy(row + 1, col + 1)) {
				minEnergy[row + 1][col + 1] = minEnergy[row][col] + this.energy(row + 1, col + 1);
				minMem[row + 1][col + 1] = key;
			}
		}
		
	}
	
	private int[] getRowCol(int key) {
		if (key < 0 || key >= width * height)
			throw new IndexOutOfBoundsException();
		int[] result = new int[2];
		result[0] = key / height;
		result[1] = key % height;
		return result;
	}
	
	private void getTopologicalSort() {
		marked = new boolean[width][height];
		reversePost = new Stack<Integer>();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (!marked[i][j]) dfs(i, j);
		marked = null;
	}
	
	private void dfs(int row, int col) {
		int key = getKey(row, col);
		marked[row][col] = true;
		if (row - 1 >= 0 && col + 1 < height)
			if (!marked[row - 1][col + 1])
				dfs(row - 1, col + 1);
		if (row >= 0 && row < width && col + 1 < height)
			if (!marked[row][col + 1])
				dfs(row, col + 1);
		if (row + 1 < width && col + 1 < height)
			if (!marked[row + 1][col + 1])
				dfs(row + 1, col + 1);
		reversePost.push(key);
	}
	
	private int getKey(int row, int col) {
		if (row < 0 || row >= width)
			throw new IndexOutOfBoundsException();
		if (col < 0 || col >= height)
			throw new IndexOutOfBoundsException();
		return row * height + col;
	}
	
	//private Iterable<Integer> reversePost() {
		//return reversePost;
	//}
	
	
	
	
	
	
	
    public static void main(String[] args)
    {
        Picture inputImg = new Picture(args[0]);
        System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        inputImg.show();        
        SeamCarver sc = new SeamCarver(inputImg);
        
        System.out.printf("Displaying energy calculated for each pixel.\n");
        SCUtility.showEnergy(sc);

    }
}

    
		