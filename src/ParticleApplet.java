import java.awt.event.KeyEvent;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;


public class ParticleApplet extends PApplet {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ParticleEmitter> particleEmitters = new ArrayList<ParticleEmitter>();
	private ArrayList<Thread> scriptingThreads = new ArrayList<Thread>();

    public static void main(String[] args) {
        PApplet.main(new String[] { ParticleApplet.class.getName() });
    }
	
    @Override
	public void setup() {
		size(500, 500, P3D);
		
		setupInitialScene();
	}

	@Override
	public void draw() {
		background(0, 0, 0);
		translate(width/2f, height/2f, 0);
		for (ParticleEmitter p : this.particleEmitters) {
			p.draw();
		}
		
		// Draw FPS counter
		pushMatrix();
		translate(0, 0, 100);
		fill(255, 255, 255);
		text("" + frameRate + " FPS", -(width/2f) + 60, -(height/2f) + 70);
		popMatrix();
		
		// Draw instruction text
		translate(0, 0, 160);
		text("Press a number key to change example", -70, -145);
		
		// Print FPS every 120 frames (ideally, 2 seconds)
		if (frameCount % 200 == 0) {
			System.out.println("" + frameRate + " FPS");
		}
	}
 
	public void keyPressed() {
		String key =  KeyEvent.getKeyText(keyCode);
		this.particleEmitters.clear();
		for (Thread t : this.scriptingThreads) { 
			t.stop();
		}
		this.scriptingThreads.clear();
		if (key.equals("0")) {
			setupInitialScene();
		} else if (key.equals("1")) {
			setupTextureScene();
		} else if (key.equals("2")) {
			setupTextureWithTintScene();
		} else if (key.equals("3")) {
			setupScriptedEmittersScene();
		} else if (key.equals("4")) {
			setupLotsOfParticlesScene();
		} else if (key.equals("5")) {
			setupLotsOfParticlesButWithMaxParticlesScene();
		} else if (key.equals("6")) {
			setupFireButWithSmilyFacesScene();
		}
	}
	
	private void setupInitialScene() {
		ParticleEmitter p;
		p = new ParticleEmitter(new PVector(0, -(width/2.0f) + 100, 0), this);
		p.minParticleRotationalVelocity = -0.2f;
		p.maxParticleRotationalVelocity = 0.2f;
		p.maxVelocity = new PVector(5f, 5f, 5f);
		p.minVelocity = new PVector(-5f, -2f, -5f);
		p.maxParticleScale = 1.2f;
		p.minParticleScale = 0.8f;
		p.particlesPerEmission = 3;
		p.fadeOut = true;
		p.rainbowColors = true;
		p.minParticleAcceleration = new PVector(0f, 0f, 0f);
		p.maxParticleAcceleration = new PVector(0f, 0.2f, 0f);
		this.particleEmitters.add(p);
	}
	
	private void setupTextureScene() {
		ParticleEmitter p;
		p = new ParticleEmitter(new PVector(0, -(width/2.0f) + 100, 0), this, "res/Smiley_Face.png");
		p.maxVelocity = new PVector(5f, 5f, 5f);
		p.minVelocity = new PVector(-5f, -2f, -5f);
		p.minParticleLifespan = 100;
		p.maxParticleLifespan = 100;
		p.framesPerEmission = 2;
		p.fadeOut = true;
		this.particleEmitters.add(p);
	}
	
	private void setupTextureWithTintScene() {
		ParticleEmitter p;
		p = new ParticleEmitter(new PVector(0, -(width/2.0f) + 100, 0), this, "res/Smiley_Face.png");
		p.minParticleRotationalVelocity = -0.2f;
		p.maxParticleRotationalVelocity = 0.2f;
		p.maxVelocity = new PVector(5f, 5f, 5f);
		p.minVelocity = new PVector(-5f, -2f, -5f);
		p.maxParticleScale = 1.2f;
		p.minParticleScale = 0.8f;
		p.particlesPerEmission = 3;
		p.fadeOut = true;
		p.rainbowColors = true;
		p.minParticleAcceleration = new PVector(0f, 0f, 0f);
		p.maxParticleAcceleration = new PVector(0f, 0.2f, 0f);
		this.particleEmitters.add(p);
	}
	
	private void setupScriptedEmittersScene() {
		ParticleEmitter p;
		Thread t;
		
		// Top emitter
		p = new ParticleEmitter(new PVector(0, -(height/2.0f) + 20, 0), this);
		p.minParticleRotationalVelocity = -0.2f;
		p.maxParticleRotationalVelocity = 0.2f;
		p.maxVelocity = new PVector(5f, 5f, 5f);
		p.minVelocity = new PVector(-5f, -2f, -5f);
		p.maxParticleScale = 1.2f;
		p.minParticleScale = 0.8f;
		p.particlesPerEmission = 3;
		p.fadeOut = true;
		p.rainbowColors = true;
		p.minParticleAcceleration = new PVector(0f, 0f, 0f);
		p.maxParticleAcceleration = new PVector(0f, 0.2f, 0f);
		this.particleEmitters.add(p);
		t = new Thread(new Runnable() {			
			@Override
			public void run() {
				while (true) {
					particleEmitters.get(0).inhibitEmissions = !particleEmitters.get(0).inhibitEmissions;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		this.scriptingThreads.add(t);
		t.start();
		
		// Bottom emitter
		p = new ParticleEmitter(new PVector(0, (height/2) - 20, 0), this);
		p.minParticleRotationalVelocity = -0.2f;
		p.maxParticleRotationalVelocity = 0.2f;
		p.maxVelocity = new PVector(5f, 2f, 5f);
		p.minVelocity = new PVector(-5f, -5f, -5f);
		p.maxParticleScale = 1.2f;
		p.minParticleScale = 0.8f;
		p.particlesPerEmission = 3;
		p.fadeOut = true;
		p.rainbowColors = true;
		p.minParticleAcceleration = new PVector(0f, -0.2f, 0f);
		p.maxParticleAcceleration = new PVector(0f, 0f, 0f);
		p.inhibitEmissions = true;
		this.particleEmitters.add(p);
		t = new Thread(new Runnable() {			
			@Override
			public void run() {
				while (true) {
					particleEmitters.get(1).inhibitEmissions = !particleEmitters.get(1).inhibitEmissions;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		this.scriptingThreads.add(t);
		t.start();
	}
	
	private void setupLotsOfParticlesScene() {
		for (int i = 0; i < 100; i++) {
			ParticleEmitter p = new ParticleEmitter(new PVector((float)Math.random() * 100, (float)Math.random() * 100, 0), this);
			p.minParticleRotationalVelocity = -0.2f;
			p.maxParticleRotationalVelocity = 0.2f;
			p.maxVelocity = new PVector(5f, 5f, 5f);
			p.minVelocity = new PVector(-5f, -2f, -5f);
			p.maxParticleScale = 1.2f;
			p.minParticleScale = 0.8f;
			p.particlesPerEmission = 3;
			p.fadeOut = true;
			p.rainbowColors = true;
			p.minParticleAcceleration = new PVector(0f, 0f, 0f);
			p.maxParticleAcceleration = new PVector(0f, 0.2f, 0f);
			this.particleEmitters.add(p);
		}
	}
	
	private void setupLotsOfParticlesButWithMaxParticlesScene() {
		for (int i = 0; i < 100; i++) {
			ParticleEmitter p = new ParticleEmitter(new PVector((float)Math.random() * 100, (float)Math.random() * 100, 0), this);
			p.minParticleRotationalVelocity = -0.2f;
			p.maxParticleRotationalVelocity = 0.2f;
			p.maxVelocity = new PVector(5f, 5f, 5f);
			p.minVelocity = new PVector(-5f, -2f, -5f);
			p.maxParticleScale = 1.2f;
			p.minParticleScale = 0.8f;
			p.particlesPerEmission = 3;
			p.fadeOut = true;
			p.rainbowColors = true;
			p.minParticleAcceleration = new PVector(0f, 0f, 0f);
			p.maxParticleAcceleration = new PVector(0f, 0.2f, 0f);
			p.maxNumParticles = 100;
			this.particleEmitters.add(p);
		}
	}
	
	private void setupFireButWithSmilyFacesScene() {
		for (int i = 0; i < 3; i++) {
			ParticleEmitter p = new ParticleEmitter(new PVector(i*150 - 180, (height/2) - 20, 0), this, "res/Smiley_Face.png");
			p.minParticleRotationalVelocity = -0.2f;
			p.maxParticleRotationalVelocity = 0.2f;
			p.maxVelocity = new PVector(2f, 0f, 5f);
			p.minVelocity = new PVector(-2f, -1f, -5f);
			p.maxParticleScale = 0.8f;
			p.minParticleScale = 0.4f;
			p.particlesPerEmission = 2;
			p.fadeOut = true;
			p.rainbowColors = true;
			p.minParticleLifespan = 10;
			p.maxParticleLifespan = 30;
			p.minParticleAcceleration = new PVector(0f, -0.3f, 0f);
			p.maxParticleAcceleration = new PVector(0f, -0.1f, 0f);
			this.particleEmitters.add(p);
		}
	}
}
