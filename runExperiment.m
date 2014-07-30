function [trajA, trajB, distanceTerrain, lcfMatching] = runExperiment(dataType,distanceType,chosenNorm)
[trajA, trajB] = generateArtificialData(dataType);
trajA = pad2DTrajectoryTo3D(trajA);
trajB = pad2DTrajectoryTo3D(trajB);

experimentExtension = [dataType, 'Norm', num2str(chosenNorm)];

switch distanceType
    case 'normal'
        distanceTerrain = matching.compute_distance_terrain(trajA,trajB,chosenNorm);
    case 'directionalDistance'
        distanceTerrain = matching.directionalDistanceTerrain(trajA,trajB,chosenNorm);
        [trajA, trajB, distanceTerrain] = trimOffLastPoint(trajA,trajB,distanceTerrain);
        experimentExtension = [experimentExtension, 'DirectionalDistance'];
    case 'dynamicInteraction'
        alpha = 2;
        distanceTerrain = matching.dynamicInteractionTerrain(trajA,trajB,chosenNorm,alpha);
        [trajA, trajB, distanceTerrain] = trimOffLastPoint(trajA,trajB,distanceTerrain);
        experimentExtension = [experimentExtension, 'DynamicInteraction'];
    case 'dynamicDistance'
        alpha = 2;
        distanceTerrain = matching.dynamicDistanceTerrain(trajA,trajB,chosenNorm,alpha);
        [trajA, trajB, distanceTerrain] = trimOffLastPoint(trajA,trajB,distanceTerrain);
        experimentExtension = [experimentExtension, 'DynamicDistance'];
    otherwise
        error('Cannot handle this choice');
end
pathToResults = 'results/';
matchingName = [pathToResults, 'matchingOn', experimentExtension, '.dump'];
delayPlotName = [pathToResults, 'delaySpaceOn', experimentExtension, '.png'];
lcfMatching = matching.discrete_lcfm(trajA,trajB,distanceTerrain);
matching.writeMatching(lcfMatching,matchingName);
visualise.plotMatching(lcfMatching);
delayPlot = visualise.plotMatchingInFreeSpace(distanceTerrain, lcfMatching);
saveas(delayPlot,delayPlotName);
end
