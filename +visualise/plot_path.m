function plot_path(t1, t2, matching_java_obj, name1, name2)
clf;
terrain = matching.compute_distance_terrain(t1, t2);
imagesc(terrain);
ylabel(name1)
xlabel(name2)
%%%axis equal;
hold on;

idx = [matching_java_obj.i matching_java_obj.j];

%%%results = matching_java_obj.getMatchedPoints();
x = [];
y = [];
for row = 1:size(idx,1)
%%%    p = results(row,1:3);
%%%    frame_1 = results(row,4);
%%%    bird_id1 = results(row,5);
%%%    q = results(row,6:8);
%%%    frame_2 = results(row,9);
%%%    bird_id2 = results(row,10);
%%%    x(row) = frame_1 + 1;
%%%    y(row) = frame_2 + 1;
%%%    fprintf(1,'(%d, %d)\n',frame_1, frame_2);
    x(row) = idx(row, 2) + 1; % how far along bird j we are
    y(row) = size(t1,1) - idx(row, 1); % how far along bird i we are
end
plot(x, y,'r');

