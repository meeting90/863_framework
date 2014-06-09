package workflow.task;

import java.util.ArrayList;

import Jama.Matrix;

public class NmfTrust 
{
	private Matrix oriMatrix;
	
	public NmfTrust(double[][] array)  
	{
		this.oriMatrix = new Matrix(array);
	}
	
	public double[][] getFullTrust()
	{
		Matrix fullMatrix = this.getFullMatrix();
		return fullMatrix.getArrayCopy();
	}
	
	private Matrix getFullMatrix()
	{
		double[] para = {0.1, 10, 500};   //lambda, rank, maxIte
				
		Matrix rNormMatrix = MyMatrix.getRowNormalizedMatrix(this.oriMatrix);
		Matrix rSumMatrix = MyMatrix.getRowSumMatrix(this.oriMatrix);
		
		ArrayList<Matrix> pqMatrix = this.updatePQ(rNormMatrix, para);
		Matrix pMatrix = pqMatrix.get(0);
		Matrix qMatrix = pqMatrix.get(1);
		
		Matrix fullMatrix = pMatrix.times(qMatrix.transpose());
		fullMatrix = fullMatrix.arrayTimes(rSumMatrix);
		return fullMatrix;
	}
	
	private ArrayList<Matrix> updatePQ(Matrix rMatrix, double[] para)
	{
		ArrayList<Matrix> results = new ArrayList<Matrix>(2);
		
		int m = rMatrix.getRowDimension();
		int n = rMatrix.getColumnDimension();
		int rank = (int) para[1];
		int maxIte = (int) para[2];
		
		Matrix pMatrix = Matrix.random(m, rank);
		Matrix qMatrix = Matrix.random(n, rank);
		
		int ite = 0;
		while (ite < maxIte)
		{
			pMatrix = this.updateP(rMatrix, pMatrix, qMatrix, para);
			qMatrix = this.updateP(rMatrix.transpose(), qMatrix, pMatrix, para);
			
			ite++;
		}
		
		results.add(pMatrix);
		results.add(qMatrix);
		
		return results;
	}
	
	private Matrix updateP(Matrix rMatrix, Matrix p0Matrix, Matrix qMatrix, double[] para)
	{
		double lambda = para[0];
		
		Matrix irMatrix = MyMatrix.getIndicatorMatrix(rMatrix);
		
		Matrix upMatrix = irMatrix.arrayTimes(rMatrix).times(qMatrix);
		Matrix downMatrix = irMatrix.arrayTimes(p0Matrix.times(qMatrix.transpose())).times(qMatrix);
		downMatrix = downMatrix.plus(p0Matrix.times(lambda));
		
		Matrix p1Matrix =p0Matrix.arrayTimes(upMatrix.arrayRightDivide(downMatrix));
		p1Matrix = MyMatrix.modifyMatrix(p1Matrix);
		
		return p1Matrix;
	}

}
