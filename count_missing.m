function [num_fixes, range, num_missing] = count_missing(t)

num_fixes = size(t,1);
range = max(t(:,4)) - min(t(:,4));
num_missing = range - num_fixes;

