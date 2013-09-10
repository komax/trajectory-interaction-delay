function plot_matching(t1, t2, matching_java_obj)
clf; hold on;
terrain = matching.compute_distance_terrain(t1, t2, 'square');
imagesc(terrain);

idx = [matching_java_obj.i matching_java_obj.j];
start_frame = min([idx(:,1); idx(:,2)]);
stop_frame = max([idx(:,1); idx(:,2)]);
frame_range = stop_frame - start_frame + 1;
x(length(idx)) = 0;
y(length(idx)) = 0;
for index = 1:length(idx)
    frame_1 = t1(idx(index, 1)+1,4);
    frame_2 = t2(idx(index, 2)+1,4);
    x(index) = frame_range - frame_1;
    y(index) = frame_range - frame_2;
end
disp(x)
disp(y)
plot(x,y)
%%%    p = t1(t1(:,4) == frame_1);
%%%    q = t2(t2(:,4) == frame_2);

