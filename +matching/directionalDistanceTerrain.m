function [distanceTerrain] = directionalDistanceTerrain(traject1,traject2)
distanceTerrain = matching.compute_distance_terrain(traject1,traject2);
attract = matching.computeHeading(traject1,traject2);
visualise.plotDistanceTerrain(attract);
distanceTerrain = distanceTerrain * (1 - attract);
end
