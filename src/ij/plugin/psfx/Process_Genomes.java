package ij.plugin.psfx;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

import ij.IJ;

public class Process_Genomes {

	private Args_Getter agt;
	private prepareATimp_ pam;
	private SecureRandom rand ;
	private Eval ev;
	private ComPara_Genome comG = new ComPara_Genome();

	//GA parameter
//	private int genome_length = 3;
	private int max_population =30;
//	private int select_genom = 200;

	//rate of elite
	//private double select_genom_rate = 0.3;

	//rate of preserved parents;
	double preserve_parent_rate =0.4;

	//rate of invader genome;
	double invader_genom_rate = 0.01;
	//private double new_progeny_rate = 0.5;
	private double carry_over_rate = 0.5;

	private double individual_mutation_rate = 0.01;
	private double genome_mutation_rate= 0.01;
	private int process_generation=10;


	//gene range
	private int[] x_range;
	private int[] y_range;
	private double[] theta_range;
	private int xl;
	private int yl;
	private double thel;

	private ArrayList<Genome_ga> genome_list = new ArrayList<Genome_ga>();

	Process_Genomes(int random_seed){
		this.rand = new SecureRandom();
		this.rand.setSeed((long)random_seed);
	}

	public void setProcessPram(
			int max_population,
			double preserve_parent_rate,
			double invader_genome_rate,
			double carry_over_rate,
			double individual_mutation_rate,
			double genome_mutation_rate,
			int process_generation
			) {
		this.max_population = max_population;
		this.preserve_parent_rate = preserve_parent_rate;
		this.invader_genom_rate = invader_genome_rate;
		this.carry_over_rate = carry_over_rate;
		this.individual_mutation_rate = individual_mutation_rate;
		this.genome_mutation_rate = genome_mutation_rate;
		this.process_generation = process_generation;
	}

	public void setEval(Eval ev) {
		this.ev = ev;
	}

	public void setGenesRange(
			int[] x_range,
			int[] y_range,
			double[] theta_range) {
		this.x_range = x_range;
		this.y_range = y_range;
		this.theta_range = theta_range;

		this.xl = Math.max(x_range[0], x_range[1]) - Math.min(x_range[0], x_range[1]);
		this.yl = Math.max(y_range[0], y_range[1]) - Math.min(y_range[0], y_range[1]);
		this.thel = Math.max(theta_range[0], theta_range[1]) - Math.min(theta_range[0], theta_range[1]);
	}

	public int getNextIntRange(int[] range) {
		//return range[0] <= val < range[1]
		int low = Math.min(range[0], range[1]);
		int high = Math.max(range[0], range[1]);
		int val = (int)((high - low)*this.rand.nextDouble() + (double)low);
		return val;
	}
	public double getNextDoubleRange(double[] range) {
		double low = Math.min(range[0], range[1]);
		double high =Math.max(range[0], range[1]);
		double val = (high - low)*this.rand.nextDouble() + low;
		return val;
	}

	public Genome_ga make1Genome() {
		Genome_ga ga1 = new Genome_ga();
		int x = getNextIntRange(this.x_range);
		int y = getNextIntRange(this.y_range);
		double theta = getNextDoubleRange(this.theta_range);
		ga1.setGenes(x, y, theta);
		ga1.setEval(ev.getEval(x, y, theta));
		return ga1;
	}

	public ArrayList<Genome_ga> makeGenomeList(int length){
		//make genome population. number of list is length-1
		ArrayList<Genome_ga> genomelist = new ArrayList<Genome_ga>();
		for(int i =0; i<length;i++) {
			genomelist.add(make1Genome());
		}
		return genomelist;
	}

	public ArrayList<Genome_ga> appendInvaders2GL(ArrayList<Genome_ga> genomelist, int invader_size){
		for(int i =0;i<invader_size;i++) {
			genomelist.add(make1Genome());
		}
		return genomelist;
	}


	public double eval1Genome(Genome_ga ga1) {
		double d = this.ev.getEval(ga1.getGeneX(), ga1.getGeneY(), ga1.getGeneTheta());
		ga1.setEval(d);
		return d;
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

	public int[] getRankBiasPair(int rankers_length) {
		//return 2 rankers randomly selected but biased by rank
		//rank is {1, 2, 3, ....rankers_length}.
		//but this return index of rankig array. return values are chozen from 0, 1, ...,(lengh -1)
		int l = rankers_length;
		double d1 =this.rand.nextDouble()*l;
		double d2 =this.rand.nextDouble()*l;
		int i1 = (int)(Math.min(d1, d2));
		IJ.log(String.valueOf(i1)+ " is i1");
		int i2 = i1;
		while(i1 == i2) {
			double d3 =this.rand.nextDouble()*l;
			double d4 =this.rand.nextDouble()*l;
			i2 = (int)(Math.min(d3, d4));
			IJ.log(String.valueOf(i2)+ " is i2");
		}

		int[] result = {i1, i2};
		return result;
	}

	public Genome_ga CrossOver(Genome_ga ga1, Genome_ga ga2) {
		double carry_over_rate = this.carry_over_rate;

		if(carry_over_rate < 0 || 1 < carry_over_rate) {
			IJ.log("caryy over should be in 0~1\nrate will be 0.5");
			carry_over_rate = 0.5;
		}
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
		progeny.setEval(ev.getEval(x, y, theta));
		return progeny;

	}

	public int rangerX(int x) {
		int new_x = x;
		if(new_x < Math.min(this.x_range[0], this.x_range[1])) {
			new_x = Math.min(this.x_range[0], this.x_range[1]);
		}else if(Math.max(this.x_range[0], this.x_range[1]) < new_x) {
			new_x = Math.max(this.x_range[0], this.x_range[1]);
		}
		return new_x;
	}

	public int rangerY(int y) {
		int ny = y;
		if(ny < Math.min(this.y_range[0], this.y_range[1])) {
			ny = Math.min(this.y_range[0], this.y_range[1]);
		}else if(Math.max(this.y_range[0], this.y_range[1]) < ny) {
			ny = Math.max(this.y_range[0], this.y_range[1]);
		}
		return ny;
	}

	public double rangerThe(double theta) {
		double nt = theta;
		if(nt < Math.min(this.theta_range[0], this.theta_range[1])) {
			nt = Math.min(this.theta_range[0], this.theta_range[1]);
		}else if(Math.max(this.theta_range[0], this.theta_range[1]) < nt) {
			nt = Math.max(this.theta_range[0], this.theta_range[1]);
		}
		return nt;
	}

	public Genome_ga mutate1Genome(Genome_ga ga1) {
		//perturbation
		int x = ga1.getGeneX();
		int y = ga1.getGeneY();
		double theta = ga1.getGeneTheta();

		if(this.rand.nextDouble() < this.individual_mutation_rate) {
			x = (int)(x+ xl*(100.0+this.rand.nextGaussian())/100);
			y = (int)(y+ yl*(100.0+this.rand.nextGaussian())/100);
			theta = theta + thel*(100.0 + this.rand.nextGaussian())/100;
		}

		//new val
		if(this.rand.nextDouble() < this.genome_mutation_rate) {
			x = getNextIntRange(this.x_range);
		}

		if(this.rand.nextDouble() < this.genome_mutation_rate) {
			y = getNextIntRange(this.y_range);
		}

		if(this.rand.nextDouble() < this.genome_mutation_rate) {
			theta = getNextDoubleRange(this.theta_range);;
		}

		x = rangerX(x);
		y = rangerY(y);
		theta = rangerThe(theta);

		ga1.setGenes(x, y, theta);
		ga1.setEval(ev.getEval(x, y, theta));

		return ga1;

	}

	public ArrayList<Genome_ga> sortGenomeList(ArrayList<Genome_ga> genomelist) {
		Collections.sort(genomelist, this.comG);
		ArrayList<Genome_ga> sorted_list = new ArrayList<Genome_ga>(genome_list);
		return sorted_list;
	}

	public ArrayList<Genome_ga> makeNewGenGenomeList(ArrayList<Genome_ga> genomelist){
		int size = genomelist.size();
		int preserved_parents = (int)(this.max_population*this.preserve_parent_rate);
		int invaders_size = (int)(this.invader_genom_rate*this.max_population);
		int progeny_size = size - preserved_parents - invaders_size;


		Collections.sort(genomelist, this.comG);

		ArrayList<Genome_ga> parents = new ArrayList<Genome_ga>(genomelist.subList(0, preserved_parents));
		ArrayList<Genome_ga> invaders = makeGenomeList(invaders_size);

		ArrayList<Genome_ga> progeny = new ArrayList<Genome_ga>();
		for(int i=0; i<progeny_size;i++) {
			int[] parents_pair = getRankBiasPair(size);
			Genome_ga ga1 = genomelist.get(parents_pair[0]);
			Genome_ga ga2 = genomelist.get(parents_pair[1]);
			Genome_ga child = CrossOver(ga1, ga2);
			progeny.add(mutate1Genome(child));
		}

		ArrayList<Genome_ga> new_gen = new ArrayList<Genome_ga>();
		new_gen.addAll(parents);
		new_gen.addAll(invaders);
		new_gen.addAll(progeny);

		return new_gen;
	}

	public void logGenomeListStats() {

	}
}
