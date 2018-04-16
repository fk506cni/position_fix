package ij.plugin.psfx;

import java.util.Comparator;

public class ComPara_Genome  implements Comparator<Genome_ga>{

	@Override
	public int compare(Genome_ga ga1, Genome_ga ga2) {
		int result = 0;
		result = ga1.getEval() <= ga2.getEval() ? -1: 1;
		return result;
	}

}
