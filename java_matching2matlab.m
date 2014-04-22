function [matching_matrix] = java_matching2matlab(matching_java_obj)
% Converts a java matching object into a matlab matrix.
% Column 1 are the indices i from the first trajectory of the matching
% Column 2 are the indices j from the second trajectory in the matching
matching_matrix = [matching_java_obj.i matching_java_obj.j];
end
