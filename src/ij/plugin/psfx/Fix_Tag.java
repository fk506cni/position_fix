package ij.plugin.psfx;
import ij.IJ;
//fixation tag image
import ij.ImagePlus;
public class Fix_Tag {
	private Args_Getter agt;
	private BinCent_Info bc;
	private prepareATimp_ pat;
	private Eval ev;

	ImagePlus tag;
	ImagePlus fx = new ImagePlus();

	//size of reference
	int wid;
	int hei;
	int xcent;
	int ycent;
	int xcentM;
	int ycentM;
	int dx;
	int dy;
	double dt;

	public void setSuportClass(Args_Getter agt, BinCent_Info bc, prepareATimp_ pat, Eval ev) {
		this.agt = agt;
		this.bc= bc;
		this.pat = pat;
		this.ev = ev;

		this.wid = bc.getRefSize()[0];
		this.hei = bc.getRefSize()[1];

		this.tag = IJ.openImage(this.agt.getTagImg().getAbsolutePath());
	}

	public void setTagImp(ImagePlus tagImp) {
		this.tag=tagImp;
	}

	/*
	public void setImpPram(int wid, int hei, int xcent, int ycent, int xcentM, int ycentM) {
		this.wid = wid;
		this.hei = hei;
		this.xcent = xcent;
		this.ycent = ycent;
		this.xcentM = xcentM;
		this.ycentM = ycentM;
	}*/

	public void setBestGenome(Genome_ga best1) {
		this.dx = best1.getGeneX();
		this.dy = best1.getGeneY();
		this.dt = best1.getGeneTheta();
	}

	public void main() {
		//tag.show();
		//1st tag centlization
		fx = pat.putImpIn(tag, pat.tag_putinSize, pat.tag_centM).flatten();
		//fx.show();
		//2nd dx, dy and dt
		fx = this.ev.getMVImp(fx, dx, dy, dt);
//		ImagePlus fx2 = this.ev.getMVImp(fx, dx, dy, dt);
//		fx2.show();

		//3rd re crop
		fx = this.pat.reverseImpOut(fx, this.pat.ref_centM, this.pat.ref_size);
//		ImagePlus fx3 = this.pat.reverseImpOut(fx2, this.pat.ref_centM, this.pat.ref_size);
//		fx3.show();
		fx.show();
		}
}
