package com.aidenkeating.imageanalysis.geometry;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RectangleTest {
	private static final double EUCLID_DISTANCE_5_5 = 7.0710678118654755;

	private Rectangle r;

	@BeforeEach
	void setUp() throws Exception {
		r = new Rectangle(new Point(10, 10), 10, 10);
	}

	@Test
	void testContainsSuccess() {
		final Rectangle subRect = new Rectangle(new Point(11, 11), 2, 2);
		assertTrue(r.contains(subRect));
	}

	@Test
	void testContainsFail() {
		final Rectangle externalRect = new Rectangle(new Point(0, 0), 2, 2);
		assertFalse(r.contains(externalRect));
	}

	@Test
	void testDistanceToTop() {
		final Rectangle aboveRect = new Rectangle(new Point(10, 0), 10, 5);
		assertEquals(5, r.distanceTo(aboveRect));
	}

	@Test
	void testDistanceToBottom() {
		final Rectangle belowRect = new Rectangle(new Point(10, 25), 10, 5);
		assertEquals(5, r.distanceTo(belowRect));
	}

	@Test
	void testDistanceToLeft() {
		final Rectangle leftRect = new Rectangle(new Point(0, 10), 5, 10);
		assertEquals(5, r.distanceTo(leftRect));
	}

	@Test
	void testDistanceToRight() {
		final Rectangle rightRect = new Rectangle(new Point(25, 10), 5, 10);
		assertEquals(5, r.distanceTo(rightRect));
	}

	@Test
	void testDistanceTopLeft() {
		final Rectangle topLeftRect = new Rectangle(new Point(0, 0), 5, 5);
		assertEquals(EUCLID_DISTANCE_5_5, r.distanceTo(topLeftRect));
	}

	@Test
	void testDistanceTopRight() {
		final Rectangle topRightRect = new Rectangle(new Point(25, 0), 5, 5);
		assertEquals(EUCLID_DISTANCE_5_5, r.distanceTo(topRightRect));
	}

	@Test
	void testDistanceBottomLeft() {
		final Rectangle bottomLeftRect = new Rectangle(new Point(0, 25), 5, 5);
		assertEquals(EUCLID_DISTANCE_5_5, r.distanceTo(bottomLeftRect));
	}

	@Test
	void testDistanceBottomRight() {
		final Rectangle bottomRightRect = new Rectangle(new Point(25, 25), 5, 5);
		assertEquals(EUCLID_DISTANCE_5_5, r.distanceTo(bottomRightRect));
	}

}
