function plot_path(t1, t2, matching_java_obj)
clf;
terrain = matching.compute_distance_terrain(t1, t2);
imagesc(terrain);
hold on;

idx = [matching_java_obj.i matching_java_obj.j];

results = matching_java_obj.getMatchedPoints();

for row = 1:length(results)
    p = results(row,1:3);
    frame_1 = results(row,4);
    bird_id1 = results(row,5);
    q = results(row,6:8);
    frame_2 = results(row,9);
    bird_id2 = results(row,10);

    fprintf(1,'(%d, %d)\n',frame_1, frame_2);
end
plot
