function matching = stuff(i, j)
% build filter
[b, a] = butter(2,0.1);
Hd = dfilt.df1(b,a);



bird_i = offset_filter(get_bird(i), Hd);
bird_j = offset_filter(get_bird(j), Hd);


matching = discrete_lcfm(bird_i,bird_j);
input('plot? ');
[distances, delay] = plot_matching(bird_i, bird_j, [matching.i matching.j]);
figure
plot(matching.i, matching.j);
