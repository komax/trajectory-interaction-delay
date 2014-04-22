
[trajA, trajB, timeIndices] = loadBatData();

lcfMatching = matching.discrete_lcfm(trajA,trajB);
visualise.plot_matchin(trajA,trajB,lcfMatching);
