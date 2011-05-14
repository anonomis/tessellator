package tessellator.example;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import tessellator.util.Triangle;

/**
 * Holds a set of triangles that may be drawn.
 * 
 * @author nomis
 * 
 */
public class Mesh extends Component {

	private static final long serialVersionUID = 1L;

	private List<Triangle> triangles = new ArrayList<Triangle>();
	
	private final int width;
	private final int height;

	protected final BufferedImage bufferedImage;

	public Mesh(int width, int height,  List<Triangle> triangles) {
		this.width = width;
		this.height = height;

		bufferedImage =
			new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		this.triangles = triangles;
		
		update();
	}

	public void update() {
		Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();

		graphics.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
		graphics.clearRect(0, 0, width, height);

		for (Triangle triangle : triangles) {
			graphics.setColor(new Color((float) Math.random(), (float) Math
					.random(), (float) Math.random(), 0.5f));
			graphics.fill(triangle);
		}
	}

	@Override
	public void paint(Graphics g) {
		g.drawImage(bufferedImage, 0, 0, null);
		super.paint(g);
	}

}
