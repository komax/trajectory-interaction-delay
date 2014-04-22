function [trajectoryA, trajectoryB, timeIndices] = loadBatData()
% load bat data file as matrices A, B and a vector for the time indices
pathToBatData = '/home/max/Documents/phd/bat_trajectory_data';
filename = [pathToBatData, '/', 'bat_trajectory.txt'];
delimiter = ' ';
headerLines = 1;

batData = importdata(filename,delimiter,headerLines);
dataMatrix = batData.data;
timeIndices = dataMatrix(:,1);
trajectoryA = dataMatrix(:,2:3);
trajectoryB = dataMatrix(:,4:5);
