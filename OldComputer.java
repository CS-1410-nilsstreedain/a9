package a9;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class OldComputer extends Plant {

	private static final int HEALTH = 200;
	private static final int COOLDOWN = 10;
	private static final int ATTACK_DAMAGE = 2;
	private static final BufferedImage IMAGE;

	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/a9/game-icons/computer0.png"));
		} catch (IOException e) {
			System.out.println("A file was not found");
			System.exit(0);
		}
		IMAGE = image;
	}

	/***
	 * Creates a plant of Old Computer type with specified plant attributes at a
	 * given position.
	 * 
	 * @param position
	 */
	public OldComputer(Point2D.Double position) {
		super(position, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN,
				ATTACK_DAMAGE);
	}
}
