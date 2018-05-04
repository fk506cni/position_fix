package ij.plugin.psfx;

import java.io.File;

import fiji.util.gui.GenericDialogPlus;
import ij.IJ;

public class Args_Getter12m extends Args_Getter{
	private String tag_dir = "";
	private File tag_dir_asFile;
	private File[] tag_files;
	private String[] tag_filewords;
	private int target_number;

	private Args_Getter ag0 = new Args_Getter();

	@Override
	public void getArgsViaGUI() {
		GenericDialogPlus gdp = this.gdp;
		gdp.addFileField("reference file", "");
		gdp.addChoice("ref AThr_meth",this.AT_choice , this.AT_choice[2]);

		gdp.addDirectoryField("target dir", "");
		gdp.addChoice("tag AThr_meth",this.AT_choice , this.AT_choice[2]);

		gdp.addNumericField("searchLengthL", this.searchL, 0);
		gdp.addNumericField("roundL(shouled be <=0)", this.roundL, 0);
		gdp.addNumericField("roundR(should be 0<=)", this.roundR, 0);

		gdp.addStringField("result_prefix", this.result_prefix);

		gdp.addChoice("SaveAs", this.save_formats, this.save_formats[0]);
		gdp.addDirectoryField("output dir", "");

		gdp.addNumericField("random_seed", this.random_seed, 0);

		gdp.addCheckbox("tune GA parameters", false);

		gdp.showDialog();

		if (gdp.wasCanceled()) return;

		this.gdp = gdp;

		parseArgs();
		checkSysOs();
		checkArgs();
	}

	@Override
	public void parseArgs() {
		GenericDialogPlus gdp = this.gdp;

		this.ref_image_string = gdp.getNextString();
		this.ref_image_file = new File(this.ref_image_string);
		IJ.log(this.ref_image_string+": is reference image.");

		this.ref_AT_method = gdp.getNextChoice();
		IJ.log(this.ref_AT_method+": is ref AT method.");

		this.tag_dir = gdp.getNextString();
		this.tag_dir_asFile = new File(tag_dir);
		IJ.log(this.tag_dir_asFile.getPath()+": target directory.");

		this.tag_files = tag_dir_asFile.listFiles();
		this.tag_filewords = new String[this.tag_files.length];
		this.target_number = this.tag_files.length;

		IJ.log("target files are below.");
		for(int i =0; i<this.tag_files.length;i++) {
			tag_filewords[i] = tag_files[i].getName().replaceAll("\\.[a-z0-9]*$", "");;
			IJ.log("path: "+tag_files[i].getPath());
			IJ.log("target word: "+tag_filewords[i]);
		}

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

	public File[] getTargetFiles() {
		return this.tag_files;
	}

	public String[] getTargetWords() {
		return this.tag_filewords;
	}

	public int getTargetSize() {
		return this.target_number;
	}

	public Args_Getter createAGT0() {
		Args_Getter ag0 = new Args_Getter();
		ag0.setRefImg(this.ref_image_string);
		ag0.setRef_AT_method(ref_AT_method);

		//tag image is null in this phase.

		ag0.setTag_AT_method(tag_dir);
		ag0.setSearchL(searchL);
		ag0.setRoundL(roundL);
		ag0.setRoundR(roundR);
		ag0.setResult_prefix(result_prefix);
		ag0.setSave_format(save_format);
		ag0.setOutput_dir(output_dir);
		ag0.setRandom_seed(random_seed);
		ag0.setCheckGAparam(checkGAparam);

		return ag0;
	}

	public Args_Getter getAGT0() {
		Args_Getter ag0 = createAGT0();
		return ag0;
	}


}
