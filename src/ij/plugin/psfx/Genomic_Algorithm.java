package ij.plugin.psfx;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

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
//	private int genome_length = 3;
	private int max_population =30;
//	private int select_genom = 200;

	//rate of elite
	private double select_genom_rate = 0.3;

	//rate of children
	private double new_progeny_rate = 0.5;
	private double carry_over_rate = 0.4;
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

	public int[] getNextPairInts(int lowend, int highend) {
		//give randome pair nod duplicated
		//lowend <= pair < highend

		int i1 = (int)((highend - lowend)* this.rand.nextDouble() + lowend);
		IJ.log(String.valueOf(i1)+ " is i1");
		int i2 = i1;
		while(i1 == i2) {
			i2 = (int)((highend - lowend)* this.rand.nextDouble() + lowend);
			IJ.log(String.valueOf(i2)+ " is i2");
		}

		int[] result = {i1, i2};
		return result;
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

	public ArrayList<Genome_ga> pickEliteGenomes(ArrayList<Genome_ga> genome_list) {
		int elite_size = (int)(this.max_population*this.select_genom_rate);
		int list_size = genome_list.size();
		IJ.log("original genomes");
		for(int i =0; i<list_size; i++) {
			genome_list.get(i).log_genom_eval();
		}

		Collections.sort(genome_list, comG);

		IJ.log("sorted genomes");
		for(int i =0; i<list_size; i++) {
			genome_list.get(i).log_genom_eval();
		}
		ArrayList<Genome_ga> elite_list = new ArrayList<Genome_ga>(genome_list.subList(0,  elite_size));

		IJ.log("elite genomes");
		for(int i =0; i<elite_size;i++) {
			elite_list.get(i).log_genom_eval();
		}

		return elite_list;
	}

	public Genome_ga CrossOver(Genome_ga ga1, Genome_ga ga2) {
		int x = 0;
		if(this.rand.nextDouble()<this.carry_over_rate) {
			x = this.rand.nextBoolean() ? ga1.getGeneX(): ga2.getGeneX();
		}else {
			double r = this.rand.nextDouble();
			x = (int)Math.round(r*ga1.getGeneX() +(1-r)*ga2.getGeneX());
		}

		int y = 0;
		if(this.rand.nextDouble()<this.carry_over_rate) {
			y = this.rand.nextBoolean() ? ga1.getGeneY(): ga2.getGeneY();
		}else {
			double r = this.rand.nextDouble();
			y = (int)Math.round(r*ga1.getGeneY() +(1-r)*ga2.getGeneY());
		}

		double theta = 0;
		if(this.rand.nextDouble()<this.carry_over_rate) {
			theta = this.rand.nextBoolean() ? ga1.getGeneTheta() : ga2.getGeneTheta();;
		}else {
			double r = this.rand.nextDouble();
			theta = r*ga1.getGeneTheta() +(1-r)*ga2.getGeneTheta();
		}


		Genome_ga progeny = new Genome_ga();
		progeny.setGenes(x, y, theta);
		return progeny;
	}

	public ArrayList<Genome_ga> createNextGen(ArrayList<Genome_ga> elite_list, int progeny_size){
		int elite_size = elite_list.size();
		ArrayList<Genome_ga> progeny_list = new ArrayList<Genome_ga>();

		IJ.log("creating children");

		for(int i=0; i<progeny_size;i++) {
			int[] parent_pair = getNextPairInts(0, elite_size);
			ms.ints2ijlog(parent_pair);
			Genome_ga parent0 = elite_list.get(parent_pair[0]);
			Genome_ga parent1 = elite_list.get(parent_pair[1]);
			Genome_ga child = CrossOver(parent0, parent1);
			eval1logGenome(child);
			progeny_list.add(child);
		}

		IJ.log("checking children.");

		for(int i=0; i<progeny_size;i++) {
			progeny_list.get(i).log_genom();
		}

		return progeny_list;
	}

	public void main() {
		parseParam();
		Genome_ga genome_i;


		for(int i=0; i<this.max_population;i++) {
			ms.int2ijlog(i);
			genome_i = make1Genome();
			eval1logGenome(genome_i);
			this.genome_list.add(genome_i);

			ms.ints2ijlog(getNextPairInts(0,10));
		}

		ArrayList<Genome_ga> elite_list = pickEliteGenomes(genome_list);

		ArrayList<Genome_ga> progeny_list = createNextGen(elite_list, 10);


		//ev.showTag4Check();
	}



}
