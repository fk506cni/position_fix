package ij.plugin.psfx;

import java.io.File;

import ij.IJ;
import ij.ImagePlus;

public class Process_man_SeqFx extends Process_manager{
	private Args_Getter_SeqFx asf = new Args_Getter_SeqFx();
	private String[] tag_files;
	private File[] tagfiles_asFile;
	private int target_number;
	private ImagePlus nextBinRef;

	@Override
	public void argsget() {
		asf.getArgsViaGUI();
		tagfiles_asFile = asf.getTargetFiles();
		this.target_number = asf.getTargetSize();

		if(asf.getGAtune()) {
			gat.getGAargViaGUI();
		}
	}

	@Override
	public void main() {
		argsget();
		IJ.log("begining loop fitting process.");

		for(int i =0; i<tagfiles_asFile.length  -1;i++) {
			agt = asf.getAGTi(i);
			prepareATimps();
			prepareCentimps();

			if(i!=0) {
				this.pat.overrideRefFxImp(nextBinRef);
			}

			after();
//			this.ft.getFxTagImp().show();
//			lastTagATimp.show();

			nextBinRef = this.ev.getMvImp(this.ga.getLastBest());

		}

		IJ.log("Seq Fx loop analysis was done.");

	}

	public void exchangeBinRef() {

	}
}
