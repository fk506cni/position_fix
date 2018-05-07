package ij.plugin.psfx;

import java.util.ArrayList;
import java.util.stream.Collectors;

import ij.IJ;

public class Eval_GenomeList_para extends Eval_GenomeList{
//	static final int threadNum = 2;

	@Override
	public ArrayList<Genome_ga> evalGenomeList(ArrayList<Genome_ga> gl){

//			gl.replaceAll(ga1 -> ev.getAssesedGenome(ga1));
		IJ.log("evaluation genome... \nparallel precessing...");

		ArrayList<Genome_ga> gl2 = 	new ArrayList<Genome_ga>(gl.stream().parallel()
				.map(ga1 -> ev.getAssesedGenome(ga1))
				.collect(Collectors.toList()));


			return gl2;
		}
}
