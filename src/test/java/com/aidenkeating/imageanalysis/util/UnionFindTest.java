package com.aidenkeating.imageanalysis.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnionFindTest {
	private static final int DEFAULT_UF_SIZE = 10;

	private UnionFind uf;

	@BeforeEach
	void setUp() {
		this.uf = new UnionFind(DEFAULT_UF_SIZE);
	}

	@Test
	void testConnected() {
		assertTrue(!uf.connected(0, 1));
		uf.union(0, 1);
		assertTrue(uf.connected(0, 1));
	}

	@Test
	void testGetNumberOfTrees() {
		assertEquals(uf.getNumberOfTrees(0), DEFAULT_UF_SIZE);
		uf.union(0, 1);
		assertEquals(uf.getNumberOfTrees(0), 9);
		assertEquals(uf.getNumberOfTrees(1), 1);
	}

	@Test
	void testGetRoots() {
		assertEquals(uf.getRoots(0).size(), DEFAULT_UF_SIZE);
		uf.union(0, 1);
		assertEquals(uf.getRoots(1).size(), 1);
	}

	@Test
	void testGetElementsOfTree() {
		uf.union(0, 1);
		assertEquals(uf.getElementsOfTree(1).size(), 2);
	}
}
