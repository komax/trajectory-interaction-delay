function idx = str2idx(str)
res = strsplit(str, ' -> ');
idx = sub2ind([16 16], str2num([res{1}]), str2num([res{2}]));
end
