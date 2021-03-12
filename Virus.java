package a9;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Virus extends Zombie {

	private static final int HEALTH = 10;
	private static final int COOLDOWN = 200;
	private static final int SPEED = -1;
	private static final int ATTACK_DAMAGE = 10;
	private static final BufferedImage IMAGE;
	static ArrayList<Actor> virusesToAdd = new ArrayList<>();
	// List of duplicate viruses to be places by the game loop.

	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/a9/game-icons/virus.png"));
		} catch (IOException e) {
			System.out.println("A file was not found");
			System.exit(0);
		}
		IMAGE = image;
	}

	/***
	 * Creates a Zombie of Virus type with specified plant attributes at a given
	 * position.
	 * 
	 * @param position
	 */
	public Virus(Point2D.Double position) {
		super(position, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN, SPEED,
				ATTACK_DAMAGE);
	}

	/***
	 * When the virus attacks a plant, it will create a duplicate of itself at a
	 * position 50 further down the line. To do this, it adds said duplicate to a
	 * list that the game loop will check and add to actors. This will also reset
	 * the attack cooldown.
	 * 
	 * @param other
	 */
	@Override
	public void attack(Actor other) {
		if (other instanceof Plant) {
			if (this.isCollidingOther(other)) {
				if (this.isReadyForAction()) {
					Virus virus = new Virus(
							new Point2D.Double(this.getPosition().getX() - 50, this.getPosition().getY()));
					virusesToAdd.add(virus);
					this.resetCoolDown();
				}
			}
		}
	}
}
