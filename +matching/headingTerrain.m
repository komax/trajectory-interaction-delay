function [distanceTerrain] = headingTerrain(traject1,traject2,normNumber)
attract = matching.computeHeading(traject1,traject2);
distanceTerrain = 1 - attract;
end
