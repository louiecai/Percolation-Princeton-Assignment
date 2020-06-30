package com.louiecai.percolation;

public class PercolationTest {

	public static void main(String[] args) {
		PercolationStats stats = new PercolationStats(1000, 200);
		System.out.println("mean\t= " + stats.mean());
		System.out.println("stddev\t= " + stats.stddev());
		System.out.println("95% confidence interval\t= " + "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");	
	}

}
