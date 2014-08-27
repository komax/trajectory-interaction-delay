function [trajectoryA, trajectoryB] = loadFrisbeeData()
% load frisbee data file
pathToFrisbeeData = '/home/max/Documents/phd/ultimate_frisbee_data';
delimiter = ' ';
headerLines = 1;
filename = [pathToFrisbeeData, '/', 'player1_cleaned.csv'];
player1Data = importdata(filename,delimiter,headerLines);
dataMatrix = player1Data.data;
trajectoryA = dataMatrix(:,1:2);
filename = [pathToFrisbeeData, '/', 'player2_cleaned.csv'];
player2Data = importdata(filename,delimiter,headerLines);
dataMatrix = player2Data.data;
trajectoryB = dataMatrix(:,1:2);
