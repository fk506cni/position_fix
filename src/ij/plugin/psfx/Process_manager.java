package ij.plugin.psfx;

import ij.IJ;

public class Process_manager {
	private Args_Getter agt = new Args_Getter();

	public void main() {
		IJ.log("getting args...");
		this.agt.getArgsViaGUI();

	}

}
