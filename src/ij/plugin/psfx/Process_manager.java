package ij.plugin.psfx;

import ij.IJ;
import ij.ImagePlus;

public class Process_manager {
	protected Args_Getter agt = new Args_Getter();
	protected BinCent_Info bc = new BinCent_Info();
	protected prepareATimp_ pat = new prepareATimp_();
	protected compare_Imps cim = new compare_Imps();
	protected Eval ev = new Eval();
	protected Genomic_Algorithm ga = new Genomic_Algorithm();
	protected Fix_Tag ft = new Fix_Tag();
	protected Imps_Save isv = new Imps_Save();
	protected GAargs_Getter gat = new GAargs_Getter();

	protected ImagePlus lastTagATimp;

	public void argsget() {
		IJ.log("getting args...");
		this.agt.getArgsViaGUI();

		if(agt.getGAtune()) {
			gat.getGAargViaGUI();
		}
	}

	public void prepareATimps() {
		IJ.log("preparing AT imps...");
		IJ.run("Colors...", "foreground=black background=black selection=black");
		this.bc.setArgs(this.agt);
		this.bc.prepareInfo();
	}

	public void prepareCentimps() {
		this.pat.setBinCent(this.bc);
		this.pat.setAddMargin(agt.getAddMargin());
		this.pat.FixImp();
	}

	public void after() {
		this.isv.setAGT(this.agt);
		this.isv.saveImps(this.pat.getFxRefImp(), "_ref_");
		this.isv.saveImps(this.pat.getFxTagImp(), "_tag_");
//		this.cim.setRefImp(pat.getFxRefImp());
//		this.cim.setTagImp(pat.getFxTagImp());
//		cim.comp2imp();

		this.ev.setRef(pat.getFxRefImp());
		this.ev.setTag(pat.getFxTagImp());

//		this.ev.makeComp(50, 50, 45.0);
//		this.ev.makeComp(-100, -100, -5.0);

		//double res = this.ev.getEval(-100, -100, -5.0);
		//IJ.log(String.valueOf(res));


		this.ga.setAGT(this.agt);
		this.ga.setPAM(this.pat);
		this.ga.setGAargsGetter(gat);
		this.ga.setRefTag(this.pat.getFxRefImp(), this.pat.getFxTagImp());
		this.ga.main();

		this.ft.setSuportClass(this.agt, this.bc, this.pat, this.ev);
//		this.ft.setTagImp();
		this.ft.setBestGenome(this.ga.getLastBest());
		this.ft.main();
//		this.ft.getFxTagImp().show();

		//check last best bin
		lastTagATimp = this.ev.getMvImp(this.ga.getLastBest());

		this.isv.saveImps(ft.getFxTagImp(), "final_tag");

	}

	public void main() {
		argsget();
		prepareATimps();
		prepareCentimps();
		after();
		this.ft.getFxTagImp().show();
		lastTagATimp.show();

		IJ.log("one2one process done.");
	}

}
