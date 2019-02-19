package com.aidenkeating.imageanalysis.util;
import java.util.HashSet;
import java.util.Set;

public class QuickUnionFind {

	private final int N; // Number of nodes in the forest
	private final int id[]; // The parent of node n (a node in a tree)
	private final int sz[]; // The number of nodes in a tree; allows weighted union to work (flatter tree)

	/**
	 * Nodes (zero based) are initialized as not connected
	 * 
	 * @param n Number of nodes
	 */
	public QuickUnionFind(int n) {
		N = n;
		id = new int[N];
		sz = new int[N];

		/*
		 * Initialize all nodes to point to themselves. At first, all nodes are
		 * disconnected
		 */
		for (int i = 0; i < N; i++) {
			id[i] = i;
			sz[i] = 1; // Each tree has one node
		}
	}

	private int root(int i) {
		while (i != id[i]) {
			id[i] = id[id[i]]; // Path compression
			i = id[i];
		}
		return i;
	}

	public void union(int p, int q) {
		int i = root(p);
		int j = root(q);
		/*
		 * Here we use weighting to keep the tree flat. The smaller tree is merged to
		 * the large one A flat tree has a faster find root time
		 */
		if (sz[i] < sz[j]) {
			id[i] = j;
			sz[j] += sz[i];
		} else {
			id[j] = i;
			sz[i] += sz[j];
		}

	}

	public boolean connected(int p, int q) {
		return root(p) == root(q);
	}

	public int getNumberOfTrees(final int noiseReduction) {
		Set<Integer> s = new HashSet<Integer>();
		for (int i = 0; i < N; i++) {
			if(sz[i] > noiseReduction) {
				s.add(id[i]);
			}
		}
		return s.size();
	}
}