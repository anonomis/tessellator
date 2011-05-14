package tessellator;

import java.awt.Shape;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;
import javax.media.opengl.glu.GLUtessellatorCallbackAdapter;

import tessellator.util.Point;
import tessellator.util.Triangle;

/**
 * Class for transforming common java awt/swing geometric forms to sets of
 * triangles. Utilize openGL to achieve this with perfection and speed.
 * 
 * @author nomis
 * 
 */
public class Tessellator {

	private final GLU glu = new GLU();
	private final GLUtessellator tobj = glu.gluNewTess();
	private final TessellationCallback tessCallback = new TessellationCallback();

	private TesselationException err;

	/*
	 * Either you kindly wait for your triangles.
	 */

	private boolean done = false;

	/**
	 * Tessellate a shape, transform it to a set of triangles. This method locks
	 * until rendering is completed.
	 * 
	 * @param shape
	 *            The shape to tessellate.
	 * @param hole
	 *            Shape of the hole in the tessellation (optional).
	 * @return A list of triangles that constitutes the shape.
	 * @throws TesselationException
	 *             Throws {@link TesselationException} if the tessellation was
	 *             unsuccessful, most commonly due to ambiguous shapes
	 */
	public List<Triangle> getTriangles(Shape shape, Shape hole)
			throws TesselationException {

		makeTriangles(shape, hole);

		try {
			while (!done) {
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		if (err == null) {
			return tessCallback.triangles;
		} else
			throw err;
	}

	/*
	 * Or you may implement a listener interface and handle an asynchronous
	 * call.
	 */

	private TessellatorListener listener;

	/**
	 * Tessellates a shape that is, tiling of the shape in a pattern of
	 * triangles that fills the shape with no overlaps and no gaps. Returns a
	 * set of triangles to the given {@link TessellatorListener} whenever done.
	 * 
	 * @param shape
	 *            The shape to tessellate.
	 * @param hole
	 *            Shape of the hole in the tessellation (optional).
	 * @param listener
	 *            Instance of {@link TessellatorListener} that will be invoked
	 *            whenever tessellation is done or fails.
	 */
	public void getTriangles(Shape shape, Shape hole,
			TessellatorListener listener) {
		this.listener = listener;
		makeTriangles(shape, hole);
	}

	public interface TessellatorListener {
		public void onTesselationDone(List<Triangle> triangles);

		public void onTesselationError(TesselationException err);
	}

	public class TesselationException extends Exception {
		private static final long serialVersionUID = 1L;

		public TesselationException(String message) {
			super(message);
		}
	}

	/**
	 * This method register an instance of {@link TessellationCallback} which
	 * will produce triangles given an outer- and an optional inner- (hollow)
	 * {@link Shape}. {@link TessellationCallback} will report status to callers
	 * whenever done.
	 * 
	 * @param shape
	 *            The to shape to tessellate. This shape can not be a complex
	 *            type, neither contain holes. In such case a
	 *            {@link TesselationException} will be thrown.
	 * @param hole
	 *            A subsection of the shape that will not be included in the
	 *            resulting triangles area. The hole's contour may not exceed
	 *            the contour of the shape or a {@link TesselationException}
	 *            will be thrown.
	 * 
	 */
	private void makeTriangles(Shape shape, Shape hole) {
		List<double[]> shapePath = arrayFromPath(shape);
		List<double[]> holePath = arrayFromPath(hole);

		glu.gluTessCallback(tobj, GLU.GLU_TESS_BEGIN, tessCallback);
		glu.gluTessCallback(tobj, GLU.GLU_TESS_VERTEX, tessCallback);
		glu.gluTessCallback(tobj, GLU.GLU_TESS_END, tessCallback);
		glu.gluTessCallback(tobj, GLU.GLU_TESS_COMBINE, tessCallback);
		glu.gluTessCallback(tobj, GLU.GLU_TESS_ERROR, tessCallback);

		glu.gluTessBeginPolygon(tobj, null);

		glu.gluTessBeginContour(tobj);

		for (double[] point : shapePath) {
			glu.gluTessVertex(tobj, point, 0, point);
		}

		glu.gluTessEndContour(tobj);

		glu.gluTessBeginContour(tobj);

		for (double[] point : holePath) {
			glu.gluTessVertex(tobj, point, 0, point);
		}

		glu.gluTessEndContour(tobj);

		glu.gluTessEndPolygon(tobj);

		glu.gluDeleteTess(tobj);

	}

	/**
	 * 
	 * Make a list of points from a {@link PathIterator}. Assumes that first
	 * element is of type SEG_MOVETO and remaining elements of SEG_LINETO. Other
	 * structures will be ignored.
	 * 
	 * @param shape
	 *            The {@link Shape} from which to iterate the path.
	 * @return A {@link List} of double[]'s
	 */
	private List<double[]> arrayFromPath(Shape shape) {
		ArrayList<double[]> arrayList = new ArrayList<double[]>();
		if (shape != null) {
			PathIterator pathIterator = shape.getPathIterator(null);

			while (!pathIterator.isDone()) {

				double[] coords = new double[3];
				pathIterator.currentSegment(coords);
				pathIterator.next();

				arrayList.add(coords);
			}
		}
		return arrayList;
	}

	/**
	 * Implementation of {@link GLUtessellatorCallback} thats responsible for
	 * creating {@link Triangle}s from {@link GLU}'s tessellation callbacks and
	 * reporting back to {@link TessellatorListener}s whenever done or an error
	 * occurs.
	 * 
	 * @author nomis
	 * 
	 */
	class TessellationCallback extends GLUtessellatorCallbackAdapter {

		protected List<Triangle> triangles = new ArrayList<Triangle>();

		private Point p1, p2, p3;

		private int geometricPrimitiveType;

		public void begin(int type) {
			this.geometricPrimitiveType = type;
		}

		public void end() {
			sendSuccess();
		}

		public void vertex(Object vertexData) {
			Point thisPoint = new Point((double[]) vertexData);
			switch (geometricPrimitiveType) {
			case GL.GL_TRIANGLE_FAN:

				if (p1 == null) {
					p1 = thisPoint;
				} else if (p2 == null) {
					p2 = thisPoint;
				} else {
					triangles.add(new Triangle(p1, p2, thisPoint));
					p2 = thisPoint;
				}
				break;

			case GL.GL_TRIANGLE_STRIP:
				p1 = p2;
				p2 = p3;
				p3 = thisPoint;

				if (p1 != null) {
					triangles.add(new Triangle(p1, p2, p3));
				}
				break;

			default:
				sendError("Geometric Primitive Type #"
						+ geometricPrimitiveType
						+ " not implemented. See "
						+ "http://www.glprogramming.com/red/chapter02.html#name2 "
						+ "for more.");
			}

		}

		public void combine(double[] coords, Object[] data, float[] weight,
				Object[] outData) {
			sendError("Self-intersecting polygons not supported");
		}

		public void error(int errnum) {
			sendError("Opengl error: " + glu.gluErrorString(errnum));
		}
	}

	protected void sendSuccess() {
		done = true;
		err = null;
		if (listener != null) {
			listener.onTesselationDone(tessCallback.triangles);
		}
	}

	protected void sendError(String message) {
		done = true;
		err = new TesselationException(message);
		if (listener != null) {
			listener.onTesselationError(err);
		}

	}
}