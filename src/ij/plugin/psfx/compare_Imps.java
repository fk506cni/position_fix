package ij.plugin.psfx;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.Measurements;
import ij.plugin.ImageCalculator;

public class compare_Imps {
	private ImagePlus refATimp = new ImagePlus();
	private ImagePlus tagATimp = new ImagePlus();
	private ImagePlus mrgImp = new ImagePlus();
	private Anal2Result_ a2r;
	private Mscs_ ms = new Mscs_();

	public void setRefImp(ImagePlus ref) {
		this.refATimp = ref;
	}

	public void setTagImp(ImagePlus tag) {
		this.tagATimp = tag;
	}

	public void comp2imp() {
		int[] result;
		double[] result_db;

/*		if(!check2imps()) {
			return;
		}
		*/

		IJ.log("xor images.");
		this.refATimp.show();
		this.tagATimp.show();
		ImageCalculator ic = new ImageCalculator();

		this.mrgImp = ic.run("XOR create", this.refATimp, this.tagATimp);
		this.mrgImp.show();
		this.a2r = new Anal2Result_(this.mrgImp);
		result = a2r.getIntValsArray(Measurements.MEAN);
		result_db = a2r.getDblValsArray(Measurements.MEAN);

		ms.ints2ijlog(result);
		ms.db2ijlog(result_db);

//		return result;
	}

	private boolean check2imps() {
		boolean result_bool = false;

		return result_bool;
	}

}
