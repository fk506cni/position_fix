package ij.plugin.psfx;
import fiji.util.gui.GenericDialogPlus;
import ij.IJ;

public class GAargs_Getter {
	//GA parameter
	private int max_population =100;

	//rate of preserved parents;
	double preserve_parent_rate =0.4;

	//rate of invader genome;
	double invader_genom_rate = 0.01;
	private double carry_over_rate = 0.5;
	private double perturbation_rate = 0.03;
	private double perturbation_base=100.0;
	private double catastrophe_rate= 0.01;
	private int process_generation=3;
	private GenericDialogPlus gdp = new GenericDialogPlus("GA chooning");


	public void setDefault(int max_population,
			double preserve_parent,
			double invader_rate,
			double carry_over_rate,
			double perturbation,
			double perturbation_base,
			double catastrophe,
			int process_generation) {
		this.max_population = max_population;
		this.preserve_parent_rate = preserve_parent;
		this.invader_genom_rate = invader_rate;
		this.carry_over_rate = carry_over_rate;
		this.perturbation_rate = perturbation;
		this.perturbation_base = perturbation_base;
		this.catastrophe_rate = catastrophe;
		this.process_generation = process_generation;
	}

	public void getGAargViaGUI() {
		GenericDialogPlus gdp = this.gdp;
		gdp.addNumericField("max population(int)", this.max_population, 0);
		gdp.addNumericField("preserve_parents_rate", this.preserve_parent_rate, 0);
		gdp.addNumericField("invader_genome_rate", this.invader_genom_rate, 0);
		gdp.addNumericField("carry_over_rate", this.carry_over_rate, 0);
		gdp.addNumericField("perturbation_rate", this.perturbation_rate, 0);
		gdp.addNumericField("perturbation_base", this.perturbation_base, 0);
		gdp.addNumericField("catastrophe_rate", this.catastrophe_rate, 0);
		gdp.addNumericField("process_generations", this.process_generation, 0);

		gdp.showDialog();
		if (gdp.wasCanceled()) {
			IJ.log("GA chooning was canceled. It will work as Default paramters.");
			return;
		}

		this.gdp = gdp;

		parseArgs();
		argsCheck();
	}

	private void parseArgs() {
		IJ.log("parsing GA paramters...");

		max_population = (int)gdp.getNextNumber();
		IJ.log("max pop: "+String.valueOf(this.max_population));

		preserve_parent_rate = ranger0to1(gdp.getNextNumber());
		IJ.log("prep pare rate: "+String.valueOf(preserve_parent_rate));

		invader_genom_rate = ranger0to1(gdp.getNextNumber());
		IJ.log("inv genome rate: "+String.valueOf(invader_genom_rate));

		carry_over_rate = ranger0to1(gdp.getNextNumber());
		IJ.log("carry over rate: "+String.valueOf(carry_over_rate));

		perturbation_rate = ranger0to1(gdp.getNextNumber());
		IJ.log("perturbation rate: "+String.valueOf(perturbation_rate));

		perturbation_base = gdp.getNextNumber();
		IJ.log("perturbation base: "+String.valueOf(perturbation_base));

		catastrophe_rate = ranger0to1(gdp.getNextNumber());
		IJ.log("catastrophe rate: "+String.valueOf(catastrophe_rate));

		process_generation = (int)gdp.getNextNumber();
		IJ.log("proc gens: "+String.valueOf(process_generation));
	}

	private void argsCheck() {
		if(preserve_parent_rate + invader_genom_rate > 1.0) {
			IJ.log("prep parent + invader rate >1\nsum should be less than 1.");
			preserve_parent_rate = 0.4;
			invader_genom_rate = 0.01;
		}

		if(perturbation_base <1) {
			IJ.log("perturbation_base is "+String.valueOf(perturbation_base)+"\nperturbation_base should be more than 1.");
			perturbation_base = 100;
		}

		if(process_generation <1) {
			IJ.log("Process generation is "+String.valueOf(process_generation)+"\ngeneration should be more than 1");
		}
	}


	private double ranger0to1(double x) {
		if(x<0) {
			x = 0;
		}else if(1<x){
			x = 1;
		}
		return x;
	}

	public int getMaxPop() {
		return this.max_population;
	}

	public double getPrsvPrnt() {
		return this.preserve_parent_rate;
	}

	public double getInvGenm() {
		return this.invader_genom_rate;
	}

	public double getCarryOv() {
		return this.carry_over_rate;
	}

	public double getPertRate() {
		return this.perturbation_rate;
	}

	public double getPertBase() {
		return this.perturbation_base;
	}

	public double getCatasRate() {
		return this.catastrophe_rate;
	}

	public int getProcGenNum() {
		return this.process_generation;
	}

}
