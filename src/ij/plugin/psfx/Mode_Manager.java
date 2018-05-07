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
			Process_man_one2multi p2m = new Process_man_one2multi();
			p2m.main();
		}else if(mode =="SequentialFix") {
			Process_man_SeqFx psf = new Process_man_SeqFx();
			psf.main();
		}else if(mode =="SequentialPair") {

		}else {
			IJ.log("unkown mode specified.");
			return;
		}
	}

}
//,"SequentialPair"