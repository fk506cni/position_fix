package ij.plugin.psfx;

import fiji.util.gui.GenericDialogPlus;

public class Args_Getter_SeqFx extends Args_Getter{
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
}
