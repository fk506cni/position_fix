package ij.plugin.psfx;

import java.io.File;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;
//import ij.gui.GenericDialog;
import ij.plugin.PlugIn;

public class Args_Getter implements PlugIn{
	private String ref_image_string="D:\\pf_test\\ref.png";
	private File ref_image_file = new File("D:\\pf_test\\ref.png");
	private String tag_image_string="D:\\pf_test\\tag.png";
	private File tag_image_file = new File("D:\\pf_test\\tag.png");
	private String[] AT_choice={"Default","Huang","Huang2","Intermodes","IsoData","Li","MaxEntropy",
  		  "Mean","MinError(I)","Minimum","Moments","Otsu","Percentile","RenyiEntropy",
  		  "Shanbhag","Triangle","Yen"};
	private String ref_AT_method = "Default";
	private String tag_AT_method = "Default";
	private double roundL= -60;
	private double roundR= 60;
	private int round_gradient;
	private int searchL=100;

	private String output_dir="D:\\pf_test\\";
	private File output_dir_asfile = new File(output_dir);
	private String[] save_formats =  {"ZIP","PNG","Jpeg","Tiff","Gif"};
	private String save_format = "ZIP";
	private String result_prefix ="_corected_";
	private int random_seed = 114514;
	private boolean checkGAparam = false;

	private GenericDialogPlus gdp = new GenericDialogPlus("PF entry");

	private boolean isWin;

	public void getArgsViaGUI() {
		GenericDialogPlus gdp = this.gdp;
		gdp.addFileField("reference file", "");
		gdp.addChoice("ref AThr_meth",this.AT_choice , this.AT_choice[0]);

		gdp.addFileField("target file", "");
		gdp.addChoice("tag AThr_meth",this.AT_choice , this.AT_choice[0]);

		gdp.addNumericField("searchLengthL", this.searchL, 0);
		gdp.addNumericField("roundL(shouled be <=0)", this.roundL, 0);
		gdp.addNumericField("roundR(should be 0<=)", this.roundR, 0);

		gdp.addStringField("result_prefix", this.result_prefix);

		gdp.addChoice("SaveAs", this.save_formats, this.save_formats[0]);
		gdp.addDirectoryField("output dir", "");

		gdp.addNumericField("random_seed", this.random_seed, 0);

		gdp.addCheckbox("choice GA parameters", false);

		gdp.showDialog();

		if (gdp.wasCanceled()) return;

		this.gdp = gdp;

		parseArgs();
		checkSysOs();
	}

	public void parseArgs() {
		GenericDialogPlus gdp = this.gdp;

		this.ref_image_string = gdp.getNextString();
		this.ref_image_file = new File(this.ref_image_string);
		IJ.log(this.ref_image_string+": is reference image.");

		this.ref_AT_method = gdp.getNextChoice();
		IJ.log(this.ref_AT_method+": is ref AT method.");

		this.tag_image_string = gdp.getNextString();
		this.tag_image_file = new File(this.tag_image_string);
		IJ.log(this.tag_image_string+": is target image.");

		this.tag_AT_method = gdp.getNextChoice();
		IJ.log(this.tag_AT_method+": is tag AT method.");

		this.searchL = (int)gdp.getNextNumber();
		IJ.log(String.valueOf(this.searchL+"is searching length."));

		this.roundL = gdp.getNextNumber();
		IJ.log(String.valueOf(this.roundL+"is left search round"));

		this.roundR = gdp.getNextNumber();
		IJ.log(String.valueOf(this.roundR+"is right search round"));

		this.result_prefix = gdp.getNextString();
		IJ.log(this.result_prefix+": is result prefix");

		this.save_format = gdp.getNextChoice();
		IJ.log(this.save_format+": is format for save result");

		this.output_dir = gdp.getNextString();
		this.output_dir_asfile = new File(this.output_dir);
		IJ.log(this.output_dir+" is output directory");

		this.random_seed = (int)gdp.getNextNumber();
		IJ.log(String.valueOf(this.random_seed)+": is random seed");

		this.checkGAparam = gdp.getNextBoolean();
		IJ.log(String.valueOf(checkGAparam)+": is GA parameter choice");

	}

	public void setRefImg(String ref_img_path) {
		this.ref_image_string = ref_img_path;
		this.ref_image_file = new File(this.ref_image_string);
		IJ.log(this.ref_image_string+": is reference image.");
	}

	public void setTagImg(String tag_img_path) {
		this.tag_image_string = tag_img_path;
		this.tag_image_file = new File(this.tag_image_string);
		IJ.log(this.tag_image_string+": is target image.");
	}

	public void setOutputDir(String output_dir) {
		this.output_dir = output_dir;
		this.output_dir_asfile = new File(this.output_dir);
		IJ.log(this.output_dir+" is output directory");
	}

	public void setArgsViaCLmethod() {
		//not impled...yet
	}

	public void checkSysOs() {
		boolean result = "\\".equals(File.separator);
		if(result) {
			IJ.log("Detected System is Windows system.");
		}else {
			IJ.log("Detected System is Not Windows system.");
		}
		this.isWin = result;
	}


	public void run(String arg){
		getArgsViaGUI();

	}

	public String getRefATmt() {
		return this.ref_AT_method;
	}

	public String getTagATmt() {
		return this.tag_AT_method;
	}

	public File getRefImg() {
		return this.ref_image_file;
	}

	public File getTagImg() {
		return this.tag_image_file;
	}

	public int getSearchL() {
		return this.searchL;
	}

	public double[] getThetaLR() {
		double[] result = {this.roundL, this.roundR};
		return result;
	}

	public int getSeed() {
		return this.random_seed;
	}

	public File getOutDirAsFile() {
		return this.output_dir_asfile;
	}

	public String getPrefix() {
		return this.result_prefix;
	}

	public String getSaveAs() {
		return this.save_format;
	}
}
