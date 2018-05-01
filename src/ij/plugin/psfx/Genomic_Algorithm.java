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
	private Process_Genomes prg;
	private Imps_Save isv = new Imps_Save();

	private Mscs_ ms = new Mscs_();



	private int random_seed= 114514;
	private ImagePlus ref;
	private ImagePlus tag;
	private int searchL;

	private static int generation_count =0;

	//GA parameter
	private int max_population =200;

	//rate of preserved parents;
	double preserve_parent_rate =0.4;

	//rate of invader genome;
	double invader_genom_rate = 0.01;
	private double carry_over_rate = 0.5;
	private double individual_mutation_rate = 0.03;
	private double genome_mutation_rate= 0.01;
	private int process_generation=20;

	//GA objects
	private ArrayList<Genome_ga> genome_list = new ArrayList<Genome_ga>();

	//vars range
	private int[] x_range = {0,0};
	private int[] y_range = {0,0};
	private double[] theta_range = {0,0};

	//last result
	Genome_ga lastbest;

	//setter
	public void setAGT(Args_Getter agt) {
		this.agt = agt;
		this.isv.setAGT(agt);
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
		IJ.log("search Length is");
		ms.int2ijlog(L);
		IJ.log("ref width is");
		ms.int2ijlog(wid);
		IJ.log("ref hei is");
		ms.int2ijlog(hei);

		IJ.log("search x range is");
		ms.ints2ijlog(this.x_range);
		IJ.log("search y range is");
		ms.ints2ijlog(this.y_range);
		IJ.log("search theta range is");
		ms.db2ijlog(this.theta_range);

		this.rand = new SecureRandom();
		this.rand.setSeed((long)this.random_seed);

		this.ev = new Eval();
		this.ev.setRef(this.ref);
		this.ev.setTag(this.tag);

		this.prg = new Process_Genomes(this.random_seed);
		this.prg.setGenesRange(this.x_range, this.y_range, this.theta_range);
		this.prg.setProcessPram(this.max_population,
				this.preserve_parent_rate,
				this.invader_genom_rate, this.carry_over_rate,
				this.individual_mutation_rate,
				this.genome_mutation_rate,
				this.process_generation);
		this.prg.setEval(this.ev);

	}


	public void main() {
		parseParam();
		System.gc();
		IJ.run("Colors...", "foreground=black background=black selection=black");
		ArrayList<Genome_ga> genomelist = prg.makeGenomeList(this.max_population);
		prg.logGenomeListStats(genomelist);
/*		for(int i=0; i<this.max_population;i++) {
			ms.int2ijlog(i);
			genome_i = genomelist.get(i);
			genome_i.log_genom();
		}
*/
//		prg.showBestInGen(genomelist);
		isv.saveImps(prg.getBestImpInGen(genomelist), "xor_0");



		for(int i =0; i<this.process_generation; i++) {
			genomelist = prg.makeNewGenGenomeList(genomelist);
			prg.logGenomeListStats(genomelist);
			//prg.showBestInGen(genomelist);
			isv.saveImps(prg.getBestImpInGen(genomelist), "xor_"+String.valueOf(i+1));
		}

		this.lastbest = prg.getBestInGen(genomelist);
		}

	public Genome_ga getLastBest() {
		return this.lastbest;
	}

}
