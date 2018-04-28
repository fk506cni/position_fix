package ij.plugin.psfx;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.Measurements;

public class BinCent_Info {
	protected ImagePlus imp_ref;
	protected ImagePlus imp_tag;
	protected ImagePlus impAT_ref;
	protected ImagePlus impAT_tag;


	private int[] ref_size = new int[2];
	private int[] tag_size = new int[2];

	private int[] ref_cent = new int[2];
	private int[] tag_cent = new int[2];
	private int[] ref_centM = new int[2];
	private int[] tag_centM = new int[2];

	private parseBinImp2Res_ pbi;

	private Args_Getter agt;
	private Mscs_ ms = new Mscs_();
	private Anal2Result_ a2r;


	public void setArgs(Args_Getter agt) {
		this.agt = agt;
	}

	public void prepareInfo() {
		parseImg();
		this.ref_size = parseSizeInts(this.imp_ref);
		this.ref_cent = parseCentInts(this.imp_ref);
		this.impAT_ref = parseATimg(this.imp_ref, this.agt.getRefATmt());
		//this.impAT_ref.show();
		this.a2r = new Anal2Result_(this.imp_ref);
		this.ref_centM = a2r.getIntValsArray(Measurements.CENTER_OF_MASS);

		IJ.log("ref size, cent, centM");
		ms.ints2ijlog(this.ref_size);;
		ms.ints2ijlog(this.ref_cent);
		ms.ints2ijlog(this.ref_centM);


		this.tag_size = parseSizeInts(this.imp_tag);
		this.tag_cent = parseCentInts(this.imp_tag);
		this.impAT_tag = parseATimg(this.imp_tag, this.agt.getTagATmt());
		//this.impAT_tag.show();
		this.a2r = new Anal2Result_(this.imp_tag);
		this.tag_centM = a2r.getIntValsArray(Measurements.CENTER_OF_MASS);

		IJ.log("tag size, cent, centM");
		ms.ints2ijlog(this.tag_size);
		ms.ints2ijlog(this.tag_cent);
		ms.ints2ijlog(this.tag_centM);
/*		this.pbi = new parseBinImp2Res_(this.impAT_ref);
		this.ref_cent = this.pbi.getCent();
		IJ.log("cent is");
		this.ms.ints2ijlog(this.ref_cent);
*/
	}

	private void parseImg() {
		this.imp_ref = IJ.openImage(this.agt.getRefImg().getAbsolutePath());
		IJ.run(this.imp_ref, "Set Scale...", "distance=0 global");
		//this.imp_ref.show();
		this.imp_tag = IJ.openImage(this.agt.getTagImg().getAbsolutePath());
		IJ.run(this.imp_tag, "Set Scale...", "distance=0 global");
		//this.imp_tag.show();

	}

	private ImagePlus parseATimg(ImagePlus original_imp, String ATmethod) {
		ImagePlus imp = original_imp;
		IJ.run(imp, "8-bit", "");
		IJ.run(imp, "Invert", "");
		IJ.setAutoThreshold(imp, ATmethod);
		ij.Prefs.blackBackground = true;
		IJ.run(imp, "Convert to Mask", "only");
		return imp;
	}

	public int[] parseSizeInts(ImagePlus imp) {
		int wid = imp.getWidth();
		int hei = imp.getHeight();
		int[] result = {wid, hei};
		return result;
	}

	public int[] parseCentInts(ImagePlus imp) {
		int x_cent = imp.getWidth() / 2;
		int y_cent = imp.getHeight() /2;
		int[] result = {x_cent, y_cent};
		return result;
	}

	public ImagePlus getOrgRefImp() {
		return this.imp_ref;
	}

	public ImagePlus getOrgTagImp() {
		return this.imp_tag;
	}

	public ImagePlus getBinRefImp() {
		return this.impAT_ref;
	}

	public ImagePlus getBinTagImp() {
		return this.impAT_tag;
	}

	public int[] getRefSize() {
		return this.ref_size;
	}

	public int[] getRefCent() {
		return this.ref_cent;
	}

	public int[] getRefCentM() {
		return this.ref_centM;
	}

	public int[] getTagSize() {
		return this.tag_size;
	}

	public int[] getTagCent() {
		return this.tag_cent;
	}

	public int[] getTagCentM() {
		return this.tag_centM;
	}
}
