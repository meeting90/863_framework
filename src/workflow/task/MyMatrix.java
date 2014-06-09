package workflow.task;

import Jama.Matrix;

public class MyMatrix 
{
	public static Matrix getIndicatorMatrix(Matrix rMatrix)
	{
		int m = rMatrix.getRowDimension();
		int n = rMatrix.getColumnDimension();
		double[][] array = rMatrix.getArrayCopy();
		
		for (int index1=0; index1<m; index1++)
			for (int index2=0; index2<n; index2++)
			{
				if (array[index1][index2] > 0)
					array[index1][index2] = 1;
			}
		return new Matrix(array);
	}
	
	public static Matrix modifyMatrix(Matrix rMatrix)
	{
		int m = rMatrix.getRowDimension();
		int n = rMatrix.getColumnDimension();
		double[][] array = rMatrix.getArrayCopy();
		
		for (int index1=0; index1<m; index1++)
			for (int index2=0; index2<n; index2++)
			{
				if (Double.isNaN(array[index1][index2]))
					array[index1][index2] = 0;
			}
		return new Matrix(array);
	}
	
	public static Matrix getRowNormalizedMatrix(Matrix rMatrix)
	{
		int m = rMatrix.getRowDimension();
		int n = rMatrix.getColumnDimension();
		double[][] array = rMatrix.getArrayCopy();
				
		for (int index1=0; index1<m; index1++)
		{
			double sum = 0;
			for (int index2=0; index2<n; index2++)
				sum += array[index1][index2];
			if (sum == 0)
				continue;
			for (int index2=0; index2<n; index2++)
				array[index1][index2] /= sum;
		}
		return new Matrix(array);
	}
	
	public static Matrix getRowSumMatrix(Matrix rMatrix)
	{
		int m = rMatrix.getRowDimension();
		int n = rMatrix.getColumnDimension();
		double[][] array = rMatrix.getArrayCopy();
				
		for (int index1=0; index1<m; index1++)
		{
			double sum = 0;
			for (int index2=0; index2<n; index2++)
				sum += array[index1][index2];
			if (sum == 0)
				continue;
			for (int index2=0; index2<n; index2++)
				array[index1][index2] = sum;
		}
		return new Matrix(array);
	}
}
