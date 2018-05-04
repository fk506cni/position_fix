package ij.plugin.psfx;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;

public class Mode_ArgsGetter {
	private String[] modes = {"one2one", "one2multi", "SequentialFix","SequentialPair"};
	private GenericDialogPlus gdp = new GenericDialogPlus("Mode selection");

	private String mode ="";

	public void decideModeViaGUI() {
		this.gdp.addChoice("process mode", modes, modes[0]);
		this.gdp.showDialog();
		if (this.gdp.wasCanceled()) return;

		parseArgs();
		checkArgs();
	}

	public void parseArgs() {
		mode = this.gdp.getNextChoice();
		IJ.log("process mode: "+mode);
	}

	public void checkArgs() {
		if(mode =="") {
			IJ.log("mode not chosen.");
			return;
		}
	}

	public String getMode() {
		return this.mode;
	}
}
