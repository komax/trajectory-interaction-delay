function [distanceTerrain] = dynamicIterationTerrain(traject1,traject2,alpha)
displacements = matching.computeDisplacement(traject1,traject2,alpha);
attract = matching.computeHeading(traject1,traject2);
distanceTerrain = displacements.*(attract);
end
