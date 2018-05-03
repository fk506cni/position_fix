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
	private Args_Getter agt;
	private int add_margin;

	private static int evalcount=0;

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
		IJ.run("Colors...", "foreground=black background=black selection=black");
	}

	public void setAgt(Args_Getter agt) {
		this.agt = agt;
		this.add_margin = agt.getAddMargin();
	}

	public double getEval(int x, int y, double theta) {
		//IJ.log("evalating genes on genome...");
		double score = 0.0;
		ImagePlus mv_tag = makeComp(x, y, theta);
		//score = cim.Tag2Res(mv_tag, false);
		score = cim.Tag2ResSafe(mv_tag, false);
		evalcount++;
		IJ.log("evaluation count is "+String.valueOf(evalcount));

		return score;
	}

	public double getGenomeEval(Genome_ga ga1) {
		double score;
		if(ga1.getEval() == 0.0) {
			int x = ga1.getGeneX();
			int y = ga1.getGeneY();
			double theta = ga1.getGeneTheta();
			score = getEval(x, y, theta);
		}else {
			score = ga1.getEval();
		}
		return score;

	}

	public Genome_ga getAssesedGenome(Genome_ga ga1) {
		double score;
		if(ga1.getEval() == 0.0) {
			int x = ga1.getGeneX();
			int y = ga1.getGeneY();
			double theta = ga1.getGeneTheta();
			score = getEval(x, y, theta);
			ga1.setEval(score);
			}
		return ga1;
	}

	public double getshowEval(int x, int y, double theta) {
		//IJ.log("evalating genes on genome...");
		double score = 0.0;
		ImagePlus mv_tag = makeComp(x, y, theta);
		score = cim.Tag2Res(mv_tag, true);
		return score;
	}

	public ImagePlus getEvalImp(int x, int y, double theta) {
		ImagePlus mv_tag = makeComp(x, y, theta);
		double score = cim.Tag2Res(mv_tag, false);
		return cim.getMergedImp();
	}

	public ImagePlus makeComp(int x, int y, double theta) {
//		this.ip = this.tag.duplicate().getProcessor();
		ImagePlus tag_tmp = this.dup.run(this.tag);
		ImageProcessor ip = tag_tmp.getProcessor();

		ImagePlus mv_tag = IJ.createImage("", "8-bit black",this.tag.getWidth() ,this.tag.getHeight(), 1);

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
        mv_tag.setOverlay(overlayList);
        mv_tag = mv_tag.flatten();
        IJ.run(mv_tag, "8-bit", "");

		//rotate
		IJ.run(mv_tag, "Rotate... ", "angle="+String.valueOf(theta)+" grid=1 interpolation=Bilinear fill");

		return mv_tag;


/*		this.mv_tag.setTitle("mv_tag");
		this.mv_tag.show();


		this.tag.setTitle("tag is this");
		this.tag.show();
*/
	}

	public ImagePlus getMVImp(ImagePlus imp, int x, int y, double theta) {
		//imp.setTitle("pre fix pos and axis");
		//imp.show();
		IJ.log("getMVimp. \nx ="+String.valueOf(x)+"\ny="+String.valueOf(y)+"\ntheta="+String.valueOf(theta));
		ImageProcessor ipp = imp.getProcessor();
		ImagePlus mvd = IJ.createImage("", "RGB white", imp.getWidth(), imp.getHeight(), 1);
		int[] crop_x = {0, imp.getWidth()};
		int[] crop_y = {0, imp.getHeight()};

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
		IJ.log("getmvimp s crop x y is");
		ms.ints2ijlog(crop_x);
		ms.ints2ijlog(crop_y);
		int[] param = {x, y};
		IJ.log("x, y is");
		ms.ints2ijlog(param);


		ipp.setRoi(crop_x[0], crop_y[0], crop_x[1], crop_y[1]);
		Roi roi = new ImageRoi(x, y, ipp.crop());
		//Roi roi = new ImageRoi(x+this.add_margin, y+this.add_margin, ipp.crop());
		Overlay overlayList = new Overlay();
        overlayList.add(roi);

        mvd.setOverlay(overlayList);
        mvd = mvd.flatten();
//        mvd.show();


        IJ.run(mvd, "Rotate... ", "angle="+String.valueOf(theta)+" grid=1 interpolation=Bilinear fill");
        mvd = mvd.flatten();
//        mvd.show();

        return mvd;

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
