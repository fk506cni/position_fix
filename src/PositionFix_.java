import ij.IJ;
import ij.plugin.PlugIn;
import ij.plugin.psfx.Process_manager;

public class PositionFix_ implements PlugIn{
	public void run (String arg) {
		String plg_ver = "0.84";
		IJ.log("starting PosFx plugin ver" +plg_ver);

		Process_manager pm = new Process_manager();
		pm.main();
	}
}