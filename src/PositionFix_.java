import ij.IJ;
import ij.plugin.PlugIn;
import ij.plugin.psfx.Mode_Manager;

public class PositionFix_ implements PlugIn{
	public void run (String arg) {
		String plg_ver = "0.95";
		IJ.log("starting PosFx plugin ver" +plg_ver);

		Mode_Manager mm = new Mode_Manager();
		mm.main();

	//	Process_manager pm = new Process_manager();
	//	pm.main();
	}
}