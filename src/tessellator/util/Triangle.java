package tessellator.util;

import java.awt.geom.Path2D;

public class Triangle extends Path2D.Double {
	private static final long serialVersionUID = 1;

	public final Point p1;
	public final Point p2;
	public final Point p3;

	public Triangle(Point p1, Point p2, Point p3) {
		super();
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;

		this.moveTo(p1.x, p1.y);
		this.lineTo(p2.x, p2.y);
		this.lineTo(p3.x, p3.y);
	}

	@Override
	public String toString() {
		return "Triangle [p1=" + p1 + ", p2=" + p2 + ", p3=" + p3 + "]";
	}

}
