package ij.plugin.psfx;

import ij.ImagePlus;
import ij.measure.Measurements;
import ij.plugin.ImageCalculator;

public class compare_Imps {
	private ImagePlus refATimp = new ImagePlus();
	private ImagePlus tagATimp = new ImagePlus();
	private ImagePlus mrgImp = new ImagePlus();
	private Anal2Result_ a2r;
	private Mscs_ ms = new Mscs_();
	private double result_db;
	private int result;

	ImageCalculator ic = new ImageCalculator();
	int[] results;
	double[] result_dbs;

	public void setRefImp(ImagePlus ref) {
		this.refATimp = ref;
	}

	public void setTagImp(ImagePlus tag) {
		this.tagATimp = tag;
	}

	public void comp2imp(boolean withShow) {

//		int[] result;
//		double[] result_dbs;
/*		if(!check2imps()) {
			return;
		}
		*/

		//IJ.log("xor images.");
		//this.refATimp.show();
		//this.tagATimp.show();


		this.mrgImp = ic.run("XOR create", this.refATimp, this.tagATimp);

		if(withShow) {
			this.mrgImp.show();
		}
		this.a2r = new Anal2Result_(this.mrgImp);

		result_dbs = a2r.getDblValsArray(Measurements.MEAN);
		this.result_db = result_dbs[0];

	}

	public double Tag2Res(ImagePlus tag, boolean withShow) {
		setTagImp(tag);
		comp2imp(withShow);
		return this.result_db;
	}

	public double Tag2ResSafe(ImagePlus tag, boolean withShow) {
		ImageCalculator ic2 = new ImageCalculator();
		ImagePlus mrg = ic2.run("XOR create", this.refATimp,tag);

		if(withShow) {
			mrg.show();
		}

		Anal2Result_ a2r2 = new Anal2Result_(mrg);
		double result = a2r2.getDblValsArray(Measurements.MEAN)[0];

		return result;
	}

	public ImagePlus getMergedImp() {
		return this.mrgImp;
	}

	private boolean check2imps() {
		boolean result_bool = false;

		return result_bool;
	}

}
