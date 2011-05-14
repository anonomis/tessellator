package tessellator.util;

import java.awt.geom.Point2D;

public class Point extends Point2D.Double {
	private static final long serialVersionUID = 1L;

	public Point(double x, double y) {
		super(x, y);
	}

	public Point(java.awt.geom.Point2D point) {
		super(point.getX(), point.getY());
	}

	public Point(double[] coords) {
		super(coords[0], coords[1]);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return 0;
	}

	public float getXf() {
		return (float) x;
	}

	public float getYf() {
		return (float) y;
	}

	public float getZf() {
		return (float) 0;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}


}
