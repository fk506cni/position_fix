package ij.plugin.psfx;

import java.io.File;

import ij.IJ;

public class Process_man_one2multi extends Process_manager{
	private Args_Getter12m a2m = new Args_Getter12m();
//	private Args_Getter agt;
	private String[] tag_files;
	private File[] tagfiles_asFile;

	@Override
	public void argsget() {
		a2m.getArgsViaGUI();
		tagfiles_asFile = a2m.getTargetFiles();
		this.agt = a2m.createAGT0();

		if(a2m.getGAtune()) {
			gat.getGAargViaGUI();
		}
	}


	@Override
	public void main() {
		argsget();
		IJ.log("begining loop fitting process.");

		for(int i =0; i<tagfiles_asFile.length;i++) {
			IJ.log(tagfiles_asFile[i]+" will be processed.");
			agt.setTagImgAsFile(tagfiles_asFile[i]);
			prepareATimps();
			prepareCentimps();
			after();
//			this.ft.getFxTagImp().show();
//			lastTagATimp.show();
		}

		IJ.log("one2multi loop analysis was done.");

	}

}
