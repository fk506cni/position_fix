package ij.plugin.psfx;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;

import ij.IJ;
import ij.ImagePlus;

public class Process_Genomes {
	private int perturbation_count = 0;
	private int catastrophe_count =0;

	private Args_Getter agt;
	private prepareATimp_ pam;
	private SecureRandom rand ;
	private Eval ev;
	private ComPara_Genome comG = new ComPara_Genome();
	private Num_java nj = new Num_java();
//	private Eval_GenomeList evg = new Eval_GenomeList();
	private Eval_GenomeList_para evg = new Eval_GenomeList_para();

	//GA parameter
	private int max_population =3000;
	double preserve_parent_rate =0.4;

	double invader_genom_rate = 0.01;
	private double carry_over_rate = 0.5;

	private double individual_mutation_rate = 0.01;
	private double perturbation_base = 100.0;
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
			double perturbation_base,
			double genome_mutation_rate,
			int process_generation
			) {
		this.max_population = max_population;
		this.preserve_parent_rate = preserve_parent_rate;
		this.invader_genom_rate = invader_genome_rate;
		this.carry_over_rate = carry_over_rate;
		this.individual_mutation_rate = individual_mutation_rate;
		this.perturbation_base = perturbation_base;
		this.genome_mutation_rate = genome_mutation_rate;
		this.process_generation = process_generation;
	}

	public void setEval(Eval ev) {
		this.ev = ev;
	}

	public void setSupClass(Args_Getter agt, Eval ev, prepareATimp_ pam) {
		this.agt= agt;
		this.ev = ev;
		this.ev.setAgt(agt);
		this.pam = pam;
		this.evg.setATimps(pam.getFxRefImp(), pam.getFxTagImp());
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
//		ga1.setEval(ev.getEval(x, y, theta));
		ga1.setEval(0.0);
		return ga1;
	}

	public ArrayList<Genome_ga> makeGenomeList(int length){
		//make genome population. number of list is length-1
		ArrayList<Genome_ga> genomelist = new ArrayList<Genome_ga>();
		for(int i =0; i<length;i++) {
			genomelist.add(make1Genome());
		}

		genomelist = evg.evalGenomeList(genomelist);

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


	/*
	public int[] getNextPairInts(int lowend, int highend) {
		//give randome pair nod duplicated
		//lowend <= pair < highend

		int i1 = (int)((highend - lowend)* this.rand.nextDouble() + lowend);
		//IJ.log(String.valueOf(i1)+ " is i1");
		int i2 = i1;
		while(i1 == i2) {
			i2 = (int)((highend - lowend)* this.rand.nextDouble() + lowend);
			//IJ.log(String.valueOf(i2)+ " is i2");
		}

		int[] result = {i1, i2};
		return result;
	}
	*/

	public int[] getRankBiasPair(int rankers_length) {
		//return 2 rankers randomly selected but biased by rank
		//rank is {1, 2, 3, ....rankers_length}.
		//but this return index of rankig array. return values are chozen from 0, 1, ...,(lengh -1)
		int l = rankers_length;
		double d1 =this.rand.nextDouble()*l;
		double d2 =this.rand.nextDouble()*l;
		int i1 = (int)(Math.min(d1, d2));
		//IJ.log(String.valueOf(i1)+ " is i1");
		int i2 = i1;
		while(i1 == i2) {
			double d3 =this.rand.nextDouble()*l;
			double d4 =this.rand.nextDouble()*l;
			i2 = (int)(Math.min(d3, d4));
			//IJ.log(String.valueOf(i2)+ " is i2");
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

		//progeny.setEval(ev.getEval(x, y, theta));
		progeny.setEval(0.0);
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

		int x = ga1.getGeneX();
		int y = ga1.getGeneY();
		double theta = ga1.getGeneTheta();

		//perturbation
		if(this.rand.nextDouble() < this.individual_mutation_rate) {
			x = (int)((double)x+ xl*(this.rand.nextGaussian())/perturbation_base);
			this.perturbation_count++;
		}

		if(this.rand.nextDouble() < this.individual_mutation_rate) {
			y = (int)((double)y+ yl*(this.rand.nextGaussian())/perturbation_base);
			this.perturbation_count++;
		}

		if(this.rand.nextDouble() < this.individual_mutation_rate) {
			theta = theta + thel*(this.rand.nextGaussian())/perturbation_base;
			this.perturbation_count++;
		}

		//new val
		if(this.rand.nextDouble() < this.genome_mutation_rate) {
			x = getNextIntRange(this.x_range);
			this.catastrophe_count++;
		}

		if(this.rand.nextDouble() < this.genome_mutation_rate) {
			y = getNextIntRange(this.y_range);
			this.catastrophe_count++;
		}

		if(this.rand.nextDouble() < this.genome_mutation_rate) {
			theta = getNextDoubleRange(this.theta_range);
			this.catastrophe_count++;
		}

		x = rangerX(x);
		y = rangerY(y);
		theta = rangerThe(theta);

		if(x == ga1.getGeneX() && y == ga1.getGeneY() && theta == ga1.getGeneTheta()) {
			return ga1;
		}else {
			ga1.setGenes(x, y, theta);
			//ga1.setEval(ev.getEval(x, y, theta));
			ga1.setEval(0.0);
			return ga1;
		}

	}

	public ArrayList<Genome_ga> sortGenomeList(ArrayList<Genome_ga> genomelist) {
		Collections.sort(genomelist, this.comG);
		ArrayList<Genome_ga> sorted_list = new ArrayList<Genome_ga>(genome_list);
		return sorted_list;
	}

	public ArrayList<Genome_ga> makeNewGenGenomeList(ArrayList<Genome_ga> genomelist){
		this.perturbation_count =0;
		this.catastrophe_count =0;

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

//		invaders = evg.evalGenomeList(invaders);
//		progeny = evg.evalGenomeList(progeny);

		ArrayList<Genome_ga> new_gen = new ArrayList<Genome_ga>();
		new_gen.addAll(invaders);
		new_gen.addAll(progeny);
		new_gen = evg.evalGenomeList(new_gen);
		new_gen.addAll(parents);

		IJ.log("total perturbation in this generation creation: "+String.valueOf(this.perturbation_count));
		IJ.log("total catastrophe in this generation creation: "+String.valueOf(this.catastrophe_count));
//		new_gen = evg.evalGenomeList(new_gen);
		return new_gen;
	}

	public void showBestInGen(ArrayList<Genome_ga> genomelist) {
		Collections.sort(genomelist, this.comG);

		Genome_ga best1 = genomelist.get(0);
		IJ.log("best genome in this generation is...");
		best1.log_genom();
		ev.getshowEval(best1.getGeneX(), best1.getGeneY(), best1.getGeneTheta());
	}

	public ImagePlus getBestImpInGen(ArrayList<Genome_ga> genomelist) {
		Collections.sort(genomelist, this.comG);

		Genome_ga best1 = genomelist.get(0);
		IJ.log("best genome in this generation is...");
		best1.log_genom();

		ImagePlus bestImp = ev.getEvalImp(best1.getGeneX(), best1.getGeneY(), best1.getGeneTheta());
		return bestImp;
	}

	public Genome_ga getBestInGen(ArrayList<Genome_ga> genomelist) {
		Collections.sort(genomelist, this.comG);
		Genome_ga best1 = genomelist.get(0);
		return best1;
	}

	public void logGenomeListStats(ArrayList<Genome_ga> genomelist) {
		int[] xs = new int[genomelist.size()];
		int[] ys = new int[genomelist.size()];
		double[] ths = new double[genomelist.size()];
		double[] evals = new double[genomelist.size()];

		for(int i=0; i<genomelist.size();i++) {
			xs[i] = genomelist.get(i).getGeneX();
			ys[i] = genomelist.get(i).getGeneY();
			ths[i] = genomelist.get(i).getGeneTheta();
			evals[i] = genomelist.get(i).getEval();
		}

		String xa = String.valueOf(nj.ints2avg(xs));
		String xv = String.valueOf(nj.ints2sd(xs));
		String xmd = String.valueOf(nj.ints2med(xs));
		String xma = String.valueOf(nj.ints2mad(xs));
		IJ.log("geneX avg:"+xa+". sd:"+xv);
		IJ.log("geneX med:"+xmd+". mad:"+xma);

		String ya = String.valueOf(nj.ints2avg(ys));
		String yv = String.valueOf(nj.ints2sd(ys));
		String ymd = String.valueOf(nj.ints2med(ys));
		String yma = String.valueOf(nj.ints2mad(ys));
		IJ.log("geneY avg:"+ya+". sd:"+yv);
		IJ.log("geneY med:"+ymd+". mad:"+yma);

		String ta = String.valueOf(nj.doubles2avg(ths));
		String tv = String.valueOf(nj.doubles2sd(ths));
		String tmd = String.valueOf(nj.dbs2med(ths));
		String tma = String.valueOf(nj.dbs2mad(ths));
		IJ.log("geneTheta avg:"+ta+". sd:"+tv);
		IJ.log("geneTheta med:"+tmd+". mad:"+tma);

		String ea = String.valueOf(nj.doubles2avg(evals));
		String ev = String.valueOf(nj.doubles2sd(evals));
		String emd = String.valueOf(nj.dbs2med(evals));
		String ema = String.valueOf(nj.dbs2mad(evals));
		IJ.log("Eval_val avg:"+ea+". sd:"+ev);
		IJ.log("Eval val med:"+emd+". mad:"+ema);
	}
}
