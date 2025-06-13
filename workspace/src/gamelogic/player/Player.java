package gamelogic.player;

import java.awt.Color;
import java.awt.Graphics;

import gameengine.PhysicsObject;
import gameengine.graphics.MyGraphics;
import gameengine.hitbox.RectHitbox;
import gamelogic.Main;
import gamelogic.level.Level;
import gamelogic.tiles.Tile;

public class Player extends PhysicsObject {
	public float walkSpeed = 400;
	public float jumpPower = 1350;
	private boolean doubleJump = false;
	private boolean hasDoubleJumped = false;
	private boolean hasLetGo = false;
	private boolean hasTouchedGround = true;
	private boolean isJumping = false;

	public Player(float x, float y, Level level) {
		
		super(x, y, level.getLevelData().getTileSize(), level.getLevelData().getTileSize(), level);
		doubleJump = false;
		int offset = (int) (level.getLevelData().getTileSize() * 0.1); // hitbox is offset by 10% of the player size.
		this.hitbox = new RectHitbox(this, offset, offset, width - offset, height - offset);
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		
		movementVector.x = 0;
		if (PlayerInput.isLeftKeyDown()) {
			movementVector.x = -walkSpeed;
		}
		if (PlayerInput.isRightKeyDown()) {
			movementVector.x = +walkSpeed;
		}
		// First Jump
		if (PlayerInput.isJumpKeyDown()) {
			if(!isJumping){
				movementVector.y = -jumpPower;
				isJumping = true;
				hasTouchedGround = false;
				hasDoubleJumped = false;
			}
		}
		// Checks if player has let go of spacebar.
		if (!PlayerInput.isJumpKeyDown()) {
			hasLetGo = true;
		}
		// Double jump
		if (!hasDoubleJumped && doubleJump && hasLetGo && !hasTouchedGround && PlayerInput.isJumpKeyDown()) {
				movementVector.y = -jumpPower;
				hasDoubleJumped = true;
				isJumping = true;
				hasLetGo = false;
			} 

		isJumping = true;
		if (collisionMatrix[BOT] != null){
			isJumping = false;
			hasDoubleJumped = false;
			hasTouchedGround = true;
		}

	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		MyGraphics.fillRectWithOutline(g, (int) getX(), (int) getY(), width, height);

		if (Main.DEBUGGING) {
			for (int i = 0; i < closestMatrix.length; i++) {
				Tile t = closestMatrix[i];
				if (t != null) {
					g.setColor(Color.RED);
					g.drawRect((int) t.getX(), (int) t.getY(), t.getSize(), t.getSize());
				}
			}
		}

		hitbox.draw(g);
	}

	// Pre-condition: newSpeed is a float and a potential walking speed.
	// Post-condition: walkSpeed is set to newSpeed.
	public void setWalkSpeed(float newSpeed) {
		walkSpeed = newSpeed;
	}

	// Pre-condition: newPower is a float and a potential jumping power.
	// Post-condition: jumpPower is set to newPower.
	public void setJumpPower(float newPower) {
		jumpPower = newPower;
	}

	// Pre-condition: hasDoubleJump is a boolean that indicates whether the player has touched 3 flowers.
	// Post-condition: player is allowed to double jump
	public void setDoubleJump(boolean hasDoubleJump) {
		doubleJump = hasDoubleJump;
	}
}