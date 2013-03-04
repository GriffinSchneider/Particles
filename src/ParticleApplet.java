import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;


public class ParticleApplet extends PApplet {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ParticleEmitter> particleEmitters = new ArrayList<ParticleEmitter>();

    public static void main(String[] args) {
        PApplet.main(new String[] { ParticleApplet.class.getName() });
    }
	
	public void setup() {
		size(500, 500, P3D);
		
		ParticleEmitter p;
		p = new ParticleEmitter(new PVector(0, 0, 0), this);
		p.minParticleRotationalVelocity = -0.2f;
		p.maxParticleRotationalVelocity = 0.2f;
		p.maxVelocity = new PVector(5f, 5f, 5f);
		p.minVelocity = new PVector(-5f, -5f, -5f);
		p.particlesPerEmission = 2;
		p.fadeOut = true;
		this.particleEmitters.add(p);
	}

	public void draw() {
		background(0, 0, 0);
		translate(width/2f, height/2f, 0);
		for (ParticleEmitter p : this.particleEmitters) {
			p.draw();
		}
		
		// Draw FPS counter
		translate(0, 0, 100);
		fill(255, 255, 255);
		text("" + frameRate + " FPS", -(width/2f) + 60, -(height/2f) + 70);
	}
	
}
