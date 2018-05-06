package ij.plugin.psfx;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageRoi;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.process.ImageProcessor;

public class prepareATimp_{
	private ImagePlus refAT;
	private ImagePlus tagAT;
	private ImagePlus refFXimp;
	private ImagePlus tagFXimp;
	private BinCent_Info bc;
	private Mscs_ ms =new Mscs_();
	private int add_margin = 150;

	protected int[] ref_size;
	protected int[] ref_cent;
	protected int[] ref_centM;

	protected int[] tag_size;
	protected int[] tag_putinSize;
	protected int[] tag_cent;
	protected int[] tag_centM;




	public void setBinCent(BinCent_Info bc) {
		this.bc = bc;
	}

	public void setAddMargin(int add_margin) {
		this.add_margin = add_margin;
	}

	public void overrideRefFxImp(ImagePlus newRef) {
		this.refFXimp = newRef;
	}

	public void FixImp() {
		ImagePlus refFx = new ImagePlus();
		ref_size = bc.getRefSize();
		ref_cent = bc.getRefCent();
		ref_centM = bc.getRefCentM();
		refFx = prepCentImp(this.bc.getBinRefImp(), ref_size, ref_cent, ref_centM).flatten();
		IJ.run(refFx, "8-bit", "");
		this.refFXimp = refFx;
		//refFx.show();

		ImagePlus tagFx = new ImagePlus();

		tag_putinSize = new int[]{refFx.getWidth(), refFx.getHeight()};
		tag_size = bc.getTagSize();
		tag_cent = bc.getTagCent();
		tag_centM = bc.getTagCentM();
		//tagFx = prepCentImp(this.bc.getBinTagImp(), tag_size, tag_cent, tag_centM);
		tagFx = putImpIn(this.bc.getBinTagImp(),tag_putinSize, tag_centM, "black").flatten();
		IJ.run(tagFx, "8-bit", "");
		//tagFx.show();
		this.tagFXimp = tagFx;
		}



	private ImagePlus prepCentImp(ImagePlus ATimp, int[] size, int[] cent, int[] centM){
		IJ.log("preparing imp");
		IJ.log("size is ");
		ms.ints2ijlog(size);
		IJ.log("center is ");
		ms.ints2ijlog(cent);
		IJ.log("center of Mass is ");
		ms.ints2ijlog(centM);

		int XL = size[0];
		int YL = size[1];
		int centx = cent[0];
		int centy = cent[1];
		int centMx = centM[0];
		int centMy = centM[1];

		int dx = centMx - centx;
		int dy = centMy - centy;

		IJ.log("center dx and dy is");
		ms.int2ijlog(dx);
		ms.int2ijlog(dy);

		int FxL = 0;
		int FyL = 0;
		int xPos = 0;
		int yPos = 0;

		if(dx >= 0) {
			FxL = 2 * centMx;
			xPos = 0;
		}else if(dx < 0) {
			FxL = 2 *(XL - centMx);
			xPos = XL - ( 2* centMx);
		}
		IJ.log("FxL, xPos, centMx, centx, dx is");
		int[] designX = {FxL, xPos, centMx, centx, dx};
		ms.ints2ijlog(designX);

		if(dy >= 0) {
			FyL = 2 * centMy;
			yPos = 0;
		}else if(dy < 0) {
			FyL = 2 *(YL - centMy);
			yPos = YL - (2 * centMy);
		}

		IJ.log("FyL, yPos, centMy, centy, dy is");
		int[] designY = {FyL, yPos, centMy, centy, dy};
		ms.ints2ijlog(designY);

		ImagePlus imp = IJ.createImage("", "8-bit black", FxL+2*this.add_margin, FyL+2*this.add_margin, 1);
		//imp.show();
		int x=0, y=0;
        x = xPos+this.add_margin;
        y = yPos+this.add_margin;

        int[] pm = {x, y};
        ms.ints2ijlog(pm);
        ImagePlus overlay = ATimp;
        Roi roi = new ImageRoi(x, y, overlay.getProcessor());
        Overlay overlayList = new Overlay();
        overlayList.add(roi);
        imp.setOverlay(overlayList);
		return imp;
	}

	public ImagePlus putImpIn(ImagePlus ATimp, int[] size_ofPallette, int[] centM_ofImp, String bgcolor) {
		IJ.log("put imp in. bg color is "+bgcolor);

		IJ.run("Colors...", "foreground=black background="+bgcolor+" selection=black");
		ImagePlus overlay = ATimp;

		int ATx = ATimp.getWidth();
		int ATy = ATimp.getHeight();

		int[] cropX = RangeDesign(size_ofPallette[0],ATx, centM_ofImp[0]);
		int[] cropY = RangeDesign(size_ofPallette[1],ATy, centM_ofImp[1]);

		int x = cropX[0];
		int rwidth = cropX[1] -x;
		int xPos = cropX[2];

		int y = cropY[0];
		int rheight = cropY[1] -y;
		int yPos = cropY[2];

		ms.ints2ijlog(cropX);
		ms.ints2ijlog(cropY);

		ImageProcessor ip = overlay.getProcessor();
		//ip.setInterpolationMethod(ImageProcessor.BILINEAR);
		ip.setRoi(x, y, rwidth, rheight);

		Roi roi = new ImageRoi(xPos, yPos, ip.crop());
		ImagePlus imp = IJ.createImage("", "RGB "+bgcolor, size_ofPallette[0], size_ofPallette[1], 1);

        Overlay overlayList = new Overlay();
        overlayList.add(roi);
        imp.setOverlay(overlayList);
		return imp;
	}





	public ImagePlus reverseImpOut(ImagePlus imp,  int[] ref_centM, int[] ref_size) {
		IJ.log("reverse imp out.");


		int[] imp_cent = {(int)(imp.getWidth()/2), (int)(imp.getHeight()/2)};

//		int x = imp_cent[0] - ref_centM[0]+this.add_margin;
//		int y = imp_cent[1] - ref_centM[1]+this.add_margin;
		int x = imp_cent[0] - ref_centM[0];
		int y = imp_cent[1] - ref_centM[1];
		int wid = ref_size[0];
		int hei = ref_size[1];

		int[] condition = {x, y, wid, hei};
		IJ.log("crop condition is below. x, y, wid, hei.");
		ms.ints2ijlog(condition);

		imp.setRoi(x, y, wid, hei);
		IJ.run(imp, "Crop", "");
		return imp;
	}

	private int[] RangeDesign(int put_size, int org_size, int centM) {
		int l =0, r = org_size, pos =0;
		int half = (int)(put_size/2);

		l = Math.max(0,
				(centM - half));
		r = Math.min(org_size,
				(centM + half));
		pos = Math.max(0,
				(half - centM));

		int[] result = {l, r, pos};
		return result;
	}


	public ImagePlus getFxRefImp() {
		return this.refFXimp;
	}

	public ImagePlus getFxTagImp() {
		return this.tagFXimp;
	}


}

