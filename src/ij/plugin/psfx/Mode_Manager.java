package ij.plugin.psfx;

import ij.IJ;

public class Mode_Manager {
	private Mode_ArgsGetter mag = new Mode_ArgsGetter();
	String mode ="";

	public void main() {
		mag.decideModeViaGUI();
		mode = mag.getMode();
		IJ.log("processing mode: "+mode);

		if(mode == "one2one") {
			Process_manager prm = new Process_manager();
			prm.main();
		}else if(mode =="one2multi") {

		}
	}

}
