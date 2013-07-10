function stuff(i, j)
% build filter
[b, a] = butter(2,0.1);
Hd = dfilt.df1(b,a);



bird_i = offset_filter(get_bird(i), Hd);
bird_j = offset_filter(get_bird(j), Hd);


matching = discrete_lcfm(bird_i,bird_j);
input('plot? ');
plot_matching(bird_i, bird_j, matching)
