function [threeDtrajectory] = pad2DTrajectoryTo3D(twoDtrajectory)
% Appends a new column in the trajectory for the "empty" third dimension with 0
zeroColumn = zeros(length(twoDtrajectory),1);
threeDtrajectory = [twoDtrajectory zeroColumn];
