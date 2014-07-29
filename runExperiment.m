function runExperiment(dataType,distanceType,chosenNorm)
[trajA, trajB] = generateArtificialData(dataType);
trajA = pad2DTrajectoryTo3D(trajA);
trajB = pad2DTrajectoryTo3D(trajB);

experimentExtension = [dataType, 'Norm', num2str(chosenNorm)];

switch distanceType
    case 'normal'
        distanceTerrain = matching.compute_distance_terrain(trajA,trajB,chosenNorm);
    otherwise
        error('Cannot handle this choice');
end
matchingName = ['matchingOn', experimentExtension, '.dump'];
delayPlotName = ['delaySpaceOn', experimentExtension, '.png'];
lcfMatching = matching.discrete_lcfm(trajA,trajB,distanceTerrain);
matching.writeMatching(lcfMatching,matchingName);
visualise.plotMatching(lcfMatching);
delayPlot = visualise.plotMatchingInFreeSpace(distanceTerrain, lcfMatching);
saveas(delayPlot,delayPlotName);

