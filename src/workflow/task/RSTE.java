package workflow.task;

import java.util.ArrayList;

import Jama.Matrix;

public class RSTE
{
	private Matrix oriMatrix;
	private Matrix sMatrix;
	
	public RSTE(double[][] array1, double[][] array2)
	{
		this.oriMatrix = new Matrix(array1);
		this.sMatrix = new Matrix(array2);
	}
	
	public double[][] getFullRating()
	{
		Matrix fullMatrix = this.getFullMatrix();
		return fullMatrix.getArrayCopy();
	}
	
	private Matrix getFullMatrix()
	{
		double[] para = {0.4, 0.1, 10, 0.01, 1000};  //alpha, lambda, rank, step, maxIte
		
		Matrix sNormMatrix = MyMatrix.getRowNormalizedMatrix(this.sMatrix);
		
		int m = this.oriMatrix.getRowDimension();
		
		double alpha = para[0];
		
		Matrix aMatrix = Matrix.identity(m, m).times(alpha).plus(sNormMatrix.times(1-alpha));
		
		ArrayList<Matrix> pqMatrix = this.updatePQ(this.oriMatrix, aMatrix, para);
		Matrix pMatrix = pqMatrix.get(0);
		Matrix qMatrix = pqMatrix.get(1);
		
		Matrix fullMatrix = aMatrix.times(pMatrix.transpose()).times(qMatrix);
		return fullMatrix;
		
	}
	
	private ArrayList<Matrix> updatePQ(Matrix rMatrix, Matrix aMatrix, double[] para)
	{
		ArrayList<Matrix> results = new ArrayList<Matrix>(2);
		
		int m = rMatrix.getRowDimension();
		int n = rMatrix.getColumnDimension();
		int rank = (int) para[2];
		int maxIte = (int) para[4];
		
		Matrix pMatrix = Matrix.random(rank, m);
		Matrix qMatrix = Matrix.random(rank, n);
		
		Matrix irMatrix = MyMatrix.getIndicatorMatrix(rMatrix);
		
		int ite = 0;
		while (ite < maxIte)
		{
			Matrix apqMatrix = aMatrix.times(pMatrix.transpose()).times(qMatrix);
			Matrix tMatrix = irMatrix.arrayTimes(apqMatrix.minus(rMatrix));
			
			
			pMatrix = this.updateP(tMatrix, aMatrix, pMatrix, qMatrix, para);
			qMatrix = this.updateP(aMatrix, tMatrix, qMatrix, pMatrix, para);
			
			ite++;
		}
		
		results.add(pMatrix);
		results.add(qMatrix);
		
		return results;
	}
	
	private Matrix updateP(Matrix tMatrix, Matrix aMatrix, Matrix p0Matrix, Matrix qMatrix, double[] para)
	{
		double lambda = para[1];
		double step = para[3];
		
		Matrix gPMatrix = qMatrix.times(tMatrix.transpose()).times(aMatrix);
		gPMatrix = gPMatrix.plus(p0Matrix.times(lambda));
		
		Matrix p1Matrix = p0Matrix.minus(gPMatrix.times(step));
		
		return p1Matrix;
	}

}
