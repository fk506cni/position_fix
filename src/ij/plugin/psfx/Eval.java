package ij.plugin.psfx;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageRoi;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.plugin.Duplicator;
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
	private Duplicator dup = new Duplicator();
	


	int x;
	int y;
	double theta;

	public void setRef(ImagePlus ref) {
		this.ref = ref;
		this.cim.setRefImp(this.ref);
	}

	public void setTag(ImagePlus tag) {
		this.tag = tag;


		this.canvas_x = tag.getWidth();
		this.canvas_y = tag.getHeight();


		this.crop_x[1] = this.canvas_x;
		this.crop_y[1] = this.canvas_y;
		IJ.run("Colors...", "foreground=black background=white selection=white");
	}

	public double getEval(int x, int y, double theta) {
		IJ.log("evalating genes on genome...");
		double score = 0.0;
		makeComp(x, y, theta);
		score = cim.Tag2Res(this.mv_tag);
		return score;
	}

	public void makeComp(int x, int y, double theta) {
//		this.ip = this.tag.duplicate().getProcessor();
		ImagePlus tag_tmp = this.dup.run(this.tag);
		ImageProcessor ip = tag_tmp.getProcessor();
		this.mv_tag = IJ.createImage("", "8-bit white",this.tag.getWidth() ,this.tag.getHeight(), 1);

		int[] crop_x = {this.crop_x[0], this.crop_x[1]};
		int[] crop_y = {this.crop_y[0], this.crop_y[1]};

		if(x <= 0) {
			crop_x[1] = crop_x[1] +x;
			//x= 0;
		}else {
			crop_x[0] = crop_x[0] -x;
		}

		if(y <= 0) {
			crop_y[1] = crop_y[1] +y;
			//y = 0;
		}else {
			crop_y[0] = crop_y[0] -y;
		}

/*		IJ.log("crop condition is...");
		ms.ints2ijlog(this.crop_x);
		ms.ints2ijlog(this.crop_y);
		ms.ints2ijlog(crop_x);
		ms.ints2ijlog(crop_y);
*/
		//crop out
		ip.setRoi(crop_x[0], crop_y[0], crop_x[1], crop_y[1]);
		Roi roi = new ImageRoi(x, y, ip.crop());

        Overlay overlayList = new Overlay();
        overlayList.add(roi);
        this.mv_tag.setOverlay(overlayList);
        this.mv_tag = this.mv_tag.flatten();
        IJ.run(this.mv_tag, "8-bit", "");

		//rotate
		IJ.run(this.mv_tag, "Rotate... ", "angle="+String.valueOf(theta)+" grid=1 interpolation=Bilinear fill");

/*		this.mv_tag.setTitle("mv_tag");
		this.mv_tag.show();

		this.tag.setTitle("tag is this");
		this.tag.show();
*/
	}

	public void showTag4Check() {
		this.mv_tag.setTitle("mv_tag");
		this.mv_tag.show();

		this.tag.setTitle("tag is this");
		this.tag.show();
		ms.ints2ijlog(this.crop_x);
		ms.ints2ijlog(this.crop_y);
	}

}
