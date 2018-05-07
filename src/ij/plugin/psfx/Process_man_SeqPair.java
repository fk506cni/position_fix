package ij.plugin.psfx;

import java.io.File;

import ij.IJ;

public class Process_man_SeqPair extends Process_manager{

	private String[] tag_files;
	private File[] tagfiles_asFile;
	private int target_number;

	private Args_Getter_SeqPair asp = new Args_Getter_SeqPair();

	@Override
	public void argsget() {
		asp.getArgsViaGUI();
		tagfiles_asFile = asp.getTargetFiles();
		this.target_number = asp.getTargetSize();

		if(asp.getGAtune()) {
			gat.getGAargViaGUI();
		}
	}

	@Override
	public void main() {
		argsget();
		IJ.log("begining loop fitting process.");

		for(int i =0; i<tagfiles_asFile.length  -1;i++) {
			agt = asp.getAGTi(i);
			prepareATimps();
			prepareCentimps();

			after();
//			this.ft.getFxTagImp().show();
//			lastTagATimp.show();

		}

		IJ.log("Seq Pair loop analysis was done.");

	}

}
