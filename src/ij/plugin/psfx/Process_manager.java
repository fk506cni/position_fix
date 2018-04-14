package ij.plugin.psfx;

import ij.IJ;

public class Process_manager {
	private Args_Getter agt = new Args_Getter();
	private BinCent_Info bc = new BinCent_Info();
	private prepareATimp_ pat = new prepareATimp_();

	public void main() {
		IJ.log("getting args...");
		//this.agt.getArgsViaGUI();

		this.bc.setArgs(this.agt);
		this.bc.prepareInfo();

		this.pat.setBinCent(this.bc);
		this.pat.FixImp();


	}

}
