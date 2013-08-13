function [matching, distances, delay] = stuff(i, j)
% build filter
[b, a] = butter(2,0.1);
Hd = dfilt.df1(b,a);



bird_i = offset_filter(get_bird(i), Hd);
bird_j = offset_filter(get_bird(j), Hd);


matching = discrete_lcfm(bird_i,bird_j);
%%%input('plot? ');
[distances, delay] = plot_matching(bird_i, bird_j, [matching.i matching.j]);
figure
frames_i = bird_i(matching.i + 1, 4); % transform from Java 0-based
frames_j = bird_j(matching.j + 1, 4); % transform from Java 0-based
plot(frames_i, frames_j)
xlabel(['bird ', num2str(i)]);
ylabel(['bird ', num2str(j)]);
figure
plot(1:length(distances),distances)
