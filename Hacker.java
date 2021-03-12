package a9;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Hacker extends Zombie {

	private static final int HEALTH = 100;
	private static final int COOLDOWN = 50;
	private static final int SPEED = -1;
	private static final int ATTACK_DAMAGE = 10;
	private static final BufferedImage IMAGE;

	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/a9/game-icons/hackerIcon.png"));
		} catch (IOException e) {
			System.out.println("A file was not found");
			System.exit(0);
		}
		IMAGE = image;
	}

	/***
	 * Creates a Zombie of Hacker type with specified plant attributes at a given
	 * position.
	 * 
	 * @param position
	 */
	public Hacker(Point2D.Double position) {
		super(position, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN, SPEED,
				ATTACK_DAMAGE);
	}
}
