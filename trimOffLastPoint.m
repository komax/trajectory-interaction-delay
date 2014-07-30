function [trimmedTrajA,trimmedTrajB,trimmedDistanceTerrain] = trimOffLastPoint(trajA,trajB,distanceTerrain)
trimmedTrajA = trajA(1:end-1,:);
trimmedTrajB = trajB(1:end-1,:);
trimmedDistanceTerrain = distanceTerrain(1:end-1,1:end-1);
end