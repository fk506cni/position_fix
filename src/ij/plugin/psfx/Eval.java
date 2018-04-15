package ij.plugin.psfx;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageRoi;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.ImageCalculator;
import ij.process.ImageProcessor;

public class Eval {
	private ImagePlus ref;
	private ImagePlus tag;
	private ImagePlus mv_tag = new ImagePlus();
	private ImageCalculator ic = new ImageCalculator();
	private ImageProcessor ip;
	private int canvas_x = 0;
	private int canvas_y = 0;
	private int[] crop_x = {0,0};
	private int[] crop_y = {0,0};

	private Mscs_ ms = new Mscs_();
	private compare_Imps cim = new compare_Imps();


	int x;
	int y;
	double theta;

	public void setRef(ImagePlus ref) {
		this.ref = ref;
		this.cim.setRefImp(this.ref);
	}

	public void setTag(ImagePlus tag) {
		this.tag = tag;
		this.ip = this.tag.getProcessor();

		this.canvas_x = tag.getWidth();
		this.canvas_y = tag.getHeight();

		this.mv_tag = IJ.createImage("", "8-bit white",tag.getWidth() ,tag.getHeight(), 1);

		this.crop_x[1] = this.canvas_x;
		this.crop_y[1] = this.canvas_y;
		IJ.run("Colors...", "foreground=black background=white selection=white");
	}

	public double getEval(int x, int y, double theta) {
		double score = 0.0;
		makeComp(x, y, theta);
		score = cim.Tag2Res(this.mv_tag);
		return score;
	}

	public void makeComp(int x, int y, double theta) {
		if(x <= 0) {
			this.crop_x[1] = this.crop_x[1] +x;
			//x= 0;
		}else {
			this.crop_x[0] = this.crop_x[0] -x;
		}

		if(y <= 0) {
			this.crop_y[1] = this.crop_y[1] +y;
			//y = 0;
		}else {
			this.crop_y[0] = this.crop_y[0] -y;
		}
		//crop out
		this.ip.setRoi(this.crop_x[0], this.crop_y[0], this.crop_x[1], this.crop_y[1]);
		Roi roi = new ImageRoi(x, y, ip.crop());

        Overlay overlayList = new Overlay();
        overlayList.add(roi);
        this.mv_tag.setOverlay(overlayList);
        this.mv_tag = this.mv_tag.flatten();
        IJ.run(this.mv_tag, "8-bit", "");

        IJ.log(String.valueOf(theta));

		//rotate
		IJ.run(this.mv_tag, "Rotate... ", "angle="+String.valueOf(theta)+" grid=1 interpolation=Bilinear fill");

		//this.mv_tag.show();

	}

}
