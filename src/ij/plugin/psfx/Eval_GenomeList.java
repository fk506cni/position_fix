package ij.plugin.psfx;

import java.util.ArrayList;

import ij.ImagePlus;

public class Eval_GenomeList {
	private Eval ev;
	private Args_Getter agt;


	public void setATimps(ImagePlus refATimp, ImagePlus tagATimp) {
		ev = new Eval();
		ev.setRef(refATimp);
		ev.setTag(tagATimp);

	}

	public ArrayList<Genome_ga> evalGenomeList(ArrayList<Genome_ga> gl){
		int glsize = gl.size();
		double score = 0.0;
		for(int i = 0;i<glsize;i++) {
			Genome_ga gi = gl.get(i);
			if(gi.getEval()==0.0) {
				score = ev.getEval(gi.getGeneX(), gi.getGeneY(), gi.getGeneTheta());
				gi.setEval(score);
				gl.set(i, gi);
			}
		}

		return gl;
	}

}
