function [distanceTerrain] = directionDistanceTerrain(traject1,traject2)
distanceTerrain = compute_distance_terrain(traject1,traject2);
attract = computeHeading(traject1,traject2);
distanceTerrain = distanceTerrain * (1 - attract);
end
