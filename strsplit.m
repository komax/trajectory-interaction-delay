function res = strsplit(str, delimiter)
%%% only needed for MATLAB versions earlier than 2013
res = regexp(str, delimiter, 'split');
end
