package com.louiecai.percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class PercolationStats {

	private double results[];

	// perform independent trials on an n-by-n grid
	public PercolationStats(int n, int trials) {

		if (n > 2147483646 || trials > 2147483646) {
			throw new IllegalArgumentException();
		} else {
			results = new double[trials];

			for (int i = 0; i < trials; i++) {
				Percolation testPercolation = new Percolation(n);
				while (!testPercolation.percolates()) {
					int rowRand = StdRandom.uniform(1, n + 1);
					int colRand = StdRandom.uniform(1, n + 1);
					//System.out.println(rowRand + ", " + colRand);
					testPercolation.open(rowRand, colRand);
				}
				results[i] = ((double) testPercolation.numberOfOpenSites() / (double) (n * n));

				 System.out.println("results[" + i + "] = " + results[i]);
			}
		}
	}

	// sample mean of percolation threshold
	public double mean() {
		return StdStats.mean(results);
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return StdStats.stddev(results);
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return StdStats.min(results);
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return StdStats.max(results);
	}

	// test client (see below)
	public static void main(String[] args) {

		if (args.length != 2) {
			throw new IllegalArgumentException();
		} else {
			int n = Integer.parseInt(args[0]);
			int trials = Integer.parseInt(args[1]);

			PercolationStats stats = new PercolationStats(n, trials);
			System.out.println("mean\t= " + stats.mean());
			System.out.println("stddev\t= " + stats.stddev());
			System.out.println(
					"95% confidence interval\t= " + "[" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
		}
	}
}
