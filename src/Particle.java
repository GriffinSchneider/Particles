import java.awt.Color;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;


public class Particle {

	public PVector velocity = new PVector(0f, 0f, 0f);
	public PVector acceleration = new PVector(0f, 0f, 0f);
	public float rotationalVelocity;
	public float rotation;
	public Color color;
	public boolean fadeOut = false;
	public float scale = 1.0f;
	public PImage texture;
	
	private ParticleEmitter emitter;
	private PVector pos;
	private PApplet parent;
	public int lifespan;
	private int lifeCount;
	
	public Particle(ParticleEmitter emitter, PVector pos, PApplet parent, int lifespan) {
		this.emitter = emitter;
		this.pos = pos;
		this.parent = parent;
		this.lifespan = lifespan;
		this.color = Color.BLUE;
	}
	
	public void draw() {
		
		this.lifeCount++;
		if (lifespan != 0 && lifeCount >= lifespan) {
			emitter.removeParticle(this);
			return;
		}
		
		this.velocity.add(acceleration);
		this.pos.add(velocity);
		rotation += rotationalVelocity;
		
		if (this.fadeOut && this.lifespan != 0) {
			int alpha = (int) (255 * ((float)(lifespan - lifeCount) / (float)lifespan));
			this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
		}
		
		parent.pushMatrix();
		
		parent.fill(color.getRGB());
		// The particle's position is relative to its emitter, and the matrix has
		// already been pre-translated by the emitter to account for its position.
		parent.translate(pos.x, pos.y, pos.z);
		parent.rotate(this.rotation);
		parent.scale(scale);
		
		if (texture == null) {
			parent.rect(-10*scale, -10*scale, 20*scale, 20*scale);
		} else {
			parent.image(texture, 
					     -texture.height*scale*0.5f, 
					     -texture.height*scale*0.5f, 
					     texture.height*scale, 
					     texture.width*scale);
		}
		
		parent.popMatrix();
	}

}
