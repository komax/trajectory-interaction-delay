function [distanceTerrain] = dynamicDistanceTerrain(traject1,traject2,normNumber,alpha)
distanceTerrain = matching.compute_distance_terrain(traject1,traject2,normNumber);
displacements = matching.computeDisplacement(traject1,traject2,normNumber,alpha);
attract = matching.computeHeading(traject1,traject2);
dynamicInteractionTerrain = displacements.*(attract);
distanceTerrain = distanceTerrain.*(2 - dynamicInteractionTerrain);
end
