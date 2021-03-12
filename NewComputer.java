package a9;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class NewComputer extends Plant {

	private static final int HEALTH = 100;
	private static final int COOLDOWN = 5;
	private static final int ATTACK_DAMAGE = 1;
	private static final BufferedImage IMAGE;

	static {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("src/a9/game-icons/computer1.png"));
		} catch (IOException e) {
			System.out.println("A file was not found");
			System.exit(0);
		}
		IMAGE = image;
	}

	/***
	 * Creates a plant of New Computer type with specified plant attributes at a
	 * given position.
	 * 
	 * @param position
	 */
	public NewComputer(Point2D.Double position) {
		super(position, new Point2D.Double(IMAGE.getWidth(), IMAGE.getHeight()), IMAGE, HEALTH, COOLDOWN,
				ATTACK_DAMAGE);
	}

	/***
	 * When the New Computer attacks, it will attack colliding Viruses (with no
	 * cooldown) but will also attack the first Hacker in the actors array list (The
	 * Hacker the farthest down the line).
	 * 
	 * @param other
	 */
	@Override
	public void attack(Actor other) {
		// Attacks Hacker farthest down the line with a cooldown.
		if (this.isReadyForAction()) {
			if (other instanceof Hacker) {
				other.changeHealth(-attackDamage);
				this.resetCoolDown();
			}
		}

		// Attacks colliding viruses with no cooldown so they cannot spread.
		if (other instanceof Virus && this.isCollidingOther(other)) {
			other.changeHealth(-attackDamage);
		}
	}
}
