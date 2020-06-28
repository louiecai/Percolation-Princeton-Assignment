package com.louiecai.percolation;

/**
 * This is a class designed for an n-by-n percolation grid that implements Princeton University's {@link WeightedQuickUnionUF}.
 * More details on  <a href="https://coursera.cs.princeton.edu/algs4/assignments/percolation/specification.php">
 * 
 * @author Louie Cai
 */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private int n;
	private int UFLength;

	private int length;
	private int width;

	private WeightedQuickUnionUF trueUFGrid;
	private boolean[][] booleanUFGrid;

	private int upperRoot;
	private int lowerRoot;

	private int numberOfOpenSites;

	/**
	 * Creates {@code n}-by-{@code n} square grid and initialize with all sites
	 * closed
	 * 
	 * @param n the side length of the percolation grid
	 * @throws IllegalArgumentException if {@code 1 < n > 2147483646}
	 */
	public Percolation(int n) {

		if (n < 1 || n >= 2147483646) {
			throw new IllegalArgumentException();

		} else if (n == 1) {
			this.n = 1;
			UFLength = 1;
			upperRoot = 1;
			lowerRoot = 1;
			numberOfOpenSites = 0;

			trueUFGrid = new WeightedQuickUnionUF(3);
			// method that initializes trueUFGrid

			booleanUFGrid = new boolean[1][1];
			// method that initializes booleanUFGrid
			booleanUFGrid[0][0] = false;

		} else if (n == 2) {
			this.n = 2;
			UFLength = 6;
			upperRoot = 4;
			lowerRoot = 5;
			numberOfOpenSites = 0;

			trueUFGrid = new WeightedQuickUnionUF(UFLength);
			// method that initializes trueUFGrid
			trueUFGrid.union(0, upperRoot);
			trueUFGrid.union(1, upperRoot);
			trueUFGrid.union(2, lowerRoot);
			trueUFGrid.union(3, lowerRoot);

			booleanUFGrid = new boolean[2][2];
			// method that initializes booleanUFGrid
			booleanUFGrid[0][0] = false;
			booleanUFGrid[0][1] = false;
			booleanUFGrid[1][1] = false;
			booleanUFGrid[1][0] = false;

		} else {
			this.n = n;
			UFLength = n * n + 2;
			upperRoot = UFLength - 2;
			lowerRoot = UFLength - 1;
			numberOfOpenSites = 0;

			trueUFGrid = new WeightedQuickUnionUF(UFLength);
			// method that initializes trueUFGrid
			for (int row = 0; row < n; row++) {
				trueUFGrid.union(row, upperRoot);
			}

			for (int row = gridIntoArray(n - 1, 0); row <= gridIntoArray(n - 1, n - 1); row++) {
				trueUFGrid.union(row, lowerRoot);
			}

			booleanUFGrid = new boolean[n][n];
			// method that initializes booleanUFGrid
			for (int row = 0; row < n; row++) {
				for (int col = 0; col < n; col++) {
					booleanUFGrid[row][col] = false;
				}
			}
		}
	}

	/**
	 * Creates {@code length}-by-{@code width} rectangular grid and initialize with
	 * all sites closed
	 * 
	 * @param length the length of the grid
	 * @param width  the width of the grid
	 * @throws IllegalArgumentException if {@code 1 < length/width > 2147483646}
	 * @deprecated Unfinished
	 */
	public Percolation(int length, int width) {
		
	}

	/**
	 * Opens a specific site and connects it to the open sites near it (if there are
	 * any)
	 * 
	 * @param row the row where the site is located
	 * @param col the column where the site is located
	 * @throws IllegalArgumentException if the coordinates entered are outside of
	 *                                  the grid
	 */
	public void open(int row, int col) {
		int trueRow = row - 1;
		int trueCol = col - 1;

		if (!isBoundaryValid(trueRow, trueCol)) {
			throw new IllegalArgumentException();
		} else {
			privateOpen(trueRow, trueCol);
		}
	}

	/**
	 * Returns whether a site is open at a specific location
	 * 
	 * @param row the row where the site is located
	 * @param col the column where the site is located
	 * @return {@code true} if the site is open; {@code false} if the site is not
	 *         open
	 */
	public boolean isOpen(int row, int col) {
		int trueRow = row - 1;
		int trueCol = col - 1;
		if (!isBoundaryValid(trueRow, trueCol)) {
			throw new IllegalArgumentException();
		} else {
			return booleanUFGrid[trueRow][trueCol];
		}
	}

	/**
	 * Return whether a site is full. A site if full when it is both open and
	 * connected to the top row through a chain of open sites
	 * 
	 * @param row the row where the site is located
	 * @param col the column where the site is located
	 * @return {@code true} if the site is full; {@code false} if the site is not
	 *         full
	 */
	public boolean isFull(int row, int col) {
		int trueRow = row - 1;
		int trueCol = col - 1;
		if (!isBoundaryValid(trueRow, trueCol)) {
			throw new IllegalArgumentException();
		} else {
			if (isOpen(row, col) && trueUFGrid.find(gridIntoArray(trueRow, trueCol)) == trueUFGrid.find(upperRoot)) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * @return the number of open site in the percolation system
	 */
	public int numberOfOpenSites() {
		return numberOfOpenSites;
	}

	/**
	 * Checks whether the system percolates. A system percolations if there is a
	 * path from the top row to the bottom row
	 * 
	 * @return {@code true} if the system {@code percolates}; {@code false} if the
	 *         system does not percolate
	 */
	public boolean percolates() {
		if (n == 1) {
			return booleanUFGrid[0][0];
		} else {
			return trueUFGrid.find(lowerRoot) == trueUFGrid.find(upperRoot);
		}
	}

	private int gridIntoArray(int trueRow, int trueCol) {
		return trueRow * n + trueCol;
	}

	private void privateOpen(int row, int col) {
		if (booleanUFGrid[row][col] == false) {

			booleanUFGrid[row][col] = true;

			if (isBoundaryValid(row + 1, col) && booleanUFGrid[row + 1][col]) {
				trueUFGrid.union(gridIntoArray(row, col), gridIntoArray(row + 1, col));
				// System.out.println("Connected to the below");
			}
			if (isBoundaryValid(row - 1, col) && booleanUFGrid[row - 1][col]) {
				trueUFGrid.union(gridIntoArray(row, col), gridIntoArray(row - 1, col));
				// System.out.println("Connected to the above");
			}
			if (isBoundaryValid(row, col + 1) && booleanUFGrid[row][col + 1]) {
				trueUFGrid.union(gridIntoArray(row, col), gridIntoArray(row, col + 1));
				// System.out.println("Connected to the right");
			}
			if (isBoundaryValid(row, col - 1) && booleanUFGrid[row][col - 1]) {
				trueUFGrid.union(gridIntoArray(row, col), gridIntoArray(row, col - 1));
				// System.out.println("Connected to the left");
			}
			numberOfOpenSites++;
		}
	}

	private boolean isBoundaryValid(int trueRow, int trueCol) {
		if (trueRow < 0 || trueCol < 0 || trueRow >= n || trueCol >= n) {
			return false;
		} else {
			return true;
		}
	}
}