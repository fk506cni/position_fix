package ij.plugin.psfx;

import java.security.SecureRandom;
import java.util.ArrayList;

import ij.IJ;
import ij.ImagePlus;

public class Genomic_Algorithm {
	private Args_Getter agt;
	private prepareATimp_ pam;
	static private SecureRandom rand ;
	private Eval ev;
	private ComPara_Genome comG = new ComPara_Genome();

	private Mscs_ ms = new Mscs_();

	private int random_seed= 114514;
	private ImagePlus ref;
	private ImagePlus tag;
	private int searchL;

	//GA parameter
	private int genome_length = 3;
	private int max_population =1000;
	private int select_genom = 200;
	private double individual_mutation_rate = 0.05;
	private double genome_mutation_rate= 0.05;
	private int process_generation=10;

	//GA objects
	private ArrayList<Genome_ga> genome_list = new ArrayList<Genome_ga>();

	//vars range
	private int[] x_range = {0,0};
	private int[] y_range = {0,0};
	private double[] theta_range = {0,0};

	//setter
	public void setAGT(Args_Getter agt) {
		this.agt = agt;
	}

	public void setPAM(prepareATimp_ pam) {
		this.pam = pam;
	}

	public void setRefTag(ImagePlus ref, ImagePlus tag) {
		this.ref = ref;
		this.tag = tag;
	}

	public void parseParam() {
		int L = this.agt.getSearchL();
		int wid = this.ref.getWidth();
		int hei = this.ref.getHeight();

		if(2*L >= wid) {
			this.x_range[0] = -(int)wid/2;
			this.x_range[1] = (int)wid/2;
		}else {
			this.x_range[0] = -L;
			this.x_range[1] = L;
		}

		if(2*L >= hei) {
			this.y_range[0] = -(int)hei/2;
			this.y_range[1] = (int)hei/2;
		}else {
			this.y_range[0] = -L;
			this.y_range[1] = L;
		}

		this.theta_range = this.agt.getThetaLR();
		this.random_seed = this.agt.getSeed();

		IJ.log("Genomic Alg, parsed parameters...");
		ms.int2ijlog(L);
		ms.int2ijlog(wid);
		ms.int2ijlog(hei);

		ms.ints2ijlog(this.x_range);
		ms.ints2ijlog(this.y_range);
		ms.db2ijlog(this.theta_range);

		this.rand = new SecureRandom();
		this.rand.setSeed((long)this.random_seed);

		this.ev = new Eval();
		this.ev.setRef(this.ref);
		this.ev.setTag(this.tag);
	}

	public int getNextX() {
		double d = this.rand.nextDouble();
		d = (this.x_range[1] - this.x_range[0])*d + (double)this.x_range[0];
		int x = (int)Math.round(d);
		return x;
	}

	public int getNextY() {
		double d = this.rand.nextDouble();
		d = (this.y_range[1] - this.y_range[0])*d + (double)this.y_range[0];
		int y = (int)Math.round(d);
		return y;
	}

	public double getNextTheta() {
		double d = this.rand.nextDouble();
		double theta = (this.theta_range[1] - this.theta_range[0])*d + this.theta_range[0];
		return theta;
	}


	public Genome_ga make1Genome() {
		Genome_ga ga1 = new Genome_ga();
		int x = getNextX();
		int y = getNextY();
		double theta= getNextTheta();
		ga1.setGenes(x, y, theta);
		//ga1.setEval(ev.getEval(x, y, theta));
		return ga1;
	}

	public double eval1Genome(Genome_ga ga1) {
		double d = this.ev.getEval(ga1.getGeneX(), ga1.getGeneY(), ga1.getGeneTheta());
		ga1.setEval(d);
		return d;
	}

	public void eval1logGenome(Genome_ga ga1) {
		double d = this.ev.getEval(ga1.getGeneX(), ga1.getGeneY(), ga1.getGeneTheta());
		ga1.setEval(d);
		//ga1.log_genom();
	}

	public void main() {
		parseParam();
		Genome_ga genome_i;
		for(int i=0; i<this.max_population;i++) {
			ms.int2ijlog(i);
			genome_i = make1Genome();
			eval1logGenome(genome_i);
			this.genome_list.add(genome_i);
		}

		//ev.showTag4Check();
	}



}
