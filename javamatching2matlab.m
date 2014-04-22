function [matchingMatrix] = javamatching2matlab(matchingJavaObj)
% Converts a java matching object into a matlab matrix.
% Column 1 are the indices i from the first trajectory of the matching
% Column 2 are the indices j from the second trajectory in the matching
matchingMatrix = [matchingJavaObj.i matchingJavaObj.j];
end
