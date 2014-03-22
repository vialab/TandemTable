package TandemTable.util;
import java.awt.Color;

import org.jdesktop.animation.timing.interpolation.Evaluator;


public class ColourEval extends Evaluator<Color> {

	@Override
	public Color evaluate(Color v0, Color v1, float fraction) {
		//for more pronounced/longer lasting v1 Color
		//fraction = (float) Math.sqrt(fraction);

		return new Color(
				
				(int) (v0.getRed() + (v1.getRed()-v0.getRed())*fraction),
				(int) (v0.getGreen() + (v1.getGreen()-v0.getGreen())*fraction),
				(int) (v0.getBlue() + (v1.getBlue()-v0.getBlue())*fraction)
				//(int) (v0.getAlpha() + (v1.getAlpha()-v0.getAlpha())*fraction)
				);
	}

}