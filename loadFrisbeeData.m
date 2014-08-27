function [trajectoryA, trajectoryB] = loadFrisbeeData()
% load frisbee data file
pathToFrisbeeData = '/home/max/Documents/phd/ultimate_frisbee_data';
delimiter = ',';
headerLines = 1;
filename = [pathToFrisbeeData, '/', 'player1_TGISpaper.csv'];
player1Data = importdata(filename,delimiter,headerLines);
dataMatrix = player1Data.data;
trajectoryA = dataMatrix(:,2:3);
filename = [pathToFrisbeeData, '/', 'player2_TGISpaper.csv'];
player2Data = importdata(filename,delimiter,headerLines);
dataMatrix = player2Data.data;
trajectoryB = dataMatrix(:,2:3);
