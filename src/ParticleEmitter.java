import java.awt.Color;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;


public class ParticleEmitter {
	
	/** The number of frames between each particle emission. 0 = emit every frame. */
	public int framesPerEmission = 0;
	/** The number of particles to create per emission .*/
	public int particlesPerEmission = 10;
	/** The maximum number of particles. 0 = Infinite. */
	public int maxNumParticles = 0;
	/** The maximum possible velocity of each particle created by this emitter .*/
	public PVector maxVelocity = new PVector(5f, 5f, 5f);
	/** The minimum possible velocity of each particle created by this emitter .*/
	public PVector minVelocity = new PVector(-5f, -5f, -5f);
	/** The maximum possible lifespan (in frames) of each particle created by this emitter .*/
	public int maxParticleLifespan = 200;
	/** The minimum possible lifespan (in frames) of each particle created by this emitter .*/
	public int minParticleLifespan = 200;
	/** The maximum possible rotational velocity (in radians per frame) of each particle created 
	 * by this emitter .*/
	public float maxParticleRotationalVelocity = 0f;
	/** The minimum possible rotational velocity (in radians per frame) of each particle created 
	 * by this emitter .*/
	public float minParticleRotationalVelocity = 0f;
    /** The maximum possible scale factor of each particle created by this emitter .*/
	public float maxParticleScale = 1.0f;
    /** The minimum possible scale factor of each particle created by this emitter .*/
	public float minParticleScale = 1.0f;
	/** Acceleration per frame of particles created by this emitter */
	public PVector particleAcceleration = new PVector(0f, 0f, 0f);
	
	/** Whether to fade out particles so that their alpha = 0 at the end of their lifespan */
	public boolean fadeOut = false;
	
	/** Usual emissions are ignored if this is true */
	public boolean inhibitEmissions = false;
	
	public PVector pos;
	private PApplet parent;
	private int lifespan;
	private int lifeCount;
	private PImage texture;
	
	private ParticleEmitter parentEmitter;
	
	private ArrayList<Particle> particles = new ArrayList<Particle>();
	private ArrayList<ParticleEmitter> spawnedEmitters = new ArrayList<ParticleEmitter>();
	
	public ParticleEmitter(PVector pos, PApplet parent) {
		this(pos, parent, 0);
	}
	
	public ParticleEmitter(PVector pos, PApplet parent, int lifespan) {
		this(pos, parent, lifespan, null, null);
	}
		
	public ParticleEmitter(PVector pos, PApplet parent, int lifespan, ParticleEmitter parentEmitter, String filename) {
		this.pos = pos;
		this.parent = parent;
		this.lifespan = lifespan;
		this.parentEmitter = parentEmitter;
		if (filename != null) {
			this.texture = parent.loadImage(filename);
		}
	}
	
	public void draw() {	
		this.lifeCount++;
		if (lifespan != 0 && this.lifeCount >= lifespan) {
			if (this.parentEmitter != null) {
				this.parentEmitter.removeSpawnedEmitter(this);
			}
			return;
		}
		
		if (this.lifeCount % (framesPerEmission+1) == 0) {
			this.emit();
			if (maxNumParticles != 0) {
				while (this.particles.size() >= maxNumParticles) {
					this.particles.remove(0);
				}
			}
		}
		
		parent.pushMatrix();
		// Translate based on the emitter's location - particle
		// locations are relative to the emitter.
		parent.translate(this.pos.x, this.pos.y, this.pos.z);
		
		drawParticles();
		
		parent.popMatrix();
	}
	
	// Extracted into a method because of this unfortunate warning
	@SuppressWarnings("unchecked")
	private void drawParticles() {
		// Iterate over a copy of the particles array rather than the array itself,
		// since particles might remove themselves when we call draw().
		for (Particle p : (ArrayList<Particle>)particles.clone()) {
			p.draw();
		}
	}
	
	// Handle the emissions for one frame - create a number of particles
	// equal to particlesPerEmission and set their initial velocities based on
	// maxVelocity and minVelocity
	private void emit() {
		if (this.inhibitEmissions) return;
		for (int i = 0; i < particlesPerEmission; i++) {
			int lifespan = (int) randInRange(maxParticleLifespan, minParticleLifespan);
			Particle p = new Particle(this, new PVector(0, 0, 0), this.parent, lifespan);
			
			float xVel = randInRange(maxVelocity.x, minVelocity.x);
			float yVel = randInRange(maxVelocity.y, minVelocity.y);
			float zVel = randInRange(maxVelocity.z, minVelocity.z);
			p.velocity = new PVector(xVel, yVel, zVel);
			
			p.rotationalVelocity = randInRange(maxParticleRotationalVelocity, minParticleRotationalVelocity);
			p.scale = randInRange(maxParticleScale, minParticleScale);
			p.acceleration = this.particleAcceleration;
			p.color = new Color((int) (Math.random()*Math.pow(2, 24)));
			p.texture = texture;
			p.fadeOut = fadeOut;
			this.particles.add(p);
		}
	}
	
	private float randInRange(float max, float min) {
		return (max - min) * (float)Math.random() + min;
	}
	
	public void removeParticle(Particle particle) {
		this.particles.remove(particle);
	}
	
	public void removeSpawnedEmitter(ParticleEmitter emitter) {
		this.spawnedEmitters.remove(emitter);
	}

}
