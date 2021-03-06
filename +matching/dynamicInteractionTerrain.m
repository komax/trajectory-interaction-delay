function [distanceTerrain] = dynamicIterationTerrain(traject1,traject2,normNumber,alpha)
displacements = matching.computeDisplacement(traject1,traject2,normNumber,alpha);
attract = matching.computeHeading(traject1,traject2);
distanceTerrain = 1 - displacements.*(attract);
end
