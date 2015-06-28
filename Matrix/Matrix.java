package Matrix;

class Matrix {

	double[][] m;

	Matrix(int rows, int cols) {
		m = new double[rows][cols];
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < cols; c++) {
				set(r, c, 0);
			}
		}
	}
	
	Matrix(double[][] a_) {
        this.m = new double[a_.length][a_[0].length];
        for (int i = 0; i < rows(); i++)
        	for (int j = 0; j < cols(); j++)
            	this.m[i][j] = a_[i][j];
    }
	
	int rows() {
		return m.length;
	}

	int cols() {
		return m[0].length;
	}

	double get(int row, int col) {
		return m[row - 1][col - 1];
	}

	void set(int row, int col, double value) {
		m[row][col] = value;
	}
	
	Matrix add(Matrix b) {
		Matrix a = this;
		if (a.cols() != b.cols() && a.rows() != b.rows())
			throw new RuntimeException("Matrices does not have the same dimensions.");
		else {
			for (int i = 0; i < a.rows(); i++)
				for (int j = 0; j < a.cols(); j++)
					a.set(i, j, a.get(i + 1, j + 1) + b.get(i + 1, j + 1));
		}
		return a;
	}
	
	Matrix mult(Matrix b) {
		Matrix a = this;
		if (a.cols() != b.rows())
			throw new RuntimeException("Illegal matrix dimensions.");
        Matrix c = new Matrix(a.rows(), b.cols());
        for (int i = 0; i < c.rows(); i++)
            for (int j = 0; j < c.cols(); j++)
                for (int k = 0; k < a.rows(); k++)
                    c.m[i][j] += (a.m[i][k] * b.m[k][j]);
        return c;
	}
	
	Matrix inverse() {
		Matrix a = this;
		if (a.cols() == 3 && a.rows() == 3) {
			double a11 = a.get(1, 1);
			double a12 = a.get(1, 2);
			double a13 = a.get(1, 3);
			double a21 = a.get(2, 1);
			double a22 = a.get(2, 2);
			double a23 = a.get(2, 3);
			double a31 = a.get(3, 1);
			double a32 = a.get(3, 2);
			double a33 = a.get(3, 3);
		
			double determinant = (a11 * (a22*a33 - a32*a23)) - (a12 * (a21*a33 - a31*a23)) + (a13 * (a21*a32 - a31*a22));
			System.out.println("\nDeterminant: " + determinant);
			
			double[][] adjA = {
					{(a22*a33 - a32*a23) / determinant, -(a12*a33 - a32*a13) / determinant, (a12*a23 - a22*a13) / determinant},
					{-(a21*a33 - a31*a23) / determinant, (a11*a33 - a31*a13) / determinant, -(a11*a23 - a21*a13) / determinant},
					{(a21*a32 - a31*a22) / determinant, -(a11*a32 - a31*a12) / determinant, (a11*a22 - a21*a12) / determinant}
			};
			a = new Matrix(adjA);
			
		} else if (a.cols() == 2 && a.rows() == 2) {
			double a11 = a.get(1, 1);
			double a12 = a.get(1, 2);
			double a21 = a.get(2, 1);
			double a22 = a.get(2, 2);
			
			double determinant = a11*a22 - a21*a12;
			System.out.println("\nDeterminant: " + determinant);
			
			double[][] adjA = {
					{a22 / determinant, -(a12 / determinant)},
					{-(a21 / determinant), a11 / determinant}
			};
			a = new Matrix(adjA);
			
		} else {
			throw new RuntimeException("Matrices does not have the same or appropiate dimensions.");
		}
		return a;
	}
	
//	void print() {
//		for (int i = 1; i <= this.rows(); i++) {
//			int counter = 0;
//			for (int j = 1; j <= this.cols(); j++) {
//				if (counter == this.cols() - 1)
//					System.out.println(this.get(i, j) + " ");
//				else
//					System.out.print(this.get(i, j) + " ");
//				counter++;
//			}
//		}
//	}
//	
//	void print(Matrix m) {
//		for (int i = 1; i <= m.rows(); i++) {
//			int counter = 0;
//			for (int j = 1; j <= m.cols(); j++) {
//				if (counter == m.cols() - 1)
//					System.out.println(m.get(i, j) + " ");					
//				else
//					System.out.print(m.get(i, j) + " ");
//				counter++;
//			}
//		}
//	}
	
}
