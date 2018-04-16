package ij.plugin.psfx;
//genom class for GA

import ij.IJ;

public class Genome_ga {
	private int gene_x;
	private int gene_y;
	private double gene_theta;
	private double eval_val;

	public void setGenes(int x, int y, double theta){
		this.gene_x = x;
		this.gene_y = y;
		this.gene_theta = theta;
	}

	public int[] getGenesXY() {
		int[] xy = {this.gene_x, this.gene_y};
		return xy;
	}

	public int getGeneX() {
		return this.gene_x;
	}

	public int getGeneY() {
		return this.gene_y;
	}

	public double getGeneTheta() {
		return this.gene_theta;
	}

	public void setEval(double eval_val) {
		this.eval_val = eval_val;
	}

	public double getEval() {
		return this.eval_val;
	}

	public void log_genom() {
		IJ.log("gene x:"+String.valueOf(this.gene_x)+
				"\ngene y: "+String.valueOf(this.gene_y)+
				"\ngene theta: "+String.valueOf(this.gene_theta)+
				"\neval_val: "+String.valueOf(this.eval_val));
	}
	public void log_genom_eval() {
		IJ.log("eval_val: "+String.valueOf(this.eval_val));
	}
}
