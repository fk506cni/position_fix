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



	public void setBinCent(BinCent_Info bc) {
		this.bc = bc;
	}

	public void FixImp() {
		ImagePlus refFx = new ImagePlus();
		int[] ref_size = bc.getRefSize();
		int[] ref_cent = bc.getRefCent();
		int[] ref_centM = bc.getRefCentM();
		refFx = prepCentImp(this.bc.getBinRefImp(), ref_size, ref_cent, ref_centM);
		refFx.show();

		ImagePlus tagFx = new ImagePlus();

		int[] tag_putinSize = {refFx.getWidth(), refFx.getHeight()};
		int[] tag_size = bc.getTagSize();
		int[] tag_cent = bc.getTagCent();
		int[] tag_centM = bc.getTagCentM();
		//tagFx = prepCentImp(this.bc.getBinTagImp(), tag_size, tag_cent, tag_centM);
		tagFx = putImpIn(this.bc.getBinTagImp(),tag_putinSize, tag_centM);
		tagFx.show();
		}

	private ImagePlus prepCentImp(ImagePlus ATimp, int[] size, int[] cent, int[] centM){
		IJ.log("prep_imp");
		ms.ints2ijlog(size);
		ms.ints2ijlog(cent);
		ms.ints2ijlog(centM);
		int XL = size[0];
		int YL = size[1];
		int centx = cent[0];
		int centy = cent[1];
		int centMx = centM[0];
		int centMy = centM[1];

		int dx = centMx - centx;
		int dy = centMy - centy;

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

		if(dy >= 0) {
			FyL = 2 * centMy;
			yPos = 0;
		}else if(dy < 0) {
			FyL = 2 *(YL - centMy);
			yPos = YL - (2 * centMy);
		}

		ImagePlus imp = IJ.createImage("", "8-bit white", FxL, FyL, 1);
		imp.show();
		int x=0, y=0;
        x = xPos;
        y = yPos;
        ImagePlus overlay = ATimp;
        Roi roi = new ImageRoi(x, y, overlay.getProcessor());
        Overlay overlayList = new Overlay();
        overlayList.add(roi);
        imp.setOverlay(overlayList);
		return imp;
	}

	private ImagePlus putImpIn(ImagePlus ATimp, int[] size_ofPallette, int[] centM_ofImp) {
		IJ.log("put imp in.");
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
		ImagePlus imp = IJ.createImage("", "8-bit white", size_ofPallette[0], size_ofPallette[1], 1);

        Overlay overlayList = new Overlay();
        overlayList.add(roi);
        imp.setOverlay(overlayList);
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

