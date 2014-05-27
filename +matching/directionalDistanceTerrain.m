function [distanceTerrain] = directionalDistanceTerrain(traject1,traject2,normNumber)
distanceTerrain = matching.compute_distance_terrain(traject1,traject2,normNumber);
attract = matching.computeHeading(traject1,traject2);
visualise.plotDistanceTerrain(attract);
distanceTerrain = distanceTerrain * (1 - attract);
end
