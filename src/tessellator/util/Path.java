package tessellator.util;

import java.awt.geom.Path2D;

public class Path extends Path2D.Double {
	private static final long serialVersionUID = 1L;

	public Path(Point point) {
		super();
		moveTo(point.getX(), point.getY());
	}

	public void lineTo(Point point) {
		lineTo(point.getX(), point.getY());
	}

}
