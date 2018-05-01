package ij.plugin.psfx;

import java.io.File;

import ij.IJ;
import ij.ImagePlus;

public class Imps_Save {
	private Args_Getter agt;
	private File output_dir;
	private String prefix = "";
	private String saveAs = "";
	private String tag_tag ="";

	public void setAGT(Args_Getter agt) {
		this.agt= agt;
		this.output_dir = agt.getOutDirAsFile();
		this.prefix = agt.getPrefix();
		this.saveAs = agt.getSaveAs();
		this.tag_tag = agt.getTagWord();
	}

	public void saveImps(ImagePlus imp, String mark) {
		String sepStr = File.separator;
		String outfile = output_dir.getAbsolutePath()+sepStr+tag_tag+prefix+mark;
		IJ.saveAs(imp, this.saveAs, outfile);
		IJ.log("saving at "+outfile);
	}



}
