package ij.plugin.psfx;

import ij.ImagePlus;
import ij.plugin.ImageCalculator;
public class Eval {
	ImagePlus ref;
	ImagePlus tag;
	ImageCalculator ic;

	int x;
	int y;
	double theta;

	public void setRef(ImagePlus ref) {
		this.ref = ref;
	}

	public void setTag(ImagePlus tag) {
		this.tag = tag;
	}

	public double getEval(int x, int y, double theta) {
		double score = 0.0;


		return score;
	}

	private void makeComp(int x, int y, double theta) {

	}

}
