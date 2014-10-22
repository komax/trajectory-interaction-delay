function [trajectoryA, trajectoryB, timeIndices] = loadPigeonData()
% load pigeon data file as matrices A, B and a vector for the time indices
pathToPigeonData = '/home/max/Documents/phd/pigeon_trajectory_data';
filename = [pathToPigeonData, '/', 'pigeon_trajectory.txt'];
delimiter = ' ';
headerLines = 1;

pigeonData = importdata(filename,delimiter,headerLines);
dataMatrix = pigeonData.data;
timeIndices = dataMatrix(:,1);
trajectoryA = dataMatrix(:,2:3);
trajectoryB = dataMatrix(:,4:5);
