package com.aidenkeating.imageanalysis.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PointTest {
	private Point p;

	@BeforeEach
	void setUp() throws Exception {
		p = new Point(0, 0);
	}

	@Test
	void testDistanceTo() {
		assertEquals(7.0710678118654755, p.distanceTo(new Point(5, 5)));
	}

	@Test
	void testTranslate() {
		final Point newPoint = Point.translate(p, 5, 10);
		assertEquals(5, newPoint.getX());
		assertEquals(10, newPoint.getY());
	}

}
